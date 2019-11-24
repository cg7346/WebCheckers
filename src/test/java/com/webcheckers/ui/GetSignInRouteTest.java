package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link GetSignInRoute} component.
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
@Tag("UI-Tier")
public class GetSignInRouteTest {

    // CuT
    private GetSignInRoute CuT;

    //Mock Objects
    private Request request;
    private Response response;
    private TemplateEngine engine;
    private Session session;
    private GameManager gameManager;
    private PlayerLobby playerLobby;
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
        CuT = new GetSignInRoute(engine);
    }

    @Test
    public void sign_in(){
        //create my testHelper
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //invoke the route
        CuT.handle(request, response);

        //make sure it exists
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //contains all View Model Data
        testHelper.assertViewModelAttribute(GetHomeRoute.WELCOME_ATTR, GetSignInRoute.TITLE_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE, GetSignInRoute.SIGNIN_MSG);

        //test view name
        testHelper.assertViewName(GetSignInRoute.VIEW_NAME);
    }
}
