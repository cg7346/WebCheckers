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
    private Player activePlayer;

    public Turn(Player activePlayer) {
        moves = new Stack<>();
        removedPieces = new Stack<>();
        isJumpPossible = false;
        this.activePlayer = activePlayer;
    }

    public Move lastMove()
    {
        return moves.peek();
    }

    public void addMove(Move m, Piece p)
    {
        moves.push(m.addPiece(p));
        removedPieces.push(p);
    }

    public void addMove(Move m)
    {
        moves.push(m);
    }

    /**
     * Removes the most recent move and piece associated (if there is one)
     * @return the piece removed in the move. Null if no piece associated
     */
    public Piece backupLastMove()
    {
        if(!moves.empty())
        {
            moves.pop();
            if(!removedPieces.empty())
            {
                return removedPieces.pop();
            }
        }
        return null;
    }

    public void jumpIsPossible()
    {
        isJumpPossible = true;
    }

    public boolean isJumpPossible()
    {
        return isJumpPossible;
    }

    public Player getActivePlayer(){
        return activePlayer;
    }
}
