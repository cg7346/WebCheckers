package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

/**
 * The unit test suite for the {@link PostSignInRoute} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("UI-tier")
public class PostSignOutRouteTest {

    // Constants
    private static final String NAME = "User";


    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PostSignInRoute} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSignOutRoute SignOut;

    // friendly objects
    private PlayerLobby playerLobby;
    private Player player;

    // attributes holding mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        this.request = mock(Request.class);
        this.session = mock(Session.class);
        this.templateEngine = mock(TemplateEngine.class);
        this.playerLobby = mock(PlayerLobby.class);
        this.response = mock(Response.class);

        when(request.session()).thenReturn(session);

        // create a unique for each test
        SignOut = new PostSignOutRoute(templateEngine, playerLobby);

    }


    /**
     * Test that the "sign-out" action this handled when the player is not null
     */
    @Test
    public void signOutPlayer() {
        this.player = new Player(NAME);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        SignOut.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(
                GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        testHelper.assertViewModelAttribute(
                GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        testHelper.assertViewModelAttribute(
                GetHomeRoute.CURRENT_USER, null);
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    /**
     * Test that the "sign-out" action this handled when the player is null
     */
    @Test
    public void signOutNullPlayer() {
        this.player = new Player(null);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        SignOut.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(
                GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        testHelper.assertViewModelAttribute(
                GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        testHelper.assertViewModelAttribute(
                GetHomeRoute.CURRENT_USER, null);
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }
}


