package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import com.webcheckers.model.TimeWatch;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The {@code POST /checkTurn} route handler
 * this checks for the turn of the user
 * @author <a href='mailto:jil4009@rit.edu'>Jackie Leung</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class PostCheckTurn implements Route {

    // Constants
    static final String MESSAGE_ERR = "message error";
    static TimeWatch timer;

    // Attributes
    private final GameManager gameManager;
    private final Gson gson;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param gameManager
     * @param gson
     * @throws NoSuchElementException when the {@code gameManager} or
     * {@code gson} parameter is null
     */
    public PostCheckTurn(GameManager gameManager,Gson gson){
        this.gameManager = Objects.requireNonNull(gameManager,
                "game manager is required");
        this.gson = Objects.requireNonNull(gson,
                "gson is required");
    }

    /**
     * Handles game over
     *
     * @param request is the HTTP request
     * @param response is the HTTP response
     * @param  game is the Checkers game
     * @param message is the game over message
     */
    public void GameOver(Request request, Response response, CheckersGame game,
                         String message) {
        // Gets the session
        Session session = request.session();
        // Sets mode options to game over
        GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
        GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
        // Show message
        response.body(gson.toJson(message));
        // Remove the game
        gameManager.removeGame(game);
        // Show error message
        Message er = Message.error(message);
        session.attribute(MESSAGE_ERR, er);
    }

    /**
     * Checks to see if all the pieces are blocked or captured
     *
     * @param count     the number of pieces left on the board for a player
     * @param winOrlose whether they have either won or lost
     * @return an end of game message
     */
    public static String BlockedOrCaptured(Integer count, String winOrlose){
        String endGame = null;
        // If the count is greater than zero then end game is blocked
        if (count > 0) {
            endGame = " has blocked all pieces, " + winOrlose;
            // If the count is zero then end game is captured
        } else {
            endGame = " has captured all pieces, " + winOrlose;
        }
        return endGame;
    }

    /**
     *this function handles the post check turn
     * @param request is the HTTP request
     * @param response is the HTTP response
     * @return gson
     */
    @Override
    public Object handle(Request request, Response response) {
        // Gets the session
        Session session = request.session();
        // Gets the player
        Player currentPlayer = session.attribute("Player");
        // Gets the checkers game
        CheckersGame game = gameManager.getGame(currentPlayer);
        String message = null;
        // If game is not null, handle
        if (game != null) {
            // If a player has resigned a game, go to game over
            if (game.getResignedPlayer() != null) {
                message = "Opponent has resigned. You won!";
                GameOver(request, response, game, message);
                // Return true
                return gson.toJson(Message.info("true"));
            // If the game is a tie, go to game over
            } else if (game.isTie()) {
                message = "The game has ended in a tie.";
                GameOver(request, response, game, message);
                // Return true
                return gson.toJson(Message.info("true"));
            // If the game is over, go to game over
            } else if (game.isGameOver()) {
                String endGame;
                // If white won, show message that red lost
                if (game.getWinner() == game.getWhitePlayer()) {
                    endGame = BlockedOrCaptured(MoveValidator.redCount,
                            "you lost.");
                // If red won, show message that white lost
                } else {
                    endGame = BlockedOrCaptured(MoveValidator.whiteCount,
                            "you lost.");
                }
                // Go to game over
                message = game.getWinner().getName() + endGame;
                GameOver(request, response, game, message);
                // Return true
                return gson.toJson(Message.info("true"));
            // If active player is the current player, return true
            } else if (game.getActivePlayer().equals(currentPlayer)) {
                // Set timer for the spectator
                timer = TimeWatch.start();
                return gson.toJson(Message.info("true"));
            // If mode options is null, return true
            } else if (GetGameRoute.modeOptionsAsJSON != null) {
                return gson.toJson(Message.info("true"));
            // Otherwise, return false
            } else {
                return gson.toJson(Message.info("false"));
            }

        }
        // If game is null, then the game is with an AI, therefore return
        // game over AI message
        message = "AI" + BlockedOrCaptured(MoveValidator.redCount,
                "you lost.");
        Message er = Message.error(message);
        session.attribute(MESSAGE_ERR, er);
        // Return true
        return gson.toJson(Message.info("true"));
    }
}
