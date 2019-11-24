package com.webcheckers.ui;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.Row;
import com.webcheckers.model.Space;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * The board view for the UI
 *
 * @author <a href='mailto:jil4009@rit.edu'>Jackie Leung</a>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>

 */
public class BoardView {

    // holds all the rows in the game
    private ArrayList<Row> rows;

    //
    // Constructor
    //

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

        for (int i = 0; i < CheckersGame.ROWS; i++) {
            spaceList = new ArrayList<>();
            for (int j = 0; j < CheckersGame.COLS; j++) {
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
