package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The UI controller to POST the Spectator page
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella<a/>
 */
public class GetSpectatorStopWatching implements Route {

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;


    /**
     * Create the UI controller to handle all {@code GET /spectator/stopWatching} HTTP requests.
     * <p>
     * //     * @param checkersGame holds all the information about players playing a game
     *
     * @param gameManager how to access a game
     */
    public GetSpectatorStopWatching(final GameManager gameManager, final TemplateEngine templateEngine) {
        this.gameManager = Objects.requireNonNull(gameManager, "Game Manager must not be null.");
        // Sets and validates the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }


    /**
     * Render the WebCheckers Home page for a spectator
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) {
        Player spectator = request.session().attribute("Player");
        Map<String, Object> vm = new HashMap<>();

        System.out.println("I'm in!!!!!!!!!!!!!!");
        spectator.setInGame(false);
        gameManager.removeSpectator(gameManager.getGame(spectator), spectator);
        vm.put("message", "Spectating mode has ended.");
        response.redirect(WebServer.HOME_URL);
        halt();
        request.session().attribute("message", Message.info("Spectating mode has ended."));
        return null;
    }
}
