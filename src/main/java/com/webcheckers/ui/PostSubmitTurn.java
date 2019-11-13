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


    @Override
    public Object handle(Request request, Response response) throws Exception {
        String gameIDString = request.queryParams("gameID");
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));;
        MoveValidator moveValidator = new MoveValidator(game);
        Move lastMove = game.getLastMove();
        Message responseMessage = null;
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
                game.checkForKings();
                responseMessage = Message.info(moveValidator.validMove);
            }
        }
        response.body(gson.toJson(responseMessage));
        return gson.toJson(responseMessage);
    }
}
