package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The {@code POST /resignGame} route handler
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostResignGame implements Route {

    //
    // Constants
    //

    // Values used in the view-model map for rendering the game view after a guess/
    private static final String RESIGN = "%s has resigned.";
    private static final String TITLE_ATTR = "title";
    private static final String TITLE_ATTR_MSG = "Game Title";
    private static final String CURRENT_USER_ATTR = "currentUser";
    private static final String BOARD_ATTR = "board";
    private static final String COLOR_ATTR = "activeColor";
    private static final String RED_PLAYER_ATTR = "redPlayer";
    private static final String WHITE_PLAYER_ATTR = "whitePlayer";
    private static final String IS_GAME_OVER = "isGameOver";
    private static final String GAME_OVER_ATTR = "gameOverMessage";
    private static final Message GAME_OVER_ATTR_MSG = Message.info("The game is over"); /* Get the game over message */
    private static final String VIEW_NAME = "game.ftl";

    //
    // Static Methods
    //

    /**
     * A String representing how the game ended. Such as:
     * <p>
     * Bryan has captured all of the pieces.
     * Fred has resigned.
     *
     * @return the message that will be displayed when a player resigns
     */
    public static Message resignMessage(Player player) {
        return Message.info(String.format(RESIGN, player.getName()));
    }

    //
    // Attributes
    //
    private final GameManager gameManager;
    private final Gson gson;


    //
    // Constructor
    //
    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gameManager
     * @param gson
     * @throws NoSuchElementException when the {@code gameManager} or {@code gson} parameter is null
     */
    public PostResignGame(GameManager gameManager, Gson gson) {
        // Makes sure the gameManager and gson objects are not null
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        Objects.requireNonNull(gson, "gson must not be null");

        // Sets the gameManager attribute
        this.gameManager = gameManager;
        // Sets the gson attribute
        this.gson = gson;
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
        // Gets the seesion
        Session session = request.session();
        // Gets the session's player attribute
        Player currentPlayer = session.attribute("Player");
        // Gets the game ID
        String gameIDString = request.queryParams("gameID");
        // Gets the checkers game
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
        Player winner = null;
        // Mode options for the JSON
        final Map<String, Object> modeOptions = new HashMap<>(2);
        // Gets the red player
        Player redPlayer = game.getRedPlayer();
        // Gets the while player
        Player whitePlayer = game.getWhitePlayer();
        // start building a mode options
        modeOptions.put(IS_GAME_OVER, true);
        modeOptions.put(GAME_OVER_ATTR, resignMessage(currentPlayer));
        // If the current player is the red player and they resign, then the
        // white player wins
        if (currentPlayer == redPlayer){
            //TODO: Set whitePlayer as winner
            winner = whitePlayer;
         // If the current player is the white player and they resign, then the
         // red player wins
        } else if (currentPlayer == whitePlayer) {
            //TODO: Set redPlayer as winner
            winner = redPlayer;
        }
        // Sends the resign message to the html body
        response.body(gson.toJson(resignMessage(currentPlayer)));
        // Returns the JSON mode options
        return gson.toJson(modeOptions);
    }
}

