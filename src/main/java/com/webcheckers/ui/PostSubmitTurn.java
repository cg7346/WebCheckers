package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.List;
import java.util.Objects;

public class PostSubmitTurn implements Route {

    private GameManager gameManager;
    private final Gson gson;

    public static Boolean AI = false;
    static final String MESSAGE_ERR = "message error";


    /**
     * Construct a Post Submit Turn
     * @param playerLobby PlayerLobby of the session
     * @param gameManager GameManager of the session
     */
    public PostSubmitTurn(PlayerLobby playerLobby, GameManager gameManager, Gson gson){
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    public String BlockedOrCaptured(Integer count, String winOrlose){
        String endGame = null;
        if (count > 0) {
            endGame = " has blocked all pieces, " + winOrlose;
        } else {
            endGame = " has captured all pieces, " + winOrlose;
        }
        return endGame;
    }

    public void AIEndGame(CheckersGame game, String message, Session session){
        if (AI) {
            gameManager.removeGame(game);
            Message er = Message.error(message);
            session.attribute(MESSAGE_ERR, er);
        }
    }

    public Message gameOver(MoveValidator moveValidator, CheckersGame game, Session session, Boolean win){
        Message responseMessage = null;
        String end;
        if(win) {
            end = "you won!";
        } else {
            end = "you lost.";
        }

        if (moveValidator.isOut(game.getRedPlayer()) && moveValidator.isOut(game.getWhitePlayer())) {
            game.setTie(true);
            responseMessage = Message.info("The game has ended in a tie.");
            AIEndGame(game, "The game has ended in a tie.", session);
        } else if (moveValidator.isOut(game.getRedPlayer()) && !moveValidator.isOut(game.getWhitePlayer())) {
            game.setWinner(game.getWhitePlayer());
            String endGame = BlockedOrCaptured(moveValidator.getCount(game.getRedPlayer()), end);
            String message = game.getWhitePlayer().getName() + endGame;
            responseMessage = Message.info(message);
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
            AIEndGame(game, message, session);
        } else if (!moveValidator.isOut(game.getRedPlayer()) && moveValidator.isOut(game.getWhitePlayer())) {
            game.setWinner(game.getRedPlayer());
            String endGame = BlockedOrCaptured(moveValidator.getCount(game.getWhitePlayer()), end);
            String message = game.getRedPlayer().getName() + endGame;
            responseMessage = Message.info(message);
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
            AIEndGame(game, message, session);
        }
        return responseMessage;
    }

    /**
     * this handles the post submit turn
     * @param request
     * @param response
     * @return message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String gameIDString = request.queryParams("gameID");
        Session session = request.session();
        Player player = session.attribute("Player");
        CheckersGame game = gameManager.getGame(player);
        MoveValidator moveValidator = new MoveValidator(game);
        if (game.getWhitePlayer().getName().equals("AI")) {
            AI = true;
        }
        //System.out.println("LastMoveMade ->> " + lastMove);
        moveValidator.lookForMoves();
        Message responseMessage = gameOver(moveValidator, game, session, true);
        if( responseMessage == null) {
//        if (moveValidator.isRedOut() && moveValidator.isWhiteOut()) {
//            game.setTie(true);
//            responseMessage = Message.info("The game has ended in a tie.");
//            if (AI) {
//                gameManager.removeGame(game);
//                Message er = Message.error("The game has ended in a tie.");
//                session.attribute(MESSAGE_ERR, er);
//            }
//        } else if (moveValidator.isRedOut() && !moveValidator.isWhiteOut()) {
//            game.setWinner(game.getWhitePlayer());
//            String endGame;
//            if (moveValidator.getRedCount() > 0) {
//                endGame = " has blocked all pieces, you won!";
//            } else {
//                endGame = " has captured all pieces, you won!";
//            }
//            String message = game.getWhitePlayer().getName() + endGame;
//            responseMessage = Message.info(message);
//            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
//            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
//            if (AI) {
//                gameManager.removeGame(game);
//            }
//        } else if (!moveValidator.isRedOut() && moveValidator.isWhiteOut()) {
//            game.setWinner(game.getRedPlayer());
//            String endGame;
//            if (moveValidator.getWhiteCount() > 0) {
//                endGame = " has blocked all pieces, you won!";
//            } else {
//                endGame = " has captured all pieces, you won!";
//            }
//            String message = game.getRedPlayer().getName() + endGame;
//            responseMessage = Message.info(message);
//            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
//            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
//            if (AI) {
//                gameManager.removeGame(game);
//                Message er = Message.error(message);
//                session.attribute(MESSAGE_ERR, er);
//            }
//        } else {
            Move lastMove = game.getLastMove();
            if (lastMove == null) {
                responseMessage = Message.error("Make move first");
            } else {
                if (lastMove.hasPiece()) {
                    moveValidator.clearArrays();
                    lastMove = game.moveConverter(lastMove);
                    moveValidator.lookInSpace(lastMove.getEnd().getRow(), lastMove.getEnd().getCol());
                    if (moveValidator.areThereJumpMoves()) {
                        responseMessage = Message.error(moveValidator.jumpAvail);
                    } else {
                        game.completeTurn();
                        responseMessage = Message.info(moveValidator.validMove);
                    }
                } else {
                    game.completeTurn();
                    responseMessage = Message.info(moveValidator.validMove);
                }
                if (AI) {
                    AI ai = new AI(game, moveValidator);
                    Move aiMove = ai.makeTurn();
                    System.out.println(aiMove);
                    game.makeMove(aiMove);
                    if (moveValidator.areThereJumpMoves()) {
                        moveValidator.lookForMoves();
                        for (Move jump : moveValidator.getJumpMoves(game.getWhitePlayer())) {
                            if (jump.getStart().toString().equals(aiMove.getEnd().toString())) {
                                System.out.println(jump.getStart() + " equals " + aiMove.getEnd());
                                game.makeMove(jump);
                                break;
                            }
                        }
                    }
                    game.completeTurn();
                    moveValidator.lookForMoves();
                    if (gameOver(moveValidator, game, session, false) != null){
                        responseMessage = gameOver(moveValidator, game, session, false);
                    }
                }
            }
            //       }
        }
        AI = false;
        response.body(gson.toJson(responseMessage));
        return gson.toJson(responseMessage);
    }
}
