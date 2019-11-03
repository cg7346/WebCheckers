package com.webcheckers.model;


/**
 * Represents a Space on the board
 * Contains its position in the row and the piece on it
 * Can determine validity of movement and other space purposes
 *
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class Space {
    private int cellIdx;
    private Piece piece;
    private boolean isBlack;


    /**
     * Creates a space on the board without a piece
     *
     * @param cellIdx the index of the space in the row (0-7)
     * @param isBlack boolean if piece is dark tile or not
     */
    public Space(int cellIdx, boolean isBlack)
    {
        if(cellIdx < 0 || cellIdx > 7) {
            throw new IndexOutOfBoundsException();
        }
        this.cellIdx = cellIdx;
        this.piece = null;
        this.isBlack = isBlack;
    }

    /**
     * Gets the index of the space in the row
     *
     * @return cell index
     */
    public int getCellIdx()
    {
        return cellIdx;
    }

    /**
     * Checks if the space is valid for player movement
     *
     * @return validity of space
     */
    public boolean isValid() {
        return !hasPiece() && isBlack;
    }

    /**
     * Get the piece currently on the space
     *
     * @return Piece object on Space
     */
    public Piece getPiece()
    {
        return piece;
    }

    /**
     * Adds a piece onto the space if valid
     *
     * @param piece the piece to add
     */
    public void addPiece(Piece piece){
        if(isValid()) {
            this.piece = piece;
        }

    }

    /**
     * Removes the piece from the space
     */
    public void removePiece()
    {
        this.piece = null;
    }

    /**
     * Checks if there is a piece on the space
     *
     * @return whether or not a piece is present
     */
    public boolean hasPiece()
    {
        return piece != null;
    }

    /**
     * If there is a piece in the space,
     * is it red?
     * @return true if red piece, false if not
     */
    public boolean isRedPiece(){
        if (hasPiece()){
            return piece.isRedPiece();
        }
        return false;
    }

    /**
     * If there is a piece in the space,
     * if it a king piece? Use wit
     * @return
     */
    public boolean isKingPiece(){
        if (hasPiece()){
            return piece.isPieceKing();
        }
        return false;
    }


}
