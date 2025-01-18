// Chessboard.java
package View;

import Model.*;
import java.awt.*;
import javax.swing.*;

public class Chessboard extends JFrame {

    public JLabel[][] boardLabels;
    private int height = 8; // Number of rows
    private int width = 5;  // Number of columns
    private boolean isFlipped = false; // Track if the board is flipped

    public Chesspiece selectedPiece;

    public Chessboard() {
        this.boardLabels = new JLabel[height][width];
        setTitle("Kwazam Chess");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int squareSize = 100;
        setSize(width * squareSize, height * squareSize);
        setLayout(new GridLayout(height, width));
    }

    public void initializeBoard(ChessModel model) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);

                // Add a checkerboard background
                if ((row + col) % 2 == 0) {
                    label.setBackground(Color.WHITE);
                } else {
                    label.setBackground(Color.GRAY);
                }

                label.setOpaque(true);
                Chesspiece piece = model.getPiece(col, row);
                if (piece != null) {
                    label.setIcon(piece.getImagePath());
                }

                boardLabels[row][col] = label;
                add(label);
            }
        }
    }

    public void refreshBoard(ChessModel model) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int displayRow = isFlipped ? height - 1 - row : row;
                int displayCol = isFlipped ? width - 1 - col : col;

                Chesspiece piece = model.getPiece(col, row);
                JLabel label = boardLabels[displayRow][displayCol];
                if (piece != null) {
                    label.setIcon(piece.getImagePath());
                } else {
                    label.setIcon(null);
                }
            }
        }
    }

    public void flipBoard(ChessModel model) {
        isFlipped = !isFlipped;
        rotatePieceImages(model);
        refreshBoard(model);
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    private void rotatePieceImages(ChessModel model) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Chesspiece piece = model.getPiece(col, row);
                if (piece != null) {
                    ImageIcon currentIcon = piece.getImagePath();
                    ImageIcon rotatedIcon = piece.rotateImageIcon(currentIcon);
                    piece.setImageIcon(rotatedIcon);
                }
            }
        }
    }
}
