package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
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
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        CheckersGame game = gameManager.getGame(currentPlayer);
        if (PostResignGame.called) {
            boolean isPossibleMove = game.isInMoves(move);
            System.out.println(isPossibleMove);
            Message responseMessage = null;
            if(isPossibleMove) {
                responseMessage = Message.info("Valid Move!");
                game.keepLastMove(move);
                game.completeMove();
                if (move.hasPiece()) {
                    System.out.println("AAAAAAHHHHHHHHH");
                    move = game.moveConverter(move);
                    game.lookInSpace(move.getEnd().getRow(), move.getEnd().getCol());
                }
            } else {
                responseMessage = Message.error("Invalid MMMOOOOVEEE!");
            }
            response.body(gson.toJson(responseMessage));
            return gson.toJson(responseMessage);
        } else {
            PostResignGame.modeOptionsAsJSON.put("isGameOver", true);
            PostResignGame.modeOptionsAsJSON.put("gameOverMessage", PostResignGame.resignPlayer.getName() + " has resigned.");
            response.body(gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer)));
            return gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer));
        }
    }
}
