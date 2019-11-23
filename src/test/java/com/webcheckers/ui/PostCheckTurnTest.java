package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        CuT = new PostCheckTurn(gameManager, gson);
        when(session.attribute("Player")).thenReturn(p1);
        when(gameManager.getGame(p1)).thenReturn(mockGame);
    }

    @Test
    void test_nullGame(){
        Player mockP = mock(Player.class);
        when(session.attribute("Player")).thenReturn(mockP);
        when(gameManager.getGame(mockP)).thenReturn(null);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void opponentResigned(){
        when(mockGame.getResignedPlayer()).thenReturn(p2);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void tieGame(){
        when(mockGame.getResignedPlayer()).thenReturn(null);
        when(mockGame.isTie()).thenReturn(true);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void isActivePlayer(){
        when(mockGame.getResignedPlayer()).thenReturn(null);
        when(mockGame.isTie()).thenReturn(false);
        when(mockGame.isGameOver()).thenReturn(false);
        when(mockGame.getActivePlayer()).thenReturn(p1);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void modeOptionsNotNull(){
        when(mockGame.getResignedPlayer()).thenReturn(null);
        when(mockGame.isTie()).thenReturn(false);
        when(mockGame.isGameOver()).thenReturn(false);
        when(mockGame.getActivePlayer()).thenReturn(p2);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void testGameOver(){
        String testString = "Testing";
        Message expected = Message.error(testString);
        CuT.GameOver(request, response, mockGame, testString);
        assertEquals(true, GetGameRoute.modeOptionsAsJSON.get("isGameOver"));
        assertEquals(testString, GetGameRoute.modeOptionsAsJSON.get("gameOverMessage"));
    }
    @Test
    void test_isNotActivePlayer(){
        when(mockGame.getResignedPlayer()).thenReturn(null);
        when(mockGame.isTie()).thenReturn(false);
        when(mockGame.isGameOver()).thenReturn(false);
        when(session.attribute("Player")).thenReturn(p1);
        when(mockGame.getActivePlayer()).thenReturn(p2);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void gameOverWhiteWin(){
        when(mockGame.getResignedPlayer()).thenReturn(null);
        when(mockGame.isTie()).thenReturn(false);
        when(mockGame.isGameOver()).thenReturn(true);
        when(mockGame.getWinner()).thenReturn(p2);
        when(mockGame.getWhitePlayer()).thenReturn(p2);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }

    }

    @Test
    void gameOverRedWin(){
        //String expectedWin = "P1 has blocked all pieces "
        when(p1.getName()).thenReturn("P1");
        when(mockGame.getResignedPlayer()).thenReturn(null);
        when(mockGame.isTie()).thenReturn(false);
        when(mockGame.isGameOver()).thenReturn(true);
        when(mockGame.getWinner()).thenReturn(p1);
        when(mockGame.getWhitePlayer()).thenReturn(p2);
        String expected = "{\"text\":\"true\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o);
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void test_blockOrCaptured(){
        String test = "test";
        assertEquals(" has blocked all pieces, test",
                CuT.BlockedOrCaptured(12, test));
        assertEquals(" has captured all pieces, test",
                CuT.BlockedOrCaptured(0, test));
    }
}
