package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    private Piece RedCuT;
    private Piece WhiteCuT;

    @BeforeEach
    void construct(){
        this.RedCuT = new Piece(0);
        this.WhiteCuT = new Piece(1);
    }

    @Test
    void test_get_color(){
        assertEquals(RedCuT.getColor(), Piece.color.RED,
                "Red Piece should be Red");
        assertEquals(WhiteCuT.getColor(), Piece.color.WHITE,
                "White piece should be White");
    }

    @Test
    void test_is_red_Piece(){
        assertTrue(RedCuT.isRedPiece(),
                "Red piece is Red");
        assertFalse(WhiteCuT.isRedPiece(),
                "White piece is White");
    }

    @Test
    void test_is_king_piece_not_king(){
        assertFalse(WhiteCuT.isPieceKing(),
                "White piece should not be king");
        assertFalse(RedCuT.isPieceKing(),
                "Red piece should not be king");
    }

    @Test
    void test_make_piece_king(){
        WhiteCuT.makePieceKing();
        RedCuT.makePieceKing();
        assertTrue(RedCuT.isPieceKing(),
                "Red Piece should now be king");
        assertTrue(RedCuT.isPieceKing(),
                "White Piece should now be king");
    }
}
