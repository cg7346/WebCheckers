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
     * @param redPlayer the red player
     * @param whitePlayer the white player
     * @return Checkers Game
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

    /**
     * Removes game ID for players to end game
     * <p>
     * A POST method should define who is redPlayer (the clicker) and
     * whitePlayer (the player clicked on)
     *
     * @param game is the Checkers game
     * @return Checkers game that was removed
     */
    public CheckersGame removeGame(CheckersGame game) {

        synchronized (this) {
            totalGames--;

            //TODO see if we can remove that catch,
            //when you remove something from a list
            //that isn't there, it does not throw an error
            games.remove(game);
//            try {
//                games.remove(game);
//            } catch (IndexOutOfBoundsException err) {
//                System.err.println(err);
//            }
            game.getRedPlayer().setInGame(false);
            game.getWhitePlayer().setInGame(false);
            return null;
        }
    }


    /**
     * Gets a game that the sessionPlayer is in
     * @param sessionPlayer the Player looking at the screen
     * @return the game if the player has one, null if not
     */
    public CheckersGame getGame(Player sessionPlayer){
        for (CheckersGame game : games){
            if (isPlayerInGame(game, sessionPlayer)){
                return game;
            }
        }
        return  null;
    }

    /**
     * This gets the game given a game id
     * @param gameID the id of the game you want
     * @return Checkers Game
     */
    public CheckersGame getGame(int gameID){
        for (CheckersGame game : games){
            if (game.getGameID() == gameID){
                return game;
            }
        }
        return null;
    }


    /**
     * Just checking to see if the Player is in a particular
     * game of checkers
     * @param game the game to look in
     * @param player the player to look for
     * @return true if in, false if not
     */
    public synchronized boolean isPlayerInGame(CheckersGame game, Player player) {
        return player.equals(game.getRedPlayer()) || player.equals(game.getWhitePlayer());
    }

}
