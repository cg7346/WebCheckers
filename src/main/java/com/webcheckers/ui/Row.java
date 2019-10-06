package com.webcheckers.ui;

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
    public Row(int index, Space[] spaces){
        if(index > 7 || index < 0){
            throw new IndexOutOfBoundsException();
        } else {
            this.index = index;
        }
        spaceList = new ArrayList<>();
        spaceList.addAll(Arrays.asList(spaces));

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
}
