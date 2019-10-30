package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;

import static spark.Spark.halt;

/**
 * The UI controller to GET the Game page
 *
 * @author <a href='mailto:jil4009@rit.edu'>Jacquelyn Leung</a>
 * @author <a href='mailto:mmb2582@rit.edu'>Mallory Bridge</a>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class GetGameRoute implements Route {

    static final String TITLE_ATTR = "title";
    static final String TITLE_ATTR_MSG = "Game Title";
    static final String CURRENT_USER_ATTR = "currentUser";
    static final String START_ATTR = "message";
    static final Message START_ATTR_MSG = Message.info("A game has been started");
    static final String BOARD_ATTR = "board";
    static final String COLOR_ATTR = "activeColor";
    static final String RED_PLAYER_ATTR = "redPlayer";
    static final String WHITE_PLAYER_ATTR = "whitePlayer";
    static final String GAME_OVER_ATTR = "gameOverMessage";
    static final Message GAME_OVER_ATTR_MSG = Message.info("The game is over"); /* Get the game over message */
    static final String VIEW_NAME = "game.ftl";
    static final String PLAYER_IN_GAME= "Chosen player is already in a game.";
    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_ERR = "message error";
    static final String OPP_USER = "opp_user";
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private final Gson gson;
    enum viewMode {PLAY, SPECTATOR,REPLAY}
    enum activeColor {RED, WHITE}

    static CheckersGame game;

    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby,
                        GameManager gameManager, Gson gson) {
        // validation
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        this.gson = gson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handle(Request request, Response response) {
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        CheckersGame game = null;
        Map<String, Object> vm = new HashMap<>();
        //that means a player click on another one, get their name and make a game
        if (!request.queryParams().isEmpty()){
            String opponentName = request.queryParams(OPP_USER);
                Player chosenOpponent = playerLobby.findPlayer(opponentName);
                if (playerLobby.isInGame(chosenOpponent)|| chosenOpponent == null ){
                    //we will send an error
                    Message er = Message.error(PLAYER_IN_GAME);
                    session.attribute(MESSAGE_ERR, er);
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
                else{game = gameManager.makeGame(currentPlayer, chosenOpponent);}
        //you are the person click on, find your game
        }else{ game = gameManager.getGame(currentPlayer);}
        Player redPlayer = game.getRedPlayer();
        Player whitePlayer = game.getWhitePlayer();
        BoardView board = new BoardView(currentPlayer, game);
        vm.put(TITLE_ATTR, TITLE_ATTR_MSG);
        vm.put(CURRENT_USER_ATTR, currentPlayer);
        vm.put("viewMode", viewMode.PLAY);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", false);
        modeOptions.put(GAME_OVER_ATTR, GAME_OVER_ATTR_MSG);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        vm.put(RED_PLAYER_ATTR, redPlayer);
        vm.put(WHITE_PLAYER_ATTR, whitePlayer);
        vm.put(COLOR_ATTR, activeColor.RED);
        vm.put(BOARD_ATTR, board);
        vm.put(START_ATTR, START_ATTR_MSG);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }

    /**
     * this handles errors in the model view
     * @param vm
     * @param message
     * @param currentPlayer
     * @return new Model and View
     * */
    private ModelAndView error(final Map<String, Object> vm, final Message message, final Player currentPlayer) {
        vm.put("title", GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.CURRENT_USER, currentPlayer);
        vm.put(PostSignInRoute.CURRENT, currentPlayer.getName());
        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        vm.put(GetHomeRoute.USERS_LIST, playerLobby.getUsernames());
        vm.put(MESSAGE_ATTR, message);
        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    }
}
