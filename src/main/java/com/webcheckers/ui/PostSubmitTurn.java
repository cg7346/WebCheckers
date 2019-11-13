package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;

public class PostSubmitTurn implements Route {

    private PlayerLobby playerLobby;
    private GameManager gameManager;
    private final Gson gson;

    /**
     * Construct a Post Submit Turn
     * @param playerLobby PlayerLobby of the session
     * @param gameManager GameManager of the session
     */
    public PostSubmitTurn(PlayerLobby playerLobby, GameManager gameManager, Gson gson){
        this.playerLobby = Objects.requireNonNull(playerLobby, "player lobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
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
        //System.out.println("LastMoveMade ->> " + lastMove);
        Message responseMessage = null;
        moveValidator.lookForMoves();
        if (moveValidator.isRedOut() && moveValidator.isWhiteOut()) {
            game.setTie(true);
            responseMessage = Message.info("The game has ended in a tie.");
        } else if (moveValidator.isRedOut() && !moveValidator.isWhiteOut()) {
            game.setWinner(game.getWhitePlayer());
            String endGame;
            if (moveValidator.getRedCount() > 0) {
                endGame = " has blocked all pieces, you won!";
            } else {
                endGame = " has captured all pieces, you won!";
            }
            String message = game.getWhitePlayer().getName() + endGame;
            responseMessage = Message.info(message);
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
        } else if (!moveValidator.isRedOut() && moveValidator.isWhiteOut()) {
            game.setWinner(game.getRedPlayer());
            String endGame;
            if (moveValidator.getWhiteCount() > 0) {
                endGame = " has blocked all pieces, you won!";
            } else {
                endGame = " has captured all pieces, you won!";
            }
            String message = game.getRedPlayer().getName() + endGame;
            responseMessage = Message.info(message);
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
        } else {
            Move lastMove = game.getLastMove();
            if (lastMove == null){
                responseMessage = Message.error("Make move first");
            }else{
                if (lastMove.hasPiece()){
                    lastMove = game.moveConverter(lastMove);
                    moveValidator.lookInSpace(lastMove.getEnd().getRow(), lastMove.getEnd().getCol());
                    if (moveValidator.areThereJumpMoves()) {
                        responseMessage = Message.error(moveValidator.jumpAvail);
                    }else{
                        game.completeTurn();
                        responseMessage = Message.info(moveValidator.validMove);
                    }
                }else{
                    game.completeTurn();
                    responseMessage = Message.info(moveValidator.validMove);
                }
            }
        }
        response.body(gson.toJson(responseMessage));

        return gson.toJson(responseMessage);
    }
}
