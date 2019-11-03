package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

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
//        System.out.println("move string is this " + moveString);
        String gameIdString = request.queryParams("gameID");

        Move move = gson.fromJson(moveString, Move.class);
//        System.out.println("------Checking for THIS move!!");
//        System.out.println(move);
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIdString));
        if (game != null) {
            game.lookForMoves();
            boolean isPossibleMove = game.isInMoves(move);
            //        System.out.println(isPossibleMove);
            Message responseMessage = null;
            if (isPossibleMove) {
                responseMessage = Message.info("Valid Move!");
                game.keepLastMove(move);

            } else {
                responseMessage = Message.error("Invalid MMMOOOOVEEE!");
            }
            response.body(gson.toJson(responseMessage));
            return gson.toJson(responseMessage);
        } else {
            //halt();
            return gson.toJson("false");
        }
    }
}
