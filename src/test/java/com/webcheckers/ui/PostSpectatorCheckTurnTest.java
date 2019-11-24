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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//import static org.testng.Assert.*;

public class PostSpectatorCheckTurnTest
{
    //CuT
    private PostSpectatorCheckTurn CuT;

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

        CuT = new PostSpectatorCheckTurn(gameManager, engine, gson);
        String gameIdString = "3";
        when(request.queryParams("gameID")).thenReturn(gameIdString);
    }

    @Test
    void test_nullGame(){
        String expected = "{\"text\":\"Last turn was about 0 seconds ago.\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void test_isNotActivePlayer(){
        when(mockGame.getActivePlayer()).thenReturn(mock(Player.class));
        when(session.attribute("Player")).thenReturn(mock(Player.class));
        String expected = "{\"text\":\"Last turn was about 0 seconds ago.\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        }catch (Exception e){
            //squahs
        }
    }
}