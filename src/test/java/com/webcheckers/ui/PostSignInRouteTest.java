package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.webcheckers.ui.PostSignInRoute.CURRENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The unit test suite for the {@link PostSignInRoute} component.
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
@Tag("UI-tier")
public class PostSignInRouteTest {

    // Constants
    private static final String PLAYER1 = "Bobby";
    private static final String PLAYER2 = "Bruce";
    private static final String PLAYER3 = "    ";
    private static final String PLAYER4 = "Bobby";
    private static final String AI_PLAYER = "AI";

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PostSignInRoute} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSignInRoute CuT;

    // friendly objects
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    // attributes holding mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;
    private PlayerLobby playerLobby;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);

        // internal mocks
        playerLobby = mock(PlayerLobby.class);

        // build model objects
        player1 = new Player(PLAYER1);
        player2 = new Player(PLAYER2);
        player3 = new Player(PLAYER3);
        player4 = new Player(PLAYER4);

        // mock behavior
        when(request.session()).thenReturn(session);

        // create a unique for each test
        CuT = new PostSignInRoute(templateEngine, playerLobby);

    }

    /**
     * Test that the "sign-in" action this handled and is redirected to the home page
     */
    @Test
    public void sign_in_valid_player() {
        when(session.attribute("Player")).thenReturn(player1);
        when(request.queryParams("myUsername")).thenReturn((PLAYER1));
        when(playerLobby.isNewPlayer(player1)).thenReturn(true);
        when(playerLobby.isValidPlayer(player1)).thenReturn(true);


        when(session.attribute("Player")).thenReturn(player2);
        when(request.queryParams("myUsername")).thenReturn((PLAYER2));
        when(playerLobby.isValidPlayer(player2)).thenReturn(true);
        when(playerLobby.isNewPlayer(player2)).thenReturn(true);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);
    }

    /**
     * Test that the "sign-in" action this handled
     */
    @Test
    public void sign_in_taken() {
        playerLobby.addPlayer(player1);

        when(session.attribute("Player")).thenReturn(player1);
        when(request.queryParams("myUsername")).thenReturn(PLAYER1);
        when(playerLobby.isInGame(player1)).thenReturn(true);

        when(session.attribute("Player")).thenReturn(player4);
        when(request.queryParams("myUsername")).thenReturn((PLAYER4));
        when(!playerLobby.isNewPlayer(player4)).thenReturn(false);

        when(session.attribute("Player")).thenReturn(player4);
        when(request.queryParams("myUsername")).thenReturn((AI_PLAYER));
        when(!playerLobby.isNewPlayer(player4)).thenReturn(false);


        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(
                GetSignInRoute.TITLE, GetSignInRoute.TITLE_MSG);
//        testHelper.assertViewModelAttribute(
//                PostSignInRoute.MESSAGE_ATTR, PostSignInRoute.makeTakenUsrMessage());
        testHelper.assertViewModelAttributeIsAbsent(PostSignInRoute.TAKEN_USR);
        //   * test view name
        testHelper.assertViewName(PostSignInRoute.VIEW_NAME);
    }

    /**
     * Test that the make Taken User Message is handled
     */
    @Test
    public void taken_message() {
        assertEquals(PostSignInRoute.makeTakenUsrMessage().getText(), PostSignInRoute.TAKEN_USR);
    }

    /**
     * Test that the make Taken User Message is handled
     */
    @Test
    public void invalid_message() {
        assertEquals(PostSignInRoute.makeInvalidUsrMessage().getText(), PostSignInRoute.INVALID_USR);
    }

    /**
     * Test that the "sign-in" action this handled
     */
    @Test
    public void sign_in_invalid() {
        playerLobby.addPlayer(player1);

        when(session.attribute("Player")).thenReturn(player1);
        when(playerLobby.isInGame(player1)).thenReturn(true);


        when(session.attribute("Player")).thenReturn(player3);
        when(request.queryParams("myUsername")).thenReturn((PLAYER3));
        when(!playerLobby.isValidPlayer(player3)).thenReturn(false);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

//        testHelper.assertViewModelAttribute(playerLobby.getUsernames(), playerLobby.findPlayer(PLAYER2));
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(
                GetSignInRoute.TITLE, GetSignInRoute.TITLE_MSG);
//        testHelper.assertViewModelAttribute(
//                PostSignInRoute.MESSAGE_ATTR, "Must start with at least one alphanumeric character.");
        testHelper.assertViewModelAttributeIsAbsent(PostSignInRoute.INVALID_USR);
        //   * test view name
        testHelper.assertViewName(PostSignInRoute.VIEW_NAME);
    }

    @Test
    void currentUser(){
        List<String> fakeList = new ArrayList<>();
        fakeList.add(player1.getName());
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);
        vm.put(GetHomeRoute.CURRENT_USER, player1);
        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        vm.put(GetHomeRoute.USERS_LIST, fakeList);
        vm.put(CURRENT, player1.getName());
        ModelAndView mv = new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
        assertEquals(mv.getModel(), CuT.currentUser(fakeList, vm, player1).getModel());
    }

    @Test
    void nullPlayerLobby(){
        playerLobby = null;
        CuT = new PostSignInRoute(templateEngine, playerLobby);
        assertThrows(spark.HaltException.class, () -> CuT.handle(request, response));
    }

    @Test
    void currentUserAgain(){
        when(request.queryParams("myUsername")).thenReturn(player1.getName());
        when(playerLobby.isValidPlayer(any(Player.class))).thenReturn(true);
        when(playerLobby.isNewPlayer(any(Player.class))).thenReturn(true);
        CuT.handle(request, response);
        verify(response).redirect(WebServer.HOME_URL);
    }


}