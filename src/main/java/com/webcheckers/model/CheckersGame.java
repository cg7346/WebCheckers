package com.webcheckers.model;

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

    //possible moves to be made the current player
    //according to their apparent location on BoardView
    private ArrayList<Move> singleRedMoves;
    private ArrayList<Move> singleWhiteMoves;

    //unique identifier for the game
    private int gameID;

    //Dimensions for the board
    public final static int ROWS = 8;
    public final static int COLS = 8;

    //2D array that represents a checkers board
    private Space[][] board;

    //Whose turn is it?
    private Player activePlayer;

    //The last move made in the game
    private Turn currentTurn;


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
        this.singleRedMoves = new ArrayList<>();
        this.singleWhiteMoves = new ArrayList<>();
        this.currentTurn = new Turn();

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
     * @param col column of space
     * @param row row of space
     * @return char symbol that represents space
     */
    private Space getSpace(int row, int col){
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
     *
     * DESIGN CHOICE - decided to reverse rows only due to difficulty with conversions of colums
     *
     * So instead of white at top it's red
     * @return the board, but reverse
     */
    public Space[][] inverseBoard(){
        Space[][] inverseBoard = new Space[ROWS][COLS];
        int rowOriginal = -1;
        for (int row = 7; row >= 0; row--) {
            rowOriginal++;
            int colOriginal = -1;
           for (int col = 7; col >= 0; col--) {
               colOriginal++;
               inverseBoard[row][col] = board[rowOriginal][colOriginal];
               System.out.println("original row" + rowOriginal);
               System.out.println("original col" + colOriginal);
               System.out.println("new row" + row);
               System.out.println("new col" + colOriginal);
               System.out.println("--------------------------------------------");
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

    /**
     * Removes a piece from the board
     * @param row row where piece lived
     * @param col col where piece lived
     * @return the piece that used to live in that location
     */
    private Piece removePieceToMove(int row, int col){
        Space space = getSpace(row, col);
        Piece piece = null;
        if (space.hasPiece()){
            piece = space.getPiece();
            space.removePiece();
        }
        return piece;
    }

    /**
     * Add a piece to the a new space
     * @param row row to put piece
     * @param col col to put piece
     * @param piece the piece to put in the square
     */
    private void addPiece(int row, int col, Piece piece){
        Space space = getSpace(row, col);
        if (!space.hasPiece() && space.isValid()){
            space.addPiece(piece);
        }
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
        currentTurn = new Turn();
    }


    /**
     * Looks for moves possible in the game
     */
    public void lookForMoves() {
        for (int row = 0; row < ROWS; row++) {
            System.out.println();
            for (int col = 0; col < COLS; col++) {
                Space space = getSpace(row, col);
                if (space.hasPiece()) {
                    Piece p = space.getPiece();
                    //piece if white and single
                    if (!p.isRedPiece() && !p.isPieceKing()) {
                        System.out.println("Checking White------");
                        checkWhiteSingleMoves(row, col);
                    }
                    //piece red and single
                    if (p.isRedPiece() && !p.isPieceKing()) {
                        System.out.println("Checking Red--------");
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
    public ArrayList<Move> checkColumns(int row, int col, int nextRow) {
        ArrayList<Move> moves = new ArrayList<>();
        if (col + 1 < COLS) {
            if (getSpace(nextRow, col+1).isValid()) {
                Move moveToAdd = new Move(new Position(row, col),
                        new Position(nextRow, col + 1));
                System.out.println(moveToAdd);
                moves.add(moveToAdd);
            }
        }
        if (col - 1 >= 0) {
            if (getSpace(nextRow, col-1).isValid()) {
                Move moveToAdd = new Move(new Position(row, col),
                        new Position(nextRow, col - 1));
                System.out.println(moveToAdd);
                moves.add(moveToAdd);
            }
        }
        return moves;
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
                singleWhiteMoves.addAll(checkColumns(row, col, nextRow));
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
            singleRedMoves.addAll(checkColumns(row, col, nextRow));
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
        boolean isRed = activePlayer.equals(redPlayer);
        move = isRed ? move : moveConverter(move);
        ArrayList<Move> moves = isRed ? singleRedMoves : singleWhiteMoves;
        for (Move possibleMove : moves){
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
        this.currentTurn.AddMove(move);
    }

    /**
     * Returns the last move made by a player in
     * a game
     * @return a Move that shows the last move made
     */
    public Move getLastMove(){
        return currentTurn.LastMove();
    }

    /**
     * Removes the last turn made by the user
     */
    public void BackupMove()
    {
        Piece p = currentTurn.BackupLastMove();
    }

    /**
     * Makes a move and updates the board according
     * @param move the move to make
     */
    public void makeMove(Move move){
        move = (activePlayer.equals(redPlayer)) ? move : moveConverter(move);
        Position start = move.getStart();
        Piece piece = removePieceToMove(start.getRow(), start.getCol());
        Position end = move.getEnd();
        if (piece != null){
            if(piece.isRedPiece() && end.getRow() == 0){
                piece.makePieceKing();
            }
            if(!piece.isRedPiece() && end.getRow() == 7){
                piece.makePieceKing();
            }
            addPiece(end.getRow(), end.getCol(), piece);
        }
    }

    /**
     * Converts a move made by the whitePlayer on
     * the board view to something we can check
     * with the official CheckersGame Board
     * @param move the Move to convert
     * @return a move flipped of the original
     */
    public Move moveConverter(Move move){
        System.out.println("COVERTING MOVE--------");
        System.out.println("ORIGINAL: " + move);
        Position start = move.getStart();
        Position end = move.getEnd();
        if (!activePlayer.equals(redPlayer)) {
            Position convertedStart = new Position(
                    ROWS - start.getRow() - 1,
                    start.getCol());
            Position convertedEnd = new Position(
                    ROWS - end.getRow() - 1,
                    end.getCol());
            Move newMove = new Move(convertedStart, convertedEnd);
            System.out.println("CONVERTED: " + newMove);
            System.out.println();
            return new Move(convertedStart, convertedEnd);
        }
        return move;
    }
}
