package com.webcheckers.model;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit test suite for the {@link Piece} component.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-tier")
public class PieceTest {
    /**
     * The component-under-test (CuT).
     */
    private Piece single = new Single(Piece.Color.RED);
    private Piece king = new King(Piece.Color.WHITE);

    /**
     * Test that getType() works correctly.
     */
    @Test
    public void test_getType() {
        assertEquals(Piece.Type.SINGLE, single.getType());
        assertEquals(Piece.Type.KING, king.getType());
    }

    /**
     * Test that getColor() works correctly.
     */
    @Test
    public void test_getColor() {
        assertEquals(Piece.Color.RED, single.getColor());
        assertEquals(Piece.Color.WHITE, king.getColor());
    }

    /**
     * Test that equals() works correctly.
     */
    @Test
    public void test_equals() {
        assertEquals(single, single);
        assertFalse(single.equals("not a piece"));

        Piece equalSingle = new Single(Piece.Color.RED);
        assertEquals(equalSingle, single);

        Piece equalKing = new King(Piece.Color.WHITE);
        assertEquals(equalKing, king);

        Piece notEqual = new Single(Piece.Color.WHITE);
        assertNotEquals(notEqual, single);
        assertNotEquals(notEqual, king);
    }
}
