package com.webcheckers.model;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.util.Message;

import java.util.LinkedList;
import java.util.Objects;

/**
 * A class to represent a game of web checkers.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class CheckersGame {
    //Values used to determine the status of the current game being played
    private Player red;
    private Player white;
    private Mode mode;
    private BoardView board;
    private int gameID;
    private Piece.Color currentColor;
    private LinkedList<Move> turnMoves;
    private boolean isGameOver;
    private boolean newTurn = false;
    private EndReason endReason;
    private Piece.Color endingColor;

    // Messages to alert to a properly submitted and backed up move
    static final Message MOVE_BACKED_UP_MESSAGE = Message.info("Move backed up!");
    static final Message TURN_SUBMITTED_MESSAGE = Message.info("Turn submitted successfully!");

    // Messages to alert to an unmade move/invalid single move when jump move is available
    static final Message NO_MOVES_MADE_MESSAGE = Message.error("You have not made any moves yet!");
    static final Message JUMP_EXISTS_MESSAGE = Message.error("When a jump is possible, you must must jump!!");

    // Message to alert the player the the game has ended
    static final String GAME_OVER_MESSAGE = "The game has ended!";
    static final String ALL_PIECES_CAPTURED_MESSAGE = "%s has captured all of %s's pieces.";
    static final String PLAYER_RESIGNED_MESSAGE = "%s has resigned.";
    static final String NO_MOVES_LEFT_MESSAGE = "%s has no moves left. %s wins.";

    /**
     * An enum for the different modes a user could view a game in (SPECTATOR and REPLAY are for enhancements).
     */
    public enum Mode {
        PLAY,
        SPECTATOR
    }

    /**
     * An enum for the different reasons a game ended.
     */
    public enum EndReason {
        CAPTURED,
        RESIGNED,
        NO_MOVES_LEFT
    }

    /**
     * Get a copy of the current game board. This copy can be flipped if necessary.
     *
     * @param flip whether or not to flip the board
     * @return a copy of the board
     */
    public BoardView getBoard(boolean flip){
        return new BoardView(board, flip);
    }

    /**
     * Returns current color of the player in turn
     *
     * @return currentColor, holding the color of the player
     */
    public Piece.Color getCurrentColor() {
        return currentColor;
    }

    /**
     * Returns a message alerting to whether the game is over
     *
     * @return a message if the game is over
     */
    public String gameOverMessage() {
        if(isGameOver) {
            Piece.Color losingColor;
            if(endingColor == Piece.Color.RED) {
                losingColor = Piece.Color.WHITE;
            } else {
                losingColor = Piece.Color.RED;
            }
            if(this.endReason == EndReason.CAPTURED) {
                return String.format(ALL_PIECES_CAPTURED_MESSAGE, getPlayer(endingColor), getPlayer(losingColor));
            } else if(endReason == EndReason.RESIGNED) {
                return String.format(PLAYER_RESIGNED_MESSAGE, getPlayer(endingColor));
            } else if(endReason == EndReason.NO_MOVES_LEFT) {
                return String.format(NO_MOVES_LEFT_MESSAGE, getPlayer(losingColor), getPlayer(endingColor));
            } else {
                return GAME_OVER_MESSAGE;
            }
        }
        return null;
    }

    /**
     * Get the player with the color passed in.
     *
     * @param playerColor the color of the desired player
     * @return the player with said color
     */
    public Player getPlayer(Piece.Color playerColor) {
        if(playerColor == Piece.Color.RED) {
            return red;
        } else {
            return white;
        }
    }

    /**
     * Create a new game of web checkers.
     *
     * @param red the player using red pieces
     * @param white the player using white pieces
     * @param mode what mode the game is in
     */
    public CheckersGame(Player red, Player white, Mode mode, BoardView board) {
        this.red = red;
        red.setColor(Piece.Color.RED);
        this.white = white;
        white.setColor(Piece.Color.WHITE);
        this.mode = mode;
        this.board = board;
        this.gameID = Objects.hash(red, white, mode);
        this.currentColor = Piece.Color.RED;
        this.turnMoves = new LinkedList<>();
        this.isGameOver = false;
    }

    /**
     * Determines if the game is completed
     *
     * @return true if isGameOver is true, false otherwise
     */
    public boolean isGameOver() {
        return this.isGameOver;
    }

    /**
     * Ends the game.
     * We're in the endgame now. ;)
     *
     * @param endReason why the game ended
     */
    public void endGame(EndReason endReason, Piece.Color endingColor) {
        this.endReason = endReason;
        this.endingColor = endingColor;
        this.isGameOver = true;
    }

    /**
     * Return the player using red pieces.
     *
     * @return the player using red pieces
     */
    public Player redPlayer() {
        return red;
    }

    /**
     * Return the player using white pieces.
     *
     * @return the player using white pieces
     */
    public Player whitePlayer() {
        return white;
    }

    /**
     * Return the mode the game is in.
     *
     * @return the mode the game is in
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Return this game's gameID.
     *
     * @return the gameID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Test if a move is valid.
     *
     * @param move the move to be tested
     * @return a message about the tested move
     */
    public Message testMove(Move move) {
        // flip the board if it is the white player's turn
        BoardView copy = new BoardView(board, (currentColor == Piece.Color.WHITE));
        for (Move turnMove : turnMoves) {
            copy.makeMove(turnMove);
        }

        Message message = copy.checkMove(move, currentColor);
        if(message.getType() == Message.Type.INFO) {
            turnMoves.addLast(move);
        }

        return message;
    }

    /**
     * Clear the list of turn moves.
     */
    public void clearTurnMoves() {
        this.turnMoves.clear();
        this.board.setLastMoveType(BoardView.MoveType.NONE);
    }

    /**
     * Get the moves made this turn.
     *
     * @return a list of the move made this turn
     */
    protected LinkedList<Move> getTurnMoves() {
        return turnMoves;
    }

    /**
     * Tracks the latest move made and backs it up
     *
     * @return a message determining if a move was not made or was backed up
     */
    public Message backupMove() {
        if(turnMoves.size() == 0) {
            return NO_MOVES_MADE_MESSAGE;
        }
        board.resetJumpData();
        turnMoves.removeLast();
        if(turnMoves.size() == 0) {
            board.setLastMoveType(BoardView.MoveType.NONE);
        } else if(turnMoves.getLast().isSimpleMove()) {
            board.setLastMoveType(BoardView.MoveType.SIMPLE);
        } else {
            board.setLastMoveType(BoardView.MoveType.JUMP);
        }
        return MOVE_BACKED_UP_MESSAGE;
    }

    /**
     * Submits a move made by a player to be displayed on the board. Move will not
     * be submitted if it is a non-jump being made while a jump move is available
     *
     * @return TURN_SUBMITTED_MESSAGE if a move can be submitted, NO_MOVES_MADE_MESSAGE or JUMP_EXISTS_MESSAGE otherwise
     */
    public Message submitTurn() {
        if(turnMoves.size() == 0) {
            return NO_MOVES_MADE_MESSAGE;
        }
        BoardView copy = new BoardView(board, currentColor == Piece.Color.WHITE);

        if(turnMoves.getLast().isSimpleMove() && copy.playerCanJump(currentColor)) {
            return JUMP_EXISTS_MESSAGE;
        } else {
            for (Move turnMove : turnMoves) {
                copy.makeMove(turnMove);
            }
            Position endPosition = turnMoves.getLast().getEnd();
            Space endSpace = copy.getSquare(endPosition);
            if(endSpace.getPiece().hasJump(copy, endPosition.getRow(), endPosition.getCell()) && turnMoves.getLast().isJump()) {
                return JUMP_EXISTS_MESSAGE;
            }
        }

        for (Move turnMove : turnMoves) {
            if(currentColor == Piece.Color.WHITE) {
                board.makeMove(turnMove.inverse());
            } else {
                board.makeMove(turnMove);
            }
        }

        Move move = turnMoves.getLast();
        if(currentColor == Piece.Color.WHITE) {
            move = move.inverse();
        }
        Position end = move.getEnd();
        Space endSpace = board.getSpace(end);
        Piece piece = endSpace.getPiece();
        boolean redKing = (currentColor == Piece.Color.RED) && (end.getRow() == 0);
        boolean whiteKing = (currentColor == Piece.Color.WHITE) && (end.getRow() == 7);
        if((piece.getType() == Piece.Type.SINGLE) && (redKing || whiteKing)) {
            endSpace.setPiece(new King(piece.getColor()));
        }

        Piece.Color opponentColor;
        if(currentColor == Piece.Color.RED) {
            opponentColor = Piece.Color.WHITE;
        } else {
            opponentColor = Piece.Color.RED;
        }

        isGameOver = board.isGameOver();
        if(isGameOver) {
            if(!board.piecesRemaining(opponentColor)) {
                endGame(EndReason.CAPTURED, currentColor);
            } else {
                endGame(EndReason.NO_MOVES_LEFT, currentColor);
            }
        } else {
            board.resetJumpData();
        }

        if(currentColor == Piece.Color.RED) {
            currentColor = Piece.Color.WHITE;
        } else {
            currentColor = Piece.Color.RED;
        }

        turnMoves.clear();
        board.setLastMoveType(BoardView.MoveType.NONE);
        newTurn = true;
        return TURN_SUBMITTED_MESSAGE;
    }

    /**
     * Check if there is a new turn available.
     *
     * @return whether or not a new turn available.
     */
    public boolean isNewTurn() {
        return newTurn;
    }

    /**
     * Set whether or not there is a new turn available.
     *
     * @param newTurn the new value of this.newTurn
     */
    public void setNewTurn(boolean newTurn) {
        this.newTurn = newTurn;
    }

    /**
     * Return the string representation of this CheckersGame.
     *
     * @return the string representation of this CheckersGame
     */
    @Override
    public String toString() {
        return red.getName() + " VS " + white.getName();
    }
}
