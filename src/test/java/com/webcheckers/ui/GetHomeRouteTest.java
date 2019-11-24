package com.webcheckers.ui;


import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 *Unit Test suite for Get Home Route component
 *
 * author: Mallory Bridge mmb2582
 */
@Tag("UI-Tier")
public class GetHomeRouteTest {

    //attributes for testing

    //CuT
    private GetHomeRoute CuT;

    //Mock Objects
    private Request request;
    private Response response;
    private TemplateEngine engine;
    private Session session;
    private GameManager gameManager;
    private PlayerLobby playerLobby;

    //all we need from player lobby is addPlayer
    //so I will make it friendly (and bc I do not
    //know how to mock this)
    private PlayerLobby lobby;

    /**
     * Setting up the mock objects for testing
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameManager = mock(GameManager.class);
        lobby = new PlayerLobby();
        CuT = new GetHomeRoute(engine, lobby, gameManager);

    }

    //test for when no one is signed in
    @Test
    public void new_session_no_players(){
        //create my testHelper
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //invoke the route
        CuT.handle(request, response);

        //make sure it exists
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //contains all View Model Data
        testHelper.assertViewModelAttribute(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_COUNT, GetHomeRoute.NO_PLAYERS);

        //test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);

    }

    //test for when one person signed in, but new session
    //this is the ActiveUser vm
    @Test
    public void new_session_with_players(){
        //create my testHelper
        lobby.addPlayer(mock(Player.class));
        //CuT = new GetHomeRoute(engine, lobby, gameManager);;
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);


        //make sure it exists
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //contains all View Model Data
        testHelper.assertViewModelAttribute(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);

        //test count message for player count
        String countTest = String.format(GetHomeRoute.PLAYER, lobby.getPlayers().size());
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_COUNT, countTest);


        //test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    //test for when someone is signed in
    //this is the current user vm
    @Test
    public void signed_in(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(session.attribute("Player")).thenReturn(mock(Player.class));

        lobby.addPlayer(mock(Player.class));
        lobby.addPlayer(mock(Player.class));
        List<String> userList = lobby.getUsernames();

        CuT.handle(request, response);
        //make sure it exists
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //contains all View Model Data
        testHelper.assertViewModelAttribute(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);

        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_USER, session.attribute("Player"));
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);

        testHelper.assertViewModelAttribute(GetHomeRoute.USERS_LIST, userList);
    }

    /**
     * When a player is signed in and in a game, redirect them to
     * game page
     */
    @Test
    public void signed_in_in_game(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //make a player and put them in a game
        Player currentPlayer = mock(Player.class);
        when(session.attribute("Player")).thenReturn(currentPlayer);
        when(currentPlayer.isInGame()).thenReturn(Boolean.TRUE);

        try{
            CuT.handle(request, response);
            fail("Redirect invoke halt expectations.");
        }catch (HaltException e){
            //expected
        }

        verify(response).redirect(WebServer.GAME_URL);
    }
}
