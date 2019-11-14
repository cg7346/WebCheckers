package com.webcheckers.model;

/**
 * Player that uses the Tree to figure out what move to make.
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class AI {

    private CheckersGame game;
    private MoveValidator moveValidator;

    /**
     * No heuristic demanded, just uses whichever the game has set to use.
     */
    public AI(CheckersGame game, MoveValidator moveValidator) {
        this.game = game;
        this.moveValidator = moveValidator;
    }

    public Move makeTurn() {
        Tree tree = new Tree(game, moveValidator);
        while (tree.isCalculating()) {
            tree.addInLastMove(game);
        }
        return tree.bestMove();
    }

}

