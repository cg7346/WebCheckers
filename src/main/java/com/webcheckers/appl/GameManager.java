package com.webcheckers.appl;


import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetGameRoute;

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
            redPlayer.startGame();
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

        synchronized (this) {
            totalGames--;
            redPlayer.endGame(true);
            redPlayer.setInGame(false);
            whitePlayer.setInGame(false);
            try {
                System.out.println(games);
                games.remove(game);
            } catch (IndexOutOfBoundsException err) {
                System.err.println(err);
            }
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

    /**
     * Resign all currently active games with the given player in it
     * use when signing out
     *
     * @param player player who's games are being resigned
     */
    public void resignAllGames(Player player) {
        synchronized (games) {
            for (CheckersGame id : games) {
                if (id.hasPlayer(player)) {
                    String opponent = "";
                    if (id.whoseTurn(id) == GetGameRoute.activeColor.RED) {
                        if (id.getRedPlayer() == player) {
                            id.colorTurn();
                            opponent = id.getWhitePlayer().getName();
                        }
                    } else if (id.getWhitePlayer() == player) {
                        id.colorTurn();
                        opponent = id.getRedPlayer().getName();
                    }
                    id.endGame(player.getName() + " has resigned.", opponent);
                }
                id.endGame(player.getName() + " has resigned.", "");
            }
        }
    }
}
