package com.webcheckers.model;

import java.util.Iterator;
import java.util.List;

import jdk.jfr.BooleanFlag;

/**
 * A class to represent a row of the checkerboard.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class Row implements Iterable<Space> {
    //Values used to hold the index this row is at, and squares in this row
    private int index;
    private List<Space> spaces;

    /**
     * Create a new Row.
     *
     * @param index this row's index
     * @param spaces this row's list of spaces
     */
    public Row(int index, List<Space> spaces) {
        this.index = index;
        this.spaces = spaces;
    }

    /**
     * Getter for the index of the row within he board.
     *
     * @return The index of this row within the board.
     */
    public int getIndex(){
        return index;
    }

    /**
     * Creates a Java Iterator of the Spaces within a single row.
     *
     * @return Java Iterator of the Spaces within a single row.
     */
    public Iterator<Space> iterator(){
        return spaces.iterator();
    }

    /**
     * Checks if two rows are equal.
     *
     * @param obj the object to compare with
     * @return whether or not they are equal
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(!(obj instanceof Row)) return false;
        final Row o = (Row) obj;
        return this.index == o.index && this.spaces.equals(o.spaces);
    }

}
