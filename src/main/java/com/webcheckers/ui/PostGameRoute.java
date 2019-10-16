package com.webcheckers.ui;

import com.google.gson.Gson;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;


/**
 * Post game route
 * Authors: Jacquelyn Leung and Mallory Bridge (Pair programming)
 */
public class PostGameRoute implements Route {
    //Message and strings for info
    static final String MESSAGE_ATTR = "message";
    static final String USER_PARAM = "name";
    static final Message MESSAGE = Message.info("A game has been started");
    private static final String PLAYER_IN_GAME= "Chosen player is already in a game.";

    //The boss of what we doing here
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;



    public PostGameRoute( TemplateEngine templateEngine, PlayerLobby playerLobby, GameManager gameManager){
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
    }

    /**
     * THAT PLAYER IS IN A GAME ERROR
     * @return Error Message
     */
    public static Message makePlayerInGameMessage() {
        return Message.error(PLAYER_IN_GAME);
    }

    @Override
    public String handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();

        //Lets get the player and opponent
        final Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        final String opponent = request.queryParams(USER_PARAM);

        if(playerLobby != null) {
            //get other player from the lobby
            Player chosenOpponent = playerLobby.findPlayer(opponent);
            //check to see if they exists or are already in a game
            if (chosenOpponent == null || playerLobby.isInGame(chosenOpponent)) {
                //session.attribute("Player", currentPlayer);
                ModelAndView mv = error(vm, makePlayerInGameMessage(), playerLobby);
                return templateEngine.render(mv);
            }
                //just make a game we'll look at that later in get /game
                CheckersGame game = gameManager.makeGame(currentPlayer, chosenOpponent);
                response.redirect(WebServer.GAME_URL);
                halt();
                return null;
        }
        return templateEngine.render("Lobby does not exist. Help");
    }


    private ModelAndView error(final Map<String, Object> vm, final Message message, final PlayerLobby playerLobby) {
        vm.put("title", GetHomeRoute.WELCOME_ATTR_MSG);
        vm.put(GetHomeRoute.CURRENT_USER, playerLobby.getPlayers().get(playerLobby.getPlayers().size()-1));
        vm.put(GetHomeRoute.PLAYERS_ON, GetHomeRoute.PLAYERS_ONLINE);
        vm.put(GetHomeRoute.USERS_LIST, playerLobby.getUsernames());
        vm.put(MESSAGE_ATTR, message);
        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    }
}
