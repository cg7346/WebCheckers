package com.webcheckers.model;

import java.util.Stack;

/**
 * Class to represent a turn being made in the game
 * Holds the logic to store the moves being made and to undo them
 */
public class Turn {

    private Stack<Move> moves;
    private Stack<Piece> removedPieces;
    private boolean isJumpPossible;
    private boolean hasSimpleMove;

    /**
     * Creates a new stack to keep track of the moves and if
     * moves are possible
     * @param activePlayer the player of current turn
     */
    public Turn(Player activePlayer) {
        moves = new Stack<>();
        removedPieces = new Stack<>();
        isJumpPossible = false;
        hasSimpleMove = false;
    }

    public Move lastMove() {
        if (!moves.isEmpty()) {
            return moves.peek();
        }
        return null;
    }

    /**
     * Add a move to the stack with a piece
     * This is for jumps moves
     * @param m Move to add
     * @param p Piece to add
     */
    public void addMove(Move m, Piece p)
    {
        moves.push(m.addPiece(p));
        isJumpPossible = true;
        removedPieces.push(p);
    }

    /**
     * Add a move to the stack without a piece
     * This is for simple moves
     * @param m Move to add
     */
    public void addMove(Move m) {
        moves.push(m);
        hasSimpleMove = true;
    }

    /**
     * Removes the most recent move and piece associated (if there is one)
     * @return the piece removed in the move. Null if no piece associated
     */
    public Piece backupLastMove()
    {
        if(!moves.empty()) {
            moves.pop();
            hasSimpleMove = false;
            if(!removedPieces.empty()) {
                isJumpPossible = false;
                return removedPieces.pop();
            }
        }

        return null;
    }

    /**
     * checks if a simple move has been made or not
     * @return true or false of the simple move
     */
    public boolean hasSimpleMoveBeenMade(){
        return hasSimpleMove;
    }

    /**
     * Sets to true when called, meaning
     * we found a jump on this turn and we need to do it
     */
    public void jumpIsPossible()
    {
        isJumpPossible = true;
    }

    /**
     * Returns if we have found a move or not
     * this turn
     * @return
     */
    public boolean isJumpPossible()
    {
        return isJumpPossible;
    }

    /**
     * Returns the stack of moves to loop through
     * @return Stack full of moves
     */
    public Stack<Move> getMoves(){
        return this.moves;
    };

    /**
     * Checks if the stack of moves is empty
     * @return true if empty, false if not
     */
    public boolean isEmpty() {
        return moves.isEmpty();
    }
}
