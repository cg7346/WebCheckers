package com.webcheckers.model;


/**
 * Represents a Piece in the game, to be controlled by either player
 * Holds the information responsible for the color and type of piece
 *
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class Piece {
    private enum type {SINGLE, KING}
    private enum color {RED, WHITE}

    private type pieceType;
    private color pieceColor;
    private boolean canMove;

    /**
     * The constructor for a Piece. Takes an integer for the color
     *
     * @param colorNum 0 for RED, 1 for WHITE
     */
    public Piece(int colorNum)
    {
        pieceType = type.SINGLE;
        pieceColor = color.values()[colorNum];
        canMove = true;
    }

    /**
     * Gets the type of the Piece
     *
     * @return type.SINGLE or type.KING
     */
    public type getType()
    {
        return pieceType;
    }

    /**
     * Returns the color of the Piece
     *
     * @return color.RED or color.WHITE
     */
    public color getColor()
    {
        return pieceColor;
    }

    public boolean isRedPiece(){
        return pieceColor == color.RED;
    }

    public boolean isPieceKing(){
        return pieceType == type.KING;
    }

    /**
     * Either enables or disables ability for piece
     * to be moved
     * @param choice true to enable, false to disable
     */
    public void disableEnableMove(boolean choice){
        canMove = choice;
    }
}
