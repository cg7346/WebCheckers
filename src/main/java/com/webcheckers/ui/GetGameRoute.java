package com.webcheckers.ui;

import com.google.gson.Gson;
import com.sun.tools.javac.comp.Check;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetGameRoute implements Route {

    static final String VIEW_NAME = "game.ftl";

    //TODO CHANGE ALL THIS HARD CODED STUFF
    static Player redPlayer = new Player("Red", new ArrayList<>(), true);
    static Player whitePlayer = new Player("White", new ArrayList<>(), true);
    static Player currentUser = redPlayer;
    static BoardView board = new BoardView(currentUser, new CheckersGame(redPlayer, whitePlayer, 0));
    static final Message MESSAGE = Message.info("A game has been started");
    //TODO static (SOMETHING) viewMode;
    static enum viewMode {PLAY, SPECTATOR,REPLAY}
    //TODO static (SOMETHING) activeColor;
    static enum activeColor {RED, WHITE}

    static CheckersGame game;

    private final Gson gson = new Gson();
    //static (SOMETHING) modeOptionsAsJSON; (Does not need to be done for Sprint 1)

    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(CheckersGame game, final TemplateEngine templateEngine)
    {
        // validation
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.game = game;
        this.redPlayer = game.getRedPlayer();
        this.whitePlayer = game.getWhitePlayer();
        //this.currentUser =
        this.board = new BoardView(this.currentUser, game);

        //this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handle(Request request, Response response)
    {

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
