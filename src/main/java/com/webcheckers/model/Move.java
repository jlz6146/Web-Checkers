package com.webcheckers.model;

/**
 * A class to represent Move object.
 *
 * @author Eric Landers esl7511@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class Move{
    //The starting and ending position of a Move
    private Position start;
    private Position end;

    /**
     * Create a new move.
     *
     * @param start the starting position of the move
     * @param end the ending position of the move
     */
    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the starting position of a Move
     *
     * @return start
     */
    public Position getStart(){
        return start;
    }

    /**
     * Returns the ending position of a Move
     *
     * @return end
     */
    public Position getEnd(){
        return end;
    }

    /**
     * Check if this move is a simple move.
     *
     * @return whether or not the move is a simple move
     */
    public boolean isSimpleMove() {
        return (Math.abs(start.getRow() - end.getRow()) == 1) && (Math.abs(start.getCell() - end.getCell()) == 1);
    }

    /**
     * Returns if a Move can be considered a jump
     *
     * @return true if a Move is a jump, false otherwise
     */
    public boolean isJump() {
        return (Math.abs(start.getRow() - end.getRow()) == 2) && (Math.abs(start.getCell() - end.getCell()) == 2);
    }

    /**
     * Inverts a Move
     *
     * @return an inverted version of a Move
     */
    public Move inverse() {
        return new Move(start.inverse(), end.inverse());
    }

    /**
     * Checks if two moves are equal.
     *
     * @param obj the object to compare with
     * @return whether or not they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Move)) return false;
        final Move o = (Move) obj;
        return this.start.equals(o.start) && this.end.equals(o.end);
    }
}