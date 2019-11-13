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

    static Player resignPlayer;
    private static Player winningPlayer;

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

    /**
     * {@inheritDoc}
     *
     * this handles the post resign game
     * @param request the HTTP request
     * @param response the HTTP response
     * @return templateEngine to render a view or null
     * @throws java.util.NoSuchElementException when an invalid username is returned
     */
    @Override
    public Object handle(Request request, Response response) {
        Session session = request.session();
        resignPlayer = session.attribute("Player");
        CheckersGame game = gameManager.getGame(resignPlayer);
        if (!resignPlayer.getName().equals(game.getActivePlayer().getName())) {
            game.setResignedPlayer(null);
            game.setWinner(null);
            response.body(gson.toJson(Message.error("Please wait for you turn to resign.")));
            return gson.toJson(Message.error("Please wait for you turn to resign."));
        } else {
            if (resignPlayer.getName().equals(game.getRedPlayer().getName())) {
                winningPlayer = game.getWhitePlayer();
            } else {
                winningPlayer = game.getRedPlayer();
            }
            game.setResignedPlayer(resignPlayer);
            game.setWinner(winningPlayer);
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", resignPlayer.getName() + " has resigned.");
            response.body(gson.toJson(resignMessage(resignPlayer)));
            return gson.toJson(resignMessage(resignPlayer));
        }
    }
}