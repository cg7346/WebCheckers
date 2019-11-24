package com.webcheckers.model;

/**
 * Represents a Piece in the game, to be controlled by either player
 * Holds the information responsible for the color and type of piece
 *
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class Piece {
    //
    // Constants
    //
    private enum type {SINGLE, KING}
    enum color {RED, WHITE}

    //
    // Attributes
    //

    private type pieceType;
    private color pieceColor;

    //
    // Constructor
    //

    /**
     * The constructor for a Piece. Takes an integer for the color
     * @param colorNum 0 for RED, 1 for WHITE
     */
    public Piece(int colorNum) {
        pieceType = type.SINGLE;
        pieceColor = color.values()[colorNum];
    }

    /**
     * Gets the type of the Piece
     * @return type.SINGLE or type.KING
     */
    public type getType()
    {
        return pieceType;
    }

    /**
     * Returns the color of the Piece
     * @return color.RED or color.WHITE
     */
    public color getColor()
    {
        return pieceColor;
    }

    /**
     * this function checks if it is a red piece
     * @return true or false
     */
    public boolean isRedPiece(){
        return pieceColor == color.RED;
    }

    /**
     * checks if the piece is a king or not
     * @return true if piece is king, false if single
     */
    public boolean isPieceKing(){
        return pieceType == type.KING;
    }

    /**
     * make a piece king
     */
    public void makePieceKing(){
        pieceType = type.KING;
    }
}
