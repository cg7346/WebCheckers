package com.webcheckers.ui;

import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetGameRoute implements Route {

    static final String VIEW_NAME = "game.ftl";
    static Player redPlayer;
    static Player whitePlayer;
    static Player currentUser;
    static BoardView board;
    static final String MESSAGE = "A game has been started";
    //TODO static (SOMETHING) viewMode;
    //TODO static (SOMETHING) activeColor;
    //static (SOMETHING) modeOptionsAsJSON; (Does not need to be done for Sprint 1)

    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(final TemplateEngine templateEngine)
    {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
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
        //vm.put("viewMode", viewMode);
        //vm.put("modeOptionsAsJSON", modeOptionsAsJSON);
        vm.put("redPlayer", redPlayer);
        vm.put("whitePlayer", whitePlayer);
        //vm.put("activeColor", activeColor);
        vm.put("board", board);
        vm.put("message", MESSAGE);


        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
