package com.webcheckers.model;

import java.util.ArrayList;

/**
 * Creates a game state tree and uses the minmax algo with AB pruning
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class Tree {

    //
    // Attributes
    //
    private Node root;
    private CheckersGame game;  // TODO: check if we need this

    //
    // Static Variables
    //

    private static final int KING_WORTH = 5;
    private static final int WINNING_VALUE = 1000000;
    private static final int PIECE_WORTH = 1;
    private static final int TREE_DEPTH = 3;

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
            score = Integer.MAX_VALUE;

        }

    }

    //
    // Constructor
    //

    /**
     * @param game
     *         the game to build the tree out of
     */
    public Tree(CheckersGame game, MoveValidator moveValidator) {
        this.game = game;
        root = new Node(game, moveValidator);
        makeTree(TREE_DEPTH, root, Integer.MIN_VALUE, false);
    }

    /**
     * @param game
     *         the checkers game
     * @return score of that game
     */
    private static int score(CheckersGame game) {
        int score = 0;
        if (game.isGameOver()) {
            score = WINNING_VALUE;
        } else {
            for (int row = 0; row < CheckersGame.ROWS; row++) {
                for (int col = 0; col < CheckersGame.COLS; col++) {
                    if (game.doesSpaceHavePiece(row, col)) {
                        boolean isKing = game.isSpaceKingPiece(row, col);
                        boolean isRed = game.isSpaceRedPiece(row, col);
                        score += !isRed && isKing ? KING_WORTH : PIECE_WORTH;
                    }
                }
            }
        }
        return score;
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
                makeTree(TREE_DEPTH, root, Integer.MIN_VALUE, false);
                return;
            }
        }
        Node node = new Node(root.game, root.moveValidator, move);
        node.score = score(game);
        root = node;
        makeTree(TREE_DEPTH, root, Integer.MIN_VALUE, false);
    }

    /**
     * @return the move the computer should make
     */
    public Move bestMove() {
        if (root.nodeList.isEmpty()) {
            return null;
        }
        Node max = root.nodeList.get(0);
        for (Node node : root.nodeList) {
            if (max.score < node.score) {
                max = node;
            }
        }

        root = max;
        makeTree(TREE_DEPTH, root, Integer.MAX_VALUE, true);
        return max.moveUsed;
    }

    /**
     * @param layersDeep
     *         number of layers to check (-1 will fully make the tree)
     * @param node
     *         node being looked at right now
     */
    public final void makeTree(int layersDeep, Node node, int parentValue, Boolean max) {
        if (!node.game.isGameOver() && layersDeep != 0) {
            if (node.nodeList.isEmpty()) {
                for (Move move : node.moveValidator.getMoves(node.moveValidator.WHITEPLAYER)) {
                    CheckersGame game = node.game;
                    MoveValidator moveValidator = node.moveValidator;

                    Node newNode = new Node(game, moveValidator, move);
                    node.nodeList.add(newNode);
                }
                for (Node newNode : node.nodeList) {
                    if (max && parentValue >= node.score) {
                        makeTree(layersDeep - 1, newNode, node.score, false );
                    } else if (!max && parentValue <= node.score) {
                        makeTree(layersDeep - 1, newNode, node.score, true);
                    }
                    if (max) {
                        if (node.score < newNode.score) {
                            node.score = newNode.score;
                        }
                    } else {
                        if (node.score > newNode.score) {
                            node.score = newNode.score;
                        }
                    }
                }
            } else {
                node.score = score(node.game);
            }
        }
    }
}

