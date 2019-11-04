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
    // The winner of the game
    private Player winner;
    // The loser of the game
    private Player loser;

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

    // Whether or not the game is over
    private Boolean gameOver;


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
        activePlayer = redPlayer;
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
        lookForMoves();
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
     * Get the redPlayer
     * @return redPlayer
     */
    public Player getRedPlayer(){return redPlayer;}

    /**
     * Get the whitePlayer
     * @return whitePlayer
     */
    public Player getWhitePlayer(){return whitePlayer;}

    /**
     * Get the GameID
     * @return getGameID
     */
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
        currentTurn = new Turn(activePlayer);
    }


    /**
     * Iterates over the board in Checkers Game to
     * see where each piece is and where it can move
     * This is the first step when a game is created
     * or when we check a turn
     */
    public void lookForMoves()
    {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                lookInSpace(row, col);
            }
        }
    }



    /**
     * Looks into the space at those coordinates for
     * a piece and checks for possible moves there
     * This dictates which array the moves are added into.
     * Usually used after lookForMoves to check the entire board
     * but also used after a single checkers piece jump to see
     * if that jump is a double jump
     * @param row the row to look for the space
     * @param col the column to look for the space
     */
    public void lookInSpace(int row, int col) {
        Space space = getSpace(row, col);
        if(space.hasPiece()) {
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
                return new Move(new Position(startRow, startCol), new Position(newEndRow, newEndCol), piece);
            }
        }

        return null;
    }

    /**
     * Checks both side the columns to the left and right of a piece
     * to see if it is empty and a valid place to put the piece. If the
     * space is not valid because there is a piece there, then we check for
     * jumps.
     * @param row row to look in
     * @param col column to left and right of
     */
    public ArrayList<Move> checkColumns(int row, int col, int nextRow, Piece.color playerColor) {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Move> jumpMoves = playerColor.equals(Piece.color.RED) ? jumpRedMoves : jumpWhiteMoves;


        //check moving to the right
        Move moveToAdd = checkMove(row, col, nextRow, col + 1, playerColor);
        if(moveToAdd != null) {
            if(moveToAdd.hasPiece()) {
                //currentTurn.jumpIsPossible();
                jumpMoves.add(moveToAdd);
            }
            else {
                moves.add(moveToAdd);
            }
        }
        //check moving to the left
        moveToAdd = checkMove(row, col, nextRow, col - 1, playerColor);
        if(moveToAdd != null) {
            if(moveToAdd.hasPiece()) {
                //currentTurn.jumpIsPossible();
                jumpMoves.add(moveToAdd);
            }
            else {
                moves.add(moveToAdd);
            }
        }

        //if(jumpMoves.size() > 0) { return jumpMoves; }
        //else { return moves; }
        return moves;
    }

    /**
     * Next step in looking for moves for white player.
     * Looks for all pieces that can move up (closer to row 7)
     * on the board without jumping over another piece
     * @param row the row the white piece starts
     * @param col the column the white piece starts
     */
    public void checkWhiteSingleMoves(int row, int col) {
        //check next row closer to top
        int nextRow = row + 1;
        if (nextRow < 8) {
                singleWhiteMoves.addAll(checkColumns(row, col, nextRow, Piece.color.WHITE));
            }
        }

    /**
     * Next step in looking for moves for red player.
     * Looks for all pieces that can move down (closer to row 0)
     * on the board without jumping over another piece
     * @param row the row the red piece starts
     * @param col the column the red piece starts
     */
    public void checkRedSingleMoves(int row, int col) {
        //check next row closer to top
        int nextRow = row - 1;
        if (nextRow >= 0) {
            singleRedMoves.addAll(checkColumns(row, col, nextRow, Piece.color.RED));
        }
    }

    /**
     * Next step in looking for moves to any color
     * king player. We look up (closer to 7) and down
     * (closer to 0) for each king Piece
     * @param row the row the king piece starts
     * @param col the column the king piece starts
     * @param moveArray array to add moves to
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
        for (Move possibleMove : moves){
            if (possibleMove.equals(move) && !currentTurn.isJumpPossible()){
                if(jumps.size() != 0) {
                    return false;
                }
                return true;
            }
        }
        for (Move possibleMove : jumps){
            currentTurn.jumpIsPossible();
            if (possibleMove.equals(move)){
                return true;
            }
        }
        return false;
    }

    /**
     * Keeps a move to add to our stack of moves in our turn
     * @param move Move to add
     */
    public void keepLastMove(Move move){
        Position startPos = move.getStart();
        Position endPos = move.getEnd();
        if ( Math.abs(startPos.getRow() - endPos.getRow()) == 2 &&
                Math.abs(startPos.getCol() - endPos.getCol()) == 2){
            Piece piece = findPiece((activePlayer.equals(redPlayer)) ? move : moveConverter(move));
            this.currentTurn.addMove(move, piece);
        }
        else{
            this.currentTurn.addMove(move);
        }

    }

    /**
     * Finds a piece in between moves
     * @param move move to check for
     * @return the Piece if it is there
     */
    public Piece findPiece(Move move){
        Position startPos = move.getStart();
        Position endPos = move.getEnd();
        int x = startPos.getRow() + ((endPos.getRow() - startPos.getRow()) / 2);
        int y = startPos.getCol() + ((endPos.getCol() - startPos.getCol()) / 2);
        Space space = getSpace(startPos.getRow() + ((endPos.getRow() - startPos.getRow()) / 2),
                startPos.getCol() + ((endPos.getCol() - startPos.getCol()) / 2));
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
            addPiece(p_Pos.getRow(), p_Pos.getCol(), p);
        }

        if(currentTurn.isEmpty()) {
            lookForMoves();
        } else {
            lastMove = moveConverter(lastMove);
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
            Position piecePos = new Position(move.getStart().getRow() + ((move.getEnd().getRow() - move.getStart().getRow()) / 2),
                    move.getStart().getCol() + ((move.getEnd().getCol() - move.getStart().getCol()) / 2));
            removePieceToMove(piecePos.getRow(), piecePos.getCol());
        }
    }

    /**
     * If a move was made by the white player
     * it needs to be converted from how the board view
     * reads spaces to how
     * @param move the move to convert
     * @return a new converted move, or the old move if
     * the active player is red (no conversion need)
     */
    public Move moveConverter(Move move){
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
            Move convertedMove = new Move(convertedStart, convertedEnd);
            if (move.hasPiece()){
                convertedMove.addPiece(move.getPiece());
            }
            return convertedMove;
        }
        return move;
    }

    /**
     * Used after a move is submitted by PostSubmitTurn
     * Cleans up all the arrays and swaps players. Then looks
     * for moves for the next player
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

    /**
     * Used in PostValidateMove to check for moves
     * after each time the player picks and puts down a piece
     */
    public void completeMove() {
        makeMove(currentTurn.lastMove());
        singleRedMoves = new ArrayList<>();
        singleWhiteMoves = new ArrayList<>();
        jumpRedMoves = new ArrayList<>();
        jumpWhiteMoves = new ArrayList<>();
    }

    /**
     * Gets the winner of the checkers game
     *
     * @return the winning player
     */
    public Player getWinner() {
        return this.winner;
    }
    /**
     * Sets the winner of the checkers game
     *
     * @param winPlayer the winner player
     */
    public void setWinner(Player winPlayer) {
        // Sets the winner player
        this.winner = winPlayer;
        // Sets the loser player
        if (winPlayer != redPlayer) {
            this.loser = redPlayer;
        } else if (winPlayer != whitePlayer) {
            this.loser = whitePlayer;
        }
    }
    /**
     * Gets the loser of the checkers game
     *
     * @return the losing player
     */
    public Player getLoser() {
        return this.loser;
    }


}
