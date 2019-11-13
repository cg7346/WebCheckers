package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.nio.file.WatchEvent;
import java.util.NoSuchElementException;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The {@code POST /checkTurn} route handler
 * this checks for the turn of the user
 * @author jil4009
 * @author Kelly Vo kdv6978
 */
public class PostCheckTurn implements Route {

    // Constants
    static final String MESSAGE_ERR = "message error";

    // Attributes
    private final GameManager gameManager;
    private final Gson gson;

    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param gameManager
     * @param gson
     * @throws NoSuchElementException when the {@code gameManager} or {@code gson} parameter is null
     */
    public PostCheckTurn(GameManager gameManager,Gson gson){
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

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
     *this function handles the post check turn
     * @param request
     * @param response
     * @return Message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        CheckersGame game = gameManager.getGame(currentPlayer);
        //sometimes having the game undefined breaks the builder
//        if(game == null){
//            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
//            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", game.getWinner().getName() + " has won!");
//            response.body(gson.toJson(Message.info(game.getWinner().getName() + " has won!")));
//            gameManager.removeGame(game);
//        }

        if (game != null) {
            if (game.getResignedPlayer() != null) {
                String message = "Opponent has resigned. You won!";
                GameOver(request, response, game, message);
                return gson.toJson(Message.info("true"));
            } else if (game.isTie()) {
                String message = "The game has ended in a tie.";
                GameOver(request, response, game, message);
                return gson.toJson(Message.info("true"));
            } else if (game.isGameOver()) {
                String endGame;
                if (game.getWinner() == game.getWhitePlayer()) {
                    if (MoveValidator.whiteCount > 0) {
                        endGame = " has blocked all pieces, you lost.";
                    } else {
                        endGame = " has captured all pieces, you lost.";
                    }
                } else {
                    if (MoveValidator.redCount > 0) {
                        endGame = " has blocked all pieces, you lost.";
                    } else {
                        endGame = " has captured all pieces, you lost.";
                    }
                }
                String message = game.getWinner().getName() + endGame;
                GameOver(request, response, game, message);
                return gson.toJson(Message.info("true"));
            } else if (game.getActivePlayer().equals(currentPlayer)) {
                return gson.toJson(Message.info("true"));
            } else if (GetGameRoute.modeOptionsAsJSON != null) {
                return gson.toJson(Message.info("true"));
            } else {
                return gson.toJson(Message.info("false"));
            }

        }
        return gson.toJson(Message.info("true"));
    }
}
