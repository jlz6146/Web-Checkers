package com.webcheckers.model;

/**
 * A class to represent Position object.
 *
 * @author Eric Landers esl7511@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class Position{
    //Values used to hold the coordinates of this Position
    private int row;
    private int cell;

    /**
     * Creates a new position
     *
     * @param row the row location of the position
     * @param cell the cell the position occupies
     */
    public Position(int row, int cell){
        this.row = row;
        this.cell = cell;
    }

    /**
     * Returns the cell of a position
     *
     * @return cell
     */
    public int getCell(){
        return cell;
    }

    /**
     * Returns the row of a position
     *
     * @return row
     */
    public int getRow(){
        return row;
    }

    /**
     * Inverts a position by creating a new position with an inverted row and cell
     *
     * @return a new inverted Position
     */
    public Position inverse() {
        return new Position(BoardView.NUM_ROWS - row - 1, BoardView.NUM_COLS - cell - 1);
    }

    /**
     * Determines if a position is valid
     * and has a row and cell value within the bounds of the board
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        boolean validRow = (row >= 0) && (row < BoardView.NUM_ROWS);
        boolean validCell = (cell >= 0) && (cell < BoardView.NUM_COLS);
        return validRow && validCell;
    }

    /**
     * Checks if two positions are equal.
     *
     * @param obj the object to compare with
     * @return whether or not they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Position)) return false;
        final Position o = (Position) obj;
        return this.row ==o.row && this.cell ==o.cell;
    }
}