package com.webcheckers.appl;


import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle all the checker games
 * It makes checkers games and assigns players to them
 */
public class GameManager {

    //total games (will be the game id)
    int totalGames = 0;
    private final List<CheckersGame> games = new ArrayList<>();

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

        synchronized (this){
            totalGames ++;
            if (!redPlayer.isInGame() && !whitePlayer.isInGame()){
                CheckersGame game = new CheckersGame(redPlayer, whitePlayer, totalGames);
                redPlayer.setInGame(true);
                whitePlayer.setInGame(true);
                games.add(game);
                return game;
            }
            else
                return null;
        }
    }

    public CheckersGame getGame(Player sessionPlayer){
        for (CheckersGame game : games){
            if (isPlayerInGame(game, sessionPlayer)){
                return game;
            }
        }
        return  null;
    }


    public boolean isPlayerInGame(CheckersGame game, Player player){
        return player.equals(game.getRedPlayer()) || player.equals(game.getWhitePlayer());
    }
}
