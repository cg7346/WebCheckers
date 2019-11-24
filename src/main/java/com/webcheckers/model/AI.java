package com.webcheckers.model;

/**
 * Player that uses the Tree to figure out what move to make.
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class AI {

    //
    // Attributes
    //

    private CheckersGame game;
    private MoveValidator moveValidator;

    //
    // Constructor
    //

    /**
     * No heuristic demanded, just uses whichever the game has set to use.
     * @param game a checkers game
     * @param moveValidator the valid moves available for a player
     */
    public AI(CheckersGame game, MoveValidator moveValidator) {
        this.game = game;
        this.moveValidator = moveValidator;
    }

    /**
     * Creates a tree with the game and move validator and adds the last move
     * in order to get the best move possible for the AI to make
     *
     * @return the best move available for the AI to make
     */
    public Move makeTurn() {
        Tree tree = new Tree(game, moveValidator);
        tree.addInLastMove(game);
        return tree.bestMove();
    }
}

