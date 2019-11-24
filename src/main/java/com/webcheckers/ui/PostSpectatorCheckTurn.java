package com.webcheckers.ui;


import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The UI controller to POST the Spectator page
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella<a/>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo<a/>
 */
public class PostSpectatorCheckTurn implements Route {

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    static String SPECTATOR_TIME;



    /**
     * Create the UI controller to handle all {@code GET /spectator/stopWatching} HTTP requests.
     * <p>
     * //     * @param checkersGame holds all the information about players playing a game
     *
     * @param gameManager how to access a game
     */
    public PostSpectatorCheckTurn(final GameManager gameManager, final TemplateEngine templateEngine, final Gson gson) {
        this.gameManager = Objects.requireNonNull(gameManager, "Game Manager must not be null.");
        // Sets and validates the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }


    /**
     * Render an updated checker game for spectator
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) {
        if (PostCheckTurn.timer != null) {
            long passedTimeInSeconds = PostCheckTurn.timer.time(TimeUnit.SECONDS);

            SPECTATOR_TIME = String.format("Last turn was about %d seconds ago.", passedTimeInSeconds); /* Get the game over message */
            response.body(gson.toJson(SPECTATOR_TIME));
            return gson.toJson(Message.info(SPECTATOR_TIME));
        }
        return gson.toJson(Message.info("false"));
    }
}
