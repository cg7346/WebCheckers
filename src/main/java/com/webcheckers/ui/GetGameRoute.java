package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The UI controller to GET the Game page
 *
 * @author <a href='mailto:       @rit.edu'>    </a>
 * @author <a href='mailto:       @rit.edu'>    </a>
 * @author <a href='mailto:amf7619@rit.edu'>Anthony Ferraioli</a>
 */
public class GetGameRoute implements Route {

    static final String VIEW_NAME = "game.ftl";
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private final Gson gson;

    //TODO CHANGE ALL THIS HARD CODED STUFF
    //static Player redPlayer = new Player("Red");
    //static Player whitePlayer = new Player("White");
    //static Player currentUser = redPlayer;
    //static BoardView board = new BoardView(currentUser, new CheckersGame(redPlayer, whitePlayer, 0));
    static final Message MESSAGE = Message.info("A game has been started");
    //TODO static (SOMETHING) viewMode;
    static enum viewMode {PLAY, SPECTATOR,REPLAY}
    //TODO static (SOMETHING) activeColor;
    static enum activeColor {RED, WHITE}

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
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
        this.gson = gson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handle(Request request, Response response) {
        Session session = request.session();
        Player currentUser = session.attribute("Player");

        CheckersGame game = gameManager.getGame(currentUser);
        Player redPlayer = game.getRedPlayer();
        Player whitePlayer = game.getWhitePlayer();
        BoardView board = new BoardView(currentUser, game);

        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "GameTitle");
        vm.put("currentUser", currentUser);
        vm.put("viewMode", viewMode.PLAY);

        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", false);
        modeOptions.put("gameOverMessage", "/* get end of game message */");
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        //vm.put("modeOptionsAsJSON", modeOptionsAsJSON);
        vm.put("redPlayer", redPlayer);
        vm.put("whitePlayer", whitePlayer);
        vm.put("activeColor", activeColor.RED);
        vm.put("board", board);
        vm.put("message", MESSAGE);


        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
