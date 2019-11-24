package com.webcheckers.model;

import java.util.Objects;

/**
 * Position of the piece
 *
 * @author <a href='mailto:mmb2582@rit.edu'>Mallory Bridge</a>
 * @author <a href='mailto:jil4009@rit.edu'>Jackie Leung</a>
 */
public class Position {

    //
    // Attributes
    //

    private int row;
    private  int cell;

    //
    // Constructor
    //

    /**
     * constructor for position
     * @param row Board row position is in
     * @param cell Board col position is in
     */
    public Position(int row, int cell){
        if(row > 7 || row < 0 || cell > 7 || cell < 0) {
            throw new IllegalArgumentException("the rows and cells must be between 0 and 7");
        }
        this.row = row;
        this.cell = cell;

    }

    /**
     * getter for row
     * @return int row position
     */
    public int getRow(){
        return this.row;
    }

    /**
     * getter for cell
     * @return integer of column
     */
    public int getCol(){
        return this.cell;
    }

    /**
     * equals method for position
     * @param obj Object to compare
     * @return true if equals, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position otherPos = (Position)obj;
            return this.row == otherPos.row && this.cell == otherPos.cell;
        }
        return false;
    }

    /**
     * hashcode for position
     * @return int hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, cell);
    }

    /**
     * this function overrides the toString method to return a wanted string
     * @return String
     */
    @Override
    public String toString(){
        return "(" + row + ", " + cell + ")";
    }
}
