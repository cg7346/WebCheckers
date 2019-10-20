package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.*;


/**
 * The {@code POST /signout} route handler
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostSignOutRoute implements Route {

    //
    // Attributes
    //

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
    PostSignOutRoute(TemplateEngine templateEngine, PlayerLobby playerLobby) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        // instantiating attributes
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.util.NoSuchElementException
     *      when an invalid username is returned
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object handle(Request request, Response response) {
        // start building a View-Model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);
        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        final Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        List<Player> players = playerLobby.getPlayers();
        System.out.println(currentPlayer.getName());
        players.remove(currentPlayer);
        vm.put(GetHomeRoute.CURRENT_USER, null);
        System.out.println(playerLobby.getUsernames());
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
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }

}
