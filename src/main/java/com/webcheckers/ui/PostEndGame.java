package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.util.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;


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
    static final String GAME_OVER_ATTR = "gameOverMessage";
    private final Gson gson;
    private static final String VIEW_NAME = "endgame.ftl";

    //
    // Static Methods
    //
//    private final TemplateEngine templateEngine;

    //
    // Attributes
    //
//    private final PlayerLobby playerLobby;
    private final GameManager gameManager;


    //
    // Constructor
    //
    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gameManager used to end a game of checkers
     * @throws NoSuchElementException when the {@code Player} or {@code templateEngine} parameter is null
     */
    public PostEndGame(GameManager gameManager, Gson gson) {
        // validation
        Objects.requireNonNull(gameManager, "gameManger must not be null");
//        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
//        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");

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
        // start buildiing a View-Model
        final Map<String, Object> vm = new HashMap<>(2);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", true);
        // TODO: IF statement for certain gameOverMessage
        modeOptions.put(GAME_OVER_ATTR, piecesCapMessage());
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        return new ModelAndView(vm, VIEW_NAME);
    }
}
