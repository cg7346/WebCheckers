package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.*;

/**
 * The {@code POST /signout} route handler
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostSignOutRoute implements Route {

    //
    // Attributes
    //

    private final GameManager gameManager;
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /signout} route handler.
     *
     * @param templateEngine
     *    template engine to use for rendering HTML page
     *
     * @throws NoSuchElementException
     *    when the {@code Player} or {@code templateEngine} parameter is null
     */
    PostSignOutRoute(GameManager gameManager, TemplateEngine templateEngine, PlayerLobby playerLobby) {
        // Sets and validates the templateEngine attribute to not be null
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        // Sets and validates the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        // Sets and validates the playerLobby attribute to not be null
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");

    }

    /**
     * {@inheritDoc}
     *      when an invalid username is returned
     * @param request the HTTP request
     * @param response the HTTP response
     * @return renders home
     */
    @Override
    public Object handle(Request request, Response response) {
        // Remove the signed-in player from the PlayerLobby
        final Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        List<Player> players = playerLobby.getPlayers();
        players.remove(currentPlayer);
        // start building a view model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);
        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        vm.put(GetHomeRoute.CURRENT_USER, null);
        // Sets the current player to null
        session.attribute("Player", null);
        // Updates the players online count
        int playerCount = playerLobby.getPlayers().size();
        if (playerCount == 0) {
            vm.put(GetHomeRoute.PLAYERS_COUNT, GetHomeRoute.NO_PLAYERS);
        } else if (playerCount == 1) {
            String count = String.format(GetHomeRoute.PLAYER, playerCount);
            vm.put(GetHomeRoute.PLAYERS_COUNT, count);
        } else {
            String count = String.format(GetHomeRoute.PLAYERS, playerCount);
            vm.put(GetHomeRoute.PLAYERS_COUNT, count);
        }

        // Redirects to home, signed out
        response.redirect(WebServer.HOME_URL);
        // Renders home
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }
}
