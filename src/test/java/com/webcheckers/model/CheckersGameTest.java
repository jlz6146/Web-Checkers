package com.webcheckers.model;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * The unit test suite for the {@link CheckersGame} component.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-tier")
public class CheckersGameTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * The {@link Player} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private CheckersGame CuT;


    // friendly objects
    private Player red;
    private Player white;
    private BoardView boardView;

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup() {
        red = new Player("red");
        white = new Player("white");
        boardView = new BoardView(red, white);

        // create a unique CuT for each test
        CuT = new CheckersGame(red, white, CheckersGame.Mode.PLAY, boardView);
    }

    /**
     * Test that getBoard() works correctly.
     */
    @Test
    public void test_getBoard() {
        BoardView expected = new BoardView(red, white);
        BoardView flipped = new BoardView(expected, true);

        assertEquals(expected.toString(), CuT.getBoard(false).toString());
        assertEquals(flipped.toString(), CuT.getBoard(true).toString());
    }

    /**
     * Test that getCurrentColor() works correctly.
     */
    @Test
    public void test_getCurrentColor() {
        assertEquals(Piece.Color.RED, CuT.getCurrentColor());
    }

    /**
     * Test that isGameOver() and endGame() work correctly.
     */
    @Test
    public void test_isGameOver_and_endGame() {
        assertFalse(CuT.isGameOver());
        CuT.endGame(CheckersGame.EndReason.CAPTURED, Piece.Color.RED);
        assertTrue(CuT.isGameOver());
    }

    /**
     * Test that gameOverMessage() works correctly when a player captures all of their opponents pieces.
     */
    @Test
    public void test_gameOverMessageAllPiecesCaptured() {
        // Case: red wins
        assertNull(CuT.gameOverMessage());
        CuT.endGame(CheckersGame.EndReason.CAPTURED, Piece.Color.RED);
        assertEquals(String.format(CheckersGame.ALL_PIECES_CAPTURED_MESSAGE, "red", "white"), CuT.gameOverMessage());

        // Case: white wins
        CuT.endGame(CheckersGame.EndReason.CAPTURED, Piece.Color.WHITE);
        assertEquals(String.format(CheckersGame.ALL_PIECES_CAPTURED_MESSAGE, "white", "red"), CuT.gameOverMessage());
    }

    /**
     * Test that gameOverMessage() works correctly when a player resigns.
     */
    @Test
    public void test_gameOverMessageResigned() {
        assertNull(CuT.gameOverMessage());
        CuT.endGame(CheckersGame.EndReason.RESIGNED, Piece.Color.WHITE);
        assertEquals(String.format(CheckersGame.PLAYER_RESIGNED_MESSAGE, "white"), CuT.gameOverMessage());
    }

    /**
     * Test that gameOverMessage() works correctly when a player resigns.
     */
    @Test
    public void test_gameOverMessageNoMovesLeft() {
        assertNull(CuT.gameOverMessage());
        CuT.endGame(CheckersGame.EndReason.NO_MOVES_LEFT, Piece.Color.RED);
        assertEquals(String.format(CheckersGame.NO_MOVES_LEFT_MESSAGE, "white", "red"), CuT.gameOverMessage());
    }

    /**
     * Test that gameOverMessage() works correctly when the game ends in an unknown way.
     */
    @Test
    public void test_gameOverMessage() {
        assertNull(CuT.gameOverMessage());
        CuT.endGame(null, Piece.Color.WHITE);
        assertEquals(CheckersGame.GAME_OVER_MESSAGE, CuT.gameOverMessage());
    }

    /**
     * Test that redPlayer() works correctly.
     */
    @Test
    public void test_redPlayer() {
        assertEquals(red, CuT.redPlayer());
    }

    /**
     * Test that whitePlayer() works correctly.
     */
    @Test
    public void test_whitePlayer() {
        assertEquals(white, CuT.whitePlayer());
    }

    /**
     * Test that getMode() works correctly.
     */
    @Test
    public void test_getMode() {
        // mode = PLAY
        CuT = new CheckersGame(red, white, CheckersGame.Mode.PLAY, new BoardView(red, white));
        assertEquals(CheckersGame.Mode.PLAY, CuT.getMode());

        // mode = SPECTATOR
        CuT = new CheckersGame(red, white, CheckersGame.Mode.SPECTATOR, new BoardView(red, white));
        assertEquals(CheckersGame.Mode.SPECTATOR, CuT.getMode());
    }

    /**
     * Test that getGameID() works correctly.
     */
    @Test
    public void test_getGameID() {
        assertEquals(Objects.hash(red, white, CheckersGame.Mode.PLAY), CuT.getGameID());
    }

    /**
     * Test that testMove() works correctly.
     */
    @Test
    public void test_testMove() {
        Move valid = new Move(new Position(5, 0), new Position(4, 1));
        Move invalid = new Move(new Position(8, 8), new Position(9, 9));

        assertEquals(BoardView.VALID_MOVE_MESSAGE, CuT.testMove(valid));
        assertEquals(BoardView.NULL_SPACE_MESSAGE, CuT.testMove(invalid));
    }

    /**
     * Test that clearTurnMoves() works correctly.
     */
    @Test
    public void test_clearTurnMoves() {
        Move valid1 = new Move(new Position(5, 2), new Position(4, 3));
        Move valid2 = new Move(new Position(5, 0), new Position(4, 1));

        // Test move, should be valid
        assertEquals(BoardView.VALID_MOVE_MESSAGE, CuT.testMove(valid1));
        // Test second move, should be invalid, and cause a double move error.
        assertEquals(BoardView.DOUBLE_MOVE_MESSAGE, CuT.testMove(valid2));

        // Clear turn moves
        CuT.clearTurnMoves();

        // Test second move again, should be valid this time as turn moves should have been cleared
        assertEquals(BoardView.VALID_MOVE_MESSAGE, CuT.testMove(valid2));
    }

    /**
     * Test that getTurnMoves() works correctly.
     */
    @Test
    public void test_getTurnMoves() {
        Move valid = new Move(new Position(5, 2), new Position(4, 3));
        LinkedList<Move> testTurnMoves = new LinkedList<>();

        //Test that turnMoves is empty initially
        assertEquals(testTurnMoves, CuT.getTurnMoves());

        //Add a move to turnMoves
        CuT.testMove(valid);
        testTurnMoves.addLast(valid);

        //Test that turnMoves now contains the move 'valid'
        assertEquals(testTurnMoves, CuT.getTurnMoves());
    }

    /**
     * Test that backupMove() works correctly.
     */
    @Test
    public void test_backupMove() {
        // Test that we get the no move made error message when trying to backup before a move has been made
        assertEquals(CheckersGame.NO_MOVES_MADE_MESSAGE, CuT.backupMove());

        Move valid = new Move(new Position(5, 2), new Position(4, 3));
        CuT.testMove(valid);
        assertEquals(CheckersGame.MOVE_BACKED_UP_MESSAGE, CuT.backupMove());

        // Case turnMoves.getLast().isSimpleMove() is true
        LinkedList<Move> turnMoves = CuT.getTurnMoves();
        turnMoves.addLast(valid);
        turnMoves.addLast(valid);
        assertEquals(CheckersGame.MOVE_BACKED_UP_MESSAGE, CuT.backupMove());
        turnMoves.clear();

        // Set up valid jump
        CuT.testMove(new Move(new Position(5, 0), new Position(4, 1)));
        CuT.submitTurn();
        CuT.testMove(new Move(new Position(5, 4), new Position(4, 5)));
        CuT.submitTurn();
        Move jump = new Move(new Position(4, 1), new Position(2, 3));
        CuT.testMove(jump);
        // Test backing up a jump
        assertEquals(CheckersGame.MOVE_BACKED_UP_MESSAGE, CuT.backupMove());

        // Case turnMoves.getLast().isSimpleMove() is false
        turnMoves.addLast(jump);
        turnMoves.addLast(jump);
        assertEquals(CheckersGame.MOVE_BACKED_UP_MESSAGE, CuT.backupMove());
    }

    /**
     * Test that submitTurn() works correctly.
     */
    @Test
    public void test_submitTurn() {
        //Case: turnMoves.size() == 0
        assertEquals(CheckersGame.NO_MOVES_MADE_MESSAGE, CuT.submitTurn());

        //Case: jump exists, but player made a simple move
        LinkedList<Move> turnMoves = CuT.getTurnMoves();
        // Set up valid jump
        CuT.testMove(new Move(new Position(5, 0), new Position(4, 1)));
        CuT.submitTurn();
        CuT.testMove(new Move(new Position(5, 4), new Position(4, 5)));
        CuT.submitTurn();

        //Add simple move
        Move simple = new Move(new Position(5, 2), new Position(4, 3));
        turnMoves.addLast(simple);
        assertEquals(CheckersGame.JUMP_EXISTS_MESSAGE, CuT.submitTurn());
        turnMoves.removeLast();

        //Case: double jump exists, but player only did the first jump
        // Set up double jump
        CuT.testMove(new Move(new Position(4, 1), new Position(2, 3)));
        CuT.submitTurn();
        CuT.testMove(new Move(new Position(6, 3), new Position(4, 5)));
        CuT.submitTurn();
        CuT.testMove(new Move(new Position(6, 1), new Position(5, 0)));
        CuT.submitTurn();
        CuT.testMove(new Move(new Position(7, 2), new Position(6, 3)));
        CuT.submitTurn();
        CuT.testMove(new Move(new Position(5, 2), new Position(4, 1)));
        CuT.submitTurn();
        CuT.testMove(new Move(new Position(5, 0), new Position(4, 1)));
        CuT.submitTurn();
        //Only make first of two jumps
        CuT.testMove(new Move(new Position(4, 1), new Position(2, 3)));
        assertEquals(CheckersGame.JUMP_EXISTS_MESSAGE, CuT.submitTurn());

        // Case: move creates a king piece
        CuT.testMove(new Move(new Position(2, 3), new Position(0, 5)));
        Message msg = CuT.submitTurn();
        assertEquals(CheckersGame.TURN_SUBMITTED_MESSAGE, msg);
        assertEquals(new King(Piece.Color.RED), boardView.getBoard()[0][5].getPiece());

        // Case: this move caused the game to end by capturing all of the opponents pieces
        //Set up board so only two pieces remain
        Space[][] board = boardView.getBoard();
        for(Space[] row : board) {
            for(Space space : row) {
                space.setPiece(null);
            }
        }
        board[7][0].setPiece(new Single(Piece.Color.RED));
        board[6][1].setPiece(new Single(Piece.Color.WHITE));
        // Create a game with this special board
        CuT = new CheckersGame(red, white, CheckersGame.Mode.PLAY, boardView);
        CuT.testMove(new Move(new Position(7, 0), new Position(5, 2)));
        assertEquals(CheckersGame.TURN_SUBMITTED_MESSAGE, CuT.submitTurn());
        assertTrue(CuT.isGameOver());

        // Case: this move caused the game to end because the opponent has no move left
        //Set up board so the white player has no moves left
        board = boardView.getBoard();
        for(Space[] row : board) {
            for(Space space : row) {
                space.setPiece(null);
            }
        }
        board[7][0].setPiece(new Single(Piece.Color.RED));
        board[7][6].setPiece(new Single(Piece.Color.RED));
        board[6][7].setPiece(new Single(Piece.Color.WHITE));
        // Create a game with this special board
        CuT = new CheckersGame(red, white, CheckersGame.Mode.PLAY, boardView);
        CuT.testMove(new Move(new Position(7, 0), new Position(6, 1)));
        assertEquals(CheckersGame.TURN_SUBMITTED_MESSAGE, CuT.submitTurn());
        assertTrue(CuT.isGameOver());
    }

    /**
     * Test that isNewTurn() works correctly.
     */
    @Test
    public void test_isNewTurn() {
        // Case: initial state, no new turns
        assertFalse(CuT.isNewTurn());

        // Case:a move has been made, there should be a new turn
        CuT.testMove(new Move(new Position(5, 0), new Position(4, 1)));
        CuT.submitTurn();
        assertTrue(CuT.isNewTurn());
    }

    /**
     * Test that setNewTurn() works correctly.
     */
    @Test
    public void test_setNewTurn() {
        // Case: setting newTurn to true
        CuT.setNewTurn(true);
        assertTrue(CuT.isNewTurn());

        // Case: setting newTurn to false
        CuT.setNewTurn(false);
        assertFalse(CuT.isNewTurn());
    }

    /**
     * Test that toString() works correctly.
     */
    @Test
    public void test_toString() {
        String expected = red.getName() + " VS " + white.getName();
        //Invoke the test
        String actual = CuT.toString();
        assertEquals(expected, actual);
    }
}
