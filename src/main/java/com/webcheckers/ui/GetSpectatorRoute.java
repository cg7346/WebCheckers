package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The UI controller to GET the Spectator page
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella<a/>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class GetSpectatorRoute implements Route {

    private static final Message GAME_MSG = Message.info("You're a spectator!");


    //    private final TemplateEngine templateEngine;
//    private final CheckersGame checkersGame;
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    static final String SPECTATOR = "spec_user";
    static Boolean specEndGame;

    // View name
    static final String VIEW_NAME = "game.ftl";
    private final Gson gson;

    /**
     * Create the UI controller to handle all {@code GET /spectator/game} HTTP requests.
     * <p>
     * //     * @param checkersGame holds all the information about players playing a game
     *
     * @param playerLobby holds all the information about players signed in
     * @param gameManager how to access a game
     */
    public GetSpectatorRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby,
                             final GameManager gameManager, Gson gson) {
        // Sets and validate the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "Player Lobby must not be null.");
        this.gameManager = Objects.requireNonNull(gameManager, "Game Manager must not be null.");
        this.gson = gson;
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
        vm.put("viewMode", GetGameRoute.viewMode.SPECTATOR);

        // retrieve the Player from the session
        Player spectator = session.attribute("Player");

//        CheckersGame checkersGame = new CheckersGame(redPlayer, whitePlayer, gameID)
        ModelAndView mv;
        //If the player enters this page without being signed in or in a valid game,
        if (spectator == null) {
            vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
            //redirect back to home page
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        } else {
            mv = spectator(gameManager.activeGames(), vm, spectator, request, response);
            if (mv == null) {
                return null;
            } else {
                return templateEngine.render(mv);
            }
        }
    }

    /**
     * Sets the gameList variable to the spectator mode
     *
     * @param gameList is the list of games online
     * @param vm       is the view model
     * @param player   is the current player
     * @return new model and view
     */
    public ModelAndView spectator(HashMap<CheckersGame, String> gameList, Map<String, Object> vm,
                                  final Player player, Request request, Response response) {

        Session session = request.session();
        // Displays the welcome message
        vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);

        // Displays the players online title
        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);

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

        vm.put(GetHomeRoute.GAME_LIST, gameList);
        vm.put(GetHomeRoute.SPECTATOR, player.isSpectating());
        // retrieve request parameter
        final String gameNum = request.queryParams(SPECTATOR);
        CheckersGame game = gameManager.getGame(gameNum);
        if (game != null) {
            String gameID = game.getGameID();
            Player redPlayer = game.getRedPlayer();
            Player whitePlayer = game.getWhitePlayer();
            BoardView board = new BoardView(player, game);
            vm.put(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE_ATTR_MSG);
            vm.put(GetGameRoute.GAME_ID_ATTR, gameID);
            vm.put(GetGameRoute.CURRENT_USER_ATTR, game.getActivePlayer());
            vm.put("viewMode", GetGameRoute.viewMode.SPECTATOR);
//        Map<String, Object> modeOptionsAsJSON = new HashMap<>(2);
//        modeOptionsAsJSON.put("isGameOver", false);
//        modeOptionsAsJSON.put(GetGameRoute.GAME_OVER_ATTR, GetGameRoute.GAME_OVER_ATTR_MSG);
//        vm.put("modeOptionsAsJSON", modeOptionsAsJSON);
            if (PostSpectatorCheckTurn.SPECTATOR_TIME == null) {
                vm.put(GetGameRoute.START_ATTR, GetGameRoute.START_ATTR_MSG);
            } else {
                vm.put(GetGameRoute.START_ATTR, Message.info(PostSpectatorCheckTurn.SPECTATOR_TIME));
            }
            vm.put(GetGameRoute.RED_PLAYER_ATTR, redPlayer);
            vm.put(GetGameRoute.WHITE_PLAYER_ATTR, whitePlayer);
            GetGameRoute.activeColor color = (game.getActivePlayer().equals(redPlayer))
                    ? GetGameRoute.activeColor.RED : GetGameRoute.activeColor.WHITE;
            vm.put(GetGameRoute.COLOR_ATTR, color);
            vm.put(GetGameRoute.BOARD_ATTR, board);
            gameManager.addSpectator(game, player);
            // Returns new model and view
            return new ModelAndView(vm, VIEW_NAME);
        }
        specEndGame = true;
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
    }
}