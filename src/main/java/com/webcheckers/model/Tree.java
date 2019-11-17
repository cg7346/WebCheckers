package com.webcheckers.model;

import java.util.ArrayList;

/**
 * Creates a game state tree and uses the minmax algo with AB pruning
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class Tree {

    private Node root;

    public enum Heuristic {
        A("A"), B("B");

        private final String letter;

        Heuristic(String letter) {
            this.letter = letter;
        }

        public String toString() {
            return letter;
        }
    }

    /**
     * Node contains a board, the move used to get there, and the list of moves coming after.
     */
    private class Node {

        final ArrayList<Node> nodeList;

        final CheckersGame game;

        final MoveValidator moveValidator;

        final Move moveUsed;

        int score;

        /**
         * @param game builds a node around the board
         */
        Node(CheckersGame game, MoveValidator moveValidator) {
            this(game, moveValidator, null);
        }

        /**
         * @param game game state to live at node
         * @param move move used to get to this game state
         */
        Node(CheckersGame game, MoveValidator moveValidator, Move move) {
            this.game = game;
            this.moveValidator = moveValidator;
            nodeList = new ArrayList<>();
            moveUsed = move;
            score = game.isActivePlayerRed() ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        }

    }

    public Heuristic heuristic;

    public static final int TREE_DEPTH = 3;

    /**
     * @param game
     *         the game to build the tree out of
     */
    public Tree(CheckersGame game, MoveValidator moveValidator) {
        root = new Node(game, moveValidator);
        makeTree(TREE_DEPTH, root, root.game.isActivePlayerRed() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
    }

    /**
     * @param moveValidator
     *         board to score
     * @param heuristic
     *         heuristic to use
     * @return score of that game
     */
    private static int score(MoveValidator moveValidator, Heuristic heuristic) {
        if (heuristic == null) {
            return moveValidator.score();
        } else if (heuristic == Heuristic.A) {
            return moveValidator.scoreA();
        } else if (heuristic == Heuristic.B) {
            return moveValidator.scoreB();
        } else {
            return moveValidator.scoreC();
        }
    }

    /**
     * takes in the game and makes the change to the set of game states
     * @param game the webcheckers game
     */
    public final void addInLastMove(CheckersGame game) {

        Move move = game.getLastMove();
        for (Node node : root.nodeList) {
            if (node.moveUsed.equals(move)) {
                root = node;
                makeTree(TREE_DEPTH, root, root.game.isActivePlayerRed() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
                return;
            }
        }
        Node node = new Node(root.game, root.moveValidator, move);
        node.score = score(node.moveValidator, heuristic);

        root = node;

        makeTree(TREE_DEPTH, root, root.game.isActivePlayerRed() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
    }

    /**
     * @return the move the computer should make
     */
    public Move bestMove() {
        if (root.nodeList.isEmpty()) {
            return null;
        }
        Node max = root.nodeList.get(0);
        if (!root.game.isActivePlayerRed()) {
            for (Node node : root.nodeList) {
                if (max.score < node.score) {
                    max = node;
                }
            }
        } else {
            for (Node node : root.nodeList) {
                if (max.score > node.score) {
                    max = node;
                }
            }
        }
        root = max;
        makeTree(TREE_DEPTH, root, !root.game.isActivePlayerRed() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        return max.moveUsed;
    }

    /**
     * @param layersDeep
     *         number of layers to check (-1 will fully make the tree)
     * @param node
     *         node being looked at right now
     */
    public final void makeTree(int layersDeep, Node node, int parentValue) {
        if (!node.game.isGameOver() && layersDeep != 0) {
            if (node.nodeList.isEmpty()) {
                for (Move move : node.moveValidator.getMovesForWhite()) {
                    CheckersGame game = node.game;
                    MoveValidator moveValidator = node.moveValidator;

                    Node newNode = new Node(game, moveValidator, move);
                    node.nodeList.add(newNode);
                }
                for (Node newNode : node.nodeList) {
                    if ((!node.game.isActivePlayerRed() && parentValue >= node.score)
                            || (node.game.isActivePlayerRed()&& parentValue <= node.score)) {
                        makeTree(layersDeep - 1, newNode, node.score);
                    }

                    if (!node.game.isActivePlayerRed()) {
                        if (node.score < newNode.score) {
                            node.score = newNode.score;
                        }
                    } else {
                        if (node.score > newNode.score) {
                            node.score = newNode.score;
                        }
                    }
                }
            }
        } else {
            node.score = score(node.moveValidator, heuristic);
        }
    }

}

