package com.webcheckers.model;

/**
 * A class to represent a space on the checkerboard.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class Space {
    //Values used to hold index this space is at, whether this space is valid, and if it contains a piece
    private int cellIdx;
    private boolean valid;
    private Piece piece;

    /**
     * Create a new Space.
     *
     * @param cellIdx the index of this space in its row
     * @param piece the piece on this square, if there is one
     * @param valid if this square is a valid square to move to
     */
    public Space(int cellIdx, Piece piece, boolean valid) {
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.valid = valid;
    }

    /**
     * A copy constructor to create a copy of this space.
     *
     * @param space the space to be copied
     */
    public Space(Space space, boolean flip) {
        if(flip) {
            this.cellIdx = BoardView.NUM_COLS - space.getCellIdx() - 1;
        } else {
            this.cellIdx = space.getCellIdx();
        }
        if(space.getPiece() != null) {
            this.piece = space.getPiece().getCopy();
        } else {
            this.piece = null;
        }
        this.valid = space.isValid();
    }

    /**
     * Return the index of this space in its row.
     *
     * @return The index of this space within the row.
     */
    public int getCellIdx(){
        return cellIdx;
    }

    /**
     * Checks whether this space is a valid space to move a piece to.
     *
     * @return whether or not a piece can be put on this square
     */
    public boolean isValid(){
        if(!this.valid){
            return false;
        } else {
            return this.piece == null;
        }
    }

    /**
     * Return the piece on this square, if there is one.
     *
     * @return the piece if there is a piece, otherwise null.
     */
    public Piece getPiece(){
        return piece;
    }

    /**
     * Set the piece on this space.
     *
     * @param piece the piece to be on this space
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Return the string representation of this space.
     *
     * @return the string representation of this space
     */
    @Override
    public String toString(){
        return cellIdx + Boolean.toString(valid);
    }

    /**
     * Checks if two spaces are equal.
     *
     * @param obj the object to compare with
     * @return whether or not they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Space)) return false;
        final Space o = (Space) obj;
        boolean samePiece;
        if(this.piece == null || o.piece == null) {
            samePiece = this.piece == o.piece;
        } else {
            samePiece = this.piece.equals(o.piece);
        }
        return this.cellIdx == o.cellIdx && this.valid == o.valid && samePiece;
    }
}
