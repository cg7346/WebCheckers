package com.webcheckers.ui;

public class Row {
    private int index;

    public Row(int index){
        if(index > 7 || index < 0){
            throw new IndexOutOfBoundsException();
        } else {
            this.index = index;
        }
    }

    public int getIndex(){
        return this.index;
    }
}
