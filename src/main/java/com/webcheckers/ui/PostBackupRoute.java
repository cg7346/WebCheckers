package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

/**
 * this class is the post backup route
 */

public class PostBackupRoute implements Route {

    private GameManager gameManager;
    private final Gson gson;

    /**
     * the constructor the the route
     * @param gameManager a Game Manager
     * @param gson the GSON
     */
    public PostBackupRoute(GameManager gameManager, Gson gson)
    {
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    /**
     * this function handles the call
     * @param request request
     * @param response response
     * @return Response Message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception
    {
        String gameIDString = request.queryParams("gameID");
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
        MoveValidator moveValidator = new MoveValidator(game);
        moveValidator.backUpMove();

        Message responseMessage = Message.info("BACK IT UP!");
        response.body(gson.toJson(responseMessage));

        return gson.toJson(responseMessage);
    }
}
