package com.webcheckers.model;

import javax.sql.RowSet;

/**
 * class for move on the checkers board
 * @author jil4009
 * @author mmb2582
 */
public class Move {

    private final Position start;
    private final Position end;
    private Piece jumpedPiece;

    /**
     * contructor for move
     * @param start Position that started
     * @param end Position that ended
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
        jumpedPiece = null;
    }

    /**
     * antoher constructor for move
     * @param start Position that started
     * @param end Position to end at
     * @param piece the Piece
     */
    public Move(Position start, Position end, Piece piece) {
        this.start = start;
        this.end = end;
        this.jumpedPiece = piece;
    }

    /**
     * getter for start
     * @return Position
     */
    public Position getStart(){
        return this.start;
    }

    /**
     * getter for end
     * @return Position
     */
    public Position getEnd(){
        return this.end;
    }

    /**
     * Adds a piece to a move
     * So for jumps this is the move's luggage, what
     * it picked up when it jumped
     * @param p the piece to add to the move
     * @return the Move with its fresh new piece
     */
    public Move addPiece(Piece p){
        this.jumpedPiece = p;
        return this;
    }

    /**
     * Does the move have a piece it jumped
     * @return true if it does, false if not
     */
    public boolean hasPiece() {
        return jumpedPiece != null;
    }

    /**
     * Returns the piece we jumped over
     * @return the piece we jumped over, or null
     * if nothing was jumped
     */
    public Piece getPiece() {
            return jumpedPiece;
    }

    /**
     * Moves are equal if positions are equal
     * @param obj the Object to compare
     * @return true if same, false if not
     */
    @Override
    public boolean equals(Object obj){
        if (obj == this) return true;
        if (!(obj instanceof Move)) return false;
        final Move otherMove = (Move) obj;
        return otherMove.start.equals(this.start) && otherMove.end.equals(this.end);
    }

    /**
     * to string override for toString
     * @return String
     */
    @Override
    public String toString(){
        String s = "Start: " + start + " End: " + end;
        if (hasPiece()){
            s = s.concat(" with JumpPiece!");
        }
        return s;
    }

}
