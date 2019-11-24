package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetSpectatorRouteTest {

    //CuT
    private GetSpectatorRoute CuT;

    //Mock Object
    private Request request;
    private Response response;
    private Session session;
    private Gson gson;
    private GameManager gameManager;
    private PlayerLobby lobby;
    private CheckersGame mockGame;
    private Player p1;
    private Player p2;
    private TemplateEngine engine;

    @BeforeEach
    void construct() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        lobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        mockGame = mock(CheckersGame.class);
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        gson = new Gson();
        engine = mock(TemplateEngine.class);

        CuT = new GetSpectatorRoute(engine, lobby, gameManager);
        String gameIdString = "3";
        when(request.queryParams("gameID")).thenReturn(gameIdString);
    }

    @Test
    public void testHandle() {
    }

    @Test
    public void testSpectator() {
    }
}