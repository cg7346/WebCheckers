package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link PostResignGame} component.
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
@Tag("UI-tier")
public class PostResignGameTest {

    // Constants
    private static final String NAME = "User";
    private static final String PLAYER = "tester";
    private static Map<String, Object> modeOptionsAsJSON = new HashMap<>(2);
    private static Player winningPlayer;

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PostResignGame} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostResignGame CuT;

    // friendly objects
    private PlayerLobby playerLobby;
    private Player player;
    private Player player2;
    private Player resignPlayer;
    private Player winner;
    private int ID = 2;
    private GameManager manager;
    private CheckersGame game;
    private GetGameRoute gameRoute;

    // attributes holding mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;
    private Gson gson;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        this.request = mock(Request.class);
        this.session = mock(Session.class);
        this.response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        game = mock(CheckersGame.class);
        player = mock(Player.class);
        when(player.getName()).thenReturn("P1");
        player2 = mock(Player.class);
        when(player2.getName()).thenReturn("P2");
        manager = mock(GameManager.class);
        gson = new Gson();
        gameRoute = mock(GetGameRoute.class);

        String gameIdString = "1";

        when(request.session()).thenReturn(session);
        when(request.queryParams("gameID")).thenReturn(gameIdString);


        CuT = new PostResignGame(manager, gson);

        // mock behavior
        when(request.session()).thenReturn(session);
    }

    /**
     * Test that you can construct a new PostResignGame.
     */
    @Test
    public void create_post_resign_game() {
        new PostResignGame(manager, gson);
    }

    /**
     * Test that the resignMessage handle is correct
     */
    @Test
    public void resign_message() {
        Player player1 = new Player("p1");
        Player player2 = new Player("p2");
        when(session.attribute("Player")).thenReturn(player1);
        when(session.attribute("Player")).thenReturn(player2);
        assertEquals("{Msg INFO 'p1 has resigned.'}", PostResignGame.resignMessage(player1).toString(),
                "unexpected resign message");
        assertEquals("{Msg INFO 'p1 has resigned.'}", PostResignGame.resignMessage(player1).toString());
    }

    /**
     * Test that the "resignGame" action handle when players aren't null
     */
    @Test
    public void resign_game_handle() {

        Player player2 = new Player("p2");

        GameManager gameManager1 = new GameManager();
        game = gameManager1.makeGame(player, player2);
        resignPlayer = player;
        assertNotNull(game, "an error occurred when creating a game");

        assertEquals(resignPlayer.getName(), game.getActivePlayer().getName());

        assertEquals(resignPlayer.getName(), game.getRedPlayer().getName());

        winningPlayer = game.getWhitePlayer();
        game.setResignedPlayer(player);
        assertEquals(winningPlayer.getName(), "p2");
        assertEquals(game.getResignedPlayer().getName(), "P1");

        assertTrue(modeOptionsAsJSON.isEmpty());
        modeOptionsAsJSON.put("isGameOver", true);
    }

    @Test
    void testResignRedPlayer(){
        when(session.attribute("Player")).thenReturn(player);
        when(manager.getGame(player)).thenReturn(game);
        when(game.getActivePlayer()).thenReturn(player);
        when(game.getRedPlayer()).thenReturn(player);
        when(game.getWhitePlayer()).thenReturn(player2);

        String expected = "{\"text\":\"P1 has resigned.\",\"type\":\"INFO\"}";
        assertEquals(expected, CuT.handle(request, response));

    }

    @Test
    void testResignWhitePlayer(){
        when(session.attribute("Player")).thenReturn(player2);
        when(manager.getGame(player2)).thenReturn(game);
        when(game.getActivePlayer()).thenReturn(player2);
        when(game.getRedPlayer()).thenReturn(player);
        when(game.getWhitePlayer()).thenReturn(player2);

        String expected = "{\"text\":\"P2 has resigned.\",\"type\":\"INFO\"}";
        assertEquals(expected, CuT.handle(request, response));
    }

    @Test
    void testNotYourTurn(){
        when(session.attribute("Player")).thenReturn(player);
        when(manager.getGame(player)).thenReturn(game);
        when(game.getActivePlayer()).thenReturn(player2);
        when(game.getRedPlayer()).thenReturn(player);
        when(game.getWhitePlayer()).thenReturn(player2);

        String expected = "{\"text\":\"Please wait for you turn to resign.\",\"type\":\"ERROR\"}";
        assertEquals(expected, CuT.handle(request, response));
    }

    @Test
    void resignAI(){
        Player AI = mock(Player.class);
        when(AI.getName()).thenReturn("AI");
        when(session.attribute("Player")).thenReturn(player);
        when(manager.getGame(player)).thenReturn(game);
        when(game.getActivePlayer()).thenReturn(player);
        when(game.getRedPlayer()).thenReturn(player);
        when(game.getWhitePlayer()).thenReturn(AI);

        String expected = "{\"text\":\"P1 has resigned.\",\"type\":\"INFO\"}";
        assertEquals(expected, CuT.handle(request, response));
    }
}


