package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class MoveValidatorTest {

    private MoveValidator CuT;
    private CheckersGame game;

    @BeforeEach
    void construct(){
        game = mock(CheckersGame.class);
        CuT = new MoveValidator(game);
        allInvalid();
    }

    @Test
    void test_findRedValid(){
        makePiece(5, 6, true, false);
        makeValid(4, 5);
        makeValid(4, 7);
        Move m1 = new Move(new Position(5, 6), new Position(4, 5));
        Move m2 = new Move(new Position(5, 6), new Position(4, 7));
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(m1));
        assertEquals(CuT.validMove, CuT.isInMoves(m1));

    }

    private void makePiece(int row, int col, boolean isRed, boolean isKing){
        when(game.isSpaceRedPiece(row, col)).thenReturn(true);
        when(game.isSpaceRedPiece(row, col)).thenReturn(isRed);
        when(game.isSpaceKingPiece(row, col)).thenReturn(isKing);
    }

    private void makeEmpty(int row, int col, boolean isValid){
        when(game.isSpaceRedPiece(row, col)).thenReturn(false);
        when(game.isSpaceRedPiece(row, col)).thenReturn(false);
        when(game.isSpaceKingPiece(row, col)).thenReturn(false);
        when(game.isSpaceValid(row, col)).thenReturn(false);
    }

    private void makeValid(int row, int col){
        when(game.isSpaceValid(row, col)).thenReturn(true);
    }

    private void allInvalid(){
        for (int r = 0; r < 8; r++){
            for (int c = 0; c < 8; c++){
                makeEmpty(r, c, false);
            }
        }
    }


}
