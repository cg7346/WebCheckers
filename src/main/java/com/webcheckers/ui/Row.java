package com.webcheckers.ui;

public class Row {
    private int index;

    /**
     * initialize row
     * if index is out of bounds then an index out of bounds exception will be called
     * @param index the row number
     */
    public Row(int index){
        if(index > 7 || index < 0){
            throw new IndexOutOfBoundsException();
        } else {
            this.index = index;
        }
    }

    /**
     * This will return the index of the row
     * @return row index
     */
    public int getIndex(){
        return this.index;
    }
}
