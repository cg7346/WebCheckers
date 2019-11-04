package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Stack;

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
    private ArrayList<Move> jumpRedMoves;
    private ArrayList<Move> jumpWhiteMoves;

    //unique identifier for the game
    private int gameID;

    //Dimensions for the board
    public final static int ROWS = 8;
    public final static int COLS = 8;

    //2D array that represents a checkers board
    private Space[][] board;

    //Whose turn is it?
    private Player activePlayer;

    //The current turn being made
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
        this.jumpRedMoves = new ArrayList<>();
        this.jumpWhiteMoves = new ArrayList<>();
        this.currentTurn = new Turn(activePlayer);

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
        //lookForMoves();
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
     * Returns if the space we are looking at has
     * a piece and it is red, does not care if
     * king piece. If no piece is in it, will be false
     * @param row row to check
     * @param col column to check
     * @return true if red piece, false if not
     */
    public boolean isSpaceRedPiece(int row, int col){
        return getSpace(row, col).isRedPiece();
    }

    /**
     * Returns if the space we are looking at has
     * a king piece, does not care about color. If
     * no piece is in it will return false
     * @param row row to check
     * @param col column to check
     * @return true if king, false if not
     */
    public boolean isSpaceKingPiece(int row, int col){
        return getSpace(row, col).isKingPiece();
    }

    /**
     * Returns if the space is empty and black
     * @param row row to check
     * @param col column to check
     * @return true if empty and black, false if not
     */
    public boolean isSpaceValid(int row, int col){
        return getSpace(row, col).isValid();
    }

    /**
     * Returns if the space has piece
     * @param row row to check
     * @param col column to check
     * @return true if has piece, false if not
     */
    public boolean doesSpaceHavePiece(int row, int col){
        return getSpace(row, col).hasPiece();
    }

    /**
     * Returns the piece in that spot b/c we need
     * it to construct a new move ;)
     * @param row row to check
     * @param col column check
     * @return the Piece in that space (if there is one
     * but its only called when we know a piece is there)
     */
    public Piece tossThatPiece(int row, int col){
        return getSpace(row, col).getPiece();
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
    public Piece removePieceToMove(int row, int col){
        Space space = getSpace(row, col);
        Piece piece = null;
        if (space.hasPiece()){
            piece = space.getPiece();
            System.out.println("Piece to remove at" + "(" + row + ", " + col +")");
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
    public void addPiece(int row, int col, Piece piece){
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
     * Returns if the activePlayer is the red player
     * @return true if red, false if white
     */
    public boolean isActivePlayerRed(){
        return activePlayer.equals(redPlayer);
    }

    /**
     * Swaps the players for their next turn
     * If player is currently red -> white,
     * otherwise -> red
     */
    public void swapPlayers(){
        activePlayer = activePlayer.equals(redPlayer) ? whitePlayer : redPlayer;
    }

    public void lookForMoves()
    {
        for (int row = 0; row < ROWS; row++) {
            System.out.println();
            for (int col = 0; col < COLS; col++) {
                lookInSpace(row, col);
            }
        }
    }



    /**
     * Looks for moves possible in the game
     */
    public void lookInSpace(int row, int col) {
        Space space = getSpace(row, col);
        if(space.hasPiece()) {
            Piece p = space.getPiece();
            System.out.println("AT -> " + row + " " + col + " ");
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
                checkKingMoves(row, col, singleWhiteMoves, Piece.color.WHITE);
            }
            //piece red and king
            if (p.isRedPiece() && p.isPieceKing()) {
                checkKingMoves(row, col, singleRedMoves, Piece.color.RED);
            }
        }
    }


    /**
     * Locally checks a move to see if it is valid
     * @param startRow start row of piece
     * @param startCol start col of piece
     * @param endRow end row of piece
     * @param endCol end col of piece
     * @param playerColor the Piece.color to match the player
     * @return a move if it is valid
     */
    Move checkMove(int startRow, int startCol, int endRow, int endCol, Piece.color playerColor)
    {
        if(endCol >= COLS || endCol < 0 || endRow >= ROWS || endRow < 0) {return null;}

        Space space = getSpace(endRow, endCol);
        if(space.isValid()) {
            return new Move(new Position(startRow, startCol), new Position(endRow, endCol));
        }
        else if(space.hasPiece() && space.getPiece().getColor() != playerColor) {
            Piece piece = space.getPiece();
            int newEndRow = startRow + ((endRow - startRow) * 2);
            int newEndCol = startCol + ((endCol - startCol) * 2);

            if(newEndCol >= COLS || newEndCol < 0 || newEndRow >= ROWS || newEndRow < 0) {return null;}

            space = getSpace(newEndRow, newEndCol);
            if(space.isValid()) {
                Move newMove = new Move(new Position(startRow, startCol), new Position(newEndRow, newEndCol), piece);
                System.out.println("New Move!" + newMove);
                return new Move(new Position(startRow, startCol), new Position(newEndRow, newEndCol), piece);
            }
        }

        return null;
    }

    /**
     * Checks the left and right column of the next
     * row for a piece to see if a move is available
     * @param row row to look in
     * @param col column to left and right of
     */
    public ArrayList<Move> checkColumns(int row, int col, int nextRow, Piece.color playerColor) {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Move> jumpMoves = playerColor.equals(Piece.color.RED) ? jumpRedMoves : jumpWhiteMoves;


        //check moving to the right
        Move moveToAdd = checkMove(row, col, nextRow, col + 1, playerColor);
        //System.out.println("\t"+moveToAdd);
        if(moveToAdd != null) {
            if(moveToAdd.hasPiece()) {
                System.out.println("Move has piece!");
                //currentTurn.jumpIsPossible();
                jumpMoves.add(moveToAdd);
            }
            else {
                System.out.println(moveToAdd);
                moves.add(moveToAdd);
            }
        }
        //check moving to the left
        moveToAdd = checkMove(row, col, nextRow, col - 1, playerColor);
        //System.out.println("\t"+moveToAdd);
        if(moveToAdd != null) {
            if(moveToAdd.hasPiece()) {
                System.out.println("Move has piece!");
                //currentTurn.jumpIsPossible();
                jumpMoves.add(moveToAdd);
            }
            else {
                System.out.println(moveToAdd);
                moves.add(moveToAdd);
            }
        }

        //if(jumpMoves.size() > 0) { return jumpMoves; }
        //else { return moves; }
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
        if (nextRow < 8) {
                singleWhiteMoves.addAll(checkColumns(row, col, nextRow, Piece.color.WHITE));
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
            singleRedMoves.addAll(checkColumns(row, col, nextRow, Piece.color.RED));
        }
    }

    /**
     * this checks for king moves and adds it to the move array
     * @param row
     * @param col
     * @param moveArray
     */
    public void checkKingMoves(int row, int col, ArrayList<Move> moveArray, Piece.color color) {
        int rowUp = row -1;
        if(rowUp >= 0){
            moveArray.addAll(checkColumns(row, col, rowUp, color)); //TODO fix this so it's player color
        }
        int rowDown = row + 1;
        if(rowDown < 8){
            moveArray.addAll(checkColumns(row, col, rowDown, color)); //TODO fix this so it's player color
        }
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
        ArrayList<Move> jumps = isRed ? jumpRedMoves : jumpWhiteMoves;
        System.out.println("Current single moves...");
        for (Move possibleMove : moves){
            System.out.println("\t" + possibleMove);
            //TODO force jump before submit turn >:)
            if (possibleMove.equals(move) && !currentTurn.isJumpPossible()){
                if(jumps.size() != 0) {
                    return false;
                }
                return true;
            }
        }
        System.out.println("Current jump moves... ");
        for (Move possibleMove : jumps){
            currentTurn.jumpIsPossible();
            System.out.println("\t" + possibleMove);
            System.out.println("Jump Move ->> " + possibleMove);
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
        System.out.println("keeping move " + move);
        Position startPos = move.getStart();
        System.out.print("\tstarting at " + startPos);
        Position endPos = move.getEnd();
        System.out.println(" and ending at " + endPos);
        if ( Math.abs(startPos.getRow() - endPos.getRow()) == 2 &&
                Math.abs(startPos.getCol() - endPos.getCol()) == 2){
            System.out.println("\tWe have a difference");
            Piece piece = findPiece((activePlayer.equals(redPlayer)) ? move : moveConverter(move));
            this.currentTurn.addMove(move, piece);
        }
        else{
            this.currentTurn.addMove(move);
        }

    }

    public Piece findPiece(Move move){
        Position startPos = move.getStart();
        Position endPos = move.getEnd();
        int x = startPos.getRow() + ((endPos.getRow() - startPos.getRow()) / 2);
        int y = startPos.getCol() + ((endPos.getCol() - startPos.getCol()) / 2);
        Space space = getSpace(startPos.getRow() + ((endPos.getRow() - startPos.getRow()) / 2),
                startPos.getCol() + ((endPos.getCol() - startPos.getCol()) / 2));
        System.out.println("Looking for piece at " + x + ", " + y);
        return space.getPiece();
    }


    /**
     * Returns the last move made by a player in
     * a game
     * @return a Move that shows the last move made
     */
    public Move getLastMove(){
        return currentTurn.lastMove();
    }

    /**
     * Removes the last turn made by the user
     */
    public void backupMove() {
        Move lastMove = currentTurn.lastMove();
        Position start = lastMove.getStart();
        Position end = lastMove.getEnd();
        Move reverseMove = new Move(lastMove.getEnd(), lastMove.getStart());
        makeMove(reverseMove);
        /*Piece piece = removePieceToMove(end.getRow(), end.getCol());
        addPiece(start.getRow(), start.getCol(), piece);*/

        //remove the last move and get the piece associated
        Piece p = currentTurn.backupLastMove();

        //if there was a piece removed in that move return it to the game board
        if(p != null) {
            Move convertedMove = moveConverter(lastMove);
            Position p_Pos = new Position(
                    convertedMove.getStart().getRow() + ((convertedMove.getEnd().getRow() - convertedMove.getStart().getRow()) / 2),
                    convertedMove.getStart().getCol() + ((convertedMove.getEnd().getCol() - convertedMove.getStart().getCol()) / 2));
            System.out.println("GUCCI " + p_Pos);
            addPiece(p_Pos.getRow(), p_Pos.getCol(), p);
        }

        if(currentTurn.isEmpty()) {
            lookForMoves();
        } else {
            System.out.println("AAAAAAHHHHHHH " + lastMove);
            lastMove = moveConverter(lastMove);
            System.out.println("AAAAAAHHHHHHH " + lastMove);
            lookInSpace(lastMove.getStart().getRow(), lastMove.getStart().getCol());
        }
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
        System.out.println(move);
        if (piece != null){
            if(piece.isRedPiece() && end.getRow() == 0){
                piece.makePieceKing();
            }
            if(!piece.isRedPiece() && end.getRow() == 7){
                piece.makePieceKing();
            }
            addPiece(end.getRow(), end.getCol(), piece);
        }
        if(move.hasPiece()) {
            System.out.print("We have a move piece to remove");
            Position piecePos = new Position(move.getStart().getRow() + ((move.getEnd().getRow() - move.getStart().getRow()) / 2),
                    move.getStart().getCol() + ((move.getEnd().getCol() - move.getStart().getCol()) / 2));
            System.out.println(" at " + piecePos);
            removePieceToMove(piecePos.getRow(), piecePos.getCol());
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
        System.out.println("\tORIGINAL: " + move);
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
            System.out.println("\tCONVERTED: " + newMove);
            System.out.println();
            Move convertedMove = new Move(convertedStart, convertedEnd);
            if (move.hasPiece()){
                convertedMove.addPiece(move.getPiece());
            }
            return convertedMove;
        }
        return move;
    }

    /**
     * Completes the turn for the player
     * Loops through every move made and swaps
     * the current player
     */
    public void completeTurn(){
        Stack<Move> moves = currentTurn.getMoves();
        while(!moves.empty()){
            makeMove(moves.pop());
        }
        swapPlayers();
        currentTurn = new Turn(activePlayer);
        singleRedMoves = new ArrayList<>();
        singleWhiteMoves = new ArrayList<>();
        jumpRedMoves = new ArrayList<>();
        jumpWhiteMoves = new ArrayList<>();
        lookForMoves();
    }

    public void completeMove() {
        makeMove(currentTurn.lastMove());
        singleRedMoves = new ArrayList<>();
        singleWhiteMoves = new ArrayList<>();
        jumpRedMoves = new ArrayList<>();
        jumpWhiteMoves = new ArrayList<>();
    }
}
