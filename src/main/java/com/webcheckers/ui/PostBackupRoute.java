package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class PostBackupRoute implements Route {

    private GameManager gameManager;
    private final Gson gson;

    public PostBackupRoute(GameManager gameManager, Gson gson)
    {
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception
    {
        String gameIDString = request.queryParams("gameID");
        CheckersGame game = gameManager.getGame(Integer.parseInt(gameIDString));
        Move lastMove = game.getLastMove();


        return null;
    }
}
