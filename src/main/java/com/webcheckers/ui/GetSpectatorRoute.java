package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.List;
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

    // View name
    static final String VIEW_NAME = "home.ftl";

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
        final Session session = request.session();

        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game");

        // retrieve the PlayerServices from the session
        Player current = request.session().attribute("Player");
        ModelAndView mv;
        //If the player enters this page without being signed in or in a valid game,
        if (current == null) {
            vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
            //redirect back to home page
            response.redirect(WebServer.HOME_URL);
            return templateEngine.render(new ModelAndView(vm, "home.ftl"));
        } else if (gameManager.getGame(current) == null) {
            String opponent = request.queryParams(GetGameRoute.OPP_USER);
//            PlayerServices opponentPlayer = gameCenter.getPlayerById(opponent);
//            if (opponentPlayer == null || opponentPlayer.getCurrentGame() == null) {
//                vm.put("title", "Welcome!");
//                //redirect back to home page
//                response.redirect(WebServer.HOME_URL);
//                return templateEngine.render(new ModelAndView(vm, "home.ftl"));
//            }
//            else {
//                current.addGame(opponentPlayer.getCurrentGame());
//                current.toggleSpectate();
//            }
//        }
//        // retrieve the game this player is entering
//        Game game = current.getCurrentGame();
//        current.setLastKnownColor(game.getActiveColor());
//
//        // get required JS attributes from Game and add them to
//        Map attributes = game.getAttributes(current);
//        vm.putAll(attributes);

            // render the View
            // Adds the player to the playerLobby and redirects them to
            // home, signed in
//            playerLobby.addPlayer(player);
            session.attribute("Spectator", current);
            current.setSpectating(false);
            mv = spectator(gameManager.activeGames(), vm, current);
            response.redirect(WebServer.HOME_URL);
            return templateEngine.render(mv);

//        Player spectator = request.session().attribute("Player");
//        CheckersGame game = gameManager.getGame(spectator);
//        Player gPlayer = game.getRedPlayer();
//
//        gameManager.addSpectator(spectator, gPlayer);
//        response.redirect(WebServer.GAME_URL);
//
//            return null;
        }
        return null;
    }

    /**
     * Sets the gameList variable to the spectator mode
     *
     * @param gameList is the list of players online
     * @param vm       is the view model
     * @param player   is the current player
     * @return new model and view
     */
    public ModelAndView spectator(List<CheckersGame> gameList, Map<String, Object> vm, final Player player) {
        // Displays the welcome message
        vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);

        // Sets the current user to the current player
        vm.put(GetHomeRoute.GAME_LIST, gameList);
        vm.put(GetHomeRoute.SPECTATOR, player.isSpectating());
//        // Displays the players online title
//        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);

        // Returns new model and view
        return new ModelAndView(vm, VIEW_NAME);
    }

}


