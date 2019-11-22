package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The UI controller to POST the Spectator page
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella<a/>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class PostSpectatorCheckTurn implements Route {

    private final GameManager gameManager;


    /**
     * Create the UI controller to handle all {@code GET /spectator/stopWatching} HTTP requests.
     * <p>
     * //     * @param checkersGame holds all the information about players playing a game
     *
     * @param gameManager how to access a game
     */
    public PostSpectatorCheckTurn(final GameManager gameManager) {
        this.gameManager = Objects.requireNonNull(gameManager, "Game Manager must not be null.");
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

//        gameManager.removeSpectator(spectator);
//        request.session().attribute("message", new Message(String.format("Spectating mode has ended."),
//                                                                    Message.Type.INFO));
        vm.put("message", "Spectating mode has ended.");
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
