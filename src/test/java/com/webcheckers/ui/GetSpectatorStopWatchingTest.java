package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetSpectatorStopWatchingTest {

    private static final Boolean notVisited = false;
    private static final Boolean visited = true;
    //
    // Attributes
    //
    private GameManager gameManager;
    private Request request;
    private Response response;
    private Session session;

    /**
     * The component-under-test (CuT).
     * <p>
     * This is a stateless component so we only need one.
     * The {@link GetSpectatorStopWatchingTest} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private GetSpectatorStopWatching CuT;

    @BeforeEach
    void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

        this.gameManager = mock(GameManager.class);
        assertNotNull(gameManager, "error, please check test and code");

        CuT = new GetSpectatorStopWatching(gameManager);
    }

    @Test
    void test_stopping_spectatorMode() {
        String expected = "{\"text\":\"Last turn was about 0 seconds ago.\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        } catch (Exception e) {
            //squash
        }
    }
}