package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.ValidateMove;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;

public class PostValidateMove implements Route {
    private CheckersGame game;
    private Move move;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final Gson gson;


    public PostValidateMove(PlayerLobby playerLobby, GameManager gameManager, Gson gson){
        this.playerLobby = Objects.requireNonNull(playerLobby, "player lobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        String moveString = request.queryParams("actionData");
        String gameIdString = request.queryParams("gameID");

        Move move = gson.fromJson(moveString, Move.class);
        Player currentPlayer = playerLobby.getPlayer();
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIdString));
        game.lookForMoves();
        boolean isPossibleMove = game.isInMoves(move);
        Message responseMessage = null;
        if(isPossibleMove){
            responseMessage = Message.info("Valid Move!");

        }else {
            responseMessage = Message.error("Invalid MMMOOOOVEEE!");
        }
        response.body(gson.toJson(responseMessage));
        //System.out.println(request.queryParams("actionData"));
        System.out.println(game);

        return gson.toJson(responseMessage);
    }
}
