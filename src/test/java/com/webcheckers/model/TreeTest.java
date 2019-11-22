package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TreeTest {

    private Tree CuT;
    private CheckersGame game;
    private MoveValidator mv;

    @BeforeEach
    void creation(){
        game = mock(CheckersGame.class);
        mv = mock(MoveValidator.class);
        CuT = new Tree(game, mv);
    }

}
