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
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.webcheckers.ui.GetGameRoute.game;
import static spark.Spark.halt;


/**
 * The {@code POST /EndGame} route handler
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostEndGame implements Route {

    //
    // Constants
    //

    // Values used in the view-model map for rendering the game view after a guess/
    private static final String PIECES_CAP = "You are playing a game of checkers with %s\n" +
            "%s has captured all pieces.";
    private static final String RESIGN = "%s has resigned.";
    private static final String TITLE_ATTR = "title";
    private static final String TITLE_ATTR_MSG = "Game Title";
    private static final String CURRENT_USER_ATTR = "currentUser";
    private static final String BOARD_ATTR = "board";
    private static final String COLOR_ATTR = "activeColor";
    private static final String RED_PLAYER_ATTR = "redPlayer";
    private static final String WHITE_PLAYER_ATTR = "whitePlayer";
    private static final String GAME_OVER_ATTR = "gameOverMessage";
    private static final Message GAME_OVER_ATTR_MSG = Message.info("The game is over"); /* Get the game over message */
    private static final String VIEW_NAME = "game.ftl";
    private static final String MESSAGE_ATTR = "message";
    private static final String MESSAGE_ERR = "message error";
    private static final String OPP_USER = "opp_user";
    enum viewMode {PLAY, SPECTATOR,REPLAY}
    enum activeColor {RED, WHITE}

    //
    // Static Methods
    //
//    private final TemplateEngine templateEngine;

    //
    // Attributes
    //
//    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    private final GameManager gameManager;
    private final Gson gson;


    //
    // Constructor
    //
    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gameManager used to end a game of checkers
     * @throws NoSuchElementException when the {@code Player} or {@code templateEngine} parameter is null
     */
    public PostEndGame(TemplateEngine templateEngine, GameManager gameManager, Gson gson) {
        // validation
        Objects.requireNonNull(gameManager, "gameManger must not be null");
//        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
//        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");

        this.templateEngine = templateEngine;
        this.gameManager = gameManager;
        this.gson = gson;
    }

    /**
     * A String representing how the game ended. Such as:
     * <p>
     * Bryan has captured all of the pieces.
     * Fred has resigned.
     *
     * @return the message that will be displayed when the pieces are cap
     */
    public static Message piecesCapMessage() {
//        TODO: String.Format(PIECES_CAP, currentPlayer, opponent)
        return Message.info(PIECES_CAP);
    }
    /**
     * A String representing how the game ended. Such as:
     * <p>
     * Bryan has captured all of the pieces.
     * Fred has resigned.
     *
     * @return the message that will be displayed when a player resigns
     */
    public static Message resignMessage() {
//        TODO: String.Format(RESIGN, opponent)
        return Message.info(RESIGN);
    }

    /**
     * {@inheritDoc}
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return templateEngine to render a view or null
     * @throws java.util.NoSuchElementException when an invalid username is returned
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        final Map<String, Object> vm = new HashMap<>(2);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        Player redPlayer = game.getRedPlayer();
        Player whitePlayer = game.getWhitePlayer();
        BoardView board = new BoardView(currentPlayer, game);
        vm.put(TITLE_ATTR, TITLE_ATTR_MSG);
        vm.put(CURRENT_USER_ATTR, currentPlayer);
        vm.put("viewMode", GetGameRoute.viewMode.PLAY);
        modeOptions.put("isGameOver", false);
        modeOptions.put(GAME_OVER_ATTR, GAME_OVER_ATTR_MSG);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        vm.put(RED_PLAYER_ATTR, redPlayer);
        vm.put(WHITE_PLAYER_ATTR, whitePlayer);
        vm.put(COLOR_ATTR, GetGameRoute.activeColor.RED);
        vm.put(BOARD_ATTR, board);
        // start buildiing a View-Model
        modeOptions.put("isGameOver", true);
        // TODO: IF statement for certain gameOverMessage
        modeOptions.put(GAME_OVER_ATTR, resignMessage());
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
