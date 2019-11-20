package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The UI controller to GET the Spectator page
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella<a/>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class GetSpectatorRoute implements Route {

    private static final Message GAME_MSG = Message.info("This is not a game");


    //    private final TemplateEngine templateEngine;
//    private final CheckersGame checkersGame;
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * Create the UI controller to handle all {@code GET /spectator/game} HTTP requests.
     * <p>
     * //     * @param checkersGame holds all the information about players playing a game
     *
     * @param playerLobby holds all the information about players signed in
     * @param gameManager how to access a game
     */
    public GetSpectatorRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager) {
        // Sets and validate the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "Player Lobby must not be null.");
        this.gameManager = Objects.requireNonNull(gameManager, "Game Manager must not be null.");
//        this.checkersGame = Objects.requireNonNull(checkersGame, "Checkers Game must not be null.");
    }

    /**
     * Render the WebCheckers Game page for a spectator
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) {
        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game");

        // retrieve the PlayerServices from the session
        Player current = request.session().attribute("Player");
        //If the player enters this page without being signed in or in a valid game,
        if (current == null) {
            vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
            //redirect back to home page
            response.redirect(WebServer.HOME_URL);
            return templateEngine.render(new ModelAndView(vm, "home.ftl"));
        } else if (gameManager.getGame(current) == null) {
            String opponent = request.queryParams(GetGameRoute.OPP_USER);

            // render the View
            return templateEngine.render(new ModelAndView(vm, "game.ftl"));

        }
        return null;

    }

    enum viewMode {PLAY, SPECTATOR}
}

