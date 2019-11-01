package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;

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

    // Variables used in the view model map for rendering the sign in page
    static final String USERNAME = "myUsername";
    static final String CURRENT = "current";
    static final String MESSAGE_ATTR = "message";
    // Values ued in the view model map for rendering the sign in page
    static final String INVALID_USR = "Must start with at least one alphanumeric character.";
    static final String TAKEN_USR = "Username has already been taken.";
    static final String VIEW_NAME = "signin.ftl";


    //
    // Static Methods
    //

    /**
     * Make an info message when the username is invalid.
     * @return Returns an error message
     */
    public static Message makeInvalidUsrMessage() {
        return Message.error(INVALID_USR);
    }

    /**
     * Make an error message when the username is taken.
     * @return Returns an error message
     */
    public static Message makeTakenUsrMessage() {
        return Message.error(TAKEN_USR);
    }

    //
    // Attributes
    //

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

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
    PostSignInRoute(TemplateEngine templateEngine, PlayerLobby playerLobby) {
        // Sets and validates the templateEngine attribute to not be null
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        // Sets and validates the playerLobby attribute to not be null
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");

    }

    /**
     * {@inheritDoc}
     *
     * @throws java.util.NoSuchElementException
     *      when an invalid username is returned
     * @param request
     * @param response
     * @return templateEngine to render a view or null
     */
    @Override
    public Object handle(Request request, Response response) {
        // start building a View-Model
        final Map<String, Object> vm = new HashMap<>();
        // Displays the sign in title and message
        vm.put(GetSignInRoute.TITLE, GetSignInRoute.TITLE_MSG);
        vm.put(GetSignInRoute.MESSAGE, GetSignInRoute.SIGNIN_MSG);

        // retrieve the game object
        final Session session = request.session();
        // retrieve request parameter
        final String userStr = request.queryParams(USERNAME);
        Player player = new Player(userStr);


        /* A null playerServices indicates a timed out session or an illegal request on this URL.
         * In either case, we will redirect back to home.
         */
        if (playerLobby != null) {
            // make the player and create the appropriate ModelAndView for rendering
            ModelAndView mv;
            if (!playerLobby.isValidPlayer(player)) {
                // Error checking for invalid username
                mv = error(vm, makeInvalidUsrMessage());
            } else if (!playerLobby.isNewPlayer(player)) {
                // error checking for taken username
                mv = error(vm, makeTakenUsrMessage());
            } else {
                // Adds the player to the playerLobby and redirects them to
                // home, signed in
                playerLobby.addPlayer(player);
                session.attribute("Player", player);
                mv = currentUser(playerLobby.getUsernames(), vm, player);
                response.redirect(WebServer.HOME_URL);
            }
            return templateEngine.render(mv);
        } else {
            // Redirects to home, if playerLobby is null
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }

    //
    // Private methods
    //

    /**
     * Error model and view displays the error message
     * @param vm is the view model
     * @param message is the error message
     * @return New model and view
     */
    private ModelAndView error(final Map<String, Object> vm, final Message message) {
        // Displays the error message
        vm.put(MESSAGE_ATTR, message);
        // Returns new model and view
        return new ModelAndView(vm, VIEW_NAME);
    }

    /**
     * Sets the currentUser variable to the current player
     * @param userList is the list of players online
     * @param vm is the view model
     * @param player is the current player
     * @return new model and view
     */
    public ModelAndView currentUser(List<String> userList, Map<String, Object> vm, final Player player) {
        // Displays the welcome message
        vm.put(GetHomeRoute.WELCOME_ATTR, GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.MESSAGE, GetHomeRoute.WELCOME_MSG);

        // Sets the current user to the current player
        vm.put(GetHomeRoute.CURRENT_USER, player);
        // Displays the players online title
        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        String currentPlayer = player.getName();
        // Displays the list of players online
        vm.put(GetHomeRoute.USERS_LIST, userList);
        // Sets current variable to the current player's name
        vm.put(CURRENT, currentPlayer);
        // Returns new model and view
        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    }

}