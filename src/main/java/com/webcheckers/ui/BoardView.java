package com.webcheckers.ui;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class BoardView {
    private enum color {RED, WHITE}
    private enum viewModel { PLAY, SPECTATOR, REPLAY }
    private ArrayList<Row> rows;
    private color activeColor;

    /**
     * The constructor for the BoardView. Creates the information for each row based on the checkers game
     *
     * @param player The player associated with the Board View
     * @param checkersGame The checkers game instance
     */
    public BoardView(Player player, CheckersGame checkersGame){
        this.rows = new ArrayList<>();

        CheckersGame.Space[][] board;
        if(!checkersGame.isRedPlayer(player)){
            //inverse items
            board = checkersGame.inverseBoard();
        } else {
            board = checkersGame.getBoard();
        }
        ArrayList<Space> spaceList;

        for(int i = 0; i < checkersGame.ROWS; i++){
            spaceList = new ArrayList<>();
            for(int j = 0; j < checkersGame.COLS; j++){
                Space s;
                switch (board[i][j]){
                    case EMPTY_BLACK:
                        s = new Space(j, true);
                        break;
                    case RED_PLAYER:
                        s = new Space(j, true);
                        s.addPiece(new Piece(0));
                        break;
                    case WHITE_PLAYER:
                        s = new Space(j, true);
                        s.addPiece(new Piece(1));
                        break;
                    case EMPTY_WHITE:
                        s = new Space(j, false);
                        break;

                    // If the board symbol does not match any of the cases provided
                    default:
                        s = null;
                        System.out.println("This case has not been addressed yet");
                        break;
                }
                spaceList.add(s);
            }
            rows.add(new Row(i, spaceList));
        }
    }


    /**
     * @return the iterable list of rows containing an iterable list of spaces
     */
    public synchronized Iterator<Row> iterator() {
        return rows.iterator();
    }
}
