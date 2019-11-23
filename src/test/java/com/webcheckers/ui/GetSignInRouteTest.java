package com.webcheckers.ui;
import spark.*;


import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetSignInRouteTest {

    private GetSignInRoute CuT;
    private TemplateEngineTester helper;

    private Request request;
    private Response response;
    private TemplateEngine engine;

    @BeforeEach
    void construct(){
        engine = mock(TemplateEngine.class);
        helper = new TemplateEngineTester();
        request = mock(Request.class);
        response = mock(Response.class);
        CuT = new GetSignInRoute(engine);
    }

    @Test
    void test_handle(){
        when(engine.render(any(ModelAndView.class))).thenAnswer(helper.makeAnswer());
        CuT.handle(request, response);
        helper.assertViewModelExists();
        helper.assertViewModelIsaMap();
        helper.assertViewName(GetSignInRoute.VIEW_NAME);
        helper.assertViewModelAttribute(GetSignInRoute.TITLE, GetSignInRoute.TITLE_MSG);
        helper.assertViewModelAttribute(GetSignInRoute.MESSAGE, GetSignInRoute.SIGNIN_MSG);

    }
}
