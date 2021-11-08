package com.webcheckers.model;

/**
 * Player class: holds all player information.
 *
 * @author Eric Landers esl7511@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class Player{
    //The name and color of the player
    private String name;
    private Piece.Color color;

    /**
     * Return the color of pieces this player is using.
     *
     * @return the color of pieces this player is using
     */
    public Piece.Color getColor() {
        return color;
    }

    /**
     * Set this player's piece color.
     *
     * @param color the color this player should be using
     */
    public void setColor(Piece.Color color) {
        this.color = color;
    }

    /**
     * Set this player's name.
     *
     * @param name this player's new name
     */
    public Player(String name){
        this.name = name;
    }

    /**
     * Return this player's name.
     *
     * @return this player's name
     */
    public String getName(){
        return name;
    }


    /**
     * Check if this player is equal to the object passed in.
     *
     * @param object the object this player is being compared to
     * @return whether or not they are equal
     */
    @Override
    public boolean equals(Object object){
        if(object == this){
            return true;
        }
        if(!(object instanceof Player)){
            return false;
        }
        final Player that = (Player) object;
        return this.name.equals(that.name);
    }

    /**
     * Return the hashcode of this player.
     *
     * @return this player's hashcode
     */
    @Override
    public int hashCode(){
        return name.hashCode();
    }

    /**
     * Return the string representation of the Player.
     *
     * @return the string representation of the Player
     */
    @Override
    public String toString(){
        return name;
    }
}
