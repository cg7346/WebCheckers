package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;

import static spark.Spark.halt;

/**
 * The UI controller to GET the Game page
 *
 * @author <a href='mailto:       jil4009@rit.edu'>Jacquelyn Leung</a>
 * @author <a href='mailto:       mmb2582@rit.edu'> Mallory Bridge   </a>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class GetGameRoute implements Route {

    static final String TITLE_ATTR = "title";
    static final String TITLE_ATTR_MSG = "Game Title";
    static final String CURRENT_USER_ATTR = "currentUser";
    static final String START_ATTR = "message";
    static final Message START_ATTR_MSG = Message.info("A game has been started");
    static final String BOARD_ATTR = "board";
    static final String COLOR_ATTR = "activeColor";
    static final String RED_PLAYER_ATTR = "redPlayer";
    static final String WHITE_PLAYER_ATTR = "whitePlayer";
    static final String GAME_OVER_ATTR = "gameOverMessage";
    static final Message GAME_OVER_ATTR_MSG = Message.info("The game is over"); /* Get the game over message */
    static final String VIEW_NAME = "game.ftl";

    static final String USER_PARAM = "opponent";

    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private final Gson gson;
    enum viewMode {PLAY, SPECTATOR,REPLAY}
    enum activeColor {RED, WHITE}

    static CheckersGame game;

    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby,
                        GameManager gameManager, Gson gson)
    {
        // validation

        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobbu must not be null");
        this.gameManager = gameManager;

        this.gson = gson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handle(Request request, Response response) {
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        CheckersGame game = null;
        //that means a player click on another one, get their name and make a game
        if (!request.queryParams().isEmpty()){
            String opponentName = request.queryParams().iterator().next();
                Player chosenOpponent = playerLobby.findPlayer(opponentName);
                if (playerLobby.isInGame(chosenOpponent)|| chosenOpponent == null ){
                    //we will send an error
                }
                else{game = gameManager.makeGame(currentPlayer, chosenOpponent);}
        //you are the person click on, find your game
        }else{
            game = gameManager.getGame(currentPlayer);
        }
        Player redPlayer = game.getRedPlayer();
        Player whitePlayer = game.getWhitePlayer();
        BoardView board = new BoardView(currentPlayer, game);
        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_ATTR, TITLE_ATTR_MSG);
        vm.put(CURRENT_USER_ATTR, currentPlayer);
        vm.put("viewMode", viewMode.PLAY);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", false);
        modeOptions.put(GAME_OVER_ATTR, GAME_OVER_ATTR_MSG);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        vm.put(RED_PLAYER_ATTR, redPlayer);
        vm.put(WHITE_PLAYER_ATTR, whitePlayer);
        vm.put(COLOR_ATTR, activeColor.RED);
        vm.put(BOARD_ATTR, board);
        vm.put(START_ATTR, START_ATTR_MSG);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
