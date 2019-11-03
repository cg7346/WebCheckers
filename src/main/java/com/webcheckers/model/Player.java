package com.webcheckers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A single "Player".
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class Player {

    // name is the name of the Player
    private String name;
    private boolean inGame;
    private List<Player> currentOpponents = new ArrayList<>();
    private int activeGames;
    private double totalGames;


    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the Player's name
     *
     * @return
     *      the name of the Player as a String
     */
    public String getName(){
        return this.name;
    }

    public boolean isInGame(){
        return this.inGame;
    }

    public void setInGame(boolean inGame){
        this.inGame = inGame;
    }

    public List<Player> getCurrentOpponents() {
        return currentOpponents;
    }

    public void addOponent(Player opponent) {
        currentOpponents.add(opponent);
    }

    public void removeOpponent(Player opponent) {
        currentOpponents.remove(opponent);
    }

    /**
     * Set inGame to true
     */
    public void startGame() {
        activeGames++;
        totalGames++;
    }

    public void endGame(boolean winner) {
        activeGames--;
    }
}
