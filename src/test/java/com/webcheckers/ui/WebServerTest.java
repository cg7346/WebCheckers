package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.TemplateEngine;

import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.mock;
import java.util.Objects;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Just to make sure it doesn't shit the bed for some reason
 *  and that good good coverage %
 */
public class WebServerTest {

    private WebServer CuT;
    private TemplateEngine engine;
    private Gson gson;
    private GameManager gameManager;
    private PlayerLobby playerLobby;

    @BeforeEach
    void construct(){
        engine = mock(TemplateEngine.class);
        gson = new Gson();
        gameManager = mock(GameManager.class);
        playerLobby = mock(PlayerLobby.class);
        CuT = new WebServer(engine, gson, gameManager, playerLobby);
    }

    @Test
    void test_intialize(){
        assertDoesNotThrow(() -> CuT.initialize());
    }

}
