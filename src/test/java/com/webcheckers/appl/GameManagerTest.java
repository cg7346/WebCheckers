package com.webcheckers.appl;
import com.webcheckers.appl.GameManager;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Unit test for Game Manager
 * @author jil4009
 */
@Tag("Application-tier")

public class GameManagerTest {

    /**
     * test ability to make a game manager
     */
    @Test
    public void testGameManager(){
        GameManager gm = new GameManager();
    }

    /**
     * test ability to make a game
     */
    @Test
    public void testMakeGame(){
        GameManager gm = new GameManager();
        Player p1 = new Player("glados");
        Player p2 = new Player("yoshi");
        Player p3 = new Player("Hello");
        CheckersGame game1 = gm.makeGame(p1, p2);
        CheckersGame game2 = gm.makeGame(p1, p3);
        assertNotNull(game1, "error, please check test and code");
        assertNull(game2, "error, should be null");


    }

    /**
     * Test ability to get a game that the session player is in
     *
     */
    @Test
    public void testGetGame(){
        GameManager gm = new GameManager();
        Player p1 = new Player("123");
        Player p2 = new Player("345");
        Player p3 = new Player("456");
        CheckersGame game = gm.makeGame(p1,p3);
        CheckersGame p1Game = gm.getGame(p1);
        CheckersGame p2Game = gm.getGame(p2);
        CheckersGame gameFromGameID = gm.getGame(game.getActivePlayer());
        assertNull(p2Game, "error, please check test and code");
        assertNotNull(p1Game, "error, please check test and code");
    }

    /**
     * Test ability to see if player is in a particular game
     * checks to see if other player who are not in a game are tests
     */
    @Test
    public void testIsPlayerInGame(){
        GameManager gm = new GameManager();
        Player p1 = new Player("yoshi");
        Player p2 = new Player("pikachu");
        Player p3 = new Player("glados");
        CheckersGame game = gm.makeGame(p1,p2);
        Boolean p1InGame = gm.isPlayerInGame(game, p1);
        Boolean p3InGame = gm.isPlayerInGame(game, p3);
        assertTrue(p1InGame, "error, please check test and code");
        assertFalse(p3InGame, "error, please check test and code");

    }

    @Test
    public void testGetGame_int(){
        GameManager gm = new GameManager();
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Player p3 = mock(Player.class);
        Player p4 = mock(Player.class);

        CheckersGame g1 = gm.makeGame(p1, p2);
        CheckersGame g2 = gm.makeGame(p3, p4);

        assertEquals(g1, gm.getGame("1"));
        assertEquals(g2, gm.getGame("2"));
        assertNull(gm.getGame("700"));

    }

    @Test
    public void testRemoveGame() {
        GameManager gm = new GameManager();
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Player p3 = mock(Player.class);
        Player p4 = mock(Player.class);

        CheckersGame g1 = gm.makeGame(p1, p2);
        CheckersGame g2 = gm.makeGame(p3, p4);

        assertEquals(2, gm.totalGames);
        assertNull(gm.removeGame(g1));
        assertEquals(1, gm.totalGames);
        assertNull(gm.getGame("1"));

    }
}
