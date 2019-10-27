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

    public boolean validateMove(){
        char endSpace = game.getSpace(endMove.getCol(), endMove.getRow());
        if(endSpace != 'O'){
            return false;
        }
        int rowDifference = Math.abs(endMove.getRow() - startMove.getRow());
        int cellDifference = Math.abs(endMove.getCol() - startMove.getCol());

        if(rowDifference > 2 || rowDifference < 1){ //if the move was more than less than one row or greater than 1 then its false
            return false;
        }

        if(cellDifference == 1){ //moved over once
            if(endSpace == 'O'){
                return true;
            }
        }

        return false;
    }



}
