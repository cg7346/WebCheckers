package com.webcheckers.model;

import com.sun.tools.doclint.Checker;

/**
 * this class validates various moves
 * @author jil4009
 * @author mmb2582
 */
public class ValidateMove {
    private Position startMove;
    private Position endMove;
    private CheckersGame game;

    /**
     * constructor for validate move
     * @param game
     * @param move
     */
    public ValidateMove(CheckersGame game, Move move){
        this.game = game;
        this.startMove = move.getStart();
        this.endMove = move.getEnd();
    }



}
