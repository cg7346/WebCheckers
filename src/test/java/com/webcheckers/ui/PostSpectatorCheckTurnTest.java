package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.TimeWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * The unit test suite for the {@link PostSpectatorCheckTurn} component.
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class PostSpectatorCheckTurnTest
{

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PostSpectatorCheckTurn} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSpectatorCheckTurn CuT;

    //Mock Object
    private Request request;
    private Response response;
    private Session session;
    private Gson gson;
    private CheckersGame mockGame;


    @BeforeEach
    void construct() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        mockGame = mock(CheckersGame.class);

        gson = new Gson();

        CuT = new PostSpectatorCheckTurn(gson);
        String gameIdString = "3";
        when(request.queryParams("gameID")).thenReturn(gameIdString);
    }

    @Test
    void test_nullGame(){
        String expected = "{\"text\":\"Last turn was about 0 seconds ago.\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        }catch (Exception e){
            //squash
        }
    }

    @Test
    void test_isNotActivePlayer(){
        when(mockGame.getActivePlayer()).thenReturn(mock(Player.class));
        when(session.attribute("Player")).thenReturn(mock(Player.class));
        String expected = "{\"text\":\"Last turn was about 0 seconds ago.\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        }catch (Exception e){
            //squash
        }
    }

    @Test
    void test_timer_messgae() {
        PostCheckTurn.timer = TimeWatch.start();

        assertEquals(0, PostCheckTurn.timer.time(TimeUnit.SECONDS));
        String expected = "{\"text\":\"Last turn was about 0 seconds ago.\",\"type\":\"INFO\"}";
        try {
            Object o = CuT.handle(request, response);
            assertEquals(expected, o.toString());
        } catch (Exception e) {
            //squash
        }
    }
}