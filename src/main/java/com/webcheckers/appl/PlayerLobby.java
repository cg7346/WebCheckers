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
    // The list of players playing the webcheckers game
    private final List<Player> players = new ArrayList<>();


    /**
     * Create a PlayerLobby.
     *
     * @param player a player.
     * @throws IllegalArgumentException when the {@code player} is already taken
     */
    public PlayerLobby(Player player) {
        this.player = player;
    }

    /**
     * Adds player to the list of players
     */
    public void addPlayer(Player player) {
        try {
            this.players.add(player);
        } catch (Error e) {
            System.out.println("Cannot add player.");
        }
    }

    /**
     * Determines whether or not the player's name is valid
     *
     * @return a boolean of whether or not the player's name is vaild
     */
    public boolean isValidPlayer(Player player) {
        if (player != null) {
            return player.getName().matches("([\\w]([\\W]*))+");
        } else {
            return false;
        }
    }

    /**
     * Determines whether or not the player is new
     *
     * @return a boolean of whether or not the player is new
     */
    public boolean isNewPlayer(Player player) {
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                return false;
            }
        }
        return true;
    }


    /**
     * Gets the list of players playing webcheckers
     *
     * @return a list of players playing webcheckers
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Get the list of username's playing webcheckers
     *
     * @return string list of username's
     */
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();
        for (Player player : players) {
            usernames.add(player.getName());
        }
        return usernames;
    }

    public Player getPlayer(){
        return this.player;
    }
    /**
     * Cleanup the @Linkplain{PlayerLobby} object when the session expires.
     * The only cleanup will be to remove the game.
     * TODO: game = null;
     */
    public void endSession() {
    }
}




