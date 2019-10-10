package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Post game route
 * Authors: Jacquelyn Leung and Mallory Bridge (Pair programming)
 */
public class PostGameRoute implements Route {
    private Player chosenOpponent; //white player
    private Player currentPlayer; //red player
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;

    public PostGameRoute(GameManager gameManager, TemplateEngine templateEngine){
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.gameManager = gameManager;
        this.templateEngine = templateEngine;
    }

    @Override
    public String handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();

        final Session session = request.session();
        final PlayerLobby playerLobby = session.attribute(GetHomeRoute.PLAYERLOBBY_KEY);


        //player lobby needs to tall us who the current player is
        //pull parameters
        //final String guessStr = request.queryParams(GUESS_PARAM);

        if(playerLobby != null) {
            currentPlayer = playerLobby.getPlayer();
            //player lobby will tell us if chosen person is in a game
            //if yes
                //rerender home with error "already in game"
            //if not
                //have Game Manger create a new game & assign the two people
                //to it
                //create game view with all these parameters
                //send game to get game route
        }
        //redirect home
        return "";
    }
}
