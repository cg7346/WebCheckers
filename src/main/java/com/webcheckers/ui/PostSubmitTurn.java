package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

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
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        CheckersGame game = gameManager.getGame(currentPlayer);
        if (!PostResignGame.called) {
            Move lastMove = game.getLastMove();
            Message responseMessage = null;
            if (lastMove != null) {
                game.completeTurn();
                responseMessage = Message.info("Valid Move!");
            } else {
                responseMessage = Message.error("Make move first");
            }
            response.body(gson.toJson(responseMessage));

            return gson.toJson(responseMessage);
        } else {
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", PostResignGame.resignPlayer.getName() + " has resigned.");
            response.body(gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer)));
            return gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer));
        }
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
        if (game != null) {
            Move lastMove = game.getLastMove();
            System.out.println("LastMoveMade ->> " + lastMove);
            Message responseMessage = null;
            if (lastMove != null) {
                game.completeTurn();
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
