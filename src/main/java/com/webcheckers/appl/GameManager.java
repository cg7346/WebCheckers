package com.webcheckers.appl;


import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle all the checker games
 * It makes checkers games and assigns players to them
 */
public class GameManager {

    //total games (will be the game id)
    int totalGames = 0;
    private final List<CheckersGame> games = new ArrayList<>();

    /**
     * Makes a new game for players to play
     *
     * A POST method should define who is redPlayer (the clicker) and
     * whitePlayer (the player clicked on)
     * @param redPlayer
     * @param whitePlayer
     * @return
     */
    public CheckersGame makeGame(Player redPlayer, Player whitePlayer){

        synchronized (this){
            totalGames++;
            if (!redPlayer.isInGame() && !whitePlayer.isInGame()){
                CheckersGame game = new CheckersGame(redPlayer, whitePlayer, totalGames);
                redPlayer.setInGame(true);
                whitePlayer.setInGame(true);
                games.add(game);
                return game;
            }
            else
                return null;
        }
    }

    /**
     * Removes game ID for players to end game
     * <p>
     * A POST method should define who is redPlayer (the clicker) and
     * whitePlayer (the player clicked on)
     *
     * @param redPlayer
     * @param whitePlayer
     * @return
     */
    public CheckersGame removeGame(CheckersGame game, Player redPlayer, Player whitePlayer) {

        synchronized (game) {
            totalGames--;
            try {
                games.remove(game);
            } catch (IndexOutOfBoundsException err) {
                System.err.println(err);
            }
            redPlayer.setInGame(false);
            whitePlayer.setInGame(false);
            return null;
        }
    }


    /**
     * Gets a game that the sessionPlayer is in
     * @param sessionPlayer the Player looking at the screen
     * @return the game if the player has one, null if not
     */
    public CheckersGame getGame(Player sessionPlayer){
        for (CheckersGame game : games){
            if (isPlayerInGame(game, sessionPlayer)){
                return game;
            }
        }
        return  null;
    }

    public CheckersGame getGame(int gameID){
        for (CheckersGame game : games){
            if (game.getGameID() == gameID){
                return game;
            }
        }
        return null;
    }


    /**
     * Just checking to see if the Player is in a particular
     * game of checkers
     * @param game the game to look in
     * @param player the player to look for
     * @return true if in, false if not
     */
    public synchronized boolean isPlayerInGame(CheckersGame game, Player player) {
        return player.equals(game.getRedPlayer()) || player.equals(game.getWhitePlayer());
    }

//    /**
//     * Resign currently active game with the given player in it
//     * use when signing out
//     *
//     * @param player player who's games are being resigned
//     */
//    public void resignGame(Player player) {
//        synchronized (games) {
//            for (CheckersGame id : games) {
//                if (id.hasPlayer(player)) {
//                    if (id.whoseTurn(id) == GetGameRoute.activeColor.RED) {
//                        if (id.getRedPlayer() == player) {
//                            id.colorTurn();
//                        }
//                    } else if (id.getWhitePlayer() == player) {
//                        id.colorTurn();
//                    }
//                    id.endGame(player.getName() + " has resigned.");
//                }
//                id.endGame(player.getName() + " has resigned.");
//            }
//        }
//    }
}
