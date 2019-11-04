package com.webcheckers.ui;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;
import org.testng.annotations.Test;

/**
 * tests the board view
 * @author jil4009
 */

@Tag("UI-tier")
public class RedSingleJumpTest {

    private Player red;
    private Player white;
    private  CheckersGame checkersGame;
    private BoardView CuT;

    /**
     * Setting up the mock objects for testing
     */
    @Test
    public void setup(){
        this.red = Mockito.mock(Player.class);
        this.white = Mockito.mock(Player.class);
        this.checkersGame = new CheckersGame(this.red, this.white, 0);
        this.CuT = new BoardView(this.red, this.checkersGame);

        checkersGame.makeMove(new Move(new Position(5, 2), new Position(4,3)));
        checkersGame.makeMove(new Move(new Position(2, 5), new Position(3,4)));
        checkersGame.makeMove(new Move(new Position(4, 3), new Position(2,5)));

        System.out.println(checkersGame.getBoard().toString());

    }

}
