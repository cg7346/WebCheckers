package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostBackUpRouteTest {

    private PostBackupRoute CuT;
    private Gson gson;
    private GameManager gm;
    private Request request;
    private Response response;
    private Session session;

    @BeforeEach
    void creation(){
        gm = mock(GameManager.class);
        gson = new Gson();
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        CuT = new PostBackupRoute(gm, gson);
    }

    @Test
    void test_handle(){
//      CheckersGame mockGame = mock(CheckersGame.class);
//        MoveValidator mockMV = mock(MoveValidator.class);
//        when(mock)
        Player mockPlayer = mock(Player.class);
        Player mockP2 = mock(Player.class);
        CheckersGame game = new CheckersGame(mockPlayer, mockP2, "1");
        when(session.attribute("Player"))
                .thenReturn(mockPlayer);
        when(gm.getGame(mockPlayer)).thenReturn(game);

        Message expected = Message.info("BACK IT UP!");
        //assertEquals(expected, CuT.handle(request, response));
    }
}
