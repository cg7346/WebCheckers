package com.webcheckers.ui;

public class PostSignInRoute {
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
    static String makeInvalidUsrMessage(final String invalidUsr) {
        return INVALID_USR;
    }

    /**
     * Make an error message when the username is taken.
     */
    static String makeTakenUsrMessage(final String takenUsr) {
        return TAKEN_USR;
    }
}
