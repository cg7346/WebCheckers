package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@Tag("Model-Tier")
public class RowTest {

    //CuT
    private Row CuT;

    //items to create a row
    private ArrayList<Space> spaceList = new ArrayList<Space>();
    private Space[][] board;

    //These will be used for every test except invalid construct
    @BeforeEach
    void construct(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                spaceList.add(new Space(j, true));
            }
        }
        CuT = new Row(0, spaceList);

    }

    @Test
    void test_invalid_construct(){
        assertThrows(IndexOutOfBoundsException.class, () ->
                new Row(8, spaceList));
        assertThrows(IndexOutOfBoundsException.class, () ->
                new Row(-1, spaceList));
    }

    @Test
    void test_get_row_idx(){
        assertEquals(CuT.getIndex(), 0,
                "Row index should be 0");
    }

    @Test
    void test_iterator(){
        assertTrue(CuT.iterator().hasNext(), "iterator not working");
    }
}
