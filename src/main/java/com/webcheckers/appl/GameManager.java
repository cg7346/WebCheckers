package com.webcheckers.appl;


import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

/**
 * Class to handle all the checker games
 * It makes checkers games and assigns players to them
 */
public class GameManager {

    //total games (will be the game id)
    int totalGames = 0;

    /**
     * Makes a new game for players to play
     *
     * A POST method should define who is redPlayer (the clicker) and
     * whitePlayer (the player clicked on)
     * @param redPlayer
     * @param whitePlayer
     * @return
     */
    public CheckersGame makeGame(Player redPlayer, Player whitePlayer){
        CheckersGame game = new CheckersGame(redPlayer, whitePlayer, totalGames);
        synchronized (this){
            totalGames ++;
        }
        return game;
    }
}
