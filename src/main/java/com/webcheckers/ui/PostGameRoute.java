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
    static final String VIEW_NAME = "game.ftl";
    static final String MESSAGE_ATTR = "message";
    static final String USER_PARAM = "name";
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
        Player currentPlayer = request.session().attribute("Player");

        //player lobby needs to tall us who the current player is
        //pull parameters
        final String name = request.queryParams(USER_PARAM);

        if(playerLobby != null) {
            currentPlayer = playerLobby.getPlayer();
            chosenOpponent = playerLobby.findPlayer(name);
            //player lobby will tell us if chosen person is in a game

            //if opponent does not exist or opponent is in a game
            System.out.println(chosenOpponent.getName());
            System.out.println(playerLobby.isInGame(chosenOpponent));
            if (chosenOpponent == null || playerLobby.isInGame(chosenOpponent)) {
                //TODO add error message that works
                Message error = new Message("PLAEYER ALREADY IN GAME!!", Message.Type.ERROR);
                session.attribute(error.getText(), MESSAGE);
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
                CheckersGame game = gameManager.makeGame(currentPlayer, chosenOpponent);
                System.out.println("This is current user");
                System.out.println(currentPlayer.getName());

                return templateEngine.render(new ModelAndView(getGameRender(game, currentPlayer), VIEW_NAME));
        }
        //redirect home
        return templateEngine.render("Lobby does not exist. Help");
    }

    private ModelAndView error(final Map<String, Object> vm, final Message message) {
        vm.put(MESSAGE_ATTR, message);
        return new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    }

    private Map<String, Object> getGameRender(CheckersGame game, Player sessionPlayer){
        Player redPlayer = game.getRedPlayer();
        Player whitePlayer = game.getWhitePlayer();

        final Map<String, Object> vm = new HashMap<>();
        vm.put("title", VIEW_NAME);
        vm.put("currentUser", sessionPlayer);
        vm.put("viewMode", viewMode.PLAY);
        vm.put("redPlayer", redPlayer);
        vm.put("whitePlayer", whitePlayer);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", false);
        modeOptions.put("gameOverMessage", "/* get end of game message */");
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        vm.put("activeColor", PostGameRoute.activeColor.RED);
        vm.put("message", MESSAGE);
        vm.put("board", new BoardView(sessionPlayer, game));
        return vm;
    }
}
