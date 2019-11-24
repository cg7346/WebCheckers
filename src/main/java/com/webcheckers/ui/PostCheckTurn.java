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
     */
    public PostCheckTurn(GameManager gameManager, Gson gson) {
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    /**
     * Called when a game is over
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param game     the current checkers game to check
     * @param message  displaying the game over message to the modeOptionsAsJason
     */
    public void GameOver(Request request, Response response, CheckersGame game, String message) {
        Session session = request.session();
        GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
        GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
        response.body(gson.toJson(message));
        gameManager.removeGame(game);
        Message er = Message.error(message);
        session.attribute(MESSAGE_ERR, er);
    }

    /**
     * Checks to see if the pieces are blocked or all captured
     * @param count the number of pieces left (array)
     * @param winOrlose whether the a player either won or lost
     * @return the end game message
     *          {player} has blocked all pieces. You {Won/Lost}
     *          {player} has captured all pieces. You {Won/Lost}
     */
    public String BlockedOrCaptured(Integer count, String winOrlose){
        String endGame = null;
        if (count > 0) {
            endGame = " has blocked all pieces, " + winOrlose;
        } else {
            endGame = " has captured all pieces, " + winOrlose;
        }
        return endGame;
    }

    /**
     * {@inheritDoc}
     *  this function handles the post check turn
     *      when an invalid username is returned
     * @param request the HTTP request
     * @param response the HTTP response
     * @return updates gson with if a message is needed or not true or false
     */
    @Override
    public Object handle(Request request, Response response) {
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        CheckersGame game = gameManager.getGame(currentPlayer);
        String message = null;
        if (game != null) {
            if (game.getResignedPlayer() != null) {
                message = "Opponent has resigned. You won!";
                GameOver(request, response, game, message);
                return gson.toJson(Message.info("true"));
            } else if (game.isTie()) {
                message = "The game has ended in a tie.";
                GameOver(request, response, game, message);
                return gson.toJson(Message.info("true"));
            } else if (game.isGameOver()) {
                String endGame;
                if (game.getWinner() == game.getWhitePlayer()) {
                    endGame = BlockedOrCaptured(MoveValidator.redCount, "you lost.");
                } else {
                    endGame = BlockedOrCaptured(MoveValidator.whiteCount, "you lost.");
                }
                message = game.getWinner().getName() + endGame;
                GameOver(request, response, game, message);
                return gson.toJson(Message.info("true"));
            } else if (game.getActivePlayer().equals(currentPlayer)) {
                timer = TimeWatch.start();

                return gson.toJson(Message.info("true"));
            } else if (GetGameRoute.modeOptionsAsJSON != null) {
                return gson.toJson(Message.info("true"));
            } else {
                return gson.toJson(Message.info("false"));
            }

        }
        message = "AI" + BlockedOrCaptured(MoveValidator.redCount, "you lost.");
        Message er = Message.error(message);
        session.attribute(MESSAGE_ERR, er);
        return gson.toJson(Message.info("true"));
    }
}
