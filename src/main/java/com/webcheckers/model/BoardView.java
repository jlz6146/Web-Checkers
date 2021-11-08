package com.webcheckers.model;

import com.webcheckers.Application;
import com.webcheckers.util.Message;

import java.util.*;

/**
 * A class to represent the game board.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class BoardView implements Iterable<Row>{
    //Values used in generating the game board
    private Player red;
    private Player white;
    private Space[][] board;
    private MoveType lastMoveType = MoveType.NONE;
    private boolean checkedJumps = false;
    private boolean playerHasJump = false;
    private boolean isGameOver = false;

    /**
     * An enum used to keep track of the type of the last move made.
     */
    public enum MoveType {
        NONE,
        SIMPLE,
        JUMP
    }

    //The number of rows in the checkerboard
    public static final int NUM_ROWS = 8;
    //The number of cols in the checkerboard
    public static final int NUM_COLS = 8;

    //Messages for valid moves and jumps
    static final Message VALID_MOVE_MESSAGE = Message.info("That move is legal.");
    static final Message VALID_JUMP_MESSAGE = Message.info("That jump is legal.");

    //Error messages regarding illegal moves
    static final Message NULL_SPACE_MESSAGE = Message.error("You cannot move to an invalid space!");
    static final Message NULL_START_PIECE_MESSAGE = Message.error("You must move a checker!");
    static final Message OPPONENTS_PIECE_MESSAGE = Message.error("You cannot move your opponents pieces!");
    static final Message OCCUPIED_END_SPACE_MESSAGE = Message.error("You cannot move to an occupied square!");
    static final Message ILLEGAL_MOVE_MESSAGE = Message.error("That piece cannot move like that!");
    static final Message DOUBLE_MOVE_MESSAGE = Message.error("You cannot move twice in one turn!");

    //Error messages regarding illegal jumps
    static final Message FORCED_JUMP_MESSAGE = Message.error("When a jump is possible, you must must jump!");
    static final Message JUMP_OVER_NOTHING_MESSAGE = Message.error("You cannot jump over an empty square!");
    static final Message JUMP_OVER_OWN_PIECE_MESSAGE = Message.error("You cannot jump over your own piece!");
    static final Message MOVE_AFTER_JUMPING_MESSAGE = Message.error("You cannot move after jumping!");
    static final Message JUMP_AFTER_MOVING_MESSAGE = Message.error("You cannot jump after moving!");
    static final Message INVALID_MOVE_MESSAGE = Message.error("That piece cannot move there!");

    //Messages regarding game's end
    static final Message PLAYER_WON_MESSAGE = Message.info("You won! :D");
    static final Message PLAYER_LOST_MESSAGE = Message.info("Sorry! You lost :(");

    /**
     * Creates a new game board.
     *
     * @param red the player using red pieces
     * @param white the player using white pieces
     */
    public BoardView(Player red, Player white){
        this.red = red;
        this.white = white;
        this.board = new Space[NUM_ROWS][NUM_COLS];
        initBoard();
        if(Application.isInDemoMode()) {
            setupDemoBoard();
        }
    }

    /**
     * Called if the application is in demo mode to set up special boards use to demonstrate specific functionality.
     */
    private void setupDemoBoard() {
        if(red.getName().equals("jump") || white.getName().equals("jump")) {
            for (int i = 1; i < 7; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j].setPiece(null);
                }
            }
            board[5][2].setPiece(new Single(Piece.Color.RED));
            board[4][1].setPiece(new Single(Piece.Color.RED));
            board[5][6].setPiece(new Single(Piece.Color.RED));
            board[4][5].setPiece(new Single(Piece.Color.WHITE));
        } else if(red.getName().equals("mjump") || white.getName().equals("mjump")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j].setPiece(null);
                }
            }
            board[6][1].setPiece(new Single(Piece.Color.RED));
            board[5][4].setPiece(new Single(Piece.Color.RED));
            board[4][5].setPiece(new Single(Piece.Color.WHITE));
            board[3][4].setPiece(new Single(Piece.Color.WHITE));
            board[5][2].setPiece(new Single(Piece.Color.WHITE));
            board[5][2].setPiece(new Single(Piece.Color.WHITE));
            board[3][2].setPiece(new Single(Piece.Color.WHITE));
            board[2][5].setPiece(null);
            board[2][1].setPiece(null);
        } else if(red.getName().equals("king") || white.getName().equals("king")) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j].setPiece(null);
                }
            }
            board[0][1].setPiece(new Single(Piece.Color.WHITE));
            board[1][4].setPiece(new Single(Piece.Color.RED));
        } else if(red.getName().equals("mking") || white.getName().equals("mking")) {
            for (int i = 1; i < 7; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j].setPiece(null);
                }
            }
            board[4][3].setPiece(new King(Piece.Color.RED));
        } else if(red.getName().equals("jking") || white.getName().equals("jking")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j].setPiece(null);
                }
            }
            board[4][3].setPiece(new King(Piece.Color.RED));
            board[3][4].setPiece(new Single(Piece.Color.WHITE));
            board[1][6].setPiece(new Single(Piece.Color.WHITE));
            board[5][4].setPiece(new Single(Piece.Color.WHITE));
            board[5][6].setPiece(new Single(Piece.Color.WHITE));
            board[3][6].setPiece(new Single(Piece.Color.WHITE));
        } else if(red.getName().equals("win") || white.getName().equals("win")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j].setPiece(null);
                }
            }
            board[4][1].setPiece(new Single(Piece.Color.WHITE));
            board[5][0].setPiece(new Single(Piece.Color.RED));
        } else if(red.getName().equals("nomoves") || white.getName().equals("nomoves")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    board[i][j].setPiece(null);
                }
            }
            board[7][0].setPiece(new Single(Piece.Color.RED));
            board[7][6].setPiece(new Single(Piece.Color.RED));
            board[6][7].setPiece(new Single(Piece.Color.WHITE));
        }
    }

    /**
     * A copy constructor that can also return a flipped copy of the board.
     *
     * @param board the board to be copied
     * @param flip whether or not the board should be flipped
     */
    public BoardView(BoardView board, boolean flip) {
        this.board = new Space[NUM_ROWS][NUM_COLS];
        this.red = board.red;
        this.white = board.white;
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if(flip) {
                    this.board[row][col] = new Space(board.getRow(NUM_ROWS - row - 1)[NUM_COLS - col - 1], true);
                } else {
                    this.board[row][col] = new Space(board.getRow(row)[col], false);
                }
            }
        }
    }

    /**
     * Checks if the move passed in is a valid move to make.
     *
     * @param move the move to check
     * @return a message about the validity of the move being checked
     */
    public Message checkMove(Move move, Piece.Color playerColor) {
        Position start = move.getStart();
        Position end = move.getEnd();
        Space startSpace = getSpace(start);
        Space endSpace = getSpace(end);

        if(startSpace == null || endSpace == null) {
            return NULL_SPACE_MESSAGE;
        }

        Piece startPiece = startSpace.getPiece();

        if(startPiece == null) {
            return NULL_START_PIECE_MESSAGE;
        }

        if(startPiece.getColor() != playerColor) {
            return OPPONENTS_PIECE_MESSAGE;
        }

        if(endSpace.getPiece() != null) {
            return OCCUPIED_END_SPACE_MESSAGE;
        }

        if(lastMoveType == MoveType.NONE && !checkedJumps) {
            playerHasJump = playerCanJump(playerColor);
            checkedJumps = true;
        }

        if(move.isSimpleMove()) {
            if(lastMoveType == MoveType.SIMPLE) {
                return DOUBLE_MOVE_MESSAGE;
            } else if(lastMoveType == MoveType.JUMP) {
                return MOVE_AFTER_JUMPING_MESSAGE;
            }

            if(playerHasJump) {
                return FORCED_JUMP_MESSAGE;
            }

            if(startPiece.isMoveValid(move)) {
                return VALID_MOVE_MESSAGE;
            } else {
                return ILLEGAL_MOVE_MESSAGE;
            }
        } else if(move.isJump()) {
            if(lastMoveType == MoveType.SIMPLE) {
                return JUMP_AFTER_MOVING_MESSAGE;
            }

            Space jumpedSquare = getJumpedSquare(move);
            if(startPiece.isJumpValid(move, jumpedSquare, endSpace)) {
                return VALID_JUMP_MESSAGE;
            } else {
                Piece jumpedPiece = jumpedSquare.getPiece();
                if(jumpedPiece == null) {
                    return JUMP_OVER_NOTHING_MESSAGE;
                } else if(jumpedPiece.getColor() == playerColor) {
                    return JUMP_OVER_OWN_PIECE_MESSAGE;
                }
            }
        }
        return INVALID_MOVE_MESSAGE;
    }

    /**
     * Makes a move on the board.
     * Calculates if a piece is a king and sets it as one if true
     *
     * @param move the move made by the player
     */
    public void makeMove(Move move) {
        Position start = move.getStart();
        Position end = move.getEnd();

        Space startSpace = getSpace(start);
        Space endSpace = getSpace(end);
        assert startSpace != null;
        assert endSpace != null;
        Piece piece = startSpace.getPiece();
        startSpace.setPiece(null);

        Piece.Color playerColor = piece.getColor();
        Piece.Color opponentColor;
        if(playerColor == Piece.Color.RED) {
            opponentColor = Piece.Color.WHITE;
        } else {
            opponentColor = Piece.Color.RED;
        }
        endSpace.setPiece(piece);

        if(move.isJump()) {
            getJumpedSquare(move).setPiece(null);
            lastMoveType = MoveType.JUMP;
            isGameOver = (!piecesRemaining(opponentColor) || !movesRemaining(opponentColor));
            playerHasJump = endSpace.getPiece().hasJump(this, end.getRow(), end.getCell());
        } else {
            isGameOver = !movesRemaining(opponentColor);
            lastMoveType = MoveType.SIMPLE;
        }
    }

    /**
     * Locates the square a piece can jump to
     *
     * @param move the move that determines the jump
     * @return the board location that can be jumped to
     */
    public Space getJumpedSquare(Move move) {
        Position start = move.getStart();
        Position end = move.getEnd();
        int row = (start.getRow() + end.getRow()) / 2;
        int cell = (start.getCell() + end.getCell()) / 2;
        return board[row][cell];
    }

    /**
     * Calculates whether a player can make a jump move by
     * identifying each piece on the board and whether they have a jump move available
     *
     * @param playerColor the player's color that will determine which pieces they can move
     * @return true if the player can make a jump move, false otherwise
     */
    public boolean playerCanJump(Piece.Color playerColor) {
        Piece piece;
        int rowIdx = 0;
        for(Space[] row : board) {
            for(Space space : row) {
                piece = space.getPiece();
                if((piece != null) && (piece.getColor() == playerColor) && piece.hasJump(this, rowIdx, space.getCellIdx())) {
                    return true;
                }
            }
            rowIdx++;
        }
        return false;
    }

    /**
     * Returns the space of a valid position
     *
     * @param position the position to be checked
     * @return the position's location on the board if the position is valid, null otherwise
     */
    public Space getSpace(Position position) {
        if(validPosition(position)) {
            return board[position.getRow()][position.getCell()];
        }
        return null;
    }

    /**
     * Determines if a position is valid by ensuring both its column and row
     * are within the bounds of the board
     *
     * @param position the position to be checked
     * @return true if the position is valid, false otherwise
     */
    public boolean validPosition(Position position) {
        boolean validRow = ((position.getRow() < NUM_ROWS) && (position.getRow() >= 0));
        boolean validCol = ((position.getCell() < NUM_COLS) && (position.getCell() >= 0));
        return validRow && validCol;
    }

    /**
     * Returns the type of the last move made.
     *
     * @return the type of the last move made
     */
    public MoveType getLastMoveType() {
        return this.lastMoveType;
    }

    /**
     * Tracks the last type of move made by the player
     *
     * @param lastMoveType the last type of move made
     */
    public void setLastMoveType(MoveType lastMoveType) {
        this.lastMoveType = lastMoveType;
    }

    /**
     * Resets the jump data for the next potential move
     */
    public void resetJumpData() {
        this.playerHasJump = false;
        this.checkedJumps = false;
        this.isGameOver = false;
    }

    /**
     * Returns whether the player has a jump move available
     *
     * @return true or false, if there is a jump move available to the player
     */
    public boolean playerHasJump() {
        return playerHasJump;
    }

    /**
     * Return the Space at the Position passed in.
     *
     * @param position the Position
     * @return the Space at that position
     */
    public Space getSquare(Position position) {
        return board[position.getRow()][position.getCell()];
    }

    /**
     * Returns the row at the index given.
     *
     * @param rowIdx the index of the desired row
     * @return the row at the given index
     */
    public Space[] getRow(int rowIdx) {
        return board[rowIdx];
    }

    /**
     * Returns the current board.
     *
     * @return the current board
     */
    public Space[][] getBoard(){
        return board;
    }

    /**
     * Initialize the board to a valid initial board state.
     */
    public void initBoard() {
        for(int row = 0; row < NUM_ROWS; row++) {
            for(int col = 0; col < NUM_COLS; col++) {
                if((row + col) % 2 == 1) {
                    if(row <= 2) {
                        board[row][col] = new Space(col, new Single(Piece.Color.WHITE), true);
                    } else if(row >= 5) {
                        board[row][col] = new Space(col, new Single(Piece.Color.RED), true);
                    } else {
                        board[row][col] = new Space(col, null, true);
                    }
                } else {
                    board[row][col] = new Space(col, null, false);
                }
            }
        }
    }

    /**
     * Check if the player with the color passed in has any pieces left on the board.
     *
     * @return true if they have at least one piece left on the board, false otherwise
     * */
    public boolean piecesRemaining(Piece.Color playerColor){
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (board[row][col].getPiece() != null && board[row][col].getPiece().getColor() == playerColor){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the player with the color passed in has any available moves.
     *
     * @return true if they have at least one possible move
     * */
    public boolean movesRemaining(Piece.Color playerColor) {
        BoardView currentBoard = new BoardView(this, playerColor == Piece.Color.WHITE);
        for(int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Piece piece = currentBoard.getBoard()[row][col].getPiece();
                if (piece != null && piece.getColor() == playerColor) {
                    if(piece.hasMove(currentBoard, row, col) || piece.hasJump(currentBoard, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if the game is over.
     *
     * @return whether or not the game has ended
     */
    public boolean isGameOver() {
        return this.isGameOver;
    }

    /**
     * Return an iterator of the board.
     *
     * @return a linked list of the rows in the board
     */
    @Override
    public Iterator<Row> iterator(){
        Collection<Row> lst = new LinkedList<>();
        for(int row = 0; row < NUM_ROWS; row++) {
            lst.add(new Row(row, Arrays.asList(board[row])));
        }
        return lst.iterator();
    }

    /**
     * Return the string representation of the board.
     *
     * @return the string representation of the board
     */
    @Override
    public String toString(){
        return red.toString() + " : " + white.toString();
    }
}
