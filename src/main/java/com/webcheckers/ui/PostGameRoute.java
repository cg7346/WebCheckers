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


/**
 * Post game route
 * Authors: Jacquelyn Leung and Mallory Bridge (Pair programming)
 */
public class PostGameRoute implements Route {
    static final String VIEW_NAME = "game.ftl";

    static final String USER_PARAM = "user";
    private  Player chosenOpponent; //white player
    private Player currentPlayer; //red player
    private final GameManager gameManager;
    private final TemplateEngine templateEngine;
    static final Message MESSAGE = Message.info("A game has been started");
    static enum viewMode {PLAY, SPECTATOR,REPLAY}
    static enum activeColor {RED, WHITE}

    //TODO
    private final Gson gson = new Gson();

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
        final String name = request.queryParams(USER_PARAM);


        if(playerLobby != null) {
            currentPlayer = playerLobby.getPlayer();
            chosenOpponent = playerLobby.findPlayer(name);
            //player lobby will tell us if chosen person is in a game

            //if opponent does not exist or opponent is in a game
            if (chosenOpponent == null || playerLobby.isInGame(chosenOpponent)) {
                return templateEngine.render("Player is already in a game");
            }
                CheckersGame game = gameManager.makeGame(currentPlayer, chosenOpponent);
                BoardView board = new BoardView(currentPlayer, game);

                vm.put("title", "GameTitle");
                vm.put("currentUser", currentPlayer);
                vm.put("viewMode", PostGameRoute.viewMode.PLAY);
                final Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", false);
                modeOptions.put("gameOverMessage", "/* get end of game message */");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                //vm.put("modeOptionsAsJSON", modeOptionsAsJSON);
                vm.put("redPlayer", currentPlayer);
                vm.put("whitePlayer", chosenOpponent);
                vm.put("activeColor", PostGameRoute.activeColor.RED);
                vm.put("board", board);
                vm.put("message", MESSAGE);
                return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                //rerender home with error "already in game"
            //if not
                //have Game Manger create a new game & assign the two people
                //to it
                //create game view with all these parameters
                //send game to get game route
        }
        //redirect home
        return templateEngine.render("Lobby does not exist. Help");
    }



}
