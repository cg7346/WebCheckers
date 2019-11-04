package com.webcheckers.model;

import java.util.ArrayList;

/**
 * Class to help validate the moves made
 * by our Checkers Game
 *
 * @author Mallory Bridge mmb2582
 * @author Anthony Ferraioli amf7619
 * @author Jacquelyn Leung jil4009
 */
public class MoveValidator {

    //The game we are playing
    private CheckersGame game;
    //The current turn to add moves to
    private Turn currentTurn;
    //The current player for the Turn

    //Non jump moves that Red can Make
    private ArrayList<Move> singleRedMoves;
    //Non jump moves that White can Make
    private ArrayList<Move> singleWhiteMoves;
    //Jump moves Red can make;
    private ArrayList<Move> jumpRedMoves;
    //Jump moves Red can make;
    private ArrayList<Move> jumpWhiteMoves;

    /**
     * Construct a move validator for the game to use
     * for the current turn;
     * @param game
     */
    public MoveValidator(CheckersGame game){
        this.game = game;
        this.currentTurn = new Turn(game.getActivePlayer());
        this.singleRedMoves = new ArrayList<>();
        this.singleWhiteMoves = new ArrayList<>();
        this.jumpRedMoves = new ArrayList<>();
        this.jumpWhiteMoves = new ArrayList<>();
    }

    /**
     * If a move was made by the white player
     * it needs to be converted from how the board view
     * reads spaces to how
     * @param oldMove the move to convert
     * @return a new converted move, or the old move if
     * the active player is red (no conversion need)
     */
    public Move moveConverter(Move oldMove) {
        Position start = oldMove.getStart();
        Position end = oldMove.getEnd();
        if (game.isActivePlayerRed()) {
            /*Because spaces keep their index, we keep the
            same column, but flip the row */
            Position convertedStart = new Position(
                    game.ROWS - start.getRow() - 1,
                    start.getCol());
            Position convertedEnd = new Position(
                    game.ROWS - end.getRow() - 1,
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
     * Iterates over the board in Checkers Game to
     * see where each piece is and where it can move
     * This is the first step when a game is created
     * or when we check a turn
     */
    public void lookForMoves(){
        for (int row = 0; row < game.ROWS; row++) {
            for (int col = 0; col < game.COLS; col++) {
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
    public void lookInSpace(int row, int col){
        boolean isRed = game.isSpaceRedPiece(row, col);
        boolean isKing = game.isSpaceKingPiece(row, col);
        if(!isRed && !isKing){
            //checkWhiteSingleMoves
        }
        if(isRed && !isKing){
            //checkRedSingleMoves
        }
        if(!isRed && isKing){
            //checkKingMove with White
        }
        if(isRed && isKing){
            //checkKingMove with Red
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
    public void checkRedSingleMove(int row, int col){
        int nextRow = row - 1;
        if (nextRow >= 8) {
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
        if (rowUp >= 8) {
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
        boolean isPieceOppositeColor = isRed ^ game.isSpaceRedPiece(endRow, endCol);

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
        return jumps.size() == 0;
    }

    /**
     * After all of the moves have been looked for we
     * can now see if move the player was trying to create is
     * in the list of moves we have already marked as valid
     * @param move Move to see if possible
     * @return true if in the list of moves, false if not
     */
    public boolean isInMoves(Move move){
        boolean isRed = game.isActivePlayerRed();
        move = isRed ? move : moveConverter(move);
        ArrayList<Move> moves = isRed ? singleRedMoves : singleWhiteMoves;
        ArrayList<Move> jumps = isRed ? jumpRedMoves : jumpWhiteMoves;
        for (Move possibleMove : moves){
            //TODO force jump before submit turn >:)
            if (possibleMove.equals(move) && !currentTurn.isJumpPossible()){
                // forces jump moves
                if(areThereJumpMoves()) {
                    return false;
                }
                return true;
            }
        }
        for (Move possibleMove : jumps){
            //if we see there is anything in the list of moves,
            //mark it in our turn
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
    public void keepLastMove(Move move) {
        Position startPos = move.getStart();
        Position endPos = move.getEnd();
        //check to see if we have a difference of two in this move
        //meaning it is a jump and we need to tack the piece back on it
        if (Math.abs(startPos.getRow() - endPos.getRow()) == 2 &&
                Math.abs(startPos.getCol() - endPos.getCol()) == 2) {
            Piece piece = game.findPiece((game.isActivePlayerRed()) ? move : moveConverter(move));
            this.currentTurn.addMove(move, piece);
        }
        else{
            this.currentTurn.addMove(move);
        }
    }

    /**
     * Returns the last move made during our
     * current turn. (peeks are the top of stack
     */
    public Move getLastMove(){
        return currentTurn.lastMove();
    }


}
