package com.webcheckers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A single "Player".
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class Player {

    //
    // Attributes
    //

    private final String username;
    private final List<String> users = new ArrayList<>();
    private final boolean newPlayer;

    private static final Logger LOG = Logger.getLogger(Player.class.getName());
    /**
     * Create a Player with a unique username.
     *
     * @param username
     *          a unique username.
     * @param users
     *          a list of all usernames.
     *
     *
     * @throws IllegalArgumentException
     *    when the {@code username} is already taken
     */
    public Player(final String username, final List<String> users,
                  final boolean newPlayer) {
        // validate arguments
        if (users.contains(username)) {
            this.newPlayer = false;
            throw new IllegalArgumentException("Username already exists");
        }
        //
        LOG.fine("Player created " + username);
        this.username = username;
        this.newPlayer = true;
        users.add(username);
    }

    public boolean isNewPlayer() {
        return this.newPlayer;
    }

    //added this method to get currenuser/redplayer/whiteplayer/.name to work
    //changed .name -> .getUsername()
    public String getUsername(){ return this.username;}

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "{Player " + username + "}";
    }
}
