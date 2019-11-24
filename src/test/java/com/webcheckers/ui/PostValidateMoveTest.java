package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.Space;
import spark.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostValidateMoveTest {

    private PostValidateMove CuT;

    //Mock Object
    private Request request;
    private Response response;
    private Session session;
    private Gson gson;
    private GameManager gameManager;
    private PlayerLobby lobby;
    private CheckersGame realGame;
    private Player player;
    private Player player2;
    private Player AI;

    @BeforeEach
    void construct(){
        request = mock(Request.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        session = mock(Session.class);
        when(session.attribute("Player")).thenReturn(player);
        gson = new Gson();
        lobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        player = mock(Player.class);
        player2 = mock(Player.class);
        realGame = new CheckersGame(player, player2, "3");
        when(gameManager.getGame(player)).thenReturn(realGame);

        CuT = new PostValidateMove(gameManager, gson);
    }

    @Test
    void valid_move(){
        HashSet<String> testHash = new HashSet<>();
        testHash.add("Test");
        String actionData = "{\"start\":{\"row\":5,\"cell\":2},\"end\":{\"row\":4,\"cell\":1}}";
        testHash.add(actionData);
        when(request.queryParams()).thenReturn(testHash);
        when(request.queryParams("actionData")).thenReturn(actionData);

        String expected = "{\"text\":\"Valid Move!\",\"type\":\"INFO\"}";
        try{
            assertEquals(expected, CuT.handle(request, response));
        }catch (Exception e){
            //squash
        }
    }

    @Test
    void invalid_move(){
        HashSet<String> testHash = new HashSet<>();
        testHash.add("Test");
        String actionData = "{\"start\":{\"row\":1,\"cell\":2},\"end\":{\"row\":4,\"cell\":1}}";
        testHash.add(actionData);
        when(request.queryParams()).thenReturn(testHash);
        when(request.queryParams("actionData")).thenReturn(actionData);
        String expected = "{\"text\":\"INVALID MOOOVE!\",\"type\":\"ERROR\"}";
        try{
            assertEquals(expected, CuT.handle(request, response));
        }catch (Exception e){
            //squash
        }
    }

}
