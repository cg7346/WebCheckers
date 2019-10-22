package com.webcheckers.ui;
import com.webcheckers.appl.GameManager;
import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Iterator;

@Tag("UI-tier")
public class BoardViewTest {
    private Player red;
    private Player white;
    private  CheckersGame checkersGame;
    private BoardView CuT;

    @BeforeEach
    void testSetup(){
        this.red = Mockito.mock(Player.class);
        this.white = Mockito.mock(Player.class);
        this.checkersGame = new CheckersGame(this.red, this.white, 1);

        this.CuT = new BoardView(this.red, this.checkersGame);
    }

    @Test
    void RedTest() {
        int rowNum = 7;
        Iterator<Row> rows = CuT.iterator();
        while (rows.hasNext()) {
            int colNum = 7;
            Row row = rows.next();
            Iterator<Space> spaces = row.iterator();
            while (spaces.hasNext()) {
                Space space = spaces.next();
                Assertions.assertEquals(this.checkersGame.getBoard()[rowNum][colNum], space, "Space mismatch in iterator");
                colNum--;
            }
            rowNum--;
        }
    }

    @Test
    void whiteTest() {
        this.CuT = new BoardView(this.white, this.checkersGame);
        int rowNum = 0;
        Iterator<Row> rows = CuT.iterator();
        while (rows.hasNext()) {
            int colNum = 0;
            Row row = rows.next();
            Iterator<Space> spaces = row.iterator();
            while (spaces.hasNext()) {
                Space space = spaces.next();
                Assertions.assertEquals(this.checkersGame.getBoard()[rowNum][colNum], space, "Space mismatch in iterator");
                colNum++;
            }
            rowNum++;
        }
    }

}
