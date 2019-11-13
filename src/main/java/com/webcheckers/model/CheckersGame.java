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
     * If a move was made by the white player
     * it needs to be converted from how the board view
     * reads spaces to how
     * @param oldMove the move to convert
     * @return a new converted move, or the old move if
     * the active player is red (no conversion need)
     */
    public Move moveConverter (Move oldMove){
        Position start = oldMove.getStart();
        Position end = oldMove.getEnd();
        if (!isActivePlayerRed()){
            Position convertedStart = new Position(
                    ROWS - start.getRow() - 1,
                    start.getCol());
            Position convertedEnd = new Position(
                    ROWS - end.getRow() - 1,
                    end.getCol());
            Move convertedMove = new Move(convertedStart, convertedEnd);
            if (oldMove.hasPiece()) {
                convertedMove.addPiece(oldMove.getPiece());
            }
            return convertedMove;
        }
        return oldMove;
    }

    /**
     * Keeps a move to add to our stack of moves in our turn
     * @param move Move to add
     */
    public void keepLastMove(Move move){
        Position startPos = move.getStart();
        Position endPos = move.getEnd();
        if (Math.abs(startPos.getRow() - endPos.getRow()) == 2 &&
                Math.abs(startPos.getCol() - endPos.getCol()) == 2) {
            Piece piece = findPiece((isActivePlayerRed()) ? move : moveConverter(move));
            currentTurn.addMove(move, piece);
        }
        else{
            currentTurn.addMove(move);
        }
    }

    /**
     * this function is called to make a move
     * @param move a move to make
     */
    public void makeMove(Move move) {
        move = moveConverter(move);
        Position start = move.getStart();
        Piece piece = removePieceToMove(start.getRow(), start.getCol());
        Position end = move.getEnd();
        if (piece != null) {
            addPiece(end.getRow(), end.getCol(), piece);
        }
        if (move.hasPiece()) {
            Position piecePos = getPiecePosition(move);
            removePieceToMove(piecePos.getRow(), piecePos.getCol());
        }
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
     * this function gets the latest move
     * @return
     */
    public Move getLastMove(){
        return currentTurn.lastMove();
    }

    /**
     * this function checks if a valid move has been made
     * @return
     */
    public boolean hasValidMoveBeenMade(){
        return currentTurn.hasSimpleMoveBeenMade();
    }

    /**
     * this function backs up a move
     * @return
     */
    public Piece backupLastMove(){
        return currentTurn.backupLastMove();
    }

    /**
     * this function checks if the turn is empty
     * @return
     */
    public boolean isTurnEmpty(){
        return currentTurn.isEmpty();
    }

    /**
     * Sets that a jump is available in the turn
     */
    public void jumpIsPossible(){
        currentTurn.jumpIsPossible();
    }

    /**
     * Checks if there is a jump available during this turn
     * @return
     */
    public boolean isJumpPossible(){
        return currentTurn.isJumpPossible();
    }

    /**
     * Returns the Position of a piece in between
     * to positions
     * @param move the move to look between
     * @return the position of the piece
     */
    public Position getPiecePosition(Move move) {
        Position p_PosStart = move.getStart();
        Position p_PosEnd = move.getEnd();
        Position p_Pos = new Position(
                p_PosStart.getRow() + ((p_PosEnd.getRow() - p_PosStart.getRow()) / 2),
                p_PosStart.getCol() + ((p_PosEnd.getCol() - p_PosStart.getCol()) / 2));
        return p_Pos;
    }

    /**
     * Finds a piece in between moves
     * @param move move to check for
     * @return the Piece if it is there
     */
    public Piece findPiece(Move move){
        Position piecePosition = getPiecePosition(move);
        Space space = getSpace(piecePosition.getRow(), piecePosition.getCol());
        return space.getPiece();
    }

    /**
     * Used in PostValidateMove to check for moves
     * after each time the player picks and puts down a piece
     */
    public void completeMove(){
        makeMove(currentTurn.lastMove());
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
    }

    public void checkForKings() {
        for(int i = 0; i < board[0].length; i++)
        {
            Piece piece = board[0][i].getPiece();
            if(piece != null && piece.getColor() == Piece.color.RED) {
                piece.makePieceKing();
            }
        }
        int last = board.length - 1;
        for(int i = 0; i < board[last].length; i++)
        {
            Piece piece = board[last][i].getPiece();
            if(piece != null && piece.getColor() == Piece.color.WHITE) {
                piece.makePieceKing();
            }
        }
    }



}
