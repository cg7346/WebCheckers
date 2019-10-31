package com.webcheckers.model;

import java.util.Stack;

public class Turn {

    private Stack<Move> moves;
    private Stack<Piece> removedPieces;
    private Piece movePiece;

    public Turn()
    {
        moves = new Stack<>();
        removedPieces = new Stack<>();
    }

    public Move LastMove()
    {
        return moves.peek();
    }

    public void AddMove(Move m, Piece p)
    {
        moves.push(m);
        removedPieces.push(p);
    }

    public void AddMove(Move m)
    {
        moves.push(m);
    }

    public Piece BackupLastMove()
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


}
