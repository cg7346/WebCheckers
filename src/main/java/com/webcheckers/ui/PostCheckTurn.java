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

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The {@code POST /checkTurn} route handler
 * this checks for the turn of the user
 * @author jil4009
 */
public class PostCheckTurn implements Route {
    private CheckersGame game;
    private Move move;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final Gson gson;

    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param gameManager
     * @param gson
     * @throws NoSuchElementException when the {@code gameManager} or {@code gson} parameter is null
     */
    public PostCheckTurn(PlayerLobby playerLobby, GameManager gameManager,Gson gson){
        this.playerLobby = Objects.requireNonNull(playerLobby, "player lobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    /**
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        String gameIdString = request.queryParams("gameID");
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIdString));
        //sometimes having the game undefined breaks the builder
        if (game != null) {
            if (game.getActivePlayer().equals(currentPlayer)) {
                return gson.toJson(Message.info("true"));
            } else {
                return gson.toJson(Message.info("false"));
            }
        }
        return null;
    }
}
