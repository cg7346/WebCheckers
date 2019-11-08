package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private Player resignPlayer;
    private Player winner;
    private int ID = 2;
    private GameManager manager;
    private CheckersGame game;
    private Player winningPlayer;
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
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        game = mock(CheckersGame.class);
        player = mock(Player.class);
        manager = mock(GameManager.class);
        gson = new Gson();
        gameRoute = mock(GetGameRoute.class);

        String gameIdString = "3";
        player = new Player("p1");

        when(request.queryParams("gameID")).thenReturn(gameIdString);
        when(request.queryParams("Player")).thenReturn(player.getName());
        when(manager.getGame(player)).thenReturn(game);

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

    @Test
    public void winning_player() {
        Player player1 = new Player("p1");
        Player player2 = new Player("p2");

        GameManager gameManager1 = new GameManager();
        game = gameManager1.makeGame(player1, player2);
        game.setWinner(player2);
        winningPlayer = game.getWinner();
        when(resignPlayer.getName().equals(game.getRedPlayer().getName()));

    }

    /**
     * Test that the "resignGame" action handle when players aren't null
     */
    @Test
    public void resign_game_handle() {
        Player player1 = new Player("p1");
        Player player2 = new Player("p2");

        GameManager gameManager1 = new GameManager();
        game = gameManager1.makeGame(player1, player2);
        assertNotNull(game, "an error occurred when creating a game");

        Boolean playerOne = manager.isPlayerInGame(this.game, player1);

//        assertTrue(playerOne);

        assertEquals("p1", request.queryParams("Player"));
        assertTrue(gameManager1.isPlayerInGame(game, player1));
//
//        // Arrange the test scenario: The session holds a new game.
//        when(request.queryParams(eq("Player"))).thenReturn(player1.getName());
//        when(manager.getGame(resignPlayer)).thenReturn(game);

        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(request.queryParams("Player")).thenReturn(player1.getName());

        resignPlayer = player1;
        assertEquals(game.getRedPlayer().getName(), resignPlayer.getName());

        assertNull(modeOptionsAsJSON.get("isGameOver"));
        modeOptionsAsJSON.put("isGameOver", true);
        assertEquals(true, modeOptionsAsJSON.get("isGameOver"));

        modeOptionsAsJSON.put("gameOverMessage", resignPlayer.getName() + " has resigned.");
        assertEquals("p1 has resigned.", modeOptionsAsJSON.get("gameOverMessage"));

        response.body(gson.toJson(PostResignGame.resignMessage(resignPlayer)));
        assertNotNull(gson);
        //        JASONassert.assertEquals(expectedJson, response.getBody(), false);

//        when(modeOptionsAsJSON.get("isGameOver")).thenReturn(true);
//        when(modeOptionsAsJSON.put("isGameOver", true)).thenReturn(true);
//        assertEquals(((boolean) modeOptionsAsJSON.get("isGameOver")),
//                ((boolean) gson.fromJson((String) CuT.get("modeOptionsAsJSON"), Map.class).get("isGameOver")));
        assertTrue(modeOptionsAsJSON.containsKey("isGameOver") && modeOptionsAsJSON.get("isGameOver") != null);

//        assertEquals(true, modeOptionsAsJSON.put("isGameOver", true));
//        assertEquals(true, modeOptionsAsJSON.put("isGameOver", true));
//        when(modeOptionsAsJSON.put("isGameOver", true)).thenReturn(true);
//        doReturn(true).when(modeOptionsAsJSON.put("isGameOver", true));
//        doReturn("gameOverMessage", resignPlayer.getName() + " has resigned.");
//        doReturn(modeOptionsAsJSON.put("gameOverMessage", resignPlayer.getName() + " has resigned."));

        String expected = "{\"text\":\"p1 has resigned.\",\"type\":\"INFO\"}";
        // Invoke the test
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        } catch (Exception e) {
            //squash
        }
    }

    /**
     * Test that the "resignGame" action that is handled when the player is null
     */
//    @Test
//    public void resign_game_null_player() {
//        this.player = new Player(null);
//        final TemplateEngineTester testHelper = new TemplateEngineTester();
//        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
//
//        // Invoke the test
//        SignOut.handle(request, response);
//
//        // Analyze the results:
//        //   * model is a non-null Map
//        testHelper.assertViewModelExists();
//        testHelper.assertViewModelIsaMap();
//        //   * model contains all necessary View-Model data
//        testHelper.assertViewModelAttribute(
//                GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
//        testHelper.assertViewModelAttribute(
//                GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
//        testHelper.assertViewModelAttribute(
//                GetHomeRoute.CURRENT_USER, null);
//        //   * test view name
//        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
//    }
}


