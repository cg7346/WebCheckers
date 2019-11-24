package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;

/**
 * The (@code POST /submitTurn) route handler.
 *
 * @author <a href='mailto:mmb2582@rit.edu'>Mallory Bridge</a>
 * @author <a href='mailto:jil4009@rit.edu'>Jackie Leung</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class PostSubmitTurn implements Route {

    //
    // Attributes
    //

    private GameManager gameManager;
    private final Gson gson;

    //
    // Static Variables
    //

    public static Boolean AI = false;

    //
    // Constants
    //

    static final String MESSAGE_ERR = "message error";


    //
    // Constructor
    //

    /**
     * Create the UI controller to handle all {@code POST /submitTurn}
     * HTTP requests.
     *
     * @param gameManager handles all the checker games
     * @param gson the gson
     */
    public PostSubmitTurn(GameManager gameManager, Gson gson){
        this.gameManager = Objects.requireNonNull(gameManager,
                "game manager is required");
        this.gson = Objects.requireNonNull(gson,
                "gson is required");
    }

//    /**
//     * Checks to see if all the pieces are blocked or captured
//     *
//     * @param count     the number of pieces left on the board for a player
//     * @param winOrlose whether they have either won or lost
//     * @return an end of game message
//     */
//    public String BlockedOrCaptured(Integer count, String winOrlose){
//        String endGame = null;
//        // If the count is greater than zero then end game is blocked
//        if (count > 0) {
//            endGame = " has blocked all pieces, " + winOrlose;
//        // If the count is zero then end game is captured
//        } else {
//            endGame = " has captured all pieces, " + winOrlose;
//        }
//        return endGame;
//    }

    /**
     * Ends a game when the AI is playing
     * @param game the game being played
     * @param message a message if there's an error
     * @param session the current session
     */
    public void AIEndGame(CheckersGame game, String message, Session session){
        // If the game is AI, then remove the game and show the error message
        if (AI) {
            gameManager.removeGame(game);
            Message er = Message.error(message);
            session.attribute(MESSAGE_ERR, er);
        }
    }

    /**
     * Where the game over message is constructed
     *
     * @param moveValidator the moves possible array
     * @param game the game the players in
     * @param session the current session
     * @param win true is won, else wise false
     * @return the response message for gson
     *          The game has ended in a tie.
     *          {player name} pieces have been captured
     *          {player name} has no moves left.
     */
    public Message gameOver(MoveValidator moveValidator, CheckersGame game,
                            Session session, Boolean win){
        Message responseMessage = null;
        String end;
        // Whether or not the player won or lost
        if(win) {
            end = "you won!";
        } else {
            end = "you lost.";
        }
        // Tie condition when stalemate is reached
        if (moveValidator.isOut(moveValidator.REDPLAYER) &&
                moveValidator.isOut(moveValidator.WHITEPLAYER)){
            game.setTie(true);
            responseMessage = Message.info("The game has ended in a tie.");
            AIEndGame(game, "The game has ended in a tie.", session);
        // White wins when red is out of moves
        } else if (moveValidator.isOut(moveValidator.REDPLAYER) &&
                !moveValidator.isOut(moveValidator.WHITEPLAYER)) {
            // Sets the winner
            game.setWinner(game.getWhitePlayer());
            // Game ended by being blocked or captured
            String endGame = PostCheckTurn.BlockedOrCaptured(
                    moveValidator.getCount(moveValidator.REDPLAYER), end);
            // Created the end game message
            String message = game.getWhitePlayer().getName() + endGame;
            responseMessage = Message.info(message);
            // Set mode option to game over
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
            // If the game is an AI game, then do the end game for AI
            AIEndGame(game, message, session);

        // Red wins when white is out of moves
        } else if (!moveValidator.isOut(moveValidator.REDPLAYER)
                && moveValidator.isOut(moveValidator.WHITEPLAYER)) {
            // Sets the winner
            game.setWinner(game.getRedPlayer());
            // Game ended by being blocked or captured
            String endGame = PostCheckTurn.BlockedOrCaptured(
                    moveValidator.getCount(moveValidator.WHITEPLAYER), end);
            // Creates the end game message
            String message = game.getRedPlayer().getName() + endGame;
            responseMessage = Message.info(message);
            // Sets mode options to game over
            GetGameRoute.modeOptionsAsJSON.put("isGameOver", true);
            GetGameRoute.modeOptionsAsJSON.put("gameOverMessage", message);
            // If the game is an AI game, then do the end game for AI
            AIEndGame(game, message, session);
        }
        return responseMessage;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     * @return updates gson with message if you need to
     *          make a move first
     *          the game is over
     *          jump available
     *          valid move
     */
    @Override
    public Object handle(Request request, Response response) {
        // Get the session
        Session session = request.session();
        // Get the player
        Player player = session.attribute("Player");
        // Get the game
        CheckersGame game = gameManager.getGame(player);
        // Create a move validator
        MoveValidator moveValidator = new MoveValidator(game);
        // If the white player of the game is AI, then set AI to true
        if (game.getWhitePlayer().getName().equals("AI")) {
            AI = true;
        }
        // Look for moves
        moveValidator.lookForMoves();
        // Create a game over message
        Message responseMessage = gameOver(moveValidator, game, session, true);
        // If message is null, then the game is not over
        if( responseMessage == null) {
            // Get the last move
            Move lastMove = game.getLastMove();
            // If the player didn't make a move, then tell them to make a move
            if (lastMove == null) {
                responseMessage = Message.error("Make move first");
            // Otherwise, vaildate their move for existing jump move
            // and complete their turn
            } else {
                if (lastMove.hasPiece()) {
                    moveValidator.clearArrays();
                    lastMove = game.moveConverter(lastMove);
                    moveValidator.lookInSpace(lastMove.getEnd().getRow(),
                            lastMove.getEnd().getCol());
                    // Checks for jump moves
                    if (moveValidator.areThereJumpMoves()) {
                        responseMessage = Message.error(moveValidator.jumpAvail);
                    // Complete their turn
                    } else {
                        game.completeTurn();
                        responseMessage = Message.info(moveValidator.validMove);
                    }
                // Complete their turn
                } else {
                    game.completeTurn();
                    responseMessage = Message.info(moveValidator.validMove);
                }
                // If AI game, then AI makes a turn
                if (AI) {
                    // AI makes a turn
                    AI ai = new AI(game, moveValidator);
                    Move aiMove = ai.makeTurn();
                    game.makeMove(aiMove);
                    // Checks are more jump moves in order to do a multi-jump
                    // move
                    while (moveValidator.areThereJumpMoves()) {
                        moveValidator.lookForMoves();
                        // For all jump moves available, look for a jump that
                        // corresponds to the previous jump move
                        for (Move jump : moveValidator.getJumpMoves(
                                moveValidator.WHITEPLAYER)) {
                            if (jump.getStart().toString().equals(
                                    aiMove.getEnd().toString())) {
                                game.makeMove(jump);
                                aiMove = jump;
                                break;
                            }
                        }
                    }
                    // End AI turn
                    game.completeTurn();
                    // Start the spectator timer
                    PostCheckTurn.timer = TimeWatch.start();
                    // Look for moves
                    moveValidator.lookForMoves();
                    // If game over, then make the respone mesage game over
                    if (gameOver(moveValidator, game, session,
                            false) != null) {
                        responseMessage = gameOver(moveValidator, game,
                                session, false);
                    }
                }
            }
        }
        // Set AI to false;
        AI = false;
        response.body(gson.toJson(responseMessage));
        return gson.toJson(responseMessage);
    }
}
