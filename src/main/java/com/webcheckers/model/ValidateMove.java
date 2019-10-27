package com.webcheckers.model;

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
        if(game.getSpace(endMove.getCol(), endMove.getRow()) == 'O'){
            return true;
        }
        return false;
    }



}
