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

    private Player player;
    private final List<String> users = new ArrayList<>();
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

    public boolean isNewPlayer() {
        return this.newPlayer;
    }

    public List<String> getPlayers(){
        return this.users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "{Player " + player.getName() + "}";
    }
}


