package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertNotNull;
//import static org.testng.AssertJUnit.assertFalse;


@Tag("Model-Tier")
public class TurnTest {
    private Player activePlayer;
    private Turn turn;
    public Stack<Move> moves;
    public Stack<Move> testingMoves;
    public Stack<Move> emptyMoves;
    private Stack<Piece> removedPieces;
    private boolean isJumpPossible;

    @BeforeEach
    void setup(){
        this.activePlayer = mock(Player.class);
        this.turn = new Turn(activePlayer);
    }

    /**
     * tests the last move function
     */
    @Test
    void test_last_move(){
        assertNull(turn.lastMove(), "Something wrong with last move function");
        Position start = new Position(1,2);
        Position end = new Position(2, 4);
        Move move = mock(Move.class);
        turn.addMove(move);
        assertNotNull(turn.lastMove(), "Something wrong with last move function");

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
        turn.addMove(m, piece);

        assertNotNull(turn.getMoves(), "adding moves not working");


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
        turn.addMove(m, piece);
        assertEquals(piece, turn.backupLastMove(), "backup not working");
        assertNull(turn.backupLastMove());

    }

    /**
     * tests the is jump and jump is possible function
     */
    @Test
    void test_is_jump_possible(){
        assertFalse(turn.isJumpPossible());
        turn.jumpIsPossible();
        assertTrue(turn.isJumpPossible());
    }

    /**
     * tests the is empty function
     */
    @Test
    void test_is_empty(){
        assertTrue(turn.isEmpty());
        Position start = new Position(1,2);
        Position end = new Position(2, 4);
        Piece piece = new Piece(1);
        Move m = new Move(start, end);
        turn.addMove(m, piece);
        assertFalse(turn.isEmpty());
    }
}
