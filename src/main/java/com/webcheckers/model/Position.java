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
     * @param row
     * @param cell
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
     * @return
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
     * equals method for postion
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            return this.row == ((Position) obj).row && this.cell == ((Position) obj).cell;
        }
        return false;
    }

    /**
     * hashcode for position
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, cell);
    }




}
