package com.webcheckers.model;

/**
 * Class to represent a Checkers game
 * Holds the logic for an average game of checkers
 *
 * Holds information about who the two players are
 * and only information for that individual game
 */
public class CheckersGame {

    //The players in the game
    //(the 1st player to click another player)
    private Player redPlayer;
    //(the person clicked on by the other player)
    private Player whitePlayer;

    //Game ID
    private int gameID;

    //Dimensions for the board
    public final static int ROWS = 8;
    public final static int COLS = 8;

    /**
     * Represent a space on the board that will mark
     * if it is empty (black or white) or if it has
     * a chip on it (red or white)
     */
    public enum Space {
        RED_PLAYER('R'),
        WHITE_PLAYER('W'),
        EMPTY_BLACK('O'),
        EMPTY_WHITE('X');

        //Add stuff for king

        private char symbol;

        private Space(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    //2D array that represents a checkers board
    private Space[][] board;

    //Whose turn is it? Red Player is R, White Player is W
    private char turn;


    /**
     * One instance of a checkers board
     * Creates a and 8x8 board of Space enums
     * that start with 3 rows of alternating R and X
     * then two rows of alternative O and X then
     * three final rows of W and X
     *
     * Sets turn to red player
     *
     * @param gameID int, unique identifier for the game
     */
    public CheckersGame(int gameID){
        this.gameID = gameID;




    }


}
