package com.webcheckers.appl;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//import com.example.model.GuessGame;
//import com.example.model.GuessGame.GuessResult;

/**
 * The unit test suite for the {@link PlayerLobby} component.
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
@Tag("Application-tier")
public class PlayerLobbyTest {

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
    private TemplateEngine templateEngine;
    private Response response;

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PlayerLobby CuT;


    private GameManager gameManager;
    private Player player;
    private CheckersGame checkersGame;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);
        player = mock(Player.class);
        checkersGame = mock(CheckersGame.class);


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
//    @BeforeEach
//    public void testSetup() {
//        gameCenter = mock(GameCenter.class);
//        GuessGame fixedGame = new GuessGame(RIGHT_GUESS);
//        when(gameCenter.getGame()).thenReturn(fixedGame);
//
//        // Setup CuT and create the game
//        CuT = new PlayerServices(gameCenter);
//        game = CuT.currentGame();
//    }


    /**
     * Test that you can construct a new Player Lobby.
     */
    @Test
    public void createPlayerLobby() {
        new PlayerLobby(player);
    }

    @Test
    public void testPlayerInGame() {
        GameManager gameManager1 = new GameManager();
        CheckersGame game = gameManager1.makeGame(player1, player2);
        assertNotNull(game, "an error occurred when creating a game");
        assertTrue(CuT.isInGame(player1));
        assertTrue(CuT.isInGame(player2));
    }

    @Test
    public void testNewPlayer() {
//        GameManager gameManager1 = new GameManager();
//        CheckersGame game = gameManager1.makeGame(player1, player2);
        //create my testHelper
        assertTrue(CuT.isNewPlayer(player1));
        CuT.addPlayer(player1);
        CuT.addPlayer(player2);
        assertFalse(CuT.isNewPlayer(player2));
    }

    @Test
    public void testAddPlayer() {
        assertEquals(CuT.getPlayers().size(), 0);
        CuT.addPlayer(new Player(PLAYER1));
        assertEquals(CuT.getPlayers().size(), 1);
    }

    @Test
    public void testValidPlayer() {
        assertFalse(CuT.isValidPlayer(player3));
        assertTrue(CuT.isValidPlayer(player4));
        assertFalse(CuT.isValidPlayer(player6));
    }

    @Test
    public void testSetPlayer() {
        CuT.setPlayer(player1);
        assertEquals(CuT.getPlayer(), player1);
    }

    @Test
    void testGetPlayer() {
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
}