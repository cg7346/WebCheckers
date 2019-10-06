package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;

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
    static Message makeInvalidUsrMessage() {
        return Message.info(INVALID_USR);
    }

    /**
     * Make an error message when the username is taken.
     */
    static Message makeTakenUsrMessage() {
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
    PostSignInRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "Player must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }


}
