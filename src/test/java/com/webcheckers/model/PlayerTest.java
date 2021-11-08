package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link Player} component
 *
 * @author Jess Zhao jlz6146@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-tier")
public class PlayerTest {
    // friendly objects
    private String NAME = "Test Name";

    /**
     * The component-under-test (CuT).
     */
    private Player CuT = new Player(NAME);

    /**
     * Ensures getName() method works
     */
    @Test
    public void test_getName(){
        assertEquals(NAME, CuT.getName());
    }

    /**
     * Ensures that the player's color is set correctly, and returned correctly
     * by setColor() and getColor() methods
     */
    @Test
    public void test_color(){
        CuT.setColor(Piece.Color.RED);
        assertEquals(Piece.Color.RED, CuT.getColor());
    }

    /**
     * Ensures equals() method works properly in discerning two players
     */
    @Test
    public void test_equals(){
        Player SAME_NAME = new Player(NAME);
        Player DIFF_NAME = new Player("Nope");

        assertEquals(CuT, CuT);
        assertNotEquals(CuT, "not a player object");
        assertEquals(SAME_NAME, CuT);
        assertNotEquals(DIFF_NAME, CuT);
    }

    /**
     * Ensures hashCode() method works properly in generating hashCode for a player
     */
    @Test
    public void test_hashcode(){
        assertEquals(NAME.hashCode(), CuT.hashCode());
    }

    /**
     * Tests toString() method
     */
    @Test
    public void test_toString() {
        CuT.setColor(Piece.Color.RED);
        String expected = NAME;
        assertEquals(expected, CuT.toString());
    }
}
