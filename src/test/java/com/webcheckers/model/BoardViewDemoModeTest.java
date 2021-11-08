package com.webcheckers.model;

import com.webcheckers.Application;
import org.junit.jupiter.api.*;

import static com.webcheckers.model.BoardView.NUM_ROWS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link BoardView} component's demo mode functionality.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Model-tier")
public class BoardViewDemoModeTest {
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
    private Space[][] actual;
    private Space[][] expected;

    /**
     * Set the demo mode system property.
     */
    @BeforeAll
    static void initialSetup() {
        //Used so that testing demo mode functionality is possible
        System.setProperty("demoMode", "true");
    }

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup() {
        // reset expected to the initial board state for each test
        expected = (new BoardView(new Player(""), new Player(""))).getBoard();
    }

    /**
     * Test that demo mode activates correctly.
     */
    @Test
    public void test_activeDemoMode() {
        when(red.getName()).thenReturn("normal");
        when(white.getName()).thenReturn("names");
        CuT = new BoardView(red, white);
    }

    /**
     * Test that the jump demo board is set up correctly.
     */
    @Test
    public void test_jumpDemoBoard() {
        when(red.getName()).thenReturn("jump");
        when(white.getName()).thenReturn("jump");
        CuT = new BoardView(red, white);
        actual = CuT.getBoard();
        // Set up expected board
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                expected[i][j].setPiece(null);
            }
        }
        expected[5][2].setPiece(new Single(Piece.Color.RED));
        expected[4][1].setPiece(new Single(Piece.Color.RED));
        expected[5][6].setPiece(new Single(Piece.Color.RED));
        expected[4][5].setPiece(new Single(Piece.Color.WHITE));

        // Compare
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }

    /**
     * Test that the multiple jump demo board is set up correctly.
     */
    @Test
    public void test_multipleJumpDemoBoard() {
        when(red.getName()).thenReturn("mjump");
        when(white.getName()).thenReturn("mjump");
        CuT = new BoardView(red, white);
        actual = CuT.getBoard();
        // Set up expected board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                expected[i][j].setPiece(null);
            }
        }
        expected[6][1].setPiece(new Single(Piece.Color.RED));
        expected[5][4].setPiece(new Single(Piece.Color.RED));
        expected[4][5].setPiece(new Single(Piece.Color.WHITE));
        expected[3][4].setPiece(new Single(Piece.Color.WHITE));
        expected[5][2].setPiece(new Single(Piece.Color.WHITE));
        expected[5][2].setPiece(new Single(Piece.Color.WHITE));
        expected[3][2].setPiece(new Single(Piece.Color.WHITE));
        expected[2][5].setPiece(null);
        expected[2][1].setPiece(null);

        // Compare
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }

    /**
     * Test that the king demo board is set up correctly.
     */
    @Test
    public void test_kingDemoBoard() {
        when(red.getName()).thenReturn("king");
        when(white.getName()).thenReturn("king");
        CuT = new BoardView(red, white);
        actual = CuT.getBoard();
        // Set up expected board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                expected[i][j].setPiece(null);
            }
        }
        expected[0][1].setPiece(new Single(Piece.Color.WHITE));
        expected[1][4].setPiece(new Single(Piece.Color.RED));

        // Compare
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }

    /**
     * Test that the move king demo board is set up correctly.
     */
    @Test
    public void test_moveKingDemoBoard() {
        when(red.getName()).thenReturn("mking");
        when(white.getName()).thenReturn("mking");
        CuT = new BoardView(red, white);
        actual = CuT.getBoard();
        // Set up expected board
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                expected[i][j].setPiece(null);
            }
        }
        expected[4][3].setPiece(new King(Piece.Color.RED));

        // Compare
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }

    /**
     * Test that the jump king demo board is set up correctly.
     */
    @Test
    public void test_jumpKingDemoBoard() {
        when(red.getName()).thenReturn("jking");
        when(white.getName()).thenReturn("jking");
        CuT = new BoardView(red, white);
        actual = CuT.getBoard();
        // Set up expected board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                expected[i][j].setPiece(null);
            }
        }
        expected[4][3].setPiece(new King(Piece.Color.RED));
        expected[3][4].setPiece(new Single(Piece.Color.WHITE));
        expected[1][6].setPiece(new Single(Piece.Color.WHITE));
        expected[5][4].setPiece(new Single(Piece.Color.WHITE));
        expected[5][6].setPiece(new Single(Piece.Color.WHITE));
        expected[3][6].setPiece(new Single(Piece.Color.WHITE));

        // Compare
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }

    /**
     * Test that the win demo board is set up correctly.
     */
    @Test
    public void test_winDemoBoard() {
        when(red.getName()).thenReturn("win");
        when(white.getName()).thenReturn("win");
        CuT = new BoardView(red, white);
        actual = CuT.getBoard();
        // Set up expected board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                expected[i][j].setPiece(null);
            }
        }
        expected[4][1].setPiece(new Single(Piece.Color.WHITE));
        expected[5][0].setPiece(new Single(Piece.Color.RED));

        // Compare
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }

    /**
     * Test that the no moves demo board is set up correctly.
     */
    @Test
    public void test_noMovesDemoBoard() {
        when(red.getName()).thenReturn("nomoves");
        when(white.getName()).thenReturn("nomoves");
        CuT = new BoardView(red, white);
        actual = CuT.getBoard();
        // Set up expected board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                expected[i][j].setPiece(null);
            }
        }
        expected[7][0].setPiece(new Single(Piece.Color.RED));
        expected[7][6].setPiece(new Single(Piece.Color.RED));
        expected[6][7].setPiece(new Single(Piece.Color.WHITE));

        // Compare
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < BoardView.NUM_COLS; col++) {
                assertEquals(expected[row][col], actual[row][col]);
            }
        }
    }
}
