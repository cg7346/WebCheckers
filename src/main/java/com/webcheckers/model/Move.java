package com.webcheckers.model;

/**
 * class for move on the checkers board
 * @author jil4009
 * @author mmb2582
 */
public class Move {

    private final Position start;
    private final Position end;

    /**
     * constructor for move
     * @param start
     * @param end
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
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
        return "Start: " + start + " End: " + end;
    }

}
