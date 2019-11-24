package com.webcheckers.ui;
import spark.*;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

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

    /**
     * Setting up the mock objects for testing
     */
    @BeforeEach
    public void CreateGetGameRoute(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);;
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        p1 = mock(Player.class);
        p2 = mock(Player.class);
        gameManager = mock(GameManager.class);
        gson = new Gson();
        lobby = new PlayerLobby();
        CuT = new GetGameRoute(engine, lobby, gameManager, gson);
    }

    @Test
    void test_handNewGame_nullOpponent(){
        System.out.println();
//        when((request.queryParams().isEmpty())).thenReturn(false);
        when(request.queryParams("opp_user")).thenReturn(null);
        assertNull(CuT.handle(request, response));
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