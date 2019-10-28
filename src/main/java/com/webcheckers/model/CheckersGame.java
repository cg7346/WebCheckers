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

    //Just some in class testing
    public static void main(String[] args) {
        CheckersGame testGame = new CheckersGame(new Player("1"),
                new Player("2"), 0);
//        System.out.println(testGame.toString());
//        TODO: remember to delete this

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
    public CheckersGame(Player redPlayer, Player whitePlayer, int gameID){
        this.gameID = gameID;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;

         board = new Space[COLS][ROWS];
        for (int col=0; col < COLS; col++){
            for (int row=0; row < ROWS; row ++){
                //if both row and column are even or odd then it is white space
                if ((col%2 == 0 && row%2 ==0) ||
                        (col%2 != 0 && row%2 != 0)){
                    board[col][row] = Space.EMPTY_WHITE;
                }
                //calling top of board row 0
                else {
                    //putting white player at top of board
                    if (row <= 2){
                        board[col][row] = Space.WHITE_PLAYER;
                    }
                    //putting red player at bottom of board
                    else if (row >= 5) {
                        board[col][row] = Space.RED_PLAYER;
                    }
                    //middle spaces empty black
                    else{
                        board[col][row] = Space.EMPTY_BLACK;
                    }
                }

            }
        }
        //red player
        turn = 0;
    }

    /**
     * Returns the symbol in a row and column
     * on a board
     *
     * R - Red Player
     * W - White Player
     * X - White
     * O - Black
     *
     * @param col column of space
     * @param row row of space
     * @return char symbol that represents space
     */
    public char getSpace(int col, int row){
        return board[col][row].getSymbol();
    }

    /**
     * Helper method to check if the player is the
     * red player, if not then it's the white player
     * @param player the player in the game to check
     * @return true for red player, false for white
     */
    public boolean isRedPlayer(Player player){
        return player.equals(redPlayer);
    }

    /**
     * Helper method that gets the inverse of the board
     *
     * Flips the white and red colors so that a player
     * can see their pieces near them
     *
     * So instead of white at top it's red
     * @return
     */
    public Space[][] inverseBoard(){
        Space[][] inverseBoard = new Space[COLS][ROWS];
        for (int col=0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {

                //flip red to white
                if (board[col][row] == Space.RED_PLAYER) {
                    inverseBoard[col][row] = Space.WHITE_PLAYER;

                //flip white to red
                } else if (board[col][row] == Space.WHITE_PLAYER) {
                    inverseBoard[col][row] = Space.RED_PLAYER;
                }

                //keep everything else
                else{
                    inverseBoard[col][row] = board[col][row];
                }
            }
        }
        return inverseBoard;
    }

    /**
     * Returns the original not flipped board
     * @return the original board
     */
    public Space[][] getBoard() {
        return board;
    }

    /**
     * These are getters
     */
    public Player getRedPlayer(){return redPlayer;}
    public Player getWhitePlayer(){return whitePlayer;}
    public int getGameID(){return gameID;}

    /**
     * Returns a {@link String} representation of the board, suitable for
     * printing. Used mostly for testing
     *
     * @return A {@link String} representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(int r=0; r<ROWS; r++) {
            for(int c=0; c<COLS; c++) {
                builder.append('|');
                builder.append(board[c][r].getSymbol());
                builder.append('|');
            }
            builder.append('\n');
        }
        return builder.toString();
    }


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

        //Add stuff for king later

        private char symbol;

        Space(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

//    TODO: need a record of is the game is won or not

//    TODO: need a boolean method if someone has won or not (change status)

//    TODO: is won method

}
