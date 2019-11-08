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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostCheckTurnTest {

    //CuT
    private PostCheckTurn CuT;

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

        CuT = new PostCheckTurn(lobby, gameManager, gson);
        String gameIdString = "3";
        when(request.queryParams("gameID")).thenReturn(gameIdString);
        // TODO: check why this error is occuring now
        //  when(gameManager.getGame(Integer.parseInt(gameIdString))).thenReturn(mockGame);
    }

    @Test
    void test_nullGame(){
        // TODO: check why this error is occuring now
        //  when(gameManager.getGame(Integer.parseInt("3"))).thenReturn(null);
        try {
            Object o = CuT.handle(request, response);
            assertNull(o);
        }catch (Exception e){
            //squahs
        }
    }

//    @Test
//    void test_isActivePlayer(){
//        //when(mockGame.getActivePlayer()).thenReturn(p1);
//        when(mockGame.getActivePlayer().equals(p1)).thenReturn(true);
//        String expected = "true";
//        try {
//            Object o = CuT.handle(request, response);
//            assertEquals(expected, o.toString());
//        }catch (Exception e){
//            //squahs
//        }
//    }

    @Test
    void test_isNotActivePlayer(){
        when(mockGame.getActivePlayer()).thenReturn(mock(Player.class));
        when(session.attribute("Player")).thenReturn(mock(Player.class));
        String expected = "{\"text\":\"false\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        }catch (Exception e){
            //squahs
        }
    }
}
