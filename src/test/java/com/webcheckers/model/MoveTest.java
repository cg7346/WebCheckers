package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class MoveTest {

    private Move CuT;

    //Positions are already well tested friendly
    private Position p1;
    private Position p2;
    private Position p3;

    //Mock Piece
    private Piece piece;

    @BeforeEach
    void CreateMoveTest(){
        this.p1 = new Position(0, 1);
        this.p2 = new Position(1, 2);
        this.p3 = new Position(0, 1);
        piece = mock(Piece.class);
    }

    @Test
    void test_getStart(){
        this.CuT = new Move(p1, p2);
        assertEquals(CuT.getStart(), p1);
    }

    @Test
    void test_getEnd(){
        this.CuT = new Move(p1, p2);
        assertEquals(CuT.getEnd(), p2);
    }

    @Test
    void test_unequalMoves(){
        this.CuT = new Move(p1, p2);
        Move otherMove = new Move(p2, p1);
        assertNotEquals(CuT, otherMove, "Move (0, 1) -> (1, 2) should not equal (1, 2) -> (0, 1)");
    }

    @Test
    void test_equalMoves(){
        this.CuT = new Move(p1, p3);
        Move otherMove = new Move(p1, p1);
        assertEquals(CuT, otherMove, "Move (0, 1) -> (0, 1) should equal (0, 1) -> (0, 1)");
    }

    @Test
    void does_not_has_piece(){
        this.CuT = new Move(p1, p2);
        assertFalse(CuT.hasPiece());
    }

    @Test
    void get_piece_no_piece(){
        this.CuT = new Move(p1, p2);
        assertNull(CuT.getPiece());
    }

    @Test
    void does_has_piece(){
        this.CuT = new Move(p1, p2, piece);
        assertTrue(CuT.hasPiece());
    }

    @Test
    void get_piece_with_piece(){
        this.CuT = new Move (p1, p2, piece);
        assertNotNull(CuT.getPiece());
    }

    @Test
    void no_piece_to_String(){
        this.CuT = new Move(p1, p2);
        String s = "Start: (0, 1) End: (1, 2)";
        assertEquals(CuT.toString(), s,
                "CuT.toString should equal " + s);
    }

    @Test
    void with_piece_to_String(){
        this.CuT = new Move(p1, p2, piece);
        String s = "Start: (0, 1) End: (1, 2) with JumpPiece!";
        assertEquals(s, CuT.toString(),
                "CuT.toString should equal " + s);


    }


}
