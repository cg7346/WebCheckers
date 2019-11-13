package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Tag("Model-Tier")
public class SpaceTest {

    //CuT
    private Space BlackCuT;
    private Space WhiteCuT;

    //Mock Piece for Testing
    private Piece mockPiece;

    //These will be used for every test except invalid construct
    @BeforeEach
    void construct(){
        this.BlackCuT = new Space(0, true);
        this.WhiteCuT = new Space(0, false);
        this.mockPiece = mock(Piece.class);

    }

    @Test
    void test_invalid_construct(){
        assertThrows(IndexOutOfBoundsException.class, () ->
                new Space(8, true));
        assertThrows(IndexOutOfBoundsException.class, () ->
                new Space(-1, false));
    }

    @Test
    void test_get_cell_idx(){
        assertEquals(BlackCuT.getCellIdx(), 0,
                "Cell index should be 0");
    }

    //@Test test_has_piece(){
    //    assertTrue(BlackCuT.hasPiece());
    //}

    /**
     * tests the remove piece
     */
    @Test
    void test_remove_piece(){
        BlackCuT.removePiece();
        WhiteCuT.removePiece();
        assertFalse(BlackCuT.hasPiece(), "Something wrong with remove piece for black space");
        assertFalse(WhiteCuT.hasPiece(), "Something wrong with remove piece for white space");
    }

    /**
     * testing function is red piece
     */
    @Test
    void test_is_red_piece(){
        when(mockPiece.isRedPiece()).thenReturn(true);
        BlackCuT.addPiece(mockPiece);
        assertTrue(BlackCuT.isRedPiece(), "is red piece not working");
        assertFalse(WhiteCuT.isRedPiece(), "is red piece not working");

    }


    /**
     * testing is space is valid
     */
    @Test
    void test_is_valid(){
        assertTrue(BlackCuT.isValid(), "Black space without piece is valid");
        assertFalse(WhiteCuT.isValid(), "White space is not valid");
    }
}
