package org.example;

import org.example.boardgame.Board;
import org.example.boardgame.Position;
import org.example.chess.ChessException;
import org.example.chess.ChessMatch;
import org.example.chess.ChessPiece;
import org.example.chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ChessMatch chessMatch = new ChessMatch();

        while (true) {

            try {

                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);


            } catch (ChessException e) {

                System.out.println(e.getMessage());
                sc.nextLine();

            } catch (InputMismatchException e) {

                System.out.println(e.getMessage());
                sc.nextLine();

            }

        }

    }

}