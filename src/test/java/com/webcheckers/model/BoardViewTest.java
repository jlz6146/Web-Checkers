package com.webcheckers.model;

import static com.webcheckers.model.BoardView.NUM_ROWS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Piece.Color;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * The unit test suite for the {@link BoardView} component.
 *
 * @author Eric Landers esl7511@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-tier")
public class BoardViewTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * The {@link Space}, {@link Position}, and {@link Move} components are thoroughly tested so
     * we can use them safely as "friendly" dependencies.
     */
    private BoardView CuT;

    // mock objects
    private Player red = mock(Player.class);
    private Player white = mock(Player.class);

    // friendly objects
    private Space startSpace;
    private Space endSpace;
    private Position start;
    private Position end;
    private Space[][] board;
    private Move move;

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup() {
        when(red.getName()).thenReturn("");
        when(white.getName()).thenReturn("");
        // create a unique CuT for each test
        CuT = new BoardView(red, white);
        board = CuT.getBoard();
    }

    /**
     * Test that the constructor works correctly.
     */
    @Test
    public void test_constructor() {
        CuT = new BoardView(red, white);
        assertEquals(red.toString() + " : " + white.toString(), CuT.toString());
    }

    /**
     * Test that the copy constructor works correctly when flip is true.
     */
    @Test
    public void test_constructorFlipTrue() {
        BoardView board = new BoardView(red, white);
        CuT = new BoardView(board, true);
        for(int row = 0; row<8; row++){
            for (int cell = 0; cell < 8; cell++) {
                Space expected = board.getRow(8 - row - 1)[8 - cell - 1];
                Space actual = CuT.getRow(row)[cell];
                assertEquals(expected.getPiece(), actual.getPiece());
                assertEquals(expected.isValid(), actual.isValid());
                assertEquals(8 - expected.getCellIdx() - 1, actual.getCellIdx());
            }

        }
    }

    /**
     * Test that the copy constructor works correctly when flip is false.
     */
    @Test
    public void test_constructorFlipFalse() {
        BoardView board = new BoardView(red, white);
        CuT = new BoardView(board, false);
        for(int row = 0; row<8; row++){
            for (int cell = 0; cell < 8; cell++) {
                Space expected = board.getRow(row)[cell];
                Space actual = CuT.getRow(row)[cell];
                assertEquals(expected.getPiece(), actual.getPiece());
                assertEquals(expected.isValid(), actual.isValid());
                assertEquals(expected.getCellIdx(), actual.getCellIdx());
            }

        }
    }

    /**
     * Test that checkMove() works correctly when the starting and ending space of the move are null.
     */
    @Test
    public void test_checkMoveNullSpace() {
        start = new Position(12, 12);
        end = new Position(15, 15);
        move = new Move(start, end);

        assertEquals(BoardView.NULL_SPACE_MESSAGE, CuT.checkMove(move, red.getColor()));
    }

    /**
     * Test that checkMove() works correctly when the piece being moved is null.
     */
    @Test
    public void test_checkMoveNullPiece() {
        start = new Position(4, 4);
        end = new Position(4, 4);
        move = new Move(start, end);

        assertEquals(BoardView.NULL_START_PIECE_MESSAGE, CuT.checkMove(move, red.getColor()));
    }

    /**
     * Test that checkMove() works correctly when the piece being moved is the opponents piece.
     */
    @Test
    public void test_checkMoveOpponentsPiece() {
        start = new Position(1, 4);
        end = new Position(0, 0);
        move = new Move(start, end);

        assertEquals(BoardView.OPPONENTS_PIECE_MESSAGE, CuT.checkMove(move, red.getColor()));
    }

    /**
     * Test that checkMove() works correctly when piece is moved being moved to an occupied space.
     */
    @Test
    public void test_checkMoveOccupiedEndSpace() {
        start = new Position(7, 0);
        end = new Position(6, 1);
        move = new Move(start, end);

        assertEquals(BoardView.OCCUPIED_END_SPACE_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to make two simple moves.
     */
    @Test
    public void test_checkMoveDoubleSimpleMove() {
        start = new Position(5, 0);
        end = new Position(4, 1);
        move = new Move(start, end);
        CuT.setLastMoveType(BoardView.MoveType.SIMPLE);

        assertEquals(BoardView.DOUBLE_MOVE_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to make a simple move after jumping.
     */
    @Test
    public void test_checkMoveSimpleMoveAfterJump() {
        start = new Position(5, 0);
        end = new Position(4, 1);
        move = new Move(start, end);
        CuT.setLastMoveType(BoardView.MoveType.JUMP);

        assertEquals(BoardView.MOVE_AFTER_JUMPING_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to make a simple move when a jump is possible.
     */
    @Test
    public void test_checkMoveSimpleMoveWhenJumpExists() {
        start = new Position(5, 0);
        end = new Position(4, 1);
        move = new Move(start, end);
        board[4][3].setPiece(new Single(Color.WHITE));

        assertEquals(BoardView.FORCED_JUMP_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to make a valid simple move.
     */
    @Test
    public void test_checkMoveValidSimpleMove() {
        start = new Position(5, 0);
        end = new Position(4, 1);
        move = new Move(start, end);

        assertEquals(BoardView.VALID_MOVE_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to make an invalid simple move.
     */
    @Test
    public void test_checkMoveInvalidSimpleMove() {
        start = new Position(5, 0);
        end = new Position(6, 1);
        move = new Move(start, end);
        board[6][1].setPiece(null);

        assertEquals(BoardView.ILLEGAL_MOVE_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying jump after making a simple move.
     */
    @Test
    public void test_checkMoveJumpAfterSimpleMove() {
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);
        board[4][3].setPiece(new Single(Color.WHITE));
        CuT.setLastMoveType(BoardView.MoveType.SIMPLE);

        assertEquals(BoardView.JUMP_AFTER_MOVING_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to make a valid jump.
     */
    @Test
    public void test_checkMoveValidJump() {
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);
        board[4][3].setPiece(new Single(Color.WHITE));

        assertEquals(BoardView.VALID_JUMP_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to jump over an empty square.
     */
    @Test
    public void test_checkMoveJumpOverEmptySquare() {
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);

        assertEquals(BoardView.JUMP_OVER_NOTHING_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying to jump over their own piece.
     */
    @Test
    public void test_checkMoveJumpOverOwnPiece() {
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);
        board[4][3].setPiece(new Single(Color.RED));

        assertEquals(BoardView.JUMP_OVER_OWN_PIECE_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that checkMove() works correctly when the player is trying make an invalid move.
     */
    @Test
    public void test_checkMoveInvalidMove() {
        start = new Position(7, 0);
        end = new Position(5, 1);
        move = new Move(start, end);

        assertEquals(BoardView.INVALID_MOVE_MESSAGE, CuT.checkMove(move, Color.RED));
    }

    /**
     * Test that makeMove() works correctly when the move results in a single checker being ready to be kinged.
     */
    @Test
    public void test_makeMoveCreateKingChecker() {
        //Case: red piece ready to be kinged
        start = new Position(1, 0);
        end = new Position(0, 1);
        move = new Move(start, end);
        board[1][0].setPiece(new Single(Color.RED));
        board[0][1].setPiece(null);

        CuT.makeMove(move);
        assertEquals(new Single(Color.RED), board[0][1].getPiece());
        assertNull(board[1][0].getPiece());

        //Case: white piece  ready to be kinged
        start = new Position(6, 1);
        end = new Position(7, 0);
        move = new Move(start, end);
        board[6][1].setPiece(new Single(Color.WHITE));
        board[7][0].setPiece(null);

        CuT.makeMove(move);
        assertEquals(new Single(Color.WHITE), board[7][0].getPiece());
        assertNull(board[6][1].getPiece());
    }

    /**
     * Test that makeMove() works correctly when the move does not result in a single checker being kinged.
     */
    @Test
    public void test_makeMoveNoCheckerKinged() {
        //Case: red piece being moved
        start = new Position(5, 0);
        end = new Position(4, 1);
        move = new Move(start, end);

        CuT.makeMove(move);
        assertEquals(new Single(Color.RED), board[4][1].getPiece());
        assertNull(board[5][0].getPiece());

        //Case: white piece being moved
        start = new Position(2, 1);
        end = new Position(3, 0);
        move = new Move(start, end);

        CuT.makeMove(move);
        assertEquals(new Single(Color.WHITE), board[3][0].getPiece());
        assertNull(board[2][1].getPiece());
    }

    /**
     * Test that makeMove() works correctly and removes the captured piece when the move is a jump.
     */
    @Test
    public void test_makeMoveJump() {
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);
        board[4][3].setPiece(new Single(Color.WHITE));

        CuT.makeMove(move);
        assertEquals(new Single(Color.RED), board[3][4].getPiece());
        assertNull(board[5][2].getPiece());
        assertNull(board[4][3].getPiece());
    }

    /**
     * Test that makeMove() works correctly and sets playerHasJump to true when the player jumps
     * when and a second jump is possible.
     */
    @Test
    public void test_makeMoveJumpSecondJumpPossible() {
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);
        board[4][3].setPiece(new Single(Color.WHITE));
        board[1][6].setPiece(null);

        CuT.makeMove(move);
        assertTrue(CuT.playerHasJump());
    }

    /**
     * Test that getJumpedSquare() works correctly.
     */
    @Test
    public void test_getJumpedSquare() {
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);
        Space expected = board[4][3];

        assertEquals(expected, CuT.getJumpedSquare(move));
    }

    /**
     * Test that playerCanJump() works correctly.
     */
    @Test
    public void test_playerCanJump() {
        //Case: initial board state
        assertFalse(CuT.playerCanJump(Color.RED));

        //Case: a jump is possible
        board[4][3].setPiece(new Single(Color.WHITE));
        assertTrue(CuT.playerCanJump(Color.RED));
    }

    /**
     * Test that getSpace() works correctly.
     */
    @Test
    public void test_getSpace() {
        //Case: valid space
        Space expected = board[4][3];
        Position validPosition = new Position(4, 3);

        assertEquals(expected, CuT.getSpace(validPosition));

        //Case: invalid space
        Position invalidPosition = new Position(9, 9);

        assertNull(CuT.getSpace(invalidPosition));
    }

    /**
     * Test that validPosition() works correctly.
     */
    @Test
    public void test_validPosition() {
        Position position;

        //Case: row too large
        position = new Position(9, 0);
        assertFalse(CuT.validPosition(position));

        //Case: row too small
        position = new Position(-1, 0);
        assertFalse(CuT.validPosition(position));

        //Case: cell too large
        position = new Position(0, 9);
        assertFalse(CuT.validPosition(position));

        //Case: cell too small
        position = new Position(0, -1);
        assertFalse(CuT.validPosition(position));

        //Case: valid position
        position = new Position(0, 0);
        assertTrue(CuT.validPosition(position));
    }

    /**
     * Test that getLastMoveType() works correctly.
     */
    @Test
    public void test_getLastMoveType() {
        //Case: lastMoveType should be NONE
        assertEquals(BoardView.MoveType.NONE, CuT.getLastMoveType());

        //Case: lastMoveType should be SIMPLE
        start = new Position(5, 0);
        end = new Position(4, 1);
        move = new Move(start, end);
        CuT.makeMove(move);

        assertEquals(BoardView.MoveType.SIMPLE, CuT.getLastMoveType());

        //Case: lastMoveType should be JUMP
        start = new Position(5, 2);
        end = new Position(3, 4);
        move = new Move(start, end);
        board[4][3].setPiece(new Single(Color.WHITE));
        CuT.makeMove(move);

        assertEquals(BoardView.MoveType.JUMP, CuT.getLastMoveType());
    }

    /**
     * Test that setLastMoveType() works correctly.
     */
    @Test
    public void test_setLastMoveType() {
        //Case: SIMPLE
        CuT.setLastMoveType(BoardView.MoveType.SIMPLE);
        assertEquals(BoardView.MoveType.SIMPLE, CuT.getLastMoveType());

        //Case: JUMP
        CuT.setLastMoveType(BoardView.MoveType.JUMP);
        assertEquals(BoardView.MoveType.JUMP, CuT.getLastMoveType());

        //Case: NONE
        CuT.setLastMoveType(BoardView.MoveType.NONE);
        assertEquals(BoardView.MoveType.NONE, CuT.getLastMoveType());
    }

    /**
     * Test that playerHasJump() works correctly.
     */
    @Test
    public void test_playerHasJump() {
        //Case: false
        assertFalse(CuT.playerHasJump());

        //Case: true
        board[4][1].setPiece(new Single(Color.WHITE));
        board[1][4].setPiece(null);
        start = new Position(5, 0);
        end = new Position(3, 2);
        move = new Move(start, end);
        CuT.makeMove(move);

        assertTrue(CuT.playerHasJump());
    }

    /**
     * Test that resetJumpData() works correctly.
     */
    @Test
    public void test_resetJumpData() {
        //Set it so playerHasJump is true
        board[4][1].setPiece(new Single(Color.WHITE));
        board[1][4].setPiece(null);
        start = new Position(5, 0);
        end = new Position(3, 2);
        move = new Move(start, end);
        CuT.makeMove(move);

        CuT.resetJumpData();
        assertFalse(CuT.playerHasJump());
    }

    /**
     * Test that getSquare() works correctly.
     */
    @Test
    public void test_getSquare() {
        Space expected = board[4][3];
        Position validPosition = new Position(4, 3);

        assertEquals(expected, CuT.getSpace(validPosition));
    }

    /**
     * Test that getRow() works correctly.
     */
    @Test
    public void test_getRow() {
        Space[] expected = board[4];

        assertEquals(expected, CuT.getRow(4));
    }

    /**
     * Test that getBoard() works correctly.
     */
    @Test
    public void test_getBoard() {
        //Set up expected initial board
        Space[][] expected = new Space[NUM_ROWS][BoardView.NUM_COLS];
        for(int row = 0; row < NUM_ROWS; row++) {
            for(int col = 0; col < BoardView.NUM_COLS; col++) {
                if((row + col) % 2 == 1) {
                    if(row <= 2) {
                        expected[row][col] = new Space(col, new Single(Piece.Color.WHITE), true);
                    } else if(row >= 5) {
                        expected[row][col] = new Space(col, new Single(Piece.Color.RED), true);
                    } else {
                        expected[row][col] = new Space(col, null, true);
                    }
                } else {
                    expected[row][col] = new Space(col, null, false);
                }
            }
        }

        Space[][] actual = CuT.getBoard();
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }

    /**
     * Test that piecesRemaining() works correctly.
     */
    @Test
    public void test_piecesRemaining() {
        // Case: initial board state, both players should have pieces remaining
        assertTrue(CuT.piecesRemaining(Color.RED));
        assertTrue(CuT.piecesRemaining(Color.WHITE));

        // Case: neither player has any pieces left
        // Set up board so neither player has any pieces left
        Space[][] board = CuT.getBoard();
        for(Space[] row : board) {
            for(Space space : row) {
                space.setPiece(null);
            }
        }
        assertFalse(CuT.piecesRemaining(Color.RED));
        assertFalse(CuT.piecesRemaining(Color.WHITE));
    }

    /**
     * Test that movesRemaining() works correctly.
     */
    @Test
    public void test_movesRemaining() {
        // Case: initial board state, both players should have moves remaining
        assertTrue(CuT.movesRemaining(Color.RED));
        assertTrue(CuT.movesRemaining(Color.WHITE));

        // Case: the red player has no moves left
        // Set up board so the red player has no moves left
        Space[][] board = CuT.getBoard();
        for(Space[] row : board) {
            for(Space space : row) {
                space.setPiece(null);
            }
        }
        board[0][1].setPiece(new Single(Color.WHITE));
        board[1][0].setPiece(new Single(Color.RED));

        assertFalse(CuT.movesRemaining(Color.RED));

        // Case: the white player has no moves left
        // Set up board so the red player has no moves left
        for(Space[] row : board) {
            for(Space space : row) {
                space.setPiece(null);
            }
        }
        board[7][6].setPiece(new Single(Piece.Color.RED));
        board[6][7].setPiece(new Single(Piece.Color.WHITE));

        assertFalse(CuT.movesRemaining(Color.WHITE));
    }

    /**
     * Test that isGameOver() works correctly.
     */
    @Test
    public void test_isGameOver() {
        // Case: initial board state, the game should not be over
        assertFalse(CuT.isGameOver());

        // Case: game has ended
        // Create a board where the game ends in one move
        Space[][] board = CuT.getBoard();
        for(Space[] row : board) {
            for(Space space : row) {
                space.setPiece(null);
            }
        }
        board[7][0].setPiece(new Single(Piece.Color.RED));
        board[6][1].setPiece(new Single(Piece.Color.WHITE));
        // Make the move to end the game
        CuT.makeMove(new Move(new Position(7, 0), new Position(5, 2)));
        assertTrue(CuT.isGameOver());
    }

    /**
     * Test that iterator() works correctly.
     */
    @Test
    public void test_iterator() {
        Collection<Row> lst = new LinkedList<>();
        for(int row = 0; row < NUM_ROWS; row++) {
            lst.add(new Row(row, Arrays.asList(board[row])));
        }

        Iterator<Row> expected = lst.iterator();
        Iterator<Row> actual = CuT.iterator();

        for (int i = 0; i < BoardView.NUM_COLS; i++) {
            assertEquals(expected.next(), actual.next());
        }
    }

    /**
     * Test that toString() works correctly.
     */
    @Test
    public void test_toString() {
        when(red.toString()).thenReturn("red");
        when(white.toString()).thenReturn("white");

        assertEquals("red : white", CuT.toString());
    }
}
