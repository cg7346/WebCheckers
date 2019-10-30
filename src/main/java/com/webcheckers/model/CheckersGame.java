package com.webcheckers.model;

import com.webcheckers.ui.PostSubmitTurn;

import java.util.ArrayList;

/**
 * Class to represent a Checkers game
 * Holds the logic for an average game of checkers
 *
 * Holds information about who the two players are
 * and only information for that individual game
 */
public class CheckersGame {

    //The players in the game
    //(the 1st player to click another player)
    private Player redPlayer;
    //(the person clicked on by the other player)
    private Player whitePlayer;
    //possible moves to be made by other people
    private ArrayList<Move> singleMoves;

    private int gameID;

    //Dimensions for the board
    public final static int ROWS = 8;
    public final static int COLS = 8;

    //2D array that represents a checkers board
    private Space[][] board;

    //Whose turn is it?
    private Player activePlayer;

    private Move lastMove;


    /**
     * One instance of a checkers board
     * Creates a and 8x8 board of Space enums
     * that start with 3 rows of alternating R and X
     * then two rows of alternative O and X then
     * three final rows of W and X
     *
     * Sets turn to red player
     *
     * @param gameID int, unique identifier for the game
     */
    public CheckersGame(Player redPlayer, Player whitePlayer, int gameID){
        this.gameID = gameID;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.singleMoves = new ArrayList<Move>();
        this.lastMove = null;

        board = new Space[ROWS][COLS];
        for (int row=0; row < ROWS; row++){
            for (int col=0; col < COLS; col ++){
                //if both row and column are even or odd then it is white space
                if ((col%2 == 0 && row%2 ==0) ||
                        (col%2 != 0 && row%2 != 0)){
                    board[row][col] = new Space(col, false);
                }
                //calling top of board row 0
                else {
                    //putting white player at top of board
                    if (row <= 2){
                        Space newSpace = new Space(col, true);
                        newSpace.addPiece(new Piece(1));
                        board[row][col] = newSpace;
                    }
                    //putting red player at bottom of board
                    else if (row >= 5) {
                        Space newSpace = new Space(col, true);
                        newSpace.addPiece(new Piece(0));
                        board[row][col] = newSpace;
                    }
                    //middle spaces empty black
                    else{
                        board[row][col] = new Space(col, true);
                    }
                }

            }
        }
        //redPlayer starts the game
        activePlayer = redPlayer;
    }

    /**
     * Returns the symbol in a row and column
     * on a board
     *
     * R - Red Player
     * W - White Player
     * X - White
     * O - Black
     *
     * @param col column of space
     * @param row row of space
     * @return char symbol that represents space
     */
    public Space getSpace(int row, int col){
        return board[row][col];
    }

    /**
     * Helper method to check if the player is the
     * red player, if not then it's the white player
     * @param player the player in the game to check
     * @return true for red player, false for white
     */
    public boolean isRedPlayer(Player player){
        return player.equals(redPlayer);
    }

    /**
     * Helper method that gets the inverse of the board
     *
     * Flips the white and red colors so that a player
     * can see their pieces near them
     *
     * So instead of white at top it's red
     * @return
     */
    public Space[][] inverseBoard(){
        Space[][] inverseBoard = new Space[ROWS][COLS];
        for (int row=0; row < ROWS; row++) {
           for (int col = 0; col < COLS; col++) {
               inverseBoard[row][col] = board[ROWS-row-1][COLS-col-1];
            }
        }
      return inverseBoard;
   }

    /**
     * Returns the original not flipped board
     * @return the original board
     */
    public Space[][] getBoard() {
        return board;
    }

    private void removePiece(int row, int col){
        Space space = getSpace(row, col);
        if (space.hasPiece()){
            space.removePiece();
        }
    }

    private void addPiece(int row, int col){

    }

    /**
     * These are getters
     */
    public Player getRedPlayer(){return redPlayer;}
    public Player getWhitePlayer(){return whitePlayer;}
    public int getGameID(){return gameID;}

    /**
     * Returns who the activePlayer is (whose turn)
     * @return either RedPlayer or WhitePlayer
     */
    public Player getActivePlayer(){
        return activePlayer;
    }

    /**
     * Swaps the players for their next turn
     * If player is currently red -> white,
     * otherwise -> red
     */
    public void swapPlayers(){
        activePlayer = activePlayer.equals(redPlayer) ? whitePlayer : redPlayer;
    }


    public boolean isValid(int row, int col){
        Space space = getSpace(row, col);
        return space.isValid();
    }

    public void lookForMoves() {
        for (int row = 0; row < ROWS; row++) {
            System.out.println();
            for (int col = 0; col < COLS; col++) {
                Space space = getSpace(row, col);
                if (space.hasPiece()) {
                    Piece p = space.getPiece();
                    //piece if white and single
                    if (!p.isRedPiece() && !p.isPieceKing()) {
                        checkWhiteSingleMoves(row, col);
                    }
                    //piece red and single
                    if (p.isRedPiece() && !p.isPieceKing()) {
                        checkRedSingleMoves(row, col);
                    }
                    //piece white and king
                    if (!p.isRedPiece() && p.isPieceKing()) {
                        checkWhiteKingMoves(row, col);
                    }
                    //piece red and king
                    if (p.isRedPiece() && p.isPieceKing()) {
                        checkRedKingMoves(row, col);
                    }
                }
                }
            }
    }


    /**
     * Checks the left and right column of the next
     * row for a piece to see if a move is available
     * @param row row to look in
     * @param col column to left and right of
     */
    public void checkColumns(int row, int col, int nextRow) {
        if (col + 1 < COLS) {
            if (getSpace(nextRow, col+1).isValid()) {
                Move moveToAdd = new Move(new Position(row, col),
                        new Position(nextRow, col + 1));
                singleMoves.add(moveToAdd);
            }
        }
        if (col - 1 >= 0) {
            if (getSpace(nextRow, col-1).isValid()) {
                Move moveToAdd = new Move(new Position(row, col),
                        new Position(nextRow, col - 1));
                singleMoves.add(moveToAdd);
            }
        }
    }

    /**
     * Checking to see if any single white pieces have moves
     * @param row row piece lives
     * @param col col piece lives
     */
    public void checkWhiteSingleMoves(int row, int col) {
        //check next row closer to top
        int nextRow = row + 1;
        if (nextRow >= 0) {
                checkColumns(row, col, nextRow);
            }
        }

    /**
     * Checking to see if any single red pieces have moves
     * @param row row piece lives
     * @param col col piece lives
     */
    public void checkRedSingleMoves(int row, int col) {
        //check next row closer to top
        int nextRow = row - 1;
        if (nextRow >= 0) {
            checkColumns(row, col, nextRow);
        }
    }

    //TODO: Add King Moves
    public ArrayList<Move> checkWhiteKingMoves(int row, int col) {
        return null;
    }

    public ArrayList<Move> checkRedKingMoves(int row, int col) {
        return null;
    }

    /**
     * Looks to see if a move is in the list
     * of possible moves for the game
     * @param move Move to see if is possible
     * @return true if in, false if not
     */
    public boolean isInMoves(Move move){
        for (Move possibleMove : singleMoves){
            if (possibleMove.equals(move)){
                return true;
            }
        }
        return false;
    }

    /**
     * Keeps track of the last move
     * @param move the Move to keep track of
     */
    public void keepLastMove(Move move){
        this.lastMove = move;
    }

    /**
     * Returns the last move made by a player in
     * a game
     * @return a Move that shows the last move made
     */
    public Move getLastMove(){
        return lastMove;
    }

    public void makeMove(Move move){
        Position start = move.getStart();
        removePiece(start.getRow(), start.getCol());
        Position end = move.getEnd();
        addPiece(end.getRow(), end.getCol());

    }
}
