package org.example.chess;

import org.example.boardgame.Board;
import org.example.boardgame.Piece;
import org.example.boardgame.Position;
import org.example.chess.pieces.King;
import org.example.chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {

        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        check = false;
        initialSetup();

    }

    public int getTurn() { return turn; }
    public Color getCurrentPlayer() { return currentPlayer; }
    public boolean getCheck() {

        return check;

    }

    public ChessPiece[][] getPieces(){

        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for (int i = 0; i < board.getRows(); i ++){

            for (int j = 0; j < board.getColumns(); j ++){

                mat[i][j] = (ChessPiece) board.piece(i, j);

            }

        }

        return mat;

    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {

        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);

        return board.piece(position).possibleMoves();

    }

    private void nextTurn() {

        turn ++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;

    }

    private Color opponent(Color color) {

        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;

    }

    private ChessPiece king(Color color) {

        List<Piece> list = piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == color)
                .toList();

        for (Piece piece : list) {

            if (piece instanceof King) {

                return (ChessPiece) piece;

            }

        }

        throw new IllegalStateException("There is no " + color + " King on the board");

    }

    private boolean testCheck(Color color) {

        Position kingPosition = king(color).getChessposition().toPosition();

        List<Piece> opponentPieces = piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == opponent(color))
                .toList();

        for (Piece p : opponentPieces) {

            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()] == true) {

                return true;

            }

        }

        return false;

    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {

        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {

            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");

        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        nextTurn();

        return (ChessPiece) capturedPiece;

    }

    private Piece makeMove(Position source, Position target) {

        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {

            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);

        }

        return capturedPiece;

    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {

        Piece p = board.removePiece(target);
        board.placePiece(p, source);

        if (capturedPiece != null) {

            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);

        }

    }

    private void validateSourcePosition(Position position) {

        if (!board.thereIsAPiece(position)){

            throw new ChessException("There is no Piece on source position");

        }

        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {

            throw new ChessException("The chosen piece is not yours");

        }

        if (!board.piece(position).isThereAnyPossibleMove()) {

            throw new ChessException("There is no possible moves for the chosen piece");

        }

    }

    private void validateTargetPosition(Position source, Position target) {

        if (!board.piece(source).possibleMove(target)) {

            throw new ChessException("The chosen Piece can't move to target position");

        }

    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {

        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);

    }

    private void initialSetup(){

        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));

    }

}
