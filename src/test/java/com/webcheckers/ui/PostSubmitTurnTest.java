package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UI-tier")
public class PostSubmitTurnTest {

    //CuT
    private PostSubmitTurn CuT;

    //Mock Object
    private Request request;
    private Response response;
    private Session session;
    private Gson gson;
    private GameManager gameManager;
    private PlayerLobby lobby;
    private CheckersGame mockGame;

    @BeforeEach
    void construct(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        lobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        mockGame = mock(CheckersGame.class);
        gson = new Gson();

        CuT = new PostSubmitTurn(lobby, gameManager, gson);
        String gameIdString = "3";
        when(request.queryParams("gameID")).thenReturn(gameIdString);
        when(gameManager.getGame(Integer.parseInt(gameIdString))).thenReturn(mockGame);
    }

    @Test
    void test_lastMoveIsNull(){
        when(mockGame.getLastMove()).thenReturn(null);
        String expected = "{\"text\":\"Make move first\",\"type\":\"ERROR\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        }catch (Exception e){
            //squahs
        }
    }
}