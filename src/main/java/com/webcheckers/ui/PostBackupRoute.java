package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;

/**
 * The {@code POST /backupMove} route handler
 *
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 * @author <a href='mailto:mmb2582@rit.edu'>Mallory Bridge</a>
 */

public class PostBackupRoute implements Route {

    //
    // Attributes
    //

    private GameManager gameManager;
    private final Gson gson;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /backupMove} route handler.
     *
     * @param gameManager a Game Manager
     * @param gson the GSON
     */
    public PostBackupRoute(GameManager gameManager, Gson gson)
    {
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    /**
     * {@inheritDoc}
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return updates gson with if a response message
     *          BACK IT UP!
     */
    @Override
    public Object handle(Request request, Response response)
    {
        Session session = request.session();
        Player player = session.attribute("Player");
        CheckersGame game = gameManager.getGame(player);
        MoveValidator moveValidator = mvMaker(game);
        moveValidator.backUpMove();

        Message responseMessage = Message.info("BACK IT UP!");
        response.body(gson.toJson(responseMessage));

        return gson.toJson(responseMessage);
    }

    /**
     * Factory for making that moveValidator
     * It makes testing a lot easier and replaces
     * the new statement that used to be in the handle
     * @param game game to make MoveValidator with
     * @return a brand new MoveValidator
     */
    public MoveValidator mvMaker(CheckersGame game){
        return new MoveValidator(game);
    }
}
