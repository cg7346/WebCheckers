package com.webcheckers.ui;

import com.webcheckers.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Iterator;

/**
 * tests the board view
 * @author jil4009
 */

@Tag("UI-tier")
public class BoardViewTest {
    private Player red;
    private Player white;
    private  CheckersGame checkersGame;
    private BoardView CuT;


    @Test
    void testSetup(){
        this.red = Mockito.mock(Player.class);
        this.white = Mockito.mock(Player.class);
        this.checkersGame = new CheckersGame(this.red, this.white, 0);

        this.CuT = new BoardView(this.red, this.checkersGame);
    }
/*
    //test to check red view
    @Test
    void iteratorRedTest() {
        int rowNum = 7;
        Iterator<Row> rows = CuT.iterator();
        Space[][] boardSpaces = this.checkersGame.getBoard();
        while (rows.hasNext()) {
            int colNum = 7;
            Row row = rows.next();
            Iterator<Space> spaces = row.iterator();
            while (spaces.hasNext()) {
                Space space = spaces.next();
                //TODO how to figure out how to get the space symbol
                Assertions.assertEquals(boardSpaces[rowNum][colNum], space, "Space mismatch in iterator");
                colNum--;
            }
            rowNum--;
        }

    }
    */

}
