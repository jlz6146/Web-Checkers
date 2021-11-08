package com.webcheckers.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit test suite for the {@link Single} component.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-tier")
public class SingleTest {
    /**
     * The component-under-test (CuT).
     */
    private Single CuT;

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup() {
        // create a unique CuT for each test
        CuT = new Single(Piece.Color.RED);
    }

    /**
     * Test that the constructor works correctly.
     */
    @Test
    public void test_constructor() {
        // checks to make sure the constructor works for a red piece
        CuT = new Single(Piece.Color.RED);
        assertEquals(Piece.Color.RED, CuT.getColor());
        assertEquals(Piece.Type.SINGLE, CuT.getType());

        // checks to make sure the constructor works for a white piece
        CuT = new Single(Piece.Color.WHITE);
        assertEquals(Piece.Color.WHITE, CuT.getColor());
        assertEquals(Piece.Type.SINGLE, CuT.getType());
    }

    /**
     * Test that getCopy() works correctly.
     */
    @Test
    public void test_getCopy() {
        Piece copy = CuT.getCopy();
        Piece piece = CuT;

        //Assert that they are not the same object
        assertNotSame(copy, piece);
        //Asset they hold the same information
        assertEquals(Piece.Color.RED, copy.getColor());
        assertEquals(Piece.Type.SINGLE, copy.getType());
    }

    /**
     * Test that isMoveValid() works correctly.
     */
    @Test
    public void test_isMoveValid() {
        Move valid = new Move(new Position(5, 2), new Position(4, 3));
        Move invalid = new Move(new Position(8, 8), new Position(9, 9));

        assertTrue(CuT.isMoveValid(valid));
        assertFalse(CuT.isMoveValid(invalid));
    }

    /**
     * Test that isJumpValid() works correctly.
     */
    @Test
    public void test_isJumpValid() {
        Move validJump = new Move(new Position(7, 0), new Position(5, 2));
        Move notAJump = new Move(new Position(7, 0), new Position(6, 1));
        Move invalidJump = new Move(new Position(5, 0), new Position(7, 2));
        Space validEnd = new Space(2, null, true);
        Space invalidEnd = new Space(2, new Single(Piece.Color.RED), true);
        Space nullJumpedPieceSpace = new Space(0, null, true);
        Space sameColorJumpedPieceSpace = new Space(0, new Single(Piece.Color.RED), true);
        Space validJumpedPieceSpace = new Space(0, new Single(Piece.Color.WHITE), true);

        //Case: no piece was jumped over
        assertFalse(CuT.isJumpValid(validJump, nullJumpedPieceSpace, validEnd));

        //Case: the piece jumped over is the same color as the piece that jumped
        assertFalse(CuT.isJumpValid(validJump, sameColorJumpedPieceSpace, validEnd));

        //Case: move passed in is not a jump
        assertFalse(CuT.isJumpValid(notAJump, validJumpedPieceSpace, validEnd));

        //Case: move passed in is not a valid jump
        assertFalse(CuT.isJumpValid(invalidJump, validJumpedPieceSpace, validEnd));

        //Case: the end square is occupied
        assertFalse(CuT.isJumpValid(validJump, validJumpedPieceSpace, invalidEnd));

        //Case: valid jump
        assertTrue(CuT.isJumpValid(validJump, validJumpedPieceSpace, validEnd));
    }

    /**
     * Test that hasJump() works correctly.
     */
    @Test
    public void test_hasJump() {
        Player red = new Player("red");
        Player white = new Player("white");
        BoardView board = new BoardView(red, white);

        //Case: the board is in its initial state
        assertFalse(CuT.hasJump(board, 5, 0));

        // Set up valid jump to the right of the single piece
        board.makeMove(new Move(new Position(5, 0), new Position(4, 1)));
        board.makeMove((new Move(new Position(5, 4), new Position(4, 5))).inverse());

        //Case: there is a jump to the right of the single piece
        assertTrue(CuT.hasJump(board, 4, 1));


        // Set up valid jump to the left of the single piece
        board = new BoardView(red, white);
        board.makeMove(new Move(new Position(5, 2), new Position(4, 3)));
        board.makeMove((new Move(new Position(5, 6), new Position(4, 5))).inverse());

        //Case: there is a jump to the left of the single piece
        assertTrue(CuT.hasJump(board, 4, 3));
    }

    /**
     * Test that hasMove() works correctly.
     */
    @Test
    public void test_hasMove() {
        Player red = new Player("red");
        Player white = new Player("white");
        BoardView board = new BoardView(red, white);

        //Case: the board is in its initial state
        assertTrue(CuT.hasMove(board, 5, 0));

        //Case: no valid moves exist
        Space[][] currentBoard = board.getBoard();
        currentBoard[7][2].setPiece(new Single(Piece.Color.WHITE));
        currentBoard[5][2].setPiece(new Single(Piece.Color.WHITE));
        assertFalse(CuT.hasMove(board, 6, 1));
    }
}
