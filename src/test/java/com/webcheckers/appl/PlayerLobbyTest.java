package com.webcheckers.appl;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Session;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link PlayerLobby} component.
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
@Tag("Application-tier")
public class PlayerLobbyTest {

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PlayerLobby CuT;

    // Constants
    private static final String PLAYER1 = "Bobby";
    private static final String PLAYER2 = "Bruce";
    private static final String PLAYER3 = "    ";
    private static final String PLAYER4 = "261";
    private static final String PLAYER5 = "Bobby";
    private static final String PLAYER6 = "!261";


    // friendly objects
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private Player player6;

    private Request request;
    private Session session;

    private Player player;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        player = mock(Player.class);


        // build model objects
        player1 = new Player(PLAYER1);
        player2 = new Player(PLAYER2);
        player3 = new Player(PLAYER3);
        player4 = new Player(PLAYER4);
        player5 = new Player(PLAYER5);
        player6 = new Player(PLAYER6);

        // mock behavior
        when(request.session()).thenReturn(session);

        // create a unique for each test
        CuT = new PlayerLobby();

    }

    /**
     * Test that you can construct a new Player Lobby.
     */
    @Test
    public void create_player_lobby() {
        new PlayerLobby(player);
    }

    /**
     * Checks if you can get all the players in the game.
     */
    @Test
    public void test_get_players() {
        assertEquals(CuT.getPlayers().size(), 0);
        CuT.addPlayer(new Player(PLAYER1));
        assertEquals(CuT.getPlayers().size(), 1);
    }

    /**
     * Tests if a player is in a game.
     */
    @Test
    public void test_player_in_game() {
        GameManager gameManager1 = new GameManager();
        CheckersGame game = gameManager1.makeGame(player1, player2);
        assertNotNull(game, "an error occurred when creating a game");
        assertTrue(CuT.isInGame(player1));
        assertTrue(CuT.isInGame(player2));

        assertFalse(CuT.isInGame(null));
    }

    /**
     * Tests if a player in already in the PlayerLobby or not.
     */
    @Test
    public void test_new_player() {
        assertTrue(CuT.isNewPlayer(player1));
        CuT.addPlayer(player1);
        CuT.addPlayer(player2);
        assertFalse(CuT.isNewPlayer(player2));
    }

    /**
     * Tests is a player is valid or invalid.
     */
    @Test
    public void test_valid_player() {
        assertFalse(CuT.isValidPlayer(player3));
        assertTrue(CuT.isValidPlayer(player4));
        assertFalse(CuT.isValidPlayer(player6));

        assertFalse(CuT.isValidPlayer(null));
    }

    /*    *//**
     * Tests if a player can be set to the player entered and
     * gets that current player.
     *//*
    @Test
    public void test_set_get_player() {
        assertNull(CuT.getPlayer());
        CuT.setPlayer(player1);
        assertEquals(CuT.getPlayer(), player1);
    }*/

    /**
     * Checks if when players are added to the PlayerLobby
     * there usernames can be accessed.
     */
    @Test
    public void test_get_usernames() {
        List<String> users = new ArrayList<>();
        assertEquals(CuT.getUsernames(), users);
        CuT.addPlayer(player1);
        CuT.addPlayer(player2);
        CuT.addPlayer(player4);

        users.add(PLAYER1);
        users.add(PLAYER2);
        users.add(PLAYER4);
        assertEquals(CuT.getUsernames(), users);
    }

    /**
     * Checks if a player can be found/already exists in the PlayerLobby.
     */
    @Test
    public void test_find_player() {
        assertNull(CuT.findPlayer(PLAYER1));
        CuT.addPlayer(player1);
        CuT.addPlayer(player2);
        assertEquals(CuT.findPlayer(PLAYER1), player1);
        assertEquals(CuT.findPlayer(PLAYER2), player2);
    }

    @Test
    public void test_get_player(){
        assertNull(CuT.getPlayer());
        PlayerLobby p2 = new PlayerLobby(player1);
        assertEquals(player1, p2.getPlayer());
    }

    @Test
    public void test_is_game_over(){
        assertNull(CuT.isGameOver());
    }
}