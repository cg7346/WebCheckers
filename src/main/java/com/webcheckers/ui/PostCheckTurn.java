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

import java.nio.file.WatchEvent;
import java.util.NoSuchElementException;
import java.util.Objects;

import static spark.Spark.halt;

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
    static final String PLAYER_RESIGNED = "Opponent has resigned.";
    static final String MESSAGE_ERR = "message error";



    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param playerLobby
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
        CheckersGame game = gameManager.getGame(currentPlayer);
        //sometimes having the game undefined breaks the builder
        if (game != null) {
            if (game.getActivePlayer().equals(currentPlayer)) {
                return gson.toJson(Message.info("true"));
            } else if (PostResignGame.called) {
                GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
                GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", PostResignGame.resignPlayer.getName() + " has resigned.");
                response.body(gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer)));
                return gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer));
            } else if (GetGameRoute.modeOptionsAsJSON != null) {
                if (game.getActivePlayer() == game.getRedPlayer()) {
                    game.setWinner(game.getWhitePlayer());
                } else {
                    game.setWinner(game.getRedPlayer());
                }
                return gson.toJson(Message.info("true"));
            } else {
                return gson.toJson(Message.info("false"));
            }

        }
        GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
        GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", PostResignGame.resignPlayer.getName() + " has resigned.");
        response.body(gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer)));
        Message er = Message.error(PLAYER_RESIGNED);
        session.attribute(MESSAGE_ERR, er);
        return gson.toJson(Message.info("true"));
    }
}
