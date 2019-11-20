package com.webcheckers.appl;


import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to handle all the checker games
 * It makes checkers games and assigns players to them
 */
public class GameManager {

    //total games (will be the game id)
    int totalGames = 0;
    private final List<CheckersGame> games = new ArrayList<>();
    private HashMap<Player, Player> spectators;

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

        spectators = new HashMap<>();

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
            try {
                games.remove(game);
            } catch (IndexOutOfBoundsException err) {
                System.err.println(err);
            }
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


    /**
     * checks if a player is a spectator in the game
     *
     * @param player the player to look for
     * @return true if they are a spectator, false if not
     */
    public boolean isPlayerASpectator(Player player) {
        return spectators.containsKey(player);
    }


    /**
     * Adds a spectator to a game
     *
     * @param spectator a player that is the spectator
     * @param player    a player that is in the game
     */
    public void addSpectator(Player spectator, Player player) {
        spectators.put(spectator, player);
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }

    public CheckersGame getSpectatorGame(Player player) {
        return this.getGame(spectators.get(player));
    }


}
