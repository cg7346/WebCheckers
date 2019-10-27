package com.webcheckers.ui;

import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the Sign In page.
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
public class GetSignInRoute implements Route {

    static final Logger LOG = Logger.getLogger(com.webcheckers.ui.GetSignInRoute.class.getName());

    // Values used in the view-model map for rendering the signin view.
    static final String NEWUSR_ATTR = "isNewUser";
    static final Message SIGNIN_MSG = Message.info("Player Sign In");
    static final String MESSAGE = "message";
    static final String TITLE = "title";
    static final String TITLE_MSG = "Sign In";
    static final String VIEW_NAME = "signin.ftl";

    //
    // Attributes
    //

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        // validation
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSignInRoute is initialized.");
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
        LOG.finer("GetSignInRoute is invoked.");

        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE, TITLE_MSG);
        // display a user message in the Home page
        vm.put(MESSAGE, SIGNIN_MSG);
        // render the View
        return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
    }
}

