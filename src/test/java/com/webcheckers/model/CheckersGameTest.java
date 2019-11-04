package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class CheckersGameTest {

    private CheckersGame RedCuT;
    private CheckersGame WhiteCuT;
    private Player p1;
    private Player p2;
    private int ID = 3;
    private Piece redP;
    private Piece whiteP;
    private Piece redKing;
    private Piece whiteKing;


    @BeforeEach
    void CreateCheckersGame() {
        p1 = new Player("A");
        p2 = new Player("B");
        RedCuT = new CheckersGame(p1, p2, ID);
        WhiteCuT = new CheckersGame(p2, p1, ID);
        redP = new Piece(0);
        whiteP = new Piece(1);
        redKing = new Piece(0);
        redKing.makePieceKing();
        whiteKing = new Piece(1);
        whiteKing.makePieceKing();


    }

    @Test
    void test_isRedPlayer() {
        assertEquals(true, RedCuT.isRedPlayer(p1), "P1 is supposed to be red player");
        assertNotEquals(true, RedCuT.isRedPlayer(p2), "P2 is not supposed to be red player");
    }

    @Test
    void test_inverseBoard() {
        assertNotEquals(RedCuT.getBoard(), RedCuT.inverseBoard(), "The board is the same as the original");
    }

    @Test
    void test_getBoard() {
        assertNotNull(RedCuT.getBoard(), "Board is null");
    }

    @Test
    void test_getRedPlayer() {
        assertNotNull(RedCuT.getRedPlayer(), "Red player is null");
        assertEquals(p1, RedCuT.getRedPlayer(), "Red player is not player 1");
        assertNotEquals(p2, RedCuT.getRedPlayer(), "Red player should not be player 2");
    }

    @Test
    void test_getWhitePlayer() {
        assertNotNull(RedCuT.getWhitePlayer(), "White player is null");
        assertEquals(p2, RedCuT.getWhitePlayer(), "White player is not player 2");
        assertNotEquals(p1, RedCuT.getWhitePlayer(), "White player should not be player 1");
    }

    @Test
    void test_getGameID() {
        assertEquals(ID, RedCuT.getGameID(), "Game ID is not equal to 0");
    }


    @Test
    void test_isSpaceRedPiece(){
        assertTrue(RedCuT.isSpaceRedPiece(7, 0));
        //Black space no piece
        assertFalse(RedCuT.isSpaceRedPiece(3, 0));
        //White space
        assertFalse(RedCuT.isSpaceRedPiece(4, 0));
        //White piece in top Row
        assertFalse(RedCuT.isSpaceRedPiece(0, 1));
    }

    @Test
    void test_isSpaceKingPiece(){
        //Red Piece in back Row
        assertFalse(RedCuT.isSpaceKingPiece(7, 0));
        //Black space no piece
        assertFalse(RedCuT.isSpaceKingPiece(3, 0));
        //White space
        assertFalse(RedCuT.isSpaceKingPiece(4, 0));
        //White piece in top Row
        assertFalse(RedCuT.isSpaceKingPiece(0, 1));

    }

    @Test
    void test_isSpaceValid(){
        //Red Piece in back Row
        assertFalse(RedCuT.isSpaceValid(7, 0));
        //Black space no piece
        assertTrue(RedCuT.isSpaceValid(3, 0));
        //White space
        assertFalse(RedCuT.isSpaceValid(4, 0));
        //White piece in top Row
        assertFalse(RedCuT.isSpaceValid(0, 1));
    }

    @Test
    void test_doesSpaceHavePiece(){
        //Red Piece in back Row
        assertTrue(RedCuT.doesSpaceHavePiece(7, 0));
        //Black space no piece
        assertFalse(RedCuT.doesSpaceHavePiece(3, 0));
        //White space
        assertFalse(RedCuT.doesSpaceHavePiece(4, 0));
        //White piece in top Row
        assertTrue(RedCuT.doesSpaceHavePiece(0, 1));
    }

    @Test
    void test_tossThatPiece(){
        //Red Piece in back Row
        assertNotNull(RedCuT.tossThatPiece(7,0));
        //Black space no piece
        assertNull(RedCuT.tossThatPiece(3, 0));
        //White space
        assertNull(RedCuT.tossThatPiece(4, 0));
        //White piece in top Row
        assertNotNull(RedCuT.tossThatPiece(0, 1));
    }

    @Test
    void test_removePieceToMove(){
        //Red Piece in back Row
        assertNotNull(RedCuT.removePieceToMove(7, 0));
        assertNull(RedCuT.tossThatPiece(7, 0));
        //Black space no piece
        assertNull(RedCuT.removePieceToMove(3, 0));

    }

    @Test
    void test_addPiece(){
        //Red Piece in back Row
        RedCuT.addPiece(7, 0, redKing);
        assertNotNull(RedCuT.tossThatPiece(7, 0));
        //try to add white piece on top
        RedCuT.addPiece(7, 0, whiteKing);
        assertTrue(RedCuT.isSpaceRedPiece(7, 0));
        //Try to add to white space
        RedCuT.addPiece(4, 0, whiteKing);
        assertFalse(RedCuT.doesSpaceHavePiece(4, 0));
        //Add to validSpace
        RedCuT.addPiece(3, 0, redKing);
        assertNotNull(RedCuT.doesSpaceHavePiece(3, 0));
    }

    @Test
    void test_GetActivePlayer(){
        assertEquals(RedCuT.getActivePlayer(), p1);
        assertNotEquals(RedCuT.getActivePlayer(), p2);
        assertEquals(WhiteCuT.getActivePlayer(), p2);
        assertNotEquals(WhiteCuT.getActivePlayer(), p1);
    }

    @Test
    void test_isActivePlayerRed(){
        assertTrue(RedCuT.isActivePlayerRed());
        assertTrue(WhiteCuT.isActivePlayerRed());
    }

    @Test
    void test_swapPlayers(){
        RedCuT.swapPlayers();
        assertNotEquals(RedCuT.getActivePlayer(), p1);
        assertEquals(RedCuT.getActivePlayer(), p2);
        WhiteCuT.swapPlayers();
        assertNotEquals(WhiteCuT.getActivePlayer(), p2);
        assertEquals(WhiteCuT.getActivePlayer(), p1);
    }

    @Test
    void test_simpleMove(){
        removeAllPiece();
        RedCuT.addPiece(6, 3, redP);
        RedCuT.lookForMoves();
        Position redStart = new Position(6, 3);
        Move upLeft = new Move(redStart, new Position(5, 2));
        Move upRight = new Move(redStart, new Position(5, 4));
        Move backLeft = new Move(redStart, new Position(7, 2));
        Move backRight = new Move(redStart, new Position(7, 4));
        assertTrue(RedCuT.isInMoves(upLeft) &&
                RedCuT.isInMoves(upRight));
        assertFalse(RedCuT.isInMoves(backLeft));
        assertFalse(RedCuT.isInMoves(backRight));
    }

    @Test
    void test_enforceJump(){
        removeAllPiece();
        RedCuT.addPiece(6, 3, redP);
        RedCuT.addPiece(5, 2, whiteP);
        RedCuT.lookForMoves();
        Position redStart = new Position(6, 3);
        Move upLeft = new Move(redStart, new Position(4, 1), whiteP);
        Move upRight = new Move(redStart, new Position(5, 4));
        assertTrue(RedCuT.isInMoves(upLeft));
        assertFalse(RedCuT.isInMoves(upRight));
    }

    @Test
    void test_doubleJump(){
        removeAllPiece();
        RedCuT.addPiece(6, 3, redP);
        RedCuT.addPiece(5, 4, whiteP);
        RedCuT.addPiece(3, 6, whiteP);
        RedCuT.lookForMoves();
        Position redStart = new Position(6, 3);
        Position redMiddle = new Position(4, 5);
        Position redEnd = new Position(2, 7);
        Move jump1 = new Move(redStart, redMiddle);
        Move jump2 = new Move(redMiddle, redEnd);
        assertTrue(RedCuT.isInMoves(jump1));
        RedCuT.keepLastMove(jump1);
        assertEquals(RedCuT.getLastMove(), jump1);
        RedCuT.completeMove();
        assertTrue(RedCuT.doesSpaceHavePiece(4, 5));
        RedCuT.backupMove();
        assertNull(RedCuT.getLastMove());
        assertTrue(RedCuT.doesSpaceHavePiece(6, 3));
        assertTrue(RedCuT.isInMoves(jump1));
        RedCuT.keepLastMove(jump1);
        RedCuT.completeMove();
        RedCuT.lookInSpace(jump1.getEnd().getRow(), jump1.getEnd().getCol());
        assertTrue(RedCuT.isInMoves(jump2));
        RedCuT.keepLastMove(jump2);
        assertEquals(RedCuT.getLastMove(), jump2);
        RedCuT.completeMove();
        assertTrue(RedCuT.doesSpaceHavePiece(2, 7));
        RedCuT.backupMove();
        assertEquals(RedCuT.getLastMove(), jump1);
        RedCuT.backupMove();
        assertNull(RedCuT.getLastMove());
        RedCuT.keepLastMove(jump1);
        RedCuT.completeMove();
        RedCuT.lookInSpace(jump1.getEnd().getRow(), jump1.getEnd().getCol());
        assertTrue(RedCuT.isInMoves(jump2));
        RedCuT.keepLastMove(jump2);
        assertEquals(RedCuT.getLastMove(), jump2);
        RedCuT.completeMove();
        RedCuT.completeTurn();
        assertFalse(RedCuT.doesSpaceHavePiece(5, 4));
        assertFalse(RedCuT.doesSpaceHavePiece(3, 6));



    }
    @Test
    void test_kingSingleJump(){
        removeAllPiece();
        //Jumps in all 4 directions for Red King
        RedCuT.addPiece(4, 3, redKing);
        RedCuT.addPiece(3, 4, whiteP);
        RedCuT.addPiece(3, 2, whiteP);
        RedCuT.addPiece(5, 2, whiteP);
        RedCuT.addPiece(5, 4, whiteP);
        RedCuT.lookForMoves();
        Position redStart = new Position(4, 3);
        Move RUpLeft = new Move(redStart, new Position(2, 1), whiteP);
        Move RUpRight = new Move(redStart, new Position(2, 5), whiteP);
        Move RDownLeft = new Move(redStart, new Position(6, 1), whiteP);
        Move RDownRight = new Move(redStart, new Position(6, 5), whiteP);
        Move invalid = new Move(redStart, new Position(0, 0));
        assertTrue(RedCuT.isInMoves(RUpLeft) &&
                RedCuT.isInMoves(RUpRight) &&
                RedCuT.isInMoves(RDownLeft) &&
                RedCuT.isInMoves(RDownRight));
        assertFalse(RedCuT.isInMoves(invalid));
        RedCuT.makeMove(RUpLeft);
        assertTrue(RedCuT.doesSpaceHavePiece(2, 1));
        assertTrue(RedCuT.isSpaceKingPiece(2, 1) &&
                RedCuT.isSpaceRedPiece(2, 1));
        assertTrue(RedCuT.isSpaceValid(3, 2));

        //Jumps in all 4 directions White King
        WhiteCuT.addPiece(4, 3, whiteKing);
        WhiteCuT.addPiece(3, 4, redP);
        WhiteCuT.addPiece(3, 2, redP);
        WhiteCuT.addPiece(5, 2, redP);
        WhiteCuT.addPiece(5, 4, redP);
        Move move2 = new Move(new Position(4, 3),
                new Position(6, 5), redP);
        WhiteCuT.makeMove(move2);
        assertTrue(WhiteCuT.doesSpaceHavePiece(6, 5) &&
                WhiteCuT.isSpaceKingPiece(6, 5) &&
                !WhiteCuT.isSpaceRedPiece(6, 5));
        assertTrue(WhiteCuT.isSpaceValid(5, 4));
    }

    public void removeAllPiece(){
        for (int r = 0; r < 8; r++){
            for (int c = 0; c < 8; c ++){
                RedCuT.removePieceToMove(r, c);
                WhiteCuT.removePieceToMove(r, c);
            }
        }
    }

    @Test
    void test_CompleteTurn(){
        RedCuT.completeTurn();
        assertFalse(RedCuT.isActivePlayerRed());
        assertTrue(RedCuT.getCurrentTurn().isEmpty());
    }

    @Test
    void test_Turn(){
        assertNotNull(RedCuT.getCurrentTurn());
        assertNotNull(WhiteCuT.getCurrentTurn());

    }

}