package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
