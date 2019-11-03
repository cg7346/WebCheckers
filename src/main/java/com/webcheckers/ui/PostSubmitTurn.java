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

import java.util.HashMap;
import java.util.Map;
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
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
        if (game != null) {
            Move lastMove = game.getLastMove();
            Message responseMessage = null;
            //TODO: check is there are any moves possible
            //      //TODO: if not then endGame   game.endGame(String.format(PIECES_CAPTURED_STRING, name),name);
            //TODO: set winner and loser and create message   game.endGame(loser + " cannot make a move", winner);
            if (lastMove != null) {
                //TODO: Check to see if there are jump moves
                game.makeMove(lastMove);
                game.swapPlayers();
                responseMessage = Message.info("Valid Move!");
            } else {
                responseMessage = Message.error("Make move first");
            }
            response.body(gson.toJson(responseMessage));
            return gson.toJson(responseMessage);
        } else {
            Map<String, Object> modeOptionsAsJSON = new HashMap<>(2);
            Session session = request.session();
            Player player = session.attribute("Player");
            modeOptionsAsJSON.put("isGameOver", true);
            modeOptionsAsJSON.put("gameOverMessage", player.getName() + " has resigned.");
            return gson.toJson(Message.info(player.getName() + " has resigned."));
        }
    }
}
