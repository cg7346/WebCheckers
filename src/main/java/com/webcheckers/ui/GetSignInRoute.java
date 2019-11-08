package com.webcheckers.ui;

import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The (@code GET /signin) route handler.
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
public class GetSignInRoute implements Route {

    // Variables used in the view model map for rendering the sign in view.
    static final String MESSAGE = "message";
    static final String TITLE = "title";
    // Values used in the view model map for rendering the sign in view.
    static final Message SIGNIN_MSG = Message.info("Player Sign In");
    static final String TITLE_MSG = "Sign In";
    static final String VIEW_NAME = "signin.ftl";

    //
    // Attributes
    //
    private final TemplateEngine templateEngine;

    /**
     * Create the UI controller to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        // Sets and validate the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response) {
        // Creates the view model
        Map<String, Object> vm = new HashMap<>();
        // Displays the sign in title
        vm.put(TITLE, TITLE_MSG);
        // Displays the sign in message
        vm.put(MESSAGE, SIGNIN_MSG);
        // Renders the view model
        return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
    }
}

