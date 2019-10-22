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

    // friendly objects
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;

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

//    @Test


//    @Test
//    public void testPlayer() {
//        GameManager gameManager1 = new GameManager();
//        CheckersGame game = gameManager1.makeGame(player1, player2);
//
//        CuT.addPlayer(player2);
//        when(response.)
//    }


//        final TemplateEngineTester testHelper = new TemplateEngineTester();
//        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
//
//        testHelper.assertViewModelExists();
//        testHelper.assertViewModelIsaMap();

//        //make sure it exists
//        testHelper.assertViewModelExists();
//        testHelper.assertViewModelIsaMap();
//
//        //contains all View Model Data
//        testHelper.assertViewModelAttribute(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
//        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);
//        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
//
//        //test count message for player count
//        String countTest = String.format(GetHomeRoute.PLAYER, lobby.getPlayers().size());
//        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_COUNT, countTest);
//
//
//        //test view name
//        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);

//        when(CuT.isValidPlayer(player1)).thenReturn(true);
//        when(CuT.isValidPlayer(player3)).thenReturn(false);

    //    @Test
    public void newPlayer() {
        when(CuT.isNewPlayer(player1)).thenReturn(true);

        CuT.addPlayer(player1);
        when(CuT.isNewPlayer(player1)).thenReturn(false);
    }
//    /**
//     * Test that you can construct a new Player Service.
//     */
//    @Test
//    public void test_create_service() {
//        new PlayerServices(gameCenter);
//    }
//
//    /**
//     * Test the creation of a new Game.
//     */
//    @Test
//    public void test_create_game() {
//        // Analyze results
//        //  1) game object exists
//        assertNotNull(game);
//        //  2) this is the start of the game
//        assertTrue(CuT.isStartingGame());
//        assertTrue(CuT.hasMoreGuesses());
//        assertEquals(GuessGame.NUM_OF_GUESSES, CuT.guessesLeft());
//    }
//
//    /**
//     * Test the reuse of the current Game.
//     */
//    @Test
//    public void test_current_game() {
//        // Perform action
//        final GuessGame game2 = CuT.currentGame();
//
//        // Analyze results
//        assertNotNull(game2);
//        // we should get the same Game object from when it was first created
//        assertSame(game, game2);
//    }
//
//    /**
//     * Test the process of finishing the current Game and creating a new one.
//     */
//    @Test
//    public void test_current_game_2() {
//        // Setup test
//        final PlayerServices CuT = new PlayerServices(gameCenter);
//        final GuessGame game = CuT.currentGame();
//
//        // Perform action
//        CuT.finishedGame();
//        // Make sure to return a different game when called this time
//        when(gameCenter.getGame()).thenReturn(new GuessGame());
//        final GuessGame game2 = CuT.currentGame();
//
//        // Analyze results
//        assertNotNull(game2);
//        // we should get a new game after the first one is declared "finished"
//        assertNotSame(game, game2);
//    }
//
//    /**
//     * Test the process of making a guess: invalid.
//     */
//    @Test
//    public void test_make_a_guess_1() {
//        // Perform action
//        final GuessResult result = CuT.makeGuess(INVALID_GUESS);
//
//        // Analyze results
//        assertNotNull(result);
//        assertEquals(GuessResult.INVALID, result);
//        // an invalid guess does not change the Game state
//        assertTrue(CuT.isStartingGame());
//        assertTrue(CuT.hasMoreGuesses());
//        assertEquals(GuessGame.NUM_OF_GUESSES, CuT.guessesLeft());
//    }
//
//    /**
//     * Test the process of making a guess: one wrong.
//     */
//    @Test
//    public void test_make_a_guess_2() {
//        // Perform action
//        final GuessResult result = CuT.makeGuess(WRONG_GUESS);
//
//        // Analyze results
//        assertNotNull(result);
//        assertEquals(GuessResult.WRONG, result);
//        // a wrong guess does change the Game state
//        assertFalse(CuT.isStartingGame());
//        assertTrue(CuT.hasMoreGuesses());
//        assertEquals(GuessGame.NUM_OF_GUESSES - 1, CuT.guessesLeft());
//    }
//
//    /**
//     * Test the process of making a guess: two wrong guesses.
//     */
//    @Test
//    public void test_make_a_guess_3() {
//        // Perform action
//        GuessResult result = CuT.makeGuess(3);
//        // second guess
//        result = CuT.makeGuess(3);
//
//        // Analyze results
//        assertNotNull(result);
//        assertEquals(GuessResult.WRONG, result);
//        // validate the Game state
//        assertFalse(CuT.isStartingGame());
//        assertTrue(CuT.hasMoreGuesses());
//        assertEquals(GuessGame.NUM_OF_GUESSES - 2, CuT.guessesLeft());
//    }
//
//    /**
//     * Test the process of making a guess: three wrong guesses.
//     */
//    @Test
//    public void test_make_a_guess_4() {
//        // Perform action
//        GuessResult result = CuT.makeGuess(0);
//        // second guess
//        result = CuT.makeGuess(0);
//        // last guess
//        result = CuT.makeGuess(0);
//
//        // Analyze results
//        assertNotNull(result);
//        assertEquals(GuessResult.LOST, result);
//        // validate the Game state
//        assertFalse(CuT.isStartingGame());
//        assertFalse(CuT.hasMoreGuesses());
//        assertEquals(0, CuT.guessesLeft());
//    }
//
//    /**
//     * Test the process of winning on the first guess.
//     */
//    @Test
//    public void test_win() {
//        // Perform action
//        GuessResult result = CuT.makeGuess(RIGHT_GUESS);
//
//        // Analyze results
//        assertNotNull(result);
//        assertEquals(GuessResult.WON, result);
//        // validate the Game state
//        assertFalse(CuT.isStartingGame(), "not starting game");
//        assertTrue(CuT.hasMoreGuesses(), "has more guesses");
//        assertEquals(2, CuT.guessesLeft(), "has two guesses");
//    }

}
