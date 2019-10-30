package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class PostSubmitTurn implements Route {

    private PlayerLobby playerLobby;
    private GameManager gameManager;

    /**
     * Construct a Post Submit Turn
     * @param playerLobby PlayerLobby of the session
     * @param gameManager GameManager of the session
     */
    public PostSubmitTurn(PlayerLobby playerLobby, GameManager gameManager){
        this.playerLobby = Objects.requireNonNull(playerLobby, "player lobby is required");
        this.gameManager = Objects.requireNonNull(gameManager, "game manager is required");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {

        return null;
    }
}
