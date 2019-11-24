package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The {@code GET /spectator/stopWatching} route handler
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella<a/>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo<a/>
 */
public class GetSpectatorStopWatching implements Route {

    //
    // Attributes
    //

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    //
    // Constants
    //

    static final String SPECTATOR_END = "Spectating mode has ended.";

    //
    // Static Variables
    //

    static Boolean visited = false;

    //
    // Constructor
    //

    /**
     * Create the UI controller to handle all {@code GET /spectator/stopWatching} HTTP requests.
     *
     * @param gameManager how to access a game
     */
    public GetSpectatorStopWatching(final GameManager gameManager, final TemplateEngine templateEngine) {
        this.gameManager = Objects.requireNonNull(gameManager, "Game Manager must not be null.");
        // Sets and validates the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }


    /**
     * {@inheritDoc}
     * redirects back home
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) {
        visited = true;
        Player spectator = request.session().attribute("Player");
        Map<String, Object> vm = new HashMap<>();

        gameManager.removeSpectator(gameManager.getGame(spectator), spectator);

        response.redirect(WebServer.HOME_URL);
        halt();

        return null;
    }
}
