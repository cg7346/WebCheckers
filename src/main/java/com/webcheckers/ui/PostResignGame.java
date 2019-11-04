package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code POST /resignGame} route handler
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostResignGame implements Route {

    //
    // Constants
    //

    static Map<String, Object> modeOptionsAsJSON = new HashMap<>(2);
    static Player resignPlayer;
    static Player winningPlayer;

    // Values used in the mode options for JSON after the resign button is clicked
    private static final String RESIGN = "%s has resigned.";

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gameManager is the game manager from the game manager class
     * @param gson is the JSON
    //     * @throws NoSuchElementException when the {@code gameManager} or {@code gson} parameter is null
     */
    public PostResignGame(GameManager gameManager, Gson gson) {
        // Sets and validates the gameManager attribute to not be null
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        // Sets and validates the gson attribute to not be null
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
    }

    //
    // Attributes
    //
    private final GameManager gameManager;
    private final Gson gson;


    //
    // Static Methods
    //

    /**
     * Resign message after a player clicks the resign button
     *
     * @return the message that will be displayed when a player resigns
     */
    public static Message resignMessage(Player player) {
        return Message.info(String.format(RESIGN, player.getName()));
    }

    public Map<String, Object> getOptions() {
        return modeOptionsAsJSON;
    }


    /**
     * {@inheritDoc}
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return templateEngine to render a view or null
     * @throws java.util.NoSuchElementException when an invalid username is returned
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String gameIDAsString = request.queryParams("gameID");
        Integer gameID = Integer.parseInt(gameIDAsString);
        Session session = request.session();
        resignPlayer = session.attribute("Player");
        CheckersGame game = gameManager.getGame(resignPlayer);
        if (resignPlayer.getName().equals(game.getRedPlayer().getName())) {
            winningPlayer = game.getWhitePlayer();
        } else {
            winningPlayer = game.getRedPlayer();
        }
        game.setGameOver(true);
        modeOptionsAsJSON.put("isGameOver", true);
        modeOptionsAsJSON.put("gameOverMessage", resignPlayer.getName() + " has resigned.");
        response.body(gson.toJson(resignMessage(resignPlayer)));
        gameManager.removeGame(game);
        return gson.toJson(resignMessage(resignPlayer));
    }
}

