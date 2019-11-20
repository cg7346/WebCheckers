package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Repeatable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("Model-Tier")
public class MoveValidatorTest {

    private MoveValidator CuT;
    private CheckersGame game;
    private Piece redP;
    private Piece whiteP;

    @BeforeEach
    void construct(){
        game = mock(CheckersGame.class);
        CuT = new MoveValidator(game);
        redP = mock(Piece.class);
        when(redP.isRedPiece()).thenReturn(true);
        whiteP = mock(Piece.class);
        when(whiteP.isRedPiece()).thenReturn(false);
        allInvalid();
    }

    @Test
    void test_findRedValid(){
        setActivePlayer(true);
        makePiece(5, 6, true, false);
        makeValid(4, 5);
        makeValid(4, 7);
        Move m1 = new Move(new Position(5, 6), new Position(4, 5));
        Move m2 = new Move(new Position(5, 6), new Position(4, 7));
        Move m3 = new Move (new Position(5, 6), new Position(3, 5));
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(m1));
        assertEquals(CuT.validMove, CuT.isInMoves(m2));
        assertEquals(CuT.invalidMove, CuT.isInMoves(m3));

    }

    @Test
    void test_findWhiteValid(){
        setActivePlayer(false);
        makePiece(0, 1, false, false);
        makeValid(1, 0);
        makeValid(1, 2);
        Move m1 = new Move (new Position(7, 1), new Position(6, 0));
        Move m2 = new Move (new Position(7, 1), new Position(6, 2));
        CuT.lookForMoves();
        when(game.moveConverter(m1)).thenReturn(new Move(
                new Position(0, 1), new Position(1, 0)));
        when(game.moveConverter(m2)).thenReturn(new Move(
                new Position(0, 1), new Position(1, 2)));
        assertEquals(CuT.validMove, CuT.isInMoves(m1));
        assertEquals(CuT.validMove, CuT.isInMoves(m2));
    }

    @Test
    void test_kingMoveRed(){
        setActivePlayer(true);
        makePiece(4, 3, true, true);
        //Row above
        makeValid(3, 2);
        makeValid(3, 4);
        //Row below
        makeValid(5, 2);
        makeValid(5, 4);
        //Moves
        Move upL = new Move (new Position(4, 3), new Position(3, 2));
        Move upR = new Move (new Position(4, 3), new Position(3, 4));
        Move doL = new Move (new Position(4, 3), new Position(5, 2));
        Move doR = new Move (new Position(4, 3), new Position(5, 4));
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(upL));
        assertEquals(CuT.validMove, CuT.isInMoves(upR));
        assertEquals(CuT.validMove, CuT.isInMoves(doL));
        assertEquals(CuT.validMove, CuT.isInMoves(doR));
    }

    @Test
    void test_kingMoveWhite(){
        setActivePlayer(false);
        makePiece(3, 3, false, true);
        //Row above
        makeValid(4, 2);
        makeValid(4, 4);
        //Row below
        makeValid(2, 2);
        makeValid(2, 4);
        //Moves
        Move upL = new Move (new Position(4, 3), new Position(3, 2));
        Move upR = new Move (new Position(4, 3), new Position(3, 4));
        Move doL = new Move (new Position(4, 3), new Position(5, 2));
        Move doR = new Move (new Position(4, 3), new Position(5, 4));
        //ConvertMoves
        when(game.moveConverter(upL)).thenReturn(new Move(
                new Position(3, 3), new Position(4, 2)));
        when(game.moveConverter(upR)).thenReturn(new Move(
                new Position(3, 3), new Position(4, 4)));
        when(game.moveConverter(doL)).thenReturn(new Move(
                new Position(3, 3), new Position(2, 2)));
        when(game.moveConverter(doR)).thenReturn(new Move(
                new Position(3, 3), new Position(2, 4)));
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(upL));
        assertEquals(CuT.validMove, CuT.isInMoves(upR));
        assertEquals(CuT.validMove, CuT.isInMoves(doL));
        assertEquals(CuT.validMove, CuT.isInMoves(doR));
    }

    @Test
    void test_redJump(){
        setActivePlayer(true);
        //add pieces
        makePiece(6, 5, true, false);
        makePiece(5, 4, false, false);
        //landing places
        makeValid(4, 3);
        makeValid(5, 6);
        //jump places
        Move jumpL = new Move (new Position(6, 5),
                new Position(4, 3), whiteP);
        Move jumpR = new Move (new Position(6, 5),
                new Position(5, 6));
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(jumpL));
        assertEquals(CuT.jumpAvail, CuT.isInMoves(jumpR));
    }

    @Test
    void test_whiteJump(){
        setActivePlayer(false);
        //add piece
        makePiece(1, 2, false, false);
        makePiece(2, 1, true, false);
        //landing spaces
        makeValid(3, 0);
        makeValid(2, 3);
        //jump moves
        Move jumpL = new Move(new Position(6, 2),
                new Position(4, 3), redP);
        Move moveR = new Move(new Position(6, 2),
                new Position(5, 3));
        when(game.moveConverter(jumpL)).thenReturn(new Move(
                new Position(1, 2), new Position(3, 0)));
        when(game.moveConverter(moveR)).thenReturn(new Move(
                new Position(1, 2), new Position(2, 3)));
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(jumpL));
        assertEquals(CuT.jumpAvail, CuT.isInMoves(moveR));
    }

    @Test
    void cannotJumpSameColor(){
        //Red Player
        setActivePlayer(true);
        makePiece(7, 6, true, false);
        makePiece(6, 5, true, false);
        makeValid(5, 4);
        Move jumpR = new Move(new Position(7,6),
                new Position(5, 4));
        CuT.lookForMoves();
        assertEquals(CuT.invalidMove, CuT.isInMoves(jumpR),
                "Cannot jump same color");

        //White Player
        setActivePlayer(false);
        makePiece(0, 1, false, false);
        makePiece(1, 2, false, false);
        makeValid(2, 3);
        Move jumpW = new Move(new Position(7,1),
                new Position(5, 2));
        when(game.moveConverter(jumpW)).thenReturn(new Move(
                new Position(0, 1), new Position(2, 3)));
        CuT.lookForMoves();
        assertEquals(CuT.invalidMove, CuT.isInMoves(jumpW),
                "Cannot jump same color");
    }

    @Test
    void regDoubleJump(){
        setActivePlayer(true);
        //set up double jump
        makePiece(7, 6, true, false);
        makePiece(6, 5, false, true);
        makeValid(5, 4);
        makePiece(4, 3, false, false);
        makeValid(3, 2);

        //jump moves
        Move jump1 = new Move(new Position(7,6),
                new Position(5, 4), whiteP);
        //Part 1 of Double Jump
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(jump1));

        //Fake completeMove in Game
        makeEmpty(7, 6);
        makeEmpty(6, 5);
        makePiece(5, 4, true, false);

        //Part 2 of Double Jump
        Move jump2 = new Move(new Position(5, 4),
                new Position(3, 2), whiteP);
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(jump2));
    }

//    @Test
//    void singleBackup(){
//        //pretend we already moved the piece up 1 diagnol and
//        // we want to back up
//        setActivePlayer(true);
//        makePiece(5, 6, true, false);
//        makeValid(6, 5);
//        makeValid(5, 4);
//        when(game.getLastMove()).thenReturn(new Move (
//                new Position(6, 5),
//                new Position(5, 6)));
//        when(game.backupLastMove()).thenReturn(null);
//        when(game.isTurnEmpty()).thenReturn(true);
//        //mock moving back piece
//        makePiece(6, 5, true, false);
//        makeValid(5, 6);
//        Move otherMove = new Move (new Position(6, 5),
//                new Position(5, 4));
//        assertEquals(CuT.validMove, CuT.isInMoves(otherMove));
//    }

//    @Test
//    void outNoPiecesR() {
//        //No White Pieces
//        makePiece(7, 6, true, true);
//        makeValid(6, 5);
//        makeValid(6, 7);
//        CuT.lookForMoves();
//        assertFalse(CuT.isRedOut(),
//                "Red has a piece with moves, should not be out");
//        assertTrue(CuT.isWhiteOut(),
//                "White has no pieces, should be out");
//    }
//    @Test
//    void outNoPiecesW(){
//        //No Red Pieces
//        allInvalid();
//        makePiece(0, 1, false, false);
//        makeValid(1, 0);
//        makeValid(1, 2);
//        CuT.lookForMoves();
//        assertFalse(CuT.isWhiteOut(),
//                "White has a piece with moves, should not be out");
//        assertTrue(CuT.isRedOut(),
//                "Red has no pieces, should be out");
//    }
//
//    @Test
//    void Counts(){
//        //No pieces
//        assertSame(0, CuT.getWhiteCount(),
//                "There are no white pieces yet");
//        assertSame(0, CuT.getRedCount(),
//                "There are no red pieces yet");
//
//        //1 Piece
//        makePiece(0, 1, false, false);
//        makePiece(7, 6, true, false);
//        CuT.lookForMoves();
//        assertSame(1, CuT.getWhiteCount(),
//                "There is 1 white Piece");
//        assertSame(1, CuT.getRedCount(),
//                "There is 1 red Piece");
//
//        //King Piece
//        makePiece(0, 3, true, true);
//        makePiece(7, 4, false, false);
//        CuT.lookForMoves();
//        assertSame(2, CuT.getWhiteCount(),
//                "White should count King Piece");
//        assertSame(2, CuT.getRedCount(),
//                "Red should count King Piece");
//
//        //Uneven amount of Pieces
//        makePiece(0, 5, false, false);
//        CuT.lookForMoves();
//        assertSame(3, CuT.getWhiteCount(),
//                "There are 3 White Pieces");
//        assertSame(2, CuT.getRedCount(),
//                "There is 1 red Piece");
//
//    }

    @Test
    void doubleJumpBug(){
        setActivePlayer(true);
        checkersValid();
        //All the white Pieces
        makePiece(0, 7, false, false);
        makePiece(1, 4, false, false);
        makePiece(1, 6, false, false);
        makePiece(2, 3, false, false);
        makePiece(2, 7, false, false);
        makePiece(3, 0, false, false);
        makePiece(3, 2, false, false);
        makePiece(3, 4, false, true);
        makePiece(7, 0, false, true);

        //All the redPieces
        makePiece(0, 3, true, true);
        makePiece(5, 0, true, false);
        makePiece(5,4, true, false);
        makePiece(5, 6, true, false);
        makePiece(6, 3, true, false);

        //First part of the double jump
        Move mpt1 = new Move (new Position(0, 3),
                new Position(2, 5),whiteP);
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(mpt1));
        //Fake completeMove
        makeEmpty(0, 3);
        makeEmpty(1, 4);
        makePiece(2, 5, true, true);
        Move mpt2 = new Move (new Position(2, 5),
                new Position(4, 3), whiteP);
        CuT.lookForMoves();
        assertEquals(CuT.validMove, CuT.isInMoves(mpt2));

    }


    private void makePiece(int row, int col, boolean isRed, boolean isKing){
        when(game.isSpaceRedPiece(row, col)).thenReturn(isRed);
        when(game.isSpaceKingPiece(row, col)).thenReturn(isKing);
        when(game.doesSpaceHavePiece(row, col)).thenReturn(true);
        when(game.tossThatPiece(row, col)).thenReturn(isRed ? redP : whiteP);
        when(game.isSpaceValid(row, col)).thenReturn(false);
    }

    private void makeEmpty(int row, int col){
        when(game.isSpaceRedPiece(row, col)).thenReturn(false);
        when(game.isSpaceKingPiece(row, col)).thenReturn(false);
        when(game.isSpaceValid(row, col)).thenReturn(false);
        when(game.doesSpaceHavePiece(row, col)).thenReturn(false);
    }

    private void makeValid(int row, int col){
        when(game.isSpaceValid(row, col)).thenReturn(true);
        when(game.isSpaceRedPiece(row, col)).thenReturn(false);
        when(game.isSpaceKingPiece(row, col)).thenReturn(false);
        when(game.doesSpaceHavePiece(row, col)).thenReturn(false);
    }

    private void setActivePlayer(boolean isRed){
        when(game.isActivePlayerRed()).thenReturn(isRed);
    }

    private void allInvalid(){
        for (int r = 0; r < 8; r++){
            for (int c = 0; c < 8; c++){
                makeEmpty(r, c);
            }
        }
    }

    private void checkersValid(){
        for (int r = 0; r < 8; r ++){
            for (int c = 0; c < 8; c++){
                if( r%2 != 0 && c%2 == 0 ||
                    r%2 == 0 && c%2 != 0) {
                    makeValid(r, c);
                }
            }
        }
    }

}
