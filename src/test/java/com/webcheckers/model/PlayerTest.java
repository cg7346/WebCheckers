package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@link Player} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("Model-tier")
public class PlayerTest {

    // Constants
    private static final String NAME = "Player";

    /**
     * Test that the player with a null name constructor works without failure.
     */
    @Test
    public void playerNullName() {
        new Player(null);
    }

    /**
     * Test that the player with a name constructor works without failure.
     */
    @Test
    public void playerName() {
        final Player player = new Player(NAME);
        assertEquals(NAME, player.getName());
    }

    /**
     * Test that the player is in a game constructor works without failure.
     */
    @Test
    public void playerIsInGame() {
        final Player player = new Player(NAME);
        player.setInGame(true);
        assertTrue(player.isInGame());
    }

    /**
     * Test that the player is not in a game constructor works without failure.
     */
    @Test
    public void playerIsNotInGame() {
        final Player player = new Player(NAME);
        player.setInGame(false);
        assertFalse(player.isInGame());
    }


}
