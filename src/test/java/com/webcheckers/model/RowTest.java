package com.webcheckers.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit test suite for the {@link Row} component.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-tier")
public class RowTest {
    /**
     * The component-under-test (CuT).
     */
    private Row CuT;

    // friendly objects
    private List<Space> spaces;
    private int index = 7;

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup() {
        spaces = new LinkedList<>();
        for (int i = 0; i < BoardView.NUM_COLS; i++) {
            spaces.add(new Space(i, null, (i % 2 == 0)));
        }
        // create a unique CuT for each test
        CuT = new Row(index, spaces);
    }

    /**
     * Test that getIndex() works correctly.
     */
    @Test
    public void test_getIndex() {
        assertEquals(index, CuT.getIndex());
    }

    /**
     * Test that iterator() works correctly.
     */
    @Test
    public void test_iterator() {
        Iterator<Space> expected = spaces.iterator();
        Iterator<Space> actual = CuT.iterator();

        for (int i = 0; i < BoardView.NUM_COLS; i++) {
            assertEquals(expected.next(), actual.next());
        }
    }

    /**
     * Test that equals() works correctly.
     */
    @Test
    public void test_equals() {
        assertEquals(CuT, CuT);
        assertFalse(CuT.equals("not a Row"));

        // Case: equal
        Row equal = new Row(index, spaces);
        assertEquals(equal, CuT);

        // Case: different index
        Row differentIndex = new Row(index+1, spaces);
        assertNotEquals(differentIndex, CuT);

        List<Space> newSpaces = new LinkedList<>();
        for (int i = 0; i < BoardView.NUM_COLS; i++) {
            newSpaces.add(new Space(i, null, (i % 2 == 1)));
        }

        // Case: different spaces
        Row differentSpaces = new Row(index+1, newSpaces);
        assertNotEquals(differentSpaces, CuT);
    }
}
