package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
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
    private Player player1;
    private Player player2;
    private Player resignPlayer;
    private int ID = 2;
    private GameManager gameManager;
    private CheckersGame checkersGame;
    private Player winningPlayer;

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
        when(request.session()).thenReturn(session);
        this.response = mock(Response.class);
        this.templateEngine = mock(TemplateEngine.class);
        this.checkersGame = mock(CheckersGame.class);


        this.player1 = new Player("p1");
        this.player2 = new Player("p2");
        this.gameManager = new GameManager();
        this.gameManager.makeGame(player1, player2);
        this.gson = new Gson();
        this.playerLobby = new PlayerLobby();
        CuT = new PostResignGame(gameManager, gson);
//        this.request = mock(Request.class);
//        this.session = mock(Session.class);
//        this.templateEngine = mock(TemplateEngine.class);
//        this.playerLobby = mock(PlayerLobby.class);
//        this.response = mock(Response.class);
//        this.gson = mock(Gson.class);
//        this.gameManager = mock(GameManager.class);
//        this.checkersGame = mock(CheckersGame.class);
//
//        // build model objects
//        player = new Player(PLAYER);
//
        // mock behavior
        when(request.session()).thenReturn(session);
//
//        // create a unique for each test
//        CuT = new PostResignGame(gameManager, gson);

    }

    /**
     * Test that you can construct a new PostResignGame.
     */
    @Test
    public void create_post_resign_game() {
        new PostResignGame(gameManager, gson);
    }

    /**
     * Test that the resignMessage handle is correct
     */
    @Test
    public void resign_message() {
        when(session.attribute("Player")).thenReturn(player1);
        when(session.attribute("Player")).thenReturn(player2);
        assertEquals("{Msg INFO 'p1 has resigned.'}", PostResignGame.resignMessage(player1).toString(),
                "unexpected resign message");
    }

    /**
     * Test that the "resignGame" action handle when players aren't null
     */
    @Test
    public void resign_game_handle() {
        when(request.queryParams("gameID")).thenReturn("2");
        assertEquals(2, Integer.parseInt(request.queryParams("gameID")), "invalid gameID found");

        assertEquals(resignPlayer, session.attribute("Player"), "invalid resigned player");

        assertTrue(modeOptionsAsJSON.isEmpty());
        assertFalse(modeOptionsAsJSON.containsKey("isGameOver"));
        modeOptionsAsJSON.put("isGameOver", true);
        assertFalse(modeOptionsAsJSON.isEmpty());
        assertTrue(modeOptionsAsJSON.containsKey("isGameOver"));
        assertEquals(true, modeOptionsAsJSON.get("isGameOver"));
        this.resignPlayer = player1;
        CheckersGame game = gameManager.makeGame(player1, player2);
        assertNotNull(gameManager.getGame(player1));
        assertFalse(modeOptionsAsJSON.containsKey("gameOverMessage"));

        assertEquals("p1", resignPlayer.getName());
        modeOptionsAsJSON.put("gameOverMessage", resignPlayer.getName() + " has resigned.");
        modeOptionsAsJSON.containsValue("p1 has resigned.");
        assertTrue(modeOptionsAsJSON.containsKey("gameOverMessage"));

        Session session = request.session();
        resignPlayer = session.attribute("Player");

        assertNull(response.body());
        response.body(gson.toJson(Message.info(player1.getName() + " has resigned.")));
        assertEquals(2, Message.Type.values().length);
        gson.toJson(Message.info(player1.getName() + " has resigned."));

        // Invoke the test
//        CuT.handle(request, response);
//        CheckersGame game = gameManager.getGame(resignPlayer);
//        if (resignPlayer.getName().equals(game.getRedPlayer().getName())) {
//            winningPlayer = game.getWhitePlayer();
//        } else {
//            winningPlayer = game.getRedPlayer();
//        }
//        GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
//        GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", resignPlayer.getName() + " has resigned.");
//        response.body(gson.toJson(resignMessage(resignPlayer)));
//        this.called = true;
//        return gson.toJson(resignMessage(resignPlayer));
        // Invoke the test
//        CuT.handle(request, response);

        //        final TemplateEngineTester testHelper = new TemplateEngineTester();
//        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
//
//        // Invoke the test
//        CuT.handle(request, response);
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


