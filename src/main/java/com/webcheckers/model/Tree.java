package com.webcheckers.model;

import java.util.ArrayList;

/**
 * Creates a decision tree and uses the minmax algorithm with Alpha Beta pruning
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class Tree {

    //
    // Attributes
    //
    private Node root;
    private CheckersGame game;

    //
    // Static Variables
    //

    private static final int KING_WORTH = 5;
    private static final int WINNING_VALUE = 1000000;
    private static final int PIECE_WORTH = 1;
    private static final int TREE_DEPTH = 3;

    /**
     * Node contains the list of nodes, the checkers game, move validator, move
     * used, and and the score
     */
    private class Node {

        final ArrayList<Node> nodeList;

        final CheckersGame game;

        final MoveValidator moveValidator;

        final Move moveUsed;

        int score;

        /**
         * Constructs the node class
         * @param game builds a node around the board
         * @param moveValidator is the class to determine whether a move is
         *                      valid or not
         */
        Node(CheckersGame game, MoveValidator moveValidator) {
            this(game, moveValidator, null);
        }

        /**
         * Construct the node class
         * @param game game state to live at node
         * @param moveValidator is the class to determine whether a move is
         *                      valid or not
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
     * Constructs the decision tree
     * @param game is the game to build the tree out of
     * @param moveValidator is the class to determine if a move is valid
     */
    public Tree(CheckersGame game, MoveValidator moveValidator) {
        this.game = game;
        root = new Node(game, moveValidator);
        makeTree(TREE_DEPTH, root, Integer.MIN_VALUE, false);
    }

    /**
     * Scores a move node based on whether or not it is going to win or be kinged
     * @param game is the checkers game
     * @return the score of that game
     */
    private static int score(CheckersGame game) {
        int score = 0;
        // If the winner will be the AI, then set the score to the winning value
        if (game.getWinner() == game.getWhitePlayer()) {
            score = WINNING_VALUE;
        } else {
            // Iterates through each space to find pieces to score based on king pieces
            for (int row = 0; row < CheckersGame.ROWS; row++) {
                for (int col = 0; col < CheckersGame.COLS; col++) {
                    // If space has a piece, score it
                    if (game.doesSpaceHavePiece(row, col)) {
                        // If the piece is white and king, then score it will
                        // the king worth, otherwise score it with the piece
                        // worth
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
     * Updates the decision tree and makes the change to the set of game states
     * @param game the webcheckers game
     */
    public final void updateTree(CheckersGame game) {
        // Takes in the last move made in the game
        Move move = game.getLastMove();
        // For each node, if move used is the move then make tree with the move
        // as root
        for (Node node : root.nodeList) {
            if (node.moveUsed.equals(move)) {
                root = node;
                makeTree(TREE_DEPTH, root, Integer.MIN_VALUE, false);
                return;
            }
        }
        // Make new node of the last move made
        Node node = new Node(root.game, root.moveValidator, move);
        // Score the tree
        node.score = score(game);
        // Make tree with the last move made as root
        root = node;
        makeTree(TREE_DEPTH, root, Integer.MIN_VALUE, false);
    }

    /**
     * Finds the best move to make
     * @return the move the computer should make
     */
    public Move bestMove() {
        // If nodeList is empty then return null
        if (root.nodeList.isEmpty()) {
            return null;
        }
        // Sets the max node as the first node in the nodelist
        Node max = root.nodeList.get(0);
        // For each node in the nodeList, find the node with the best score
        for (Node node : root.nodeList) {
            // If the max score is less than the node score, then set the node
            // as max
            if (max.score < node.score) {
                max = node;
            }
        }
        // Makes the max node the root node and make the tree
        root = max;
        makeTree(TREE_DEPTH, root, Integer.MAX_VALUE, true);
        // Return the max move
        return max.moveUsed;
    }

    /**
     * Makes the decision tree
     * @param layersDeep number of layers to check, -1 will fully make the tree
     * @param node current node
     * @param parentValue is the value of the parent in order to determine
     *                     whether or not the node should be pruned
     * @param max is a boolean to determine whether or not the tree is
     *            maximizing or minimizing
     */
    public final void makeTree(int layersDeep, Node node, int parentValue,
                               Boolean max) {
        // If game isn't over and the layersDeep isn't zero, then make the
        // decision tree
        if (!node.game.isGameOver() && layersDeep != 0) {
            // If node list is empty, make the decision tree
            if (node.nodeList.isEmpty()) {
                // Make the decision tree with each valid move
                for (Move move : node.moveValidator.getMoves(
                        node.moveValidator.WHITEPLAYER)) {
                    // Sets the attribute of the node
                    CheckersGame game = node.game;
                    MoveValidator moveValidator = node.moveValidator;
                    // Creates new node
                    Node newNode = new Node(game, moveValidator, move);
                    // Add node to nodeList
                    node.nodeList.add(newNode);
                }
                // For each node in the nodeList
                for (Node newNode : node.nodeList) {
                    // Prunes nodes that are scored lower than max parent value
                    if (max && parentValue >= node.score) {
                        makeTree(layersDeep - 1, newNode, node.score,
                                false);
                        // Prunes nodes that are scored higher than min parent value
                    } else if (!max && parentValue <= node.score) {
                        makeTree(layersDeep - 1, newNode, node.score,
                                true);
                    }
                    // If max then find the node with the highest score
                    if (max) {
                        if (node.score < newNode.score) {
                            node.score = newNode.score;
                        }
                        // If min, then find the node with the lowest score
                    } else {
                        if (node.score > newNode.score) {
                            node.score = newNode.score;
                        }
                    }
                }
            // Otherwise, score the node
            } else {
                node.score = score(node.game);
            }
        }
    }
}

