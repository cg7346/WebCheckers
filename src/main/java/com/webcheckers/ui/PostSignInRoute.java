package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
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

    // Values used in the view-model map for rendering the game view after a
    // guess.
    private static final String USERNAME = "myUsername";
    private static final String MESSAGE_ATTR = "message";
    private static final String MESSAGE_TYPE_ATTR = "messageType";

    private static final String ERROR_TYPE = "error";
    private static final String INVALID_USR = "Username should contain at " +
            "least one alphanumeric characters or contain one or more " +
            "characters that are not alphanumeric or spaces.";
    private static final String TAKEN_USR = "Username already has been taken. " +
            "Please enter a new Username.";
    private static final String VIEW_NAME = "signin.ftl";
    private static final String CURRENT_USER = "currentUser";

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

    private final TemplateEngine templateEngine;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param templateEngine
     *    template engine to use for rendering HTML page
     *
     * @throws NoSuchElementException
     *    when the {@code Player} or {@code templateEngine} parameter is null
     */
     PostSignInRoute(TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        // instantiating attributes
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.util.NoSuchElementException
     *      when an invalid username is returned
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object handle(Request request, Response response) {
        // start building a View-Model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetSignInRoute.TITLE, GetSignInRoute.TITLE_MSG);
        vm.put(GetSignInRoute.MESSAGE, GetSignInRoute.SIGNIN_MSG);
        vm.put(GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);

        // retrieve the game object
        final Session session = request.session();
        final PlayerLobby playerLobby = session.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        // retrieve request parameter
        final String userStr = request.queryParams(USERNAME);
        Player player = new Player(userStr);

        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (userStr != null) {
            // make the guess and create the appropriate ModelAndView for rendering
            ModelAndView mv;
            if (!playerLobby.isValidPlayer(player)) {
                mv = error(vm, makeInvalidUsrMessage().toString());
                return templateEngine.render(mv);
            } else if (!playerLobby.isNewPlayer(player)) {
                mv = error(vm, makeTakenUsrMessage().toString());
                return templateEngine.render(mv);
            } else {
                playerLobby.addPlayer(player);
                playerLobby.addUsername(player);
// debugging purposes before it was just added the Player now the usernames are now stored
//                for (int i = 0; i < playerLobby.getUsernames().size(); i++) {
////                    System.out.println(playerLobby.getUsernames());
//                    System.out.println("Player " + Integer.toString(i) + " " + playerLobby.getUsernames().get(i));
//                }
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
        }
        else {
            response.redirect(WebServer.HOME_URL);
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

    //TODO: need to figure out how to implement this
    //TODO: currentUser needs a name method currentUser.name
    private ModelAndView currentUser(final Map<String, Object> vm, final PlayerLobby playerLobby) {
        vm.put(CURRENT_USER, playerLobby.getUsernames());

        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
        //        return playerLobby.isCurrentPlayer(this.player);
    }

    //TODO: going to need this later
//    private ModelAndView signout() {
//        return ;
//    }
}
