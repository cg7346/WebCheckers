package com.webcheckers.model;

import java.util.Objects;

/**
 * Position of the piece
 * @author jil4009
 * @author  mmb2582
 */
public class Position {

    private int row;
    private  int cell;

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
     * @return
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




}
