package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * The {@code POST /resignGame} route handler
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 */
public class PostEndGame implements Route {

    //
    // Constants
    //

    // Values used in the view-model map for rendering the game view after a guess/
    private static final String PIECES_CAP = "You are playing a game of checkers with %s\n" +
            "%s has captured all pieces.";
    private static final String VIEW_NAME = "endgame.ftl";

    //
    // Static Methods
    //
    private final TemplateEngine templateEngine;

    //
    // Attributes
    //
    private final PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /resignGame} route handler.
     *
     * @param templateEngine template engine to use for rendering HTML page
     * @throws NoSuchElementException when the {@code Player} or {@code templateEngine} parameter is null
     */
    public PostEndGame(TemplateEngine templateEngine, PlayerLobby playerLobby) {
        // validation
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");

    }


    //
    // Constructor
    //

    /**
     * A String representing how the game ended. Such as:
     * <p>
     * Bryan has captured all of the pieces.
     * Fred has resigned.
     *
     * @return the message that will be displayed when the game is over
     */
    public static Message gameOverMessage() {
//        TODO: String.Format(PIECES_CAP, currentPlayer, opponent)
        return Message.info(PIECES_CAP);
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @param response
     * @return templateEngine to render a view or null
     * @throws java.util.NoSuchElementException when an invalid username is returned
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // start buildiing a View-Model
        final Map<String, Object> vm = new HashMap<>();

        return null;
    }
}
