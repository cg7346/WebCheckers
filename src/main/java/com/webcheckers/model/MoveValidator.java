package com.webcheckers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class to help validate the moves made
 * by our Checkers Game.
 * Manipulates the currentTurn in Checkers Game
 * based on moves the players made
 *
 * @author Mallory Bridge mmb2582
 * @author Anthony Ferraioli amf7619
 * @author Jacquelyn Leung jil4009
 * @author Kelly Vo kdv6978
 */
public class MoveValidator {

    //The game we are playing
    private CheckersGame game;

    //Non jump moves that Red can Make
    private ArrayList<Move> singleRedMoves;
    //Non jump moves that White can Make
    private ArrayList<Move> singleWhiteMoves;
    //Jump moves Red can make;
    private ArrayList<Move> jumpRedMoves;
    //Jump moves Red can make;
    private ArrayList<Move> jumpWhiteMoves;

    private Tree.Heuristic heuristic;
    public static final int KING_WORTH = 5;
    public static final int WINNING_VALUE = 1000000;
    public static final int PIECE_WORTH = 1;

    // The number of white pieces on the board
    public static Integer whiteCount = 0;
    // The number of red pieces on the board
    public static Integer redCount = 0;

    // Constants
    public final String jumpAvail = "A jump is available";
    public final String invalidMove = "INVALID MOOOVE";
    public final String validMove = "Valid Move!";
    /**
     * Construct a move validator for the game to use
     * for the current turn;
     * @param game
     */
    public MoveValidator(CheckersGame game){
        this.game = game;
        this.singleRedMoves = new ArrayList<>();
        this.singleWhiteMoves = new ArrayList<>();
        this.jumpRedMoves = new ArrayList<>();
        this.jumpWhiteMoves = new ArrayList<>();
        lookForMoves();
    }

    /**
     * Iterates over the board in Checkers Game to
     * see where each piece is and where it can move
     * This is the first step when a game is created
     * or when we check a turn
     */
    public void lookForMoves(){
        clearArrays();
        //System.out.println("Looking for Moves");
        for (int row = 0; row < game.ROWS; row++) {
            for (int col = 0; col < game.COLS; col++) {
                if (game.doesSpaceHavePiece(row, col)) {
                    lookInSpace(row, col);
                }
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
    public void lookInSpace(int row, int col){
        //System.out.println("Looking in " + row + " " + col);
        boolean isRed = game.isSpaceRedPiece(row, col);
        boolean isKing = game.isSpaceKingPiece(row, col);
        if(!isRed && !isKing){
            checkSingleMovesWhite(row, col);
            whiteCount++;
        }
        if(isRed && !isKing){
            checkSingleMovesRed(row, col);
            redCount++;
        }
        if(!isRed && isKing){
            checkKingMoves(row, col, false);
            whiteCount++;
        }
        if(isRed && isKing){
            checkKingMoves(row, col, true);
            redCount++;
        }

    }

    /**
     * Next step in looking for moves for white player.
     * Looks for all pieces that can move up (closer to row 7)
     * on the board without jumping over another piece
     * @param row the row the white piece starts
     * @param col the column the white piece starts
     */
    public void checkSingleMovesWhite(int row, int col){
        int nextRow = row + 1;
        if (nextRow < 8) {
            checkColumns(row, col, nextRow, false);
        }
    }

    /**
     * Next step in looking for moves for red player.
     * Looks for all pieces that can move down (closer to row 0)
     * on the board without jumping over another piece
     * @param row the row the red piece starts
     * @param col the column the red piece starts
     */
    public void checkSingleMovesRed(int row, int col){
        int nextRow = row - 1;
        if (nextRow >= 0) {
            checkColumns(row, col, nextRow, true);
        }
    }

    /**
     * Next step in looking for moves to any color
     * king player. We look up (closer to 7) and down
     * (closer to 0) for each king Piece
     * @param row the row the king piece starts
     * @param col the column the king piece starts
     * @param isRed are we looking for red or white kings
     */
    public void checkKingMoves(int row, int col,  boolean isRed) {
        int rowUp = row - 1;
        if (rowUp >= 0) {
            checkColumns(row, col, rowUp, isRed);
        }
        int rowDown = row + 1;
        if (rowDown < 8) {
            checkColumns(row, col, rowDown, isRed);
        }
    }


    /**
     * Checks both side the columns to the left and right of a piece
     * to see if it is empty and a valid place to put the piece. If the
     * space is not valid because there is a piece there, then we check for
     * jumps.
     * @param row row to start in
     * @param col column to start in
     * @param nextRow the next row to check (either one up or one down
     *                of the current row)
     * @param isRed are we looking for moves for red or white players?
     */
    public void checkColumns(int row, int col, int nextRow, boolean isRed){
        //if we are looking for red use red's arrays, use white otherwise
        ArrayList<Move> singleMoves = isRed ? singleRedMoves : singleWhiteMoves;
        ArrayList<Move> jumpMoves = isRed ? jumpRedMoves : jumpWhiteMoves;
        //check move to left
        addToMoveArray(checkMove(row, col, nextRow, col - 1, isRed),
                singleMoves, jumpMoves);
        //check move to right
        addToMoveArray(checkMove(row, col, nextRow, col + 1, isRed),
                singleMoves, jumpMoves);
    }

    /**
     * Little helper method for checkColumns to pick
     * which array to add the moves into
     * Analyzes the move to see if it is null and if not
     * if it has a piece (meaning it's a jump)
     * @param move the Move to add
     */
    public void addToMoveArray(Move move, ArrayList<Move> singleMoves,
                              ArrayList<Move> jumpMoves){
        if(move != null) {
            if (move.hasPiece()) {

                jumpMoves.add(move);
            } else {
                singleMoves.add(move);
            }
        }

    }

    /**
     * The final step in looking for moves, checking the directly surrounding
     * spaces for valid places to put a piece. If the move is valid because it is
     * a single move over one, it is returned. If it is a jump moved a move
     * constructed with the piece it jumped over is returned.
     * @param startRow row to start in
     * @param startCol column to start in
     * @param endRow row to end in
     * @param endRow column to end
     * @return the Move if we found one :)
     */
    public Move checkMove(int startRow, int startCol, int endRow, int endCol, boolean isRed){
        //make sure the move is on the board
        if(endCol >= game.COLS || endCol < 0 || endRow >= game.ROWS || endRow < 0) {return null;}
        boolean isEndValid = game.isSpaceValid(endRow, endCol);
        boolean spaceHasPiece = game.doesSpaceHavePiece(endRow, endCol);
        //either the player is red or the piece is red NOT BOTH
        boolean isPieceOppositeColor = false;
        if (isRed && !game.isSpaceRedPiece(endRow, endCol) ||
                !isRed && game.isSpaceRedPiece(endRow, endCol)){
            isPieceOppositeColor = true;
        }
        //case for when the the piece moves over one diagnol to empty space
        if (isEndValid){
            return  new Move(new Position(startRow, startCol), new Position(endRow, endCol));
        } else if(spaceHasPiece && isPieceOppositeColor){
            Piece piece = game.tossThatPiece(endRow, endCol);
            int newEndRow = startRow + ((endRow - startRow) * 2);
            int newEndCol = startCol + ((endCol - startCol) * 2);
            //make sure that new move is on the board
            if(newEndCol >= game.COLS || newEndCol < 0 ||
                    newEndRow >= game.ROWS || newEndRow < 0) {return null;}
            if(game.isSpaceValid(newEndRow, newEndCol)){
                return new Move(new Position(startRow, startCol),
                        new Position(newEndRow, newEndCol), piece);
            }
        }
        //if the end space in not valid for either a single move or jump
        return null;
    }

    /**
     * Returns if there are jump moves in our list
     * of possible for active player moves.
     * Used to enforce jumping
     * @return true if the list is not empty
     */
    public boolean areThereJumpMoves(){
        ArrayList<Move> jumps = game.isActivePlayerRed() ? jumpRedMoves : jumpWhiteMoves;
        return jumps.size() > 0;
    }

    /**
     * After all of the moves have been looked for we
     * can now see if move the player was trying to create is
     * in the list of moves we have already marked as valid
     * @param move Move to see if possible
     * @return true if in the list of moves, false if not
     */
    public String isInMoves(Move move) {
        boolean isRed = game.isActivePlayerRed();
        move = isRed ? move : game.moveConverter(move);
        ArrayList<Move> moves = isRed ? singleRedMoves : singleWhiteMoves;
        ArrayList<Move> jumps = isRed ? jumpRedMoves : jumpWhiteMoves;
        if (!game.hasValidMoveBeenMade()) {
            for (Move possibleMove : moves) {
                if (possibleMove.equals(move)) {
                    if (!areThereJumpMoves()) {
                        return validMove;
                    } else {
                        return jumpAvail;
                    }
                }
            }
            for (Move possibleMove : jumps) {
                game.jumpIsPossible();
                if (possibleMove.equals(move)) {
                    return validMove;
                }
            }
        }
        return invalidMove;
    }

    /**
     * Returns the last move made during our
     * current turn. (peeks are the top of stack
     */
    public Move getLastMove(){
        return game.getLastMove();
    }

    /**
     * Backs up the turn made by the last player
     */
    public void backUpMove(){
        Move lastMove = getLastMove();
        //Flip flops the move so now the end is the start
        Move reverseMove = new Move(lastMove.getEnd(), lastMove.getStart());
        game.makeMove(reverseMove);

        Piece p = game.backupLastMove();
        if (p != null){
            //even if the move is made by the red person, the original
            //will be returned
            Move convertedMove = game.moveConverter(lastMove);
            Position piecePos = game.getPiecePosition(convertedMove);
            game.addPiece(piecePos.getRow(), piecePos.getCol(), p);
        }
        //if we have nothing in the queue (meaning we went all the way back
        //to start of turn, look for anymove anywhere
        if(game.isTurnEmpty()) {
            lookForMoves();
        //else, look for moves only at that last move, so the jump
        //in the middle of the last double jump
        } else {
            lastMove = game.moveConverter(lastMove);
            lookInSpace(lastMove.getStart().getRow(), lastMove.getStart().getCol());
        }
    }

    /**
     * Creates new arrays for all moves to
     * 'reset' them
     */
    public void clearArrays() {
        singleRedMoves = new ArrayList<>();
        singleWhiteMoves = new ArrayList<>();
        jumpRedMoves = new ArrayList<>();
        jumpWhiteMoves = new ArrayList<>();
        redCount = 0;
        whiteCount = 0;
    }

    /**
     * checks if the red is out of moves
     * @return whether or not the red player is out of moves
     */
    public Boolean isRedOut(){
        if (jumpRedMoves.isEmpty() && singleRedMoves.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * checks if white is out of moves
     * @return whether or not the white player is out of moves
     */
    public Boolean isWhiteOut(){
        if (jumpWhiteMoves.isEmpty() && singleWhiteMoves.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Gets the number of white pieces on the board
     * @return the number of white pieces on the board
     */
    public Integer getWhiteCount(){
        return this.whiteCount;
    }


    /**
     * Gets the number of red pieces on the board
     * @return the number of red pieces on the board
     */
    public Integer getRedCount(){
        return this.redCount;
    }

    /**
     * @return list of available moves
     */
    public final List<Move> getMovesForRed() {
        java.util.List<Move> moves = new ArrayList<>();
        moves.addAll(singleRedMoves);
        moves.addAll(jumpRedMoves);
        return moves;
    }

    /**
     * @return list of available moves
     */
    public final List<Move> getMovesForWhite() {
        java.util.List<Move> moves = new ArrayList<>();
        moves.addAll(singleWhiteMoves);
        moves.addAll(jumpWhiteMoves);
        return moves;
    }

    /**
     * @return the score using the appropriate heuristic method
     */
    public final int score() {
        if (Tree.Heuristic.A == heuristic || heuristic == null) {
            return scoreA();
        } else if (Tree.Heuristic.B == heuristic) {
            return scoreB();
        } else {
            return scoreC();
        }
    }

    public final int scoreA() {
        int score = 0;
        if (game.isGameOver()) {
            score = WINNING_VALUE;
        } else {
            for (int row = 0; row < game.ROWS; row++) {
                for (int col = 0; col < game.COLS; col++) {
                    if (game.doesSpaceHavePiece(row, col)) {
                        boolean isKing = game.isSpaceKingPiece(row, col);
                        boolean isRed = game.isSpaceRedPiece(row, col);
                        score += isRed && isKing ? KING_WORTH : PIECE_WORTH;
                    }
                }
            }
            for (int row = 0; row < game.ROWS; row++) {
                for (int col = 0; col < game.COLS; col++) {
                    if (game.doesSpaceHavePiece(row, col)) {
                        boolean isKing = game.isSpaceKingPiece(row, col);
                        boolean isRed = game.isSpaceRedPiece(row, col);
                        score -= !isRed && isKing ? KING_WORTH : PIECE_WORTH;
                    }
                }
            }
        }
        return score;
    }

    public int scoreB() {
        int redScore = 0;
        for (int row = 0; row < game.ROWS; row++) {
            for (int col = 0; col < game.COLS; col++) {
                if (game.doesSpaceHavePiece(row, col)) {
                    boolean isKing = game.isSpaceKingPiece(row, col);
                    boolean isRed = game.isSpaceRedPiece(row, col);
                    redScore += isRed && isKing ? KING_WORTH : PIECE_WORTH;
                }
            }
        }
        List<Move> redMoves = getMovesForRed();

        redScore /= (redMoves.size() == 0 ? 1 : redMoves.size());

        int whiteScore = 0;
        for (int row = 0; row < game.ROWS; row++) {
            for (int col = 0; col < game.COLS; col++) {
                if (game.doesSpaceHavePiece(row, col)) {
                    boolean isKing = game.isSpaceKingPiece(row, col);
                    boolean isRed = game.isSpaceRedPiece(row, col);
                    whiteScore += !isRed && isKing ? KING_WORTH : PIECE_WORTH;
                }
            }
        }
        List<Move> whiteMoves = getMovesForWhite();
        whiteScore /= (whiteMoves.size() == 0 ? 1 : whiteMoves.size());

        return redScore - whiteScore;
    }

    public int scoreC() {
        return scoreA() + getMovesForRed().size() - getMovesForWhite().size();
    }

}
