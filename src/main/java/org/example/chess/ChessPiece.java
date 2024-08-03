package org.example.chess;

import org.example.boardgame.Board;
import org.example.boardgame.Piece;
import org.example.boardgame.Position;

public abstract class ChessPiece extends Piece {

    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {

        super(board);
        this.color = color;

    }

    public Color getColor() {
        return color;
    }
    public ChessPosition getChessposition() {

        return ChessPosition.fromPosition(position);

    }

    public int getMoveCount() {

        return moveCount;

    }

    protected void increaseMoveCount() {

        moveCount ++;

    }

    protected void decreaseMoveCount() {

        moveCount --;

    }

    protected boolean isThereOpponentPiece(Position position) {

        ChessPiece p = (ChessPiece) getBoard().piece(position);

        return ((p != null) && (p.getColor() != color));

    }

}
