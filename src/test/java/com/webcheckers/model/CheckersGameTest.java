package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class CheckersGameTest {

    private CheckersGame CuT;
    private Player p1;
    private Player p2;
    private int ID = 3;

    @BeforeEach
    void CreateCheckersGame() {
        p1 = new Player("A");
        p2 = new Player("B");
        CuT = new CheckersGame(p1, p2, ID);
    }

    @Test
    void test_isRedPlayer() {
        assertEquals(true, CuT.isRedPlayer(p1), "P1 is supposed to be red player");
        assertNotEquals(true, CuT.isRedPlayer(p2), "P2 is not supposed to be red player");
    }

    @Test
    void test_inverseBoard() {
        assertNotEquals(CuT.getBoard(), CuT.inverseBoard(), "The board is the same as the original");
    }

    @Test
    void test_getBoard() {
        assertNotNull(CuT.getBoard(), "Board is null");
    }

    @Test
    void test_getRedPlayer() {
        assertNotNull(CuT.getRedPlayer(), "Red player is null");
        assertEquals(p1, CuT.getRedPlayer(), "Red player is not player 1");
        assertNotEquals(p2, CuT.getRedPlayer(), "Red player should not be player 2");
    }

    @Test
    void test_getWhitePlayer() {
        assertNotNull(CuT.getWhitePlayer(), "White player is null");
        assertEquals(p2, CuT.getWhitePlayer(), "White player is not player 2");
        assertNotEquals(p1, CuT.getWhitePlayer(), "White player should not be player 1");
    }

    @Test
    void test_getGameID() {
        assertEquals(ID, CuT.getGameID(), "Game ID is not equal to 0");
    }

    @Test
    void test_getSpace()
    {
//        CheckersGame.Space[][] board = CuT.getBoard();
//        char symbol = CuT.getSpace(0, 0);
//        assertEquals(CheckersGame.Space.EMPTY_WHITE.getSymbol(), symbol, "First symbol on board is not empty white");
    }
}