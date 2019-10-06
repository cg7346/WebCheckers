package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The {@code POST /signin} route handler
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostSignInRoute implements Route {

    //
    // Constants
    //

    // Values used in the view-model map for rendering the game view after a guess.
    static final String USERNAME = "myUsername";
    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_TYPE_ATTR = "messageType";

    static final String ERROR_TYPE = "error";
    static final String INVALID_USR = "Username should contain at least one alphanumeric " +
            "characters or contain one or more characters that are not alphanumeric or spaces.";
    static final String TAKEN_USR = "Username already has been taken. Please enter a new Username.";
    static final String VIEW_NAME = "signin.ftl";

    //
    // Static Methods
    //

    /**
     * Make an info message when the username is invalid.
     */
    public static Message makeInvalidUsrMessage() {
        return Message.info(INVALID_USR);
    }

    /**
     * Make an error message when the username is taken.
     */
    public static Message makeTakenUsrMessage() {
        return Message.error(TAKEN_USR);
    }

    //
    // Attributes
    //

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param playerLobby
     *    {@Link Player} a player
     * @param templateEngine
     *    template engine to use for rendering HTML page
     *
     * @throws NullPointerException
     *    when the {@code Player} or {@code templateEngine} parameter is null
     */
    public PostSignInRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "Player must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        // instantiating attributes
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.util.NoSuchElementException
     *      when an invalid result is returned making a guess
     * @param request
     * @param response
     * @return
     */
    @Override
    public String handle(Request request, Response response) {
        // start building a View-Model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetSignInRoute.TITLE, GetSignInRoute.TITLE_MSG);
        vm.put(GetSignInRoute.MESSAGE, GetSignInRoute.SIGNIN_MSG);

        // retrieve the game object
        final Session session = request.session();
        final PlayerLobby playerLobby = session.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        // retrieve request parameter
        final String userStr = request.queryParams(USERNAME);

        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (playerLobby != null) {
            //vm.put(GetSignInRoute.NEWUSR_ATTR, playerLobby.isNewPlayer(userStr));

            // make the guess and create the appropriate ModelAndView for rendering
            ModelAndView mv;
            if (!playerLobby.isValidPlayer(userStr)) {
                mv = error(vm, makeInvalidUsrMessage().toString());
            }
            else if (!playerLobby.isNewPlayer(userStr)) {
                mv = error(vm, makeTakenUsrMessage().toString());
            } else {
                // Invalid username received
                throw new NoSuchElementException("Invalid username received.");

            }
            return templateEngine.render(mv);
        }
        else {
            response.redirect(WebServer.SIGNIN_URL);
            halt();
            return null;
        }
    }

    //
    // Private methods
    //

    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATTR, message);
        vm.put(MESSAGE_TYPE_ATTR, ERROR_TYPE);
        return new ModelAndView(vm, VIEW_NAME);
    }
}
