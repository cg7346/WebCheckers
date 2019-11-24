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

import java.util.Objects;

/**
 * The (@code POST /validateMove) route handler.
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
public class PostValidateMove implements Route {

    //
    // Attributes
    //

    // TODO: check if this is needed
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final Gson gson;

    //
    // Constructor
    //

    /**
     * Create the UI controller to handle all {@code POST /validateMove} HTTP requests.
     *
     * @param playerLobby   the list of players playing the WebCheckers game
     * @param gameManager   handles all the checker games
     * @param gson  is the JSON
     */
    public PostValidateMove(PlayerLobby playerLobby, GameManager gameManager, Gson gson){
        this.playerLobby = Objects.requireNonNull(playerLobby, "player lobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     * @return updates gson with message if the move is valid or not
     */
    @Override
    public Object handle(Request request, Response response) {
        String moveString = request.queryParams("actionData");
        String gameIdString = request.queryParams("gameID");
        Move move = gson.fromJson(moveString, Move.class);
        Session session = request.session();
        Player player = session.attribute("Player");
        CheckersGame game = gameManager.getGame(player);
        MoveValidator moveValidator = new MoveValidator(game);
        String moveResponse = moveValidator.isInMoves(move);
        Message responseMessage = null;
        if(moveResponse.equals(moveValidator.validMove)){
            responseMessage = Message.info(moveResponse);
            game.keepLastMove(move);
            game.completeMove();
        }else {
            responseMessage = Message.error(moveResponse);
        }
        response.body(gson.toJson(responseMessage));
        return gson.toJson(responseMessage);
    }
}
