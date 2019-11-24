package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private Player player;
    private Player player2;
    private Player AI;

    @BeforeEach
    void construct(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        lobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        mockGame = mock(CheckersGame.class);
        player = mock(Player.class);
        when(player.getName()).thenReturn("P1");
        player2 = mock(Player.class);
        when(player2.getName()).thenReturn("P2");
        AI = mock(Player.class);
        when(AI.getName()).thenReturn("AI");
        gson = new Gson();

        CuT = new PostSubmitTurn(lobby, gameManager, gson);
        String gameIdString = "3";
        when(request.queryParams("gameID")).thenReturn(gameIdString);
        when(gameManager.getGame(player)).thenReturn(mockGame);
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

    @Test
    void test_validMove(){
        when(mockGame.getLastMove()).thenReturn(mock(Move.class));
        String expected = "{\"text\":\"Valid Move!\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        }catch (Exception e){
            //squahs
        }
    }

    @Test
    void test_TwoValidJumps(){
        when(mockGame.getLastMove()).thenReturn(mock(Move.class));
        when(mock(Move.class).hasPiece()).thenReturn(true);
    }

    @Test
    void test_BlockedOrCaptured(){
        String test = "test";
        assertEquals(" has blocked all pieces, test",
                CuT.BlockedOrCaptured(12, test));
        assertEquals(" has captured all pieces, test",
                CuT.BlockedOrCaptured(0, test));
    }

    @Test
    void test_GameOverWinRed(){
        when(mockGame.getRedPlayer()).thenReturn(player);

        MoveValidator mv = mock(MoveValidator.class);
        Boolean win = true;
        when(mv.isOut(mv.REDPLAYER)).thenReturn(false);
        when(mv.isOut(mv.WHITEPLAYER)).thenReturn(true);
        when(mv.getCount(mv.WHITEPLAYER)).thenReturn(0);
        String expected = "{Msg INFO 'P1 has captured all pieces, you won!'}";
        assertEquals(expected, CuT.gameOver(mv, mockGame, session, win).toString());
    }
    @Test
    void test_GameOverWinWhite(){
        when(mockGame.getWhitePlayer()).thenReturn(player2);
        MoveValidator mv = mock(MoveValidator.class);
        Boolean win = true;
        when(mv.isOut(mv.REDPLAYER)).thenReturn(true);
        when(mv.isOut(mv.WHITEPLAYER)).thenReturn(false);
        when(mv.getCount(mv.REDPLAYER)).thenReturn(0);
        String expected = "{Msg INFO 'P2 has captured all pieces, you won!'}";
        assertEquals(expected, CuT.gameOver(mv, mockGame, session, win).toString());
    }

    @Test
    void test_GameOverLoseWhite(){
        when(mockGame.getWhitePlayer()).thenReturn(player2);
        when(mockGame.getRedPlayer()).thenReturn(player);
        MoveValidator mv = mock(MoveValidator.class);
        Boolean win = false;
        when(mv.isOut(mv.REDPLAYER)).thenReturn(false);
        when(mv.isOut(mv.WHITEPLAYER)).thenReturn(true);
        when(mv.getCount(mv.WHITEPLAYER)).thenReturn(0);
        String expected = "{Msg INFO 'P1 has captured all pieces, you lost.'}";
        assertEquals(expected, CuT.gameOver(mv, mockGame, session, win).toString());
    }

    @Test
    void test_GameOverLoseRed(){
        when(mockGame.getWhitePlayer()).thenReturn(player2);
        when(mockGame.getRedPlayer()).thenReturn(player);
        MoveValidator mv = mock(MoveValidator.class);
        Boolean win = false;
        when(mv.isOut(mv.REDPLAYER)).thenReturn(true);
        when(mv.isOut(mv.WHITEPLAYER)).thenReturn(false);
        when(mv.getCount(mv.REDPLAYER)).thenReturn(0);
        String expected = "{Msg INFO 'P2 has captured all pieces, you lost.'}";
        assertEquals(expected, CuT.gameOver(mv, mockGame, session, win).toString());
    }

    @Test
    void test_GameOverTie(){
        when(mockGame.getWhitePlayer()).thenReturn(player2);
        when(mockGame.getRedPlayer()).thenReturn(player);
        MoveValidator mv = mock(MoveValidator.class);
        Boolean win = false;
        when(mv.isOut(mv.REDPLAYER)).thenReturn(true);
        when(mv.isOut(mv.WHITEPLAYER)).thenReturn(true);
        String expected = "{Msg INFO 'The game has ended in a tie.'}";
        assertEquals(expected, CuT.gameOver(mv, mockGame, session, win).toString());
    }

    @Test
    void test_moveNotMade() {
        MoveValidator mv = mock(MoveValidator.class);
        when(mockGame.getWhitePlayer()).thenReturn(player2);
        when(session.attribute("Player")).thenReturn(player);
        when(gameManager.getGame(player)).thenReturn(mockGame);
        when(mockGame.getLastMove()).thenReturn(null);
        String expected = "{Make move first'}";
        System.out.println(CuT.handle(request, response).toString());
        assertEquals(expected, CuT.handle(request, response).toString());

    }
}
