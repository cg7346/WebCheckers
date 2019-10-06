package com.webcheckers.ui;

import com.webcheckers.model.Player;
import spark.Route;
import spark.TemplateEngine;

import java.util.Objects;

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
     * Make an error message when the username is invalid.
     */
    static String makeInvalidUsrMessage() {
        return INVALID_USR;
    }

    /**
     * Make an error message when the username is taken.
     */
    static String makeTakenUsrMessage() {
        return TAKEN_USR;
    }

    //
    // Attributes
    //

    private final Player player;
    private final TemplateEngine templateEngine;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /signin} route handler.
     *
     * @param player
     *    {@Link Player} a player
     * @param templateEngine
     *    template engine to use for rendering HTML page
     *
     * @throws NullPointerException
     *    when the {@code Player} or {@code templateEngine} parameter is null
     */
    PostSignInRoute(Player player, TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(player, "Player must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.player = player;
        this.templateEngine = templateEngine;
    }


}
