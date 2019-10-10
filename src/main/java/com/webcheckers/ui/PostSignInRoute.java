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

    private static final String INVALID_USR = "Username should contain at " +
            "least one alphanumeric characters or contain one or more " +
            "characters that are not alphanumeric or spaces.";
    private static final String TAKEN_USR = "Username already has been taken. " +
            "Please enter a new Username.";
    private static final String VIEW_NAME = "signin.ftl";
    private static final String PLAYERS_ON = "playersOnline";
    private static final String PLAYERS_ONLINE = "Players Online";
    private static final String PLAYERS_LIST = "playersList";
    private static final String PLAYERS_COUNT = "playersCount";
    private static final String PLAYERS = "There are %d other players available to play at this time.";
    private static final String NO_PLAYERS = "There are no other players available to play at this time";
    //
    // Static Methods
    //

    /**
     * Make an info message when the username is invalid.
     */
    public static Message makeInvalidUsrMessage() {
        return Message.error(INVALID_USR);
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
       // vm.put(GetHomeRoute.CURRENT_USER, Boolean.FALSE);

        // retrieve the game object
        final Session session = request.session();
        final PlayerLobby playerLobby = session.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        // retrieve request parameter
        final String userStr = request.queryParams(USERNAME);
        Player player = new Player(userStr);

        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (playerLobby != null) {
            // make the guess and create the appropriate ModelAndView for rendering
            ModelAndView mv;
            if (!playerLobby.isValidPlayer(player)) {
                mv = error(vm, makeInvalidUsrMessage());
            } else if (!playerLobby.isNewPlayer(player)) {
                mv = error(vm, makeTakenUsrMessage());
            } else {
                playerLobby.addPlayer(player);
                System.out.println(playerLobby.getUsernames());
                mv = currentUser(vm, player);
            }
            return templateEngine.render(mv);
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

    private ModelAndView error(final Map<String, Object> vm, final Message message) {
        vm.put(MESSAGE_ATTR, message);
        return new ModelAndView(vm, VIEW_NAME);
    }

    //TODO: need to figure out how to implement this
    //TODO: currentUser needs a name method currentUser.name
    private ModelAndView currentUser(final Map<String, Object> vm, final Player player) {
        vm.put(GetHomeRoute.CURRENT_USER, player);
        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    }

    //TODO: going to need this later
//    private ModelAndView signout() {
//        return ;
//    }
}
