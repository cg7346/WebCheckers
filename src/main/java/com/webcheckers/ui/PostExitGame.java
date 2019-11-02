package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The {@code POST /exitGame} route handler
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostExitGame implements Route {

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /exitGame} route handler.
     *
     * @param gameManager is the game manager from the game manager class
     * @param gson is the JSON
     * @throws NoSuchElementException when the {@code gameManager} or {@code gson} parameter is null
     */
    public PostExitGame(GameManager gameManager, Gson gson) {
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
        // Gets the game ID
        String gameIDString = request.queryParams("gameID");
        // Gets the checkers game
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
        return null;
    }

}
