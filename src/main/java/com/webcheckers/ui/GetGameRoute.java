package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The UI controller to GET the Game page
 *
 * @author <a href='mailto:jil4009@rit.edu'>Jacquelyn Leung</a>
 * @author <a href='mailto:mmb2582@rit.edu'>Mallory Bridge</a>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella<a/>
 */
public class GetGameRoute implements Route {

    static final String TITLE_ATTR = "title";
    static final String TITLE_ATTR_MSG = "Game Title";
    static final String GAME_ID_ATTR = "gameID";
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
    static final String PLAYER_IN_GAME= "Chosen player is already in a game.";
    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_ERR = "message error";
    static final String OPP_USER = "opp_user";
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private final Gson gson;
    private Boolean gameOver = false;
    private Integer visited = 0;
    static Map<String, Object> modeOptionsAsJSON = new HashMap<>(2);

    private final TemplateEngine templateEngine;
    enum viewMode {PLAY, SPECTATOR,REPLAY}

    private activeColor activeTurnColor;


    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby,
                        GameManager gameManager, Gson gson) {
        // validation
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager must not be null");
        this.gson = gson;
        this.activeTurnColor = activeColor.RED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handle(Request request, Response response) {
        Session session = request.session();
        Player currentPlayer = session.attribute("Player");
        CheckersGame game = gameManager.getGame(currentPlayer);
        Map<String, Object> vm = new HashMap<>();
        if (game == null){
            game = handleNewGame(request, response, currentPlayer);
        }
        if (PostResignGame.called || game == null) {
            modeOptionsAsJSON.put("isGameOver", true);
            modeOptionsAsJSON.put("gameOverMessage", PostResignGame.resignPlayer.getName() + " has resigned.");
            response.body(gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer)));
            gameManager.removeGame(game);
            response.redirect(WebServer.HOME_URL);
            return gson.toJson(PostResignGame.resignMessage(PostResignGame.resignPlayer));
        }
        int gameID = game.getGameID();
        Player redPlayer = game.getRedPlayer();
        Player whitePlayer = game.getWhitePlayer();
        BoardView board = new BoardView(currentPlayer, game);
        vm.put(TITLE_ATTR, TITLE_ATTR_MSG);
        vm.put(GAME_ID_ATTR, gameID);
        vm.put(CURRENT_USER_ATTR, currentPlayer);
        vm.put("viewMode", viewMode.PLAY);
        if (modeOptionsAsJSON == null) {
            Map<String, Object> modeOptionsAsJSON = new HashMap<>(2);
            modeOptionsAsJSON.put("isGameOver", false);
            modeOptionsAsJSON.put(GAME_OVER_ATTR, GAME_OVER_ATTR_MSG);
            vm.put("modeOptionsAsJSON", modeOptionsAsJSON);
            vm.put(START_ATTR, START_ATTR_MSG);
        } else {
            vm.put("modeOptionsAsJSON", gson.toJson(modeOptionsAsJSON));
        }
        vm.put(RED_PLAYER_ATTR, redPlayer);
        vm.put(WHITE_PLAYER_ATTR, whitePlayer);
        activeColor color = (game.getActivePlayer().equals(redPlayer))
                ? activeColor.RED : activeColor.WHITE;
        vm.put(COLOR_ATTR, color);
        vm.put(BOARD_ATTR, board);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }

    public enum activeColor {RED, WHITE}

    private CheckersGame handleNewGame(Request request, Response response, Player currentPlayer){
        CheckersGame game = null;
        Session session = request.session();
        modeOptionsAsJSON = new HashMap<>(2);
        PostResignGame.called = false;
        if (!request.queryParams().isEmpty()){
            String opponentName = request.queryParams(OPP_USER);
            Player chosenOpponent = playerLobby.findPlayer(opponentName);
            if (chosenOpponent == null) {
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
            if (playerLobby.isInGame(chosenOpponent)){
                //we will send an error
                Message er = Message.error(PLAYER_IN_GAME);
                session.attribute(MESSAGE_ERR, er);
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
            else{
                game = gameManager.makeGame(currentPlayer, chosenOpponent);
            }}
        return game;
    }
}

