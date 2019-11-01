package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class MoveTest {

    private Move CuT;

    //Positions are already well tested friendly
    private Position p1;
    private Position p2;
    private Position p3;

    @BeforeEach
    void CreateMoveTest(){
        this.p1 = new Position(0, 1);
        this.p2 = new Position(1, 2);
        this.p3 = new Position(0, 1);
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





}
