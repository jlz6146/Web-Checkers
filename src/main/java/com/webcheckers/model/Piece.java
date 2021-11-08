package com.webcheckers.model;

import java.util.List;

/**
 * A abstract class to represent a checker piece.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public abstract class Piece {
    //Values used to hold the type and color of this checker
    private Type rank;
    private Color color;

    /**
     * An enum to represent the type of a piece (king or single).
     */
    public enum Type {
        KING,
        SINGLE
    }

    /**
     * An enum to represent the color of a piece (red or white).
     */
    public enum Color {
        RED,
        WHITE
    }

    /**
     * An abstract definition of a copy constructor to create a copy of this piece.
     *
     * @return a copy of this piece
     */
    public abstract Piece getCopy();

    /**
     * Create a new piece. Used as a super constructor by King.java and Single.java.
     *
     * @param rank the type of piece this piece should be
     * @param color the color this piece should be
     */
    public Piece(Type rank, Color color){
        this.rank = rank;
        this.color = color;
    }

    /**
     * Return what type of piece this piece is.
     *
     * @return this pieces type
     */
    public Type getType(){
        return rank;

    }

    /**
     * Return what color this piece is.
     *
     * @return this pieces color
     */
    public Color getColor(){
        return color;
    }

    /**
     * Checks if a move is a valid simple hop.
     *
     * @param move the move to be checked
     * @return whether or not the move is valid
     */
    public abstract boolean isMoveValid(Move move);

    /**
     * Checks if a move is a valid jump.
     *
     * @param move the move to be checked
     * @param jumpedSquare the square jumped over
     * @param endSpace the jump this square ends on
     * @return whether or not the move is valid
     */
    public abstract boolean isJumpValid(Move move, Space jumpedSquare, Space endSpace);

    /**
     * Returns whether or not this piece has any possible valid jumps.
     *
     * @param startRow the row this piece is on
     * @param startCell the cell this piece is on
     * @param board the current board
     * @return whether or not this piece can jump
     */
    public abstract boolean hasJump(BoardView board, int startRow, int startCell);

    /**
     * Returns whether or not this piece has any possible valid moves.
     *
     * @param startRow the row this piece is on
     * @param startCell the cell this piece is on
     * @param board the current board
     * @return whether or not this piece can move
     */
    public abstract boolean hasMove(BoardView board, int startRow, int startCell);

    /**
     * Checks if two spaces are equal.
     *
     * @param obj the object to compare with
     * @return whether they are equal or not
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Piece)) return false;
        final Piece o = (Piece) obj;
        return this.rank == o.rank && this.color == o.color;
    }

}
