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

public class PostValidateMove implements Route {

    static final String MESSAGE_ERR = "message error";

    private CheckersGame game;
    private Move move;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final Gson gson;

    /**
     * the constructor for post validate move
     * @param playerLobby
     * @param gameManager
     * @param gson
     */
    public PostValidateMove(PlayerLobby playerLobby, GameManager gameManager, Gson gson){
        this.playerLobby = Objects.requireNonNull(playerLobby, "player lobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    /**
     * the handle for post validate move
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String moveString = request.queryParams("actionData");
        String gameIdString = request.queryParams("gameID");
        Move move = gson.fromJson(moveString, Move.class);
        Session session = request.session();
        Player player = session.attribute("Player");
        CheckersGame game = gameManager.getGame(player);
        MoveValidator moveValidator = new MoveValidator(game);
        String moveResponse = moveValidator.isInMoves(move);
        Message responseMessage = null;
        if(moveResponse.equals(moveValidator.validMove)){
            responseMessage = Message.info(moveResponse);
            game.keepLastMove(move);
            game.completeMove();

            //game.checkForDoubleJump(move);
        }else {
            responseMessage = Message.error(moveResponse);
        }
        response.body(gson.toJson(responseMessage));
        return gson.toJson(responseMessage);
    }
}
