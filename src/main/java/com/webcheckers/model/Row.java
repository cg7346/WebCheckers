package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * creates a row
 *
 * @author <a href='mailto:jil4009@rit.edu'>Jackie Leung</a>
 */
public class Row {

    //
    // Attributes
    //

    private int index;
    private ArrayList<Space> spaceList;

    //
    // Constructor
    //

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

}
