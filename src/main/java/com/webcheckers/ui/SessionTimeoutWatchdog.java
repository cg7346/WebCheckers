package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.webcheckers.appl.PlayerLobby;

/**
 * Whenever an instance of a class that implements {@linkplain HttpSessionBindingListener}
 * gets set as the value for a session attribute, the valueBound() method gets called.
 * Any time that the attribute is removed, set to another value, or the session is
 * invalidated, the valueUnbound() method gets called.
 *
 * @author <a href='cg7346@rit.edu'>Celeste Gambardella</a>
 */
public class SessionTimeoutWatchdog implements HttpSessionBindingListener {
    private static final Logger LOG = Logger.getLogger(SessionTimeoutWatchdog.class.getName());

    private final PlayerLobby playerLobby;

    public SessionTimeoutWatchdog(final PlayerLobby playerLobby) {
        LOG.fine("Watch dog created.");
        this.playerLobby = Objects.requireNonNull(playerLobby);
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        // ignore this event
        LOG.fine("Player session started.");
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        // the session is being terminated do some cleanup
        playerLobby.endSession();
        //
        LOG.fine("Player session ended.");
    }
}
