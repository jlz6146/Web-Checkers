package com.webcheckers.model;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Piece.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@link Space} component.
 *
 * @author Eric Landers esl7511@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-Tier")
public class SpaceTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * The {@link Color}, and {@link Piece}, components are thoroughly tested so
     * we can use them safely as "friendly" dependencies.
     */
    private Space CuT;

    // friendly objects
    private Color testColor = Color.WHITE;
    private Color testColor1 = Color.RED;
    private Piece testPiece = new Single(testColor);
    private Piece nullPiece = null;
    private int cellIdx = 4;

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup() {
        CuT = new Space(cellIdx, testPiece, true);
    }

    /**
     * Test that the constructor works correctly.
     */
    @Test 
    public void test_validConstructor(){
        // checks to make sure constructor main works for valid piece
        Space CuT = new Space(cellIdx, null, true);
        assertEquals(cellIdx + Boolean.toString(true), CuT.toString());
    }

    /**
     * Test that the constructor can handle being passed an invalid piece.
     */
    @Test 
    public void test_invalidConstructor(){
        // checks to make sure constructor main works for invalid piece
        Space CuT = new Space(cellIdx, null, false);
        assertEquals(cellIdx + Boolean.toString(false), CuT.toString());
    }

    /**
     * Test that the copy constructor works correctly when flip is true.
     */
    @Test 
    public void test_ConstructorFlipTrue(){
        // checks to make sure copy constructor flips the space when the flip boolean is true
        Space space = new Space(cellIdx, testPiece, true);
        Space CuT = new Space(space, true);
        // assert the original and copied pieces are equal
        assertEquals(CuT.getPiece(), space.getPiece());
        // assert the cellIdx are flipped and equal
        assertEquals(Integer.toString(BoardView.NUM_COLS - space.getCellIdx() - 1), Integer.toString(CuT.getCellIdx()));
    }

    /**
     * Test that the copy constructor works correctly when flip is false.
     */
    @Test 
    public void test_ConstructorFlipFalse(){
        // checks to make sure copy constructor doesn't flip when the flip boolean is false
        Space space = new Space(cellIdx, testPiece, true);
        Space CuT = new Space(space, false);
        // assert the original and copied pieces are equal
        assertEquals(CuT.getPiece(), space.getPiece());
        // assert the cellIdx are flipped and equal
        assertEquals(Integer.toString(space.getCellIdx()), Integer.toString(CuT.getCellIdx()));
    }

    /**
     * Test that the copy constructor works correctly given a null piece.
     */
    @Test
    public void test_ConstructorNullPiece(){
        // checks to make sure copy constructor sets the piece equal to null when a space with a null piece is passed
        Space space = new Space(cellIdx, nullPiece, true);
        Space CuT = new Space(space, true);
        // assert the pieces are equal and null
        assertEquals(space.getPiece(), CuT.getPiece());
    }

    /**
     * Test that isValid() works correctly when there is no piece and 'true' is passed through the constructor.
     */
    @Test
    public void test_isValid(){
        // valid because there is no piece and 'true' is passed through constructor
        final Space CuT = new Space(cellIdx, null, true);
        // assert it is valid
        assertTrue(CuT.isValid());
    }


    /**
     * Test that isValid() works correctly when 'false' is passed through the constructor.
     */
    @Test
    public void test_falseValidParameter(){
        // invalid because of 'false' passed into constructor
        final Space CuT = new Space(cellIdx, null, false);
        // assert it is not valid
        assertFalse(CuT.isValid());
    }

    /**
     * Test that isValid() works correctly when there is a piece on the square.
     */
    @Test 
    public void test_pieceParameter(){
        // invalid because there is a piece on the space (passed through constructor)
        final Space CuT = new Space(cellIdx, testPiece, true);
        // assert it is not valid
        assertFalse(CuT.isValid());
    }

    /**
     * Test that setPiece() works correctly.
     */
    @Test
    public void test_setPiece(){
        // checks to make sure setPiece method is working properly
        Space CuT = new Space(cellIdx, testPiece, true);
        Piece piece = new Single(testColor1);
        // invoke
        CuT.setPiece(piece);
        // assert the two pieces are equal
        assertEquals(piece, CuT.getPiece());
    }


    /**
     * Test that equals() works correctly.
     */
    @Test
    public void test_equals(){
        assertEquals(CuT, CuT);
        assertFalse(CuT.equals("not a space"));

        Space equal = new Space(cellIdx, testPiece, true);
        assertEquals(equal, CuT);

        Space notEqual = new Space(0, null, false);
        assertNotEquals(notEqual, CuT);
    }
}
