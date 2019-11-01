package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class PositionTest {

    private Position CuT;

    @BeforeEach
    void construct(){
        this.CuT = new Position(0, 0);
    }
    @Test
    void construct_valid(){
        assertDoesNotThrow(() -> { new Position(0, 0);
        });
    }

    @Test
    void construct_invalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Position(-1, 0); });
    }

    @Test
    void get_row(){
        assertEquals(CuT.getRow(), 0, "Row should be 0");
    }

    @Test
    void get_col(){
        assertEquals(CuT.getCol(), 0, "Column should be 0");
    }

    @Test
    void test_equals(){
        Position otherP = new Position(0, 0);
        assertEquals(CuT, otherP, "Position (0,0) should equal Position (0, 0");
    }

    @Test
    void test_unequal(){
        Position otherP = new Position(7, 7);
        assertNotEquals(CuT, otherP, "Position (0, 0) should " +
                "NOT equal Position (7, 7");
    }

    @Test
    void equal_Hash(){
        Position otherP = new Position(0, 0);
        assertEquals(CuT.hashCode(),  otherP.hashCode(),"Hash codes should be " +
                "equal for equal positions" );
    }

    @Test
    void unequal_Hash(){
        Position otherP = new Position(7, 7);
        assertNotEquals(CuT.hashCode(), otherP.hashCode(), "Hash codes should NOT be " +
                "equal for unequal positions" );
    }

    @Test
    void test_toString(){
        String correct = "(0, 0)";
        assertEquals(CuT.toString(), correct, "toString for Position (0, 0) should" +
                "be '(0,0)'");
    }


}
