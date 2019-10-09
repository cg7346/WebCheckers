package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The object to coordinate all the Players.
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
public class PlayerLobby {

    // The player of the webcheckers game
    private Player player;
    // The list of users playing the webcheckers game
    private final List<Player> users = new ArrayList<>();

    /**
     * Create a PlayerLobby.
     *
     * @param player
     *          a player.
     *
     *
     * @throws IllegalArgumentException
     *    when the {@code player} is already taken
     */
    public PlayerLobby(final Player player) {
        this.player = player;
    }

    /**
     * Adds player to the list of players
     */
    public void addPlayer(Player player) {
        try {
            this.users.add(player);
        } catch (Error e) {
            System.out.println("Cannot add player.");
        }
    }


    /**
     * Determines whether or not the player's name is valid
     *
     * @return
     *      a boolean of whether or not the player's name is vaild
     */
    public boolean isValidPlayer(Player player){
        if (player != null) {
            return player.getName().matches("[A-Za-z0-9]+|[a-zA-Z\\d:]+");
        } else {
            return false;
        }
    }


    /**
     * Determines whether or not the player is new
     *
     * @return
     *      a boolean of whether or not the player is new
     */
    public boolean isNewPlayer(Player player) {
        boolean newPlayer;
        if (this.users.contains(player)) {
            newPlayer = false;
        } else {
            newPlayer = true;
        }
        return newPlayer;
    }

    /**
     * Gets the list of players playing webcheckers
     *
     * @return
     *      a list of players playing webcheckers
     */
    public List<Player> getPlayers(){
        return this.users;
    }

    //TODO: cuurentGame()
    /**
     * Get the current game that the player is playing. Create one if a game has not been started.
     *
     * @return GuessGame
     *    the current game being played
     */
    /**
     public synchronized GuessGame currentGame() {
        if(game == null) {
            game = gameCenter.getGame();
        }
        return game;
    }
     */

    /**
     * Cleanup the @Linkplain{PlayerLobby} object when the session expires.
     * The only cleanup will be to remove the game.
     * TODO: game = null;
     */
    public void endSession() {
    }
}




