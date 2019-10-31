package com.webcheckers.model;

import java.util.Stack;

public class Turn {

    private Stack<Move> moves;
    private Stack<Piece> removedPieces;
    private Piece movePiece;

    public Turn(Piece movePiece)
    {
        this.movePiece = movePiece;
    }

    public Move LastMove()
    {
        return moves.peek();
    }

    public void AddMove(Move m, Piece p)
    {
        moves.push(m);
        if(p != null)
        {
            removedPieces.push(p);
        }
    }


}
