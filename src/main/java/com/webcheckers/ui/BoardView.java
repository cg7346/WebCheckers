package com.webcheckers.ui;

import com.webcheckers.model.*;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * The board view for the UI
 * @author Jacquelyn Leung and Anthony Ferraioli
 */
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

        Space[][] board;
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
                spaceList.add(board[i][j]);
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
