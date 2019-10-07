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
                //System.out.println("Im here!");
                System.out.print(board[j][i] + " ");
                switch (board[j][i]){
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
                    default:
                        s = new Space(j, false);
                        break;
                }

                spaceList.add(s);
            }
            System.out.println();
            rows.add(new Row(i, spaceList));
        }
    }


    /**
     * @return the iterable list of rows containing an iterable list of spaces
     */
    public synchronized Iterator<Row> iterator() {
        return rows.iterator();
    }

    public static void main(String[] args){
        Player testRed = new Player("TestRed", new ArrayList<>(), false );
        Player testWhite = new Player("TestRed", new ArrayList<>(), false );
        CheckersGame testGame = new CheckersGame(testRed, testWhite, 0);
        System.out.println(testGame.toString());
        System.out.println("MAKING BOARDVIEW....");
        new BoardView(testRed, testGame);
    }
}
