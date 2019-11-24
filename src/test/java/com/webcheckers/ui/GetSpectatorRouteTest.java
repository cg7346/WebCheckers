package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link GetSpectatorRoute} component.
 *
 * @author <a href='mailto:mmb2582@rit.edu'>Mallory Bridge</a>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class GetSpectatorRouteTest {

    /**
     * The component-under-test (CuT).
     * <p>
     * This is a stateless component so we only need one.
     * The {@link GetSpectatorRoute} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private GetSpectatorRoute CuT;

    //Mock Object
    private Request request;
    private Response response;
    private Session session;
    private Gson gson;
    private GameManager gameManager;
    private PlayerLobby lobby;
    private CheckersGame mockGame;
    private Player p1;
    private Player p2;
    private Player spectator;
    private TemplateEngine engine;

    private TemplateEngineTester helper;

    @BeforeEach
    void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        lobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        mockGame = mock(CheckersGame.class);
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        spectator = mock(Player.class);
        gson = new Gson();
        engine = mock(TemplateEngine.class);
        helper = new TemplateEngineTester();

        CuT = new GetSpectatorRoute(engine, lobby, gameManager);
        String gameIdString = "3";
        when(request.queryParams("gameID")).thenReturn(gameIdString);
        when(engine.render(any(ModelAndView.class))).thenAnswer(helper.makeAnswer());
    }

    @Test
    public void testNull() {
        when(session.attribute("Player")).thenReturn(null);
        assertThrows(spark.HaltException.class, () -> CuT.handle(request, response));
    }

    @Test
    void nullGame(){
        List<Player> fakeList = new ArrayList<>();
        when(lobby.getPlayers()).thenReturn(fakeList);
        assertThrows(spark.HaltException.class, () -> CuT.spectator(new HashMap<>(), new HashMap<>(), p1,
                request, response));
    }


    @Test
    public void testSpectatorCount0() {
        when(spectator.isSpectating()).thenReturn(true);
        List<Player> fakeList = new ArrayList<>();
        Set<String> fakeSet = new HashSet<>();
        HashMap<CheckersGame, String> gameList = new HashMap<>();
        CheckersGame game = new CheckersGame(p1, p2, "1");
        gameList.put(game, "1");
        when(session.attribute("Player")).thenReturn(spectator);
        when(lobby.getPlayers()).thenReturn(fakeList);
        when(request.queryParams()).thenReturn(fakeSet);
        when(request.queryParams("spec_user")).thenReturn("1");
        when(gameManager.getGame("1")).thenReturn(game);
        CuT.handle(request, response);
        assertRegulars();
    }

    @Test
    public void testSpectatorCount1() {
        when(spectator.isSpectating()).thenReturn(true);
        List<Player> fakeList = new ArrayList<>();
        fakeList.add(p1);
        Set<String> fakeSet = new HashSet<>();
        HashMap<CheckersGame, String> gameList = new HashMap<>();
        CheckersGame game = new CheckersGame(p1, p2, "1");
        gameList.put(game, "1");
        gameList.put(mock(CheckersGame.class), "2");

        when(session.attribute("Player")).thenReturn(spectator);
        when(lobby.getPlayers()).thenReturn(fakeList);
        when(request.queryParams()).thenReturn(fakeSet);
        when(request.queryParams("spec_user")).thenReturn("1");
        when(gameManager.getGame("1")).thenReturn(game);
        CuT.handle(request, response);
        assertRegulars();
    }

    @Test
    public void testSpectatorCount2() {
        when(spectator.isSpectating()).thenReturn(true);
        List<Player> fakeList = new ArrayList<>();
        fakeList.add(p1);
        fakeList.add(p2);
        Set<String> fakeSet = new HashSet<>();
        HashMap<CheckersGame, String> gameList = new HashMap<>();
        CheckersGame game = new CheckersGame(p1, p2, "1");
        gameList.put(game, "1");
        gameList.put(mock(CheckersGame.class), "2");
        gameList.put(mock(CheckersGame.class), "3");
        when(session.attribute("Player")).thenReturn(spectator);
        when(lobby.getPlayers()).thenReturn(fakeList);
        when(request.queryParams()).thenReturn(fakeSet);
        when(request.queryParams("spec_user")).thenReturn("1");
        when(gameManager.getGame("1")).thenReturn(game);
        CuT.handle(request, response);
        assertRegulars();
    }

    private void assertRegulars(){
        helper.assertViewModelExists();
        helper.assertViewModelIsaMap();
        helper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE_ATTR_MSG);


    }
}