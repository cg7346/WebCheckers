package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

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

    // Variables used in the mode options for JSON after the resign button is clicked
    private static final String IS_GAME_OVER = "isGameOver";
    private static final String GAME_OVER_ATTR = "gameOverMessage";
    // Values used in the mode options for JSON after the resign button is clicked
    private static final String RESIGN = "%s has resigned.";

//    private static final String TITLE_ATTR = "title";
//    private static final String TITLE_ATTR_MSG = "Game Title";
//    private static final String CURRENT_USER_ATTR = "currentUser";
//    private static final String BOARD_ATTR = "board";
//    private static final String COLOR_ATTR = "activeColor";
//    private static final String RED_PLAYER_ATTR = "redPlayer";
//    private static final String WHITE_PLAYER_ATTR = "whitePlayer";
//    private static final Message GAME_OVER_ATTR_MSG = Message.info("The game is over"); /* Get the game over message */
//    private static final String VIEW_NAME = "game.ftl";

    //
    // Static Methods
    //

    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param gameManager is the game manager from the game manager class
     * @param gson is the JSON
     * @throws NoSuchElementException when the {@code gameManager} or {@code gson} parameter is null
     */
    public PostResignGame(GameManager gameManager, Gson gson) {
        // Sets and validates the gameManager attribute to not be null
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        // Sets and validates the gson attribute to not be null
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
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
     * Resign message after a player clicks the resign button
     *
     * @return the message that will be displayed when a player resigns
     */
    public static Message resignMessage(Player player) {
        return Message.info(String.format(RESIGN, player.getName()));
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
        String gameIDString = request.queryParams("gameID");
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
        game.setGameOver(true);


        // Gets the session
        Session session = request.session();
        // Gets the session's player attribute
        Player currentPlayer = session.attribute("Player");
//        // Gets the game ID
//        String gameIDString = request.queryParams("gameID");
//        // Gets the checkers game
//        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
//        // Mode options for the JSON
//        final Map<String, Object> modeOptions = new HashMap<>(2);
//        // Gets the red player
//        Player redPlayer = game.getRedPlayer();
//        // Gets the while player
//        Player whitePlayer = game.getWhitePlayer();
//        // start building a mode options
//        modeOptions.put(IS_GAME_OVER, true);
//        modeOptions.put(GAME_OVER_ATTR, resignMessage(currentPlayer));
//        // If the current player is the red player and they resign, then the
//        // white player wins
//        if (currentPlayer == redPlayer){
//            game.setWinners(whitePlayer);
//         // If the current player is the white player and they resign, then the
//         // red player wins
//        } else if (currentPlayer == whitePlayer) {
//            game.setWinners(redPlayer);
//        }
//        // Sends the resign message to the html body
//        response.body(gson.toJson(resignMessage(currentPlayer)));
//        // Returns the JSON mode options
//        gson.toJson(modeOptions);
//        return gson.toJson(resignMessage(currentPlayer));
////        return gson.toJson(new Message());
        return gson.toJson(resignMessage(currentPlayer));
    }
}

