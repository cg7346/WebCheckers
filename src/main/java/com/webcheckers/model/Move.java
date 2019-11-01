package com.webcheckers.model;

/**
 * class for move on the checkers board
 * @author jil4009
 * @author mmb2582
 */
public class Move {

    private final Position start;
    private final Position end;
    private final Piece jumpedPiece;

    /**
     * contructor for move
     * @param start
     * @param end
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
        jumpedPiece = null;
    }

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

    public boolean hasPiece() {
        return jumpedPiece != null;
    }

    public Piece getPiece() {
        if (hasPiece()){
            return jumpedPiece;
        }
        return null;
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

    @Override
    public String toString(){
        String s = "Start: " + start + " End: " + end;
        if (hasPiece()){
            s = s.concat(" with JumpPiece!");
        }
        return s;
    }

}
