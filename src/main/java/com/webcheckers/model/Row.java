package com.webcheckers.model;

import java.util.*;

/**
 * creates a row
 * @author Jacquelyn Leung
 */

public class Row {
    private int index;
    private ArrayList<Space> spaceList;

    /**
     * initialize row
     * if index is out of bounds then an index out of bounds exception will be called
     * @param index the row number
     */
    public Row(int index, ArrayList<Space> spaces){
        if(index > 7 || index < 0){
            throw new IndexOutOfBoundsException();
        } else {
            this.index = index;
        }
        spaceList = new ArrayList<>();
        spaceList.addAll(spaces);

    }

    /**
     * This will return the index of the row
     * @return row index
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * creates a java iterator of the spaces in the row
     * @return Iterator of space
     */
    public Iterator<Space> iterator(){
        return this.spaceList.listIterator();

    }

    /**
     * Returns if that space has a piece that can move
     * @param col the column of piece to check
     * @return true if can move, false if not
     */
//    public boolean canSpaceFromRow(int col){
//        Space space = spaceList.get(col);
//        return space.canPieceMove();
//    }

    /**
     * Either enables or disables ability for piece
     * to be moved
     * @param col the column of piece to check
     * @param choice true to enable, false to disable
     */
    public void disableEnableMove(int col, boolean choice){
        Space space = spaceList.get(col);
        space.disableEnableMove(choice);
    }
}
