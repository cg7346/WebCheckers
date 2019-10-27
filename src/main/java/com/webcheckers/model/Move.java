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
     * contructor for move
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
}
