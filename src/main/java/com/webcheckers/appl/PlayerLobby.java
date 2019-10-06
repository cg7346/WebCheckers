package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.ui.PostSignInRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
     * @param users
     *          a list of all players.
     *
     *
     * @throws IllegalArgumentException
     *    when the {@code player} is already taken
     */
    public PlayerLobby(final Player player, final List<String> users,
                  final boolean newPlayer) {
        // validate arguments
        if (users.contains(player.getName())) {
            this.newPlayer = false;
            throw PostSignInRoute.makeTakenUsrMessage();
        }
        //
        this.player = player;
        this.newPlayer = true;
        users.add(player.getName());
    }

    public boolean isNewPlayer() {
        return this.newPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "{Player " + player.getName() + "}";
    }
}


