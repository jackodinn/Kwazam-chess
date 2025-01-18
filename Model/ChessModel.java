package Model;

import View.Chessboard;
import java.awt.*;
import java.util.*;

public class ChessModel {

    protected Color currentPlayer;
    private int round = 0;
    private Chesspiece[][] board;

    public ChessModel() {
        System.out.println("Loading Model..");
        board = new Chesspiece[8][5];
        currentPlayer = Color.blue;
    }
    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void incRound() {
        round++;
    }
    public void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Color getCurrentColor() {
        return currentPlayer;
    }

    public String getCurrentPlayer() {
        if (currentPlayer == Color.BLUE) {
            return "Blue";
        } else {
            return "Red";
        }
    }

    public Chesspiece[][] getBoard() {
        return board;
    }

    public int getBoardWidth() {
        return board[0].length; // Number of columns
    }

    public int getBoardHeight() {
        return board.length;    // Number of rows
    }

    public void setPiece(int col, int row, Chesspiece cp) {
        if (col >= 0 && row >= 0 && row < board.length && col < board[row].length) { // Corrected order
            board[row][col] = cp;
            if (cp != null) {
                cp.setPos(new Position(col, row));
            }
        }
    }

    public Chesspiece getPiece(int col, int row) {
        if (col >= 0 && row >= 0 && row < board.length && col < board[row].length) { // Corrected order
            return board[row][col];
        }
        return null;
    }

    public void initializeChesspiece() {
        for (int col = 0; col < 5; col++) {
            board[6][col] = new Ram(Color.BLUE, "/images/RamBlue.png", new Position(col, 6)); // Keep position correct
            switch (col) {
                case 0:
                    board[7][col] = new Xor(Color.BLUE, "/images/XorBlue.png", new Position(col, 7));
                    break;
                case 1:
                    board[7][col] = new Biz(Color.BLUE, "/images/BizBlue.png", new Position(col, 7));
                    break;
                case 2:
                    board[7][col] = new Sau(Color.BLUE, "/images/SauBlue.png", new Position(col, 7));
                    break;
                case 3:
                    board[7][col] = new Biz(Color.BLUE, "/images/BizBlue.png", new Position(col, 7));
                    break;
                case 4:
                    board[7][col] = new Tor(Color.BLUE, "/images/TorBlue.png", new Position(col, 7));
                    break;
            }
        }

        for (int col = 0; col < 5; col++) {
            board[1][col] = new Ram(Color.RED, "/images/Ram.png", new Position(col, 1));
            switch (col) {
                case 0:
                    board[0][col] = new Tor(Color.RED, "/images/Tor.png", new Position(col, 0));
                    break;
                case 1:
                    board[0][col] = new Biz(Color.RED, "/images/Biz.png", new Position(col, 0));
                    break;
                case 2:
                    board[0][col] = new Sau(Color.RED, "/images/Sau.png", new Position(col, 0));
                    break;
                case 3:
                    board[0][col] = new Biz(Color.RED, "/images/Biz.png", new Position(col, 0));
                    break;
                case 4:
                    board[0][col] = new Xor(Color.RED, "/images/Xor.png", new Position(col, 0));
                    break;
            }
        }
    }

    public boolean movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        if (!checkBorder(fromCol, fromRow) || !checkBorder(toCol, toRow)) {
            return false;
        }
        Chesspiece movingPiece = getPiece(fromCol, fromRow);
        Set<Position> validMoves;
        validMoves = movingPiece.ifValidMove(this);
        System.out.println("Moving Piece: " + movingPiece);
        System.out.println("Valid Moves: " + validMoves);
        System.out.println("Target Position: (" + toCol + ", " + toRow + ")");

        if (!validMoves.contains(new Position(toCol, toRow))) {
            System.out.println("Invalid move for this piece.");
            return false;
        }

        Chesspiece targetPiece = getPiece(toCol, toRow);
        if (targetPiece != null) {
            if (targetPiece.getColor().equals(movingPiece.getColor())) {
                System.out.println("Cannot move to a position occupied by your own piece.");
                return false;
            } else {
                System.out.println("Capturing opponent's piece.");
                board[toRow][toCol] = null;
            }
        }

        board[toRow][toCol] = movingPiece;
        board[fromRow][fromCol] = null;
        movingPiece.setPos(new Position(toCol, toRow));
        System.out.println("Moved piece to (" + toCol + ", " + toRow + ")");
        return true;
    }

    private boolean checkBorder(int col, int row) {
        return col >= 0 && row >= 0 && row < getBoardHeight() && col < getBoardWidth();
    }

    public void processRound(Chesspiece piece) {
        incRound();
        if (round % 2 == 0) {
            setCurrentPlayer(Color.BLUE);
            System.out.println(getCurrentPlayer() + "'s Turn");
        } else {
            setCurrentPlayer(Color.RED);
            System.out.println(getCurrentPlayer() + "'s Turn");
        }
        if (round % 4 == 0) {
            transPiece();
        }
        if (piece.getClass().getSimpleName().equals("Ram")) {
            int y = piece.getPos().getY();
            if (y == 0 || y == 7) {
                piece.setImageIcon(piece.rotateImageIcon(piece.getImagePath()));
            }
        }
    }

    public void clearChessPiece() {
        for (int y = 0; y < getBoardHeight(); y++) {
            for (int x = 0; x < getBoardHeight(); x++) {
                board[y][x] = null;
            }
        }
    }

    public void closeGame(Chessboard board) {
        System.out.println("Closing game...");
        board.dispose();
    }

    public void transPiece() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Chesspiece piece = board[row][col];
                if (piece != null) {
                    Color tempColor;
                    if (piece.getClass().getSimpleName().equals("Tor")) {

                        tempColor = piece.getColor();
                        board[row][col] = null;
                        if (tempColor == Color.RED) {
                            board[row][col] = new Xor(Color.RED, "/images/Xor.png", new Position(col, row));
                        } else {
                            board[row][col] = new Xor(Color.BLUE, "/images/XorBlue.png", new Position(col, row));
                        }
                    } else if (piece.getClass().getSimpleName().equals("Xor")) {
                        tempColor = piece.getColor();
                        board[row][col] = null;
                        if (tempColor == Color.RED) {
                            board[row][col] = new Tor(Color.RED, "/images/Tor.png", new Position(col, row));
                        } else {
                            board[row][col] = new Tor(Color.BLUE, "/images/TorBlue.png", new Position(col, row));
                        }
                    }
                }
            }
        }
    }

    public boolean isSauCaptured(Color color) {
        for (int y = 0; y < getBoardHeight(); y++) {
            for (int x = 0; x < getBoardWidth(); x++) {
                Chesspiece cp = board[y][x];
                if (cp instanceof Sau && cp.getColor().equals(color)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSauInCheck(Color sauColor) {
        Position sauPos = null;
        for (int y = 0; y < getBoardHeight(); y++) {
            for (int x = 0; x < getBoardWidth(); x++) {
                Chesspiece cp = getPiece(x, y);
                if (cp instanceof Sau && cp.getColor().equals(sauColor)) {
                    sauPos = new Position(x, y);
                    break;
                }
            }
            if (sauPos != null) {
                break;
            }
        }

        if (sauPos == null) {
            return false;
        }

        for (int y = 0; y < getBoardHeight(); y++) {
            for (int x = 0; x < getBoardWidth(); x++) {
                Chesspiece cp = getPiece(x, y);
                if (cp != null && !cp.getColor().equals(sauColor)) {
                    Set<Position> validMoves = cp.ifValidMove(this);
                    if (validMoves.contains(sauPos)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public boolean isGameOver() {
        // Check if either Sau is captured
        if (isSauCaptured(Color.BLUE)) {
            System.out.println("Red wins! Blue's Sau has been captured.");
            return true;
        }
        if (isSauCaptured(Color.RED)) {
            System.out.println("Blue wins! Red's Sau has been captured.");
            return true;
        }
        return false; // Game is not over
    }
}
