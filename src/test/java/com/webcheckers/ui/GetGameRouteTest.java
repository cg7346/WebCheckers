package com.webcheckers.ui;
import com.webcheckers.model.Space;
import spark.*;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
class GetGameRouteTest {

    //CuT
    private GetGameRoute CuT;

    //Mock Objects
    private Request request;
    private Response response;
    private TemplateEngine engine;
    private Session session;
    private Gson gson;

    private Player p1;
    private Player p2;
    private GameManager gameManager;
    private PlayerLobby lobby;

    TemplateEngineTester helper;

    /**
     * Setting up the mock objects for testing
     */
    @BeforeEach
    public void CreateGetGameRoute(){
        helper = new TemplateEngineTester();
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);;
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        p1 = mock(Player.class);
        p2 = mock(Player.class);
        gameManager = mock(GameManager.class);
        gson = new Gson();
        lobby = mock(PlayerLobby.class);
        CuT = new GetGameRoute(engine, lobby, gameManager, gson);
        when(session.attribute("Player")).thenReturn(p1);
    }

    @Test
    void test_handNewGame_nullOpponent(){
        HashSet<String> testHash = new HashSet<>();
        testHash.add("Test");
        when(gameManager.getGame(p1)).thenReturn(null);
        when(request.queryParams()).thenReturn(testHash);
        when(request.queryParams("opp_user")).thenReturn(null);
        assertThrows(spark.HaltException.class, () -> CuT.handle(request, response));
    }

    @Test
    void test_handNewGame_nullOpponentPt2(){
        HashSet<String> testHash = new HashSet<>();
        testHash.add("Test");
        when(gameManager.getGame(p1)).thenReturn(null);
        when(request.queryParams()).thenReturn(testHash);
        when(request.queryParams("opp_user")).thenReturn("Opponent");
        when(lobby.findPlayer("Opponent")).thenReturn(null);
        assertThrows(spark.HaltException.class, () -> CuT.handle(request, response));
    }

    @Test
    void test_handNewGame_oppInGame(){
        HashSet<String> testHash = new HashSet<>();
        testHash.add("Test");
        when(gameManager.getGame(p1)).thenReturn(null);
        when(request.queryParams()).thenReturn(testHash);
        when(request.queryParams("opp_user")).thenReturn("Opponent");
        when(lobby.findPlayer("Opponent")).thenReturn(p2);
        when(lobby.isInGame(p2)).thenReturn(true);
        assertThrows(spark.HaltException.class, () -> CuT.handle(request, response));
    }

    @Test
    void test_handNewGame_success(){
        CheckersGame mockGame = setUpMockGame();
        when(engine.render(any(ModelAndView.class))).thenAnswer(helper.makeAnswer());
        HashSet<String> testHash = new HashSet<>();
        testHash.add("Test");
        when(gameManager.getGame(p1)).thenReturn(null);
        when(request.queryParams()).thenReturn(testHash);
        when(request.queryParams("opp_user")).thenReturn("Opponent");
        when(lobby.findPlayer("Opponent")).thenReturn(p2);
        when(lobby.isInGame(p2)).thenReturn(false);
        when(gameManager.makeGame(p1, p2)).thenReturn(mockGame);

        CuT.handle(request, response);
        when(engine.render(any(ModelAndView.class))).thenAnswer(helper.makeAnswer());
        assertRegulars( true);
    }

    private void assertRegulars(boolean activePlayerRed){
        GetGameRoute.activeColor color = (activePlayerRed)
                ? GetGameRoute.activeColor.RED : GetGameRoute.activeColor.WHITE;
        when(engine.render(any(ModelAndView.class))).thenAnswer(helper.makeAnswer());
        helper.assertViewModelExists();
        helper.assertViewModelIsaMap();
        helper.assertViewName(GetGameRoute.VIEW_NAME);
        helper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE_ATTR_MSG);
        //helper.assertViewModelAttribute(GetGameRoute.START_ATTR, GetGameRoute.START_ATTR_MSG);
        helper.assertViewModelAttribute(GetGameRoute.CURRENT_USER_ATTR, p1);
        helper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_ATTR, p1);
        helper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_ATTR, p2);
        helper.assertViewModelAttribute(GetGameRoute.COLOR_ATTR, color);
    }

    private CheckersGame setUpMockGame(){
        CheckersGame mockGame = mock(CheckersGame.class);
        when(mockGame.isRedPlayer(p1)).thenReturn(true);
        when(mockGame.getRedPlayer()).thenReturn(p1);
        when(mockGame.getWhitePlayer()).thenReturn(p2);
        when(mockGame.isRedPlayer(p2)).thenReturn(false);
        when(mockGame.getBoard()).thenReturn(new Space[8][8]);
        when(mockGame.getActivePlayer()).thenReturn(p1);
        return mockGame;
    }
    /*


    @Test
    void test_Game_Started()
    {
        //create my testHelper
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //invoke the route
        CuT.handle(request, response);

        //make sure it exists
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE_ATTR_MSG);
        testHelper.assertViewModelAttribute(GetGameRoute.START_ATTR, GetGameRoute.START_ATTR_MSG);
        testHelper.assertViewModelAttribute(GetGameRoute.GAME_OVER_ATTR, GetGameRoute.GAME_OVER_ATTR_MSG);

        Player currentUser = session.attribute("Player");
        CheckersGame game = gameManager.getGame(currentUser);

        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_USER_ATTR, currentUser);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_ATTR, game.getRedPlayer());
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_ATTR, game.getWhitePlayer());
        testHelper.assertViewModelAttribute(GetGameRoute.COLOR_ATTR, GetGameRoute.activeColor.RED);

        testHelper.assertViewName(GetGameRoute.VIEW_NAME);
    }
    */
}