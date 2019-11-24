package com.webcheckers.model;

/**
 * AI player uses the Tree to figure out the best move to make.
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
     * Constructs the AI player
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
        // Makes a decision tree
        Tree tree = new Tree(game, moveValidator);
        // Updates the decision tree
        tree.updateTree(game);
        // Returns the best move
        return tree.bestMove();
    }
}

