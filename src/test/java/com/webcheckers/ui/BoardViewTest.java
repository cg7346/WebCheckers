package com.webcheckers.ui;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.Row;
import com.webcheckers.model.Space;
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


    void testSetup(boolean isRedPlayer){
        this.red = Mockito.mock(Player.class);
        this.white = Mockito.mock(Player.class);

        this.checkersGame = new CheckersGame(this.red, this.white, "0");

        if(!isRedPlayer){
            this.CuT = new BoardView(this.white, this.checkersGame);
        } else {
            this.CuT = new BoardView(this.red, this.checkersGame);
        }
    }
    //test to check view
    @Test
    void iteratorRedTest() {
        testSetup(true);
        int rowNum = 0;
        Iterator<Row> rows = CuT.iterator();
        Space[][] boardSpaces = this.checkersGame.getBoard();
        while (rows.hasNext()) {
            int colNum = 0;
            Row row = rows.next();
            Iterator<Space> spaces = row.iterator();
            while (spaces.hasNext()) {
                Space space = spaces.next();
                Assertions.assertEquals(boardSpaces[rowNum][colNum].getCellIdx(), space.getCellIdx(), "Space mismatch in iterator");
                colNum++;
            }
            rowNum++;
        }

    }

    @Test
    void iteratorWhiteTest(){
        testSetup(false);
        int rowNum = 7;
        Iterator<Row> rows = CuT.iterator();
        Space[][] boardSpaces = this.checkersGame.getBoard();
        while (rows.hasNext()) {
            int colNum = 7;
            Row row = rows.next();
            Iterator<Space> spaces = row.iterator();
            while (spaces.hasNext()) {
                Space space = spaces.next();
                Assertions.assertEquals(boardSpaces[rowNum][colNum].getCellIdx(), space.getCellIdx(), "Space mismatch in iterator");
                colNum--;
            }
            rowNum--;
        }
    }

    @Test
    void iteratorTest(){
        testSetup(true);
        Iterator<Row> rowsRed = CuT.iterator();
        testSetup(false);
        Iterator<Row> rowsWhite = CuT.iterator();
        Assertions.assertNotEquals(rowsWhite, rowsRed, "Something wrong with the row iterator");
    }


}
