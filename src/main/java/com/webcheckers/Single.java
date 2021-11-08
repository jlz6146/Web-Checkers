package com.webcheckers.model;

import java.util.List;

/**
 * A class to represent a single piece.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class Single extends Piece{
    /**
     * Create a new single piece.
     *
     * @param color the color this piece should be
     */
    public Single(Piece.Color color) {
        super(Type.SINGLE, color);
    }

    /**
     * A copy constructor to create a copy of this piece.
     *
     * @return a copy of this piece
     */
    @Override
    public Piece getCopy() {
        return new Single(this.getColor());
    }

    /**
     * Checks if a move is a valid simple hop.
     *
     * @param move the move to be checked
     * @return whether or not the move is valid
     */
    @Override
    public boolean isMoveValid(Move move) {
        return move.isSimpleMove() && ((move.getStart().getRow() - move.getEnd().getRow()) == 1);
    }

    /**
     * Checks if a move is a valid jump.
     *
     * @param move the move to be checked
     * @param jumpedSquare the square jumped over
     * @param endSpace the jump this square ends on
     * @return whether or not the move is valid
     */
    @Override
    public boolean isJumpValid(Move move, Space jumpedSquare, Space endSpace) {
        boolean captured = (jumpedSquare.getPiece() != null) && (jumpedSquare.getPiece().getColor() != this.getColor());
        return move.isJump() && ((move.getStart().getRow() - move.getEnd().getRow()) == 2) && captured && (endSpace.getPiece() == null);
    }

    /**
     * Returns whether or not this piece has any possible valid jumps.
     *
     * @param startRow the row this piece is on
     * @param startCell the cell this piece is on
     * @param board the current board
     * @return whether or not this piece can jump
     */
    @Override
    public boolean hasJump(BoardView board, int startRow, int startCell) {
        Move jump;
        Position start = new Position(startRow, startCell);
        Position end = new Position(startRow - 2, startCell - 2);
        jump = new Move(start, end);
        if(end.isValid() && this.isJumpValid(jump, board.getJumpedSquare(jump), board.getSquare(end))) {
            return true;
        } else {
            end = new Position(startRow - 2, startCell + 2);
            jump = new Move(start, end);
            return end.isValid() && this.isJumpValid(jump, board.getJumpedSquare(jump), board.getSquare(end));
        }
    }

    /**
     * Returns whether or not this piece has any possible valid moves.
     *
     * @param board     the current board
     * @param startRow  the row this piece is on
     * @param startCell the cell this piece is on
     * @return whether or not this piece can move
     */
    @Override
    public boolean hasMove(BoardView board, int startRow, int startCell) {
        Move move;
        Position start = new Position(startRow, startCell);

        List<List<Integer>> signs = List.of(List.of(-1, -1), List.of(-1, 1));
        for(List<Integer> pair : signs) {
            Position end = new Position(startRow + pair.get(0), startCell + pair.get(1));
            move = new Move(start, end);
            if(end.isValid() && this.isMoveValid(move) && (board.getSquare(end).getPiece() == null)) {
                return true;
            }
        }
        return false;
    }
}
