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

import java.util.Objects;

/**
 * The (@code POST /submitTurn) route handler.
 *
 * @author <a href='mailto:mmb2582@rit.edu'>Mallory Bridge</a>
 * @author <a href='mailto:jil4009@rit.edu'>Jackie Leung</a>
 */
public class PostSubmitTurn implements Route {

    //
    // Attributes
    //

    private GameManager gameManager;
    private final Gson gson;

    //
    // Static Variables
    //

    public static Boolean AI = false;

    //
    // Constants
    //

    static final String MESSAGE_ERR = "message error";


    //
    // Constructor
    //

    /**
     * Create the UI controller to handle all {@code POST /submitTurn} HTTP requests.
     *
     * @param playerLobby   the list of players playing the WebCheckers game
     * @param gameManager   handles all the checker games
     */
    public PostSubmitTurn(PlayerLobby playerLobby, GameManager gameManager, Gson gson){
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    /**
     * Checks to see if all the pieces are blocked or captured
     *
     * @param count     the number of pieces left on the board for a player
     * @param winOrlose whether they have either won or lost
     * @return an end of game message
     */
    public String BlockedOrCaptured(Integer count, String winOrlose){
        String endGame = null;
        if (count > 0) {
            endGame = " has blocked all pieces, " + winOrlose;
        } else {
            endGame = " has captured all pieces, " + winOrlose;
        }
        return endGame;
    }

    /**
     *
     * @param game
     * @param message
     * @param session
     */
    public void AIEndGame(CheckersGame game, String message, Session session){
        if (AI) {
            gameManager.removeGame(game);
            Message er = Message.error(message);
            session.attribute(MESSAGE_ERR, er);
        }
    }

    /**
     * Where the game over message is constructed
     *
     * @param moveValidator the moves possible array
     * @param game the game the players in
     * @param session
     * @param win
     * @return
     */
    public Message gameOver(MoveValidator moveValidator, CheckersGame game, Session session, Boolean win){
        Message responseMessage = null;
        String end;
        if(win) {
            end = "you won!";
        } else {
            end = "you lost.";
        }
        //Tie Condition
        if (moveValidator.isOut(moveValidator.REDPLAYER) &&
                moveValidator.isOut(moveValidator.WHITEPLAYER)){
            game.setTie(true);
            responseMessage = Message.info("The game has ended in a tie.");
            AIEndGame(game, "The game has ended in a tie.", session);
            //White Wins
        } else if (moveValidator.isOut(moveValidator.REDPLAYER) &&
                !moveValidator.isOut(moveValidator.WHITEPLAYER)) {
            game.setWinner(game.getWhitePlayer());
            String endGame = BlockedOrCaptured(moveValidator.getCount(moveValidator.REDPLAYER), end);
            String message = game.getWhitePlayer().getName() + endGame;
            responseMessage = Message.info(message);
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
            AIEndGame(game, message, session);

            //Red Wins
        } else if (!moveValidator.isOut(moveValidator.REDPLAYER)
                && moveValidator.isOut(moveValidator.WHITEPLAYER)) {
            game.setWinner(game.getRedPlayer());
            String endGame = BlockedOrCaptured(moveValidator.getCount(moveValidator.WHITEPLAYER), end);
            String message = game.getRedPlayer().getName() + endGame;
            responseMessage = Message.info(message);
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
            AIEndGame(game, message, session);
        }
        return responseMessage;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     * @return updates gson with message if you need to
     *          make a move first
     *          the game is over
     *          jump available
     *          valid move
     */
    @Override
    public Object handle(Request request, Response response) {
        String gameIDString = request.queryParams("gameID");
        Session session = request.session();
        Player player = session.attribute("Player");
        CheckersGame game = gameManager.getGame(player);
        MoveValidator moveValidator = new MoveValidator(game);
        if (game.getWhitePlayer().getName().equals("AI")) {
            AI = true;
        }
        moveValidator.lookForMoves();
        Message responseMessage = gameOver(moveValidator, game, session, true);
        if( responseMessage == null) {
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
                    game.makeMove(aiMove);
                    Boolean jumpMoves = moveValidator.areThereJumpMoves();
                    while (jumpMoves) {
                        Boolean visited = false;
                        moveValidator.lookForMoves();
                        for (Move jump : moveValidator.getJumpMoves(moveValidator.WHITEPLAYER)) {
                            if (jump.getStart().toString().equals(aiMove.getEnd().toString())) {
                                game.makeMove(jump);
                                aiMove = jump;
                                visited = true;
                                break;
                            }
                        }
                        if (!visited) {
                            jumpMoves = false;
                            break;
                        }
                    }
                    game.completeTurn();
                    PostCheckTurn.timer = TimeWatch.start();
                    moveValidator.lookForMoves();
                    if (gameOver(moveValidator, game, session, false) != null) {
                        responseMessage = gameOver(moveValidator, game, session, false);
                    }
                }
            }
        }
        //       }
        AI = false;
        response.body(gson.toJson(responseMessage));
        return gson.toJson(responseMessage);
    }
}
