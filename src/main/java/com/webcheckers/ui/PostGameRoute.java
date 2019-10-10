package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import spark.TemplateEngine;

import java.util.Objects;


/**
 * Post game route
 * Authors: Jacquelyn Leung and Mallory Bridge (Pair programming)
 */
public class PostGameRoute {
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


}
