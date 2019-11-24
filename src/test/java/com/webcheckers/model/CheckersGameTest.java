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
        RedCuT = new CheckersGame(p1, p2, Integer.toString(ID));
        WhiteCuT = new CheckersGame(p2, p1, Integer.toString(ID));
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
    void test_isSpaceRedPiece() {
        assertTrue(RedCuT.isSpaceRedPiece(7, 0));
        //Black space no piece
        assertFalse(RedCuT.isSpaceRedPiece(3, 0));
        //White space
        assertFalse(RedCuT.isSpaceRedPiece(4, 0));
        //White piece in top Row
        assertFalse(RedCuT.isSpaceRedPiece(0, 1));
    }

    @Test
    void test_isSpaceKingPiece() {
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
    void test_isSpaceValid() {
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
        // Red Piece in back Row
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
        assertNull(RedCuT.getLastMove());
        assertNull(WhiteCuT.getLastMove());
    }

    @Test
    void moveConverter(){
        Position p1 = new Position(5, 2);
        Position p2 = new Position(4, 1);
        Position conp1 = new Position(2, 2);
        Position conp2 = new Position(3, 1);
        Move move = new Move(p1, p2);
        Move convertedMove = new Move(conp1, conp2);
        assertEquals(move, RedCuT.moveConverter(move));
        WhiteCuT.swapPlayers();
        assertEquals(convertedMove, WhiteCuT.moveConverter(move));
        move.addPiece(redP);
        convertedMove.addPiece(redP);
        assertEquals(move, RedCuT.moveConverter(move));
        assertEquals(convertedMove, WhiteCuT.moveConverter(move));
    }

    @Test
    void test_keepLastMove(){
        removeAllPiece();
        RedCuT.addPiece(4, 1, whiteP);
        Position p1 = new Position(5, 2);
        Position p2 = new Position(4, 1);
        Position p3 = new Position(3, 0);
        Move rMove = new Move (p1, p2);
        Move rMove2 = new Move (p1, p3);
        Move rMove2WithPiece = new Move (p1, p3, whiteP);
        RedCuT.keepLastMove(rMove);
        assertEquals(rMove, RedCuT.getLastMove());
        RedCuT.keepLastMove(rMove2);
        assertEquals(rMove2WithPiece, RedCuT.getLastMove());
    }

    @Test
    void regularRedMove(){
        removeAllPiece();

        RedCuT.addPiece(6, 3, redP);
        Position redStart = new Position(6, 3);
        Move upLeft = new Move(redStart, new Position(5, 2));
        RedCuT.makeMove(upLeft);
        assertTrue(RedCuT.doesSpaceHavePiece(5, 2) &&
                RedCuT.isSpaceRedPiece(5, 2));
        assertFalse(RedCuT.doesSpaceHavePiece(6, 3));
    }

    @Test
    void regularWhiteMove(){
        removeAllPiece();
        RedCuT.swapPlayers();
        assertTrue(RedCuT.isTurnEmpty());
        RedCuT.addPiece(1, 3, whiteP);
        Position redStart = new Position(6, 3);
        Move downRight = new Move(redStart, new Position(5, 2));
        RedCuT.keepLastMove(downRight);
        assertFalse(RedCuT.isTurnEmpty());
        RedCuT.completeMove();
        //System.out.println(RedCuT.isActivePlayerRed());
        assertFalse(RedCuT.doesSpaceHavePiece(2, 2));
        assertTrue(!RedCuT.isSpaceRedPiece(2, 2));
        assertFalse(RedCuT.doesSpaceHavePiece(1, 3));
        assertTrue(RedCuT.hasValidMoveBeenMade());
    }

    @Test
    void jumpMove(){
        removeAllPiece();
        RedCuT.addPiece(1, 4, whiteP);
        RedCuT.addPiece(2, 5, redP);
        Position rStart = new Position(2, 5);
        Move upLeft = new Move(rStart, new Position(0, 3));
        RedCuT.keepLastMove(upLeft);
        RedCuT.completeMove();
        assertFalse(RedCuT.doesSpaceHavePiece(1, 4));
        assertFalse(RedCuT.doesSpaceHavePiece(2, 5));
        assertTrue(RedCuT.isSpaceRedPiece(0, 3));
    }

    @Test
    void completeTurnRedKing(){
        removeAllPiece();
        RedCuT.addPiece(1, 2, redP);
        Position rStart = new Position(1, 2);
        Move toKing = new Move(rStart, new Position(0, 1));
        RedCuT.keepLastMove(toKing);
        RedCuT.completeTurn();
        assertFalse(RedCuT.doesSpaceHavePiece(1, 2));
        assertTrue(RedCuT.isSpaceRedPiece(0, 1));
        assertTrue(RedCuT.isSpaceKingPiece(0, 1));
    }

    @Test
    void completeTurnWhiteKing(){
        removeAllPiece();
        RedCuT.addPiece(6, 1, whiteP);
        Position wStart = new Position(6, 1);
        Move toKing = new Move(wStart, new Position(7, 0));
        RedCuT.keepLastMove(toKing);
        RedCuT.completeTurn();
        RedCuT.checkForKings();
        assertFalse(RedCuT.doesSpaceHavePiece(6, 7));
        assertFalse(RedCuT.isSpaceRedPiece(7, 6));
        assertTrue(RedCuT.isSpaceKingPiece(7, 0));

    }

    @Test
    void startGameBooleans(){
        assertFalse(RedCuT.isTie());
        assertFalse(RedCuT.isGameOver());
        assertNull(RedCuT.getWinner());
    }

    @Test
    void tieGame(){
        RedCuT.setTie(true);
        assertTrue(RedCuT.isTie());
    }

    @Test
    void setWinner(){
        RedCuT.setWinner(p1);
        assertTrue(RedCuT.isGameOver());
        assertEquals(p1, RedCuT.getWinner());

        //In WhiteCuT p1 is whitePlayer
        WhiteCuT.setWinner(p1);
        assertTrue(WhiteCuT.isGameOver());
        assertEquals(p1, WhiteCuT.getWinner());
    }

    @Test
    void resign(){
        assertNull(RedCuT.getResignedPlayer());
        RedCuT.setResignedPlayer(p1);
        assertEquals(p1, RedCuT.getResignedPlayer());
    }

    @Test
    void backUp(){
        removeAllPiece();
        RedCuT.addPiece(7, 0, redP);
        RedCuT.addPiece(6, 1, whiteP);
        RedCuT.addPiece(4, 3, whiteP);
        Move movePt1 = new Move (new Position(7, 0),
                new Position(5, 2));
        Move movePt2 = new Move(new Position(5, 2),
                new Position(3, 4));
        RedCuT.keepLastMove(movePt1);
        RedCuT.completeMove();
        assertTrue(RedCuT.isSpaceRedPiece(5, 2));
        assertTrue(RedCuT.isSpaceValid(6, 1));
        RedCuT.keepLastMove(movePt2);
        RedCuT.completeMove();
        assertTrue(RedCuT.isSpaceRedPiece(3, 4));
        assertTrue(RedCuT.isSpaceValid(4, 3));
        assertNotNull(RedCuT.backupLastMove());
        assertEquals(movePt1, RedCuT.getLastMove());
        RedCuT.completeTurn();
        System.out.println(RedCuT);
    }

//    @Test
//    void test_simpleMove(){

//        RedCuT.lookForMoves();

//        Move upRight = new Move(redStart, new Position(5, 4));
//        Move backLeft = new Move(redStart, new Position(7, 2));
//        Move backRight = new Move(redStart, new Position(7, 4));
//        assertTrue(RedCuT.isInMoves(upLeft) &&
//                RedCuT.isInMoves(upRight));
//        assertFalse(RedCuT.isInMoves(backLeft));
//        assertFalse(RedCuT.isInMoves(backRight));
//    }

//    @Test
//    void test_enforceJump(){
//        removeAllPiece();
//        RedCuT.addPiece(6, 3, redP);
//        RedCuT.addPiece(5, 2, whiteP);
//        RedCuT.lookForMoves();
//        Position redStart = new Position(6, 3);
//        Move upLeft = new Move(redStart, new Position(4, 1), whiteP);
//        Move upRight = new Move(redStart, new Position(5, 4));
//        assertTrue(RedCuT.isInMoves(upLeft));
//        assertFalse(RedCuT.isInMoves(upRight));
//    }
//
//    @Test
//    void test_doubleJump(){
//        removeAllPiece();
//        RedCuT.addPiece(6, 3, redP);
//        RedCuT.addPiece(5, 4, whiteP);
//        RedCuT.addPiece(3, 6, whiteP);
//        RedCuT.lookForMoves();
//        Position redStart = new Position(6, 3);
//        Position redMiddle = new Position(4, 5);
//        Position redEnd = new Position(2, 7);
//        Move jump1 = new Move(redStart, redMiddle);
//        Move jump2 = new Move(redMiddle, redEnd);
//        assertTrue(RedCuT.isInMoves(jump1));
//        RedCuT.keepLastMove(jump1);
//        assertEquals(RedCuT.getLastMove(), jump1);
//        RedCuT.completeMove();
//        assertTrue(RedCuT.doesSpaceHavePiece(4, 5));
//        RedCuT.backupMove();
//        assertNull(RedCuT.getLastMove());
//        assertTrue(RedCuT.doesSpaceHavePiece(6, 3));
//        assertTrue(RedCuT.isInMoves(jump1));
//        RedCuT.keepLastMove(jump1);
//        RedCuT.completeMove();
//        RedCuT.lookInSpace(jump1.getEnd().getRow(), jump1.getEnd().getCol());
//        assertTrue(RedCuT.isInMoves(jump2));
//        RedCuT.keepLastMove(jump2);
//        assertEquals(RedCuT.getLastMove(), jump2);
//        RedCuT.completeMove();
//        assertTrue(RedCuT.doesSpaceHavePiece(2, 7));
//        RedCuT.backupMove();
//        assertEquals(RedCuT.getLastMove(), jump1);
//        RedCuT.backupMove();
//        assertNull(RedCuT.getLastMove());
//        RedCuT.keepLastMove(jump1);
//        RedCuT.completeMove();
//        RedCuT.lookInSpace(jump1.getEnd().getRow(), jump1.getEnd().getCol());
//        assertTrue(RedCuT.isInMoves(jump2));
//        RedCuT.keepLastMove(jump2);
//        assertEquals(RedCuT.getLastMove(), jump2);
//        RedCuT.completeMove();
//        RedCuT.completeTurn();
//        assertFalse(RedCuT.doesSpaceHavePiece(5, 4));
//        assertFalse(RedCuT.doesSpaceHavePiece(3, 6));
//
//
//
//    }
//    @Test
//    void test_kingSingleJump(){
//        removeAllPiece();
//        //Jumps in all 4 directions for Red King
//        RedCuT.addPiece(4, 3, redKing);
//        RedCuT.addPiece(3, 4, whiteP);
//        RedCuT.addPiece(3, 2, whiteP);
//        RedCuT.addPiece(5, 2, whiteP);
//        RedCuT.addPiece(5, 4, whiteP);
//        RedCuT.lookForMoves();
//        Position redStart = new Position(4, 3);
//        Move RUpLeft = new Move(redStart, new Position(2, 1), whiteP);
//        Move RUpRight = new Move(redStart, new Position(2, 5), whiteP);
//        Move RDownLeft = new Move(redStart, new Position(6, 1), whiteP);
//        Move RDownRight = new Move(redStart, new Position(6, 5), whiteP);
//        Move invalid = new Move(redStart, new Position(0, 0));
//        assertTrue(RedCuT.isInMoves(RUpLeft) &&
//                RedCuT.isInMoves(RUpRight) &&
//                RedCuT.isInMoves(RDownLeft) &&
//                RedCuT.isInMoves(RDownRight));
//        assertFalse(RedCuT.isInMoves(invalid));
//        RedCuT.makeMove(RUpLeft);
//        assertTrue(RedCuT.doesSpaceHavePiece(2, 1));
//        assertTrue(RedCuT.isSpaceKingPiece(2, 1) &&
//                RedCuT.isSpaceRedPiece(2, 1));
//        assertTrue(RedCuT.isSpaceValid(3, 2));
//
//        //Jumps in all 4 directions White King
//        WhiteCuT.addPiece(4, 3, whiteKing);
//        WhiteCuT.addPiece(3, 4, redP);
//        WhiteCuT.addPiece(3, 2, redP);
//        WhiteCuT.addPiece(5, 2, redP);
//        WhiteCuT.addPiece(5, 4, redP);
//        Move move2 = new Move(new Position(4, 3),
//                new Position(6, 5), redP);
//        WhiteCuT.makeMove(move2);
//        assertTrue(WhiteCuT.doesSpaceHavePiece(6, 5) &&
//                WhiteCuT.isSpaceKingPiece(6, 5) &&
//                !WhiteCuT.isSpaceRedPiece(6, 5));
//        assertTrue(WhiteCuT.isSpaceValid(5, 4));
//    }
//
    public void removeAllPiece(){
        for (int r = 0; r < 8; r++){
            for (int c = 0; c < 8; c ++){
                RedCuT.removePieceToMove(r, c);
                WhiteCuT.removePieceToMove(r, c);
            }
        }
    }

}