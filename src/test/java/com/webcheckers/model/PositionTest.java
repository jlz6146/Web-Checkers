package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link Position} component
 *
 * @author Jess Zhao jlz6146@rit.edu
 */
@Tag("Model-tier")
public class PositionTest {
    /**
     * The component-under-test (CuT).
     */
    private Position CuT;

    // friendly objects
    private int ROW = 0;
    private int CELL = 1;

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup(){
        CuT = new Position(ROW, CELL);

    }

    /**
     * Test for getRow() and getCell() functions
     */
    @Test
    public void test_values(){
        assertEquals(ROW, CuT.getRow());
        assertEquals(CELL, CuT.getCell());
    }

    /**
     * Ensures inverse() method works properly in creating a new inverted position
     */
    @Test
    public void test_inverse(){
        Position inverse = new Position(BoardView.NUM_ROWS - ROW - 1, BoardView.NUM_COLS - CELL - 1);
        assertEquals(inverse.getRow(), CuT.inverse().getRow());
        assertEquals(inverse.getCell(), CuT.inverse().getCell());
    }

    /**
     * Ensures isValid() method works properly in determining if a position is valid
     */
    @Test
    public void test_isValid(){
        Position invalidRow = new Position(9, CELL);
        Position invalidCell = new Position(ROW, 9);

        assertFalse(invalidRow.isValid());
        assertFalse(invalidCell.isValid());

        invalidRow = new Position(-1, CELL);
        invalidCell = new Position(ROW, -1);

        assertFalse(invalidRow.isValid());
        assertFalse(invalidCell.isValid());

        assertTrue(CuT.isValid());
    }
}
