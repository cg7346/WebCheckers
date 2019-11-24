package com.webcheckers.model;

/**
 * A single "Player".
 *
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</a>
 */
public class Player {

    // name is the name of the Player
    private String name;
    private boolean inGame;
    private boolean isSpectating;


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

    /**
     * this function checks if a player is in a game
     * @return true or false
     */
    public boolean isInGame(){
        return this.inGame;
    }

    /**
     * this function sets a player in the game
     * @param inGame if the person is in a game
     */
    public void setInGame(boolean inGame){
        this.inGame = inGame;
    }

    /**
     * Getter for isSpectating
     *
     * @return true if the player is spectating, false otherwise
     */
    public boolean isSpectating() {
        return isSpectating;
    }

    /**
     * Setter for spectating
     *
     * @param bool boolean value to set isSpectating to
     */
    public void setSpectating(boolean bool) {
        this.isSpectating = bool;
    }

}
