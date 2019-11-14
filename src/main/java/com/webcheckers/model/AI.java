package com.webcheckers.model;
import java.util.List;

/**
 * Player that uses the Tree to figure out what move to make.
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

//    /**
//     * @param heuristic
//     *         heuristic to use for this AI
//     */
//    public AI(Tree.Heuristic heuristic) {
//        this.heuristic = heuristic;
//    }

    public Move makeTurn(final List<Move> availableMoves) {
        Tree tree = new Tree(game, moveValidator);
        while (tree.isThinking()) {
            //turns the board string into only the last move
            tree.addInLastMove(game);
        }
        return tree.bestMove();
    }

}

