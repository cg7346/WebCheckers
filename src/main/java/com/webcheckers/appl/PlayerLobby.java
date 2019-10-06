package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.ui.PostSignInRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * The object to coordinate all the Players.
 *
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
public class PlayerLobby {

    // The player of the webcheckers game
    private Player player;
    // The list of users playing the webcheckers game
    private final List<String> users = new ArrayList<>();
    // Whether or not the player is new
    private final boolean newPlayer;

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
        // validate arguments
        // TODO: check that repeated usernames don't work
        if (this.users.contains(player.getName())) {
            this.newPlayer = false;
            PostSignInRoute.makeTakenUsrMessage();
        } else {
            this.newPlayer = true;
        }
        this.player = player;
        this.users.add(player.getName());
    }

    /**
     * Determines whether or not the player is new
     *
     * @return
     *      a boolean of whether or not the player is new
     */
    public boolean isNewPlayer() {
        return this.newPlayer;
    }

    /**
     * Gets the list of players playing webcheckers
     *
     * @return
     *      a list of players playing webcheckers
     */
    public List<String> getPlayers(){
        return this.users;
    }

    /**
     * Cleanup the @Linkplain{PlayerLobby} object when the session expires.
     * The only cleanup will be to remove the game.
     * TODO: game = null;
     */
    public void endSession() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "{Player " + player.getName() + "}";
    }
}


