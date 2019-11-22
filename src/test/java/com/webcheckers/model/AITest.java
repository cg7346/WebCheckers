package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AITest {

    private AI CuT;
    private CheckersGame game;
    private MoveValidator mv;

    @BeforeEach
    void creation(){
        game = new CheckersGame(new Player("a"),
        new Player("b"), 1);
        mv = new MoveValidator(game);
        CuT = new AI(game, mv);
    }

    @Test
    void test_makeTurn(){
        //as long as the first move is not null,
        //we are doing great
        assertNotNull(CuT.makeTurn(), "Something" +
                "in tree / CheckerGame / moveValidator is broken");
    }
}
