package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertFalse;

@Tag("Model-Tier")
public class TurnTest {
    private Player activePlayer;
    private Turn CuT;


    @BeforeEach
    void setup(){
        this.activePlayer = mock(Player.class);
        this.CuT = new Turn(activePlayer);
    }

    /**
     * tests the last move function
     */
    @Test
    void test_last_move(){
        assertNull(CuT.lastMove(), "Something wrong with last move function");
        Position start = new Position(1,2);
        Position end = new Position(2, 4);
        Move move = mock(Move.class);
        CuT.addMove(move);
        assertNotNull(CuT.lastMove(), "Something wrong with last move function");

    }

    /**
     * tests the add move function and the get moves
     */
    @Test
    void test_add_move(){

        Position start = new Position(1,2);
        Position end = new Position(2, 4);
        Piece piece = new Piece(1);
        Move m = new Move(start, end);
        CuT.addMove(m, piece);

        assertNotNull(CuT.getMoves(), "adding moves not working");


    }

    /**
     * tests the back up move function
     */
    @Test
    void test_backup_last_move(){
        Position start = new Position(1,2);
        Position end = new Position(2, 4);
        Piece piece = new Piece(1);
        Move m = new Move(start, end);
        CuT.addMove(m, piece);
        assertEquals(piece, CuT.backupLastMove(), "backup not working");
        assertNull(CuT.backupLastMove());

    }

    /**
     * tests the is jump and jump is possible function
     */
    @Test
    void test_is_jump_possible(){
        assertFalse(CuT.isJumpPossible());
        CuT.jumpIsPossible();
        assertTrue(CuT.isJumpPossible());
    }

    /**
     * tests the is empty function
     */
    @Test
    void test_is_empty(){
        assertTrue(CuT.isEmpty());
        Position start = new Position(1,2);
        Position end = new Position(2, 4);
        Piece piece = new Piece(1);
        Move m = new Move(start, end);
        CuT.addMove(m, piece);
        assertFalse(CuT.isEmpty());
    }

    @Test
    void test_hasSimpleMoveBeenMade(){
        assertFalse(CuT.hasSimpleMoveBeenMade());
        Move m = mock(Move.class);
        CuT.addMove(m);
        assertTrue(CuT.hasSimpleMoveBeenMade());
        CuT.backupLastMove();
        assertFalse(CuT.hasSimpleMoveBeenMade());
    }
}
