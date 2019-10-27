package com.webcheckers.model;

/**
 * Position of the piece
 * @author jil4009
 * @author  mmb2582
 */
public class Position {

    private int row;
    private  int col;

    public Position(int row, int col){
        if(row <= 7 && row >= 0){
            this.row = row;
        }
        if(col <= 7 && col >= 0) {
            this.col = col;
        }
    }
}
