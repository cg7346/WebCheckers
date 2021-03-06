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

    //
    // Attributes
    //

    private final Gson gson;

    //
    // Static Variable
    //

    static String SPECTATOR_TIME;

    //
    // Constructor
    //

    /**
     * Create the UI controller to handle all
     * {@code GET /spectator/stopWatching} HTTP requests.
     *
     * @param gson how to access a game
     */
    public PostSpectatorCheckTurn(final Gson gson) {
        // Sets and validates the gson attribute to not be null
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }


    /**
     * {@inheritDoc}
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     * @return updates gson with if a message is needed or not true or false
     */
    @Override
    public Object handle(Request request, Response response) {
        if (PostCheckTurn.timer != null) {
            // Gets the time
            long passedTimeInSeconds = PostCheckTurn.timer.time(TimeUnit.SECONDS);
            // Displays timer message
            SPECTATOR_TIME = String.format("Last turn was about %d seconds ago.",
                    passedTimeInSeconds);
            response.body(gson.toJson(SPECTATOR_TIME));
            return gson.toJson(Message.info(SPECTATOR_TIME));
        }
        // Return false
        return gson.toJson(Message.info("false"));
    }
}
