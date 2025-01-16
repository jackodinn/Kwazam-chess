package Controller;

import Model.*;
import View.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.Border;

public class ChessController {

    private ChessModel model;
    private Chessboard board;
    private Position selectedPos = null;
    private JLabel draggedPieceLabel = null;
    private ImageIcon draggedPieceIcon = null;
    private static final Border border = BorderFactory.createEmptyBorder();

    public ChessController(ChessModel model, Chessboard board) {
        System.out.println("Loading ChessController..");
        this.model = model;
        this.board = board;
        model.initializeChesspiece();
        board.initializeBoard(model);
        board.setVisible(true);
        InputHandler inputHandler = new InputHandler(board, model);
        board.addMouseListener(inputHandler);
        board.addMouseMotionListener(inputHandler);
    }

    // Handle drag-drop in board
    private class InputHandler extends MouseAdapter {

        private ChessModel model;
        private Chessboard board;

        public InputHandler(Chessboard board, ChessModel model) {
            this.board = board;
            this.model = model;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            clearHighlighting();
            int col = mapToBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
            int row = mapToBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());
            selectedPos = new Position(col, row);
            Chesspiece piece = model.getPiece(col, row);
            if (piece != null) {
                if (piece.getColor() == model.getCurrentColor()) {
                    board.selectedPiece = piece;
                    draggedPieceIcon = piece.getImagePath();
                    draggedPieceLabel = new JLabel(draggedPieceIcon);
                    draggedPieceLabel.setOpaque(false);
                    board.getLayeredPane().add(draggedPieceLabel, JLayeredPane.DRAG_LAYER); // Add dragged piece to LayeredPane
                    // Set the initial position of the dragged label
                    Point p = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane());
                    draggedPieceLabel.setBounds(p.x - draggedPieceIcon.getIconWidth() / 2, p.y - draggedPieceIcon.getIconHeight() / 2, draggedPieceIcon.getIconWidth(), draggedPieceIcon.getIconHeight());
                    board.repaint(); // Ensure the label is visible immediately
                    // Temporarily clear the icon from the original board label
                    JLabel pieceOnBoard = board.boardLabels[row][col];
                    pieceOnBoard.setIcon(null);
                    pieceOnBoard.repaint();

                    System.out.println("Piece selected at: " + col + ", " + row);

                    // Highlight valid moves when pressed
                    highlightValidMoves(piece);
                } else {
                    System.out.println("Cannot move enemy piece");
                }
            } else {
                System.out.println("No piece selected.");
            }
        }

        private void highlightValidMoves(Chesspiece piece) {
            if (piece != null) {
                Set<Position> validMoves = piece.ifValidMove(model);
                for (Position move : validMoves) {
                    int row = move.getY();
                    int col = move.getX();
                    Chesspiece checkEnemy = model.getPiece(col, row);
                    if (checkEnemy != null) {
                        board.boardLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                    } else {
                        board.boardLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                    }
                }
            }
        }

        private void clearHighlighting() {
            for (int r = 0; r < model.getBoardHeight(); r++) {
                for (int c = 0; c < model.getBoardWidth(); c++) {
                    board.boardLabels[r][c].setBorder(border); // Reset to default border
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (draggedPieceLabel != null) {
                Point p = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane());
                draggedPieceLabel.setLocation(p.x - draggedPieceIcon.getIconWidth() / 2, p.y - draggedPieceIcon.getIconHeight() / 2);
                board.getLayeredPane().repaint(); // Repaint the layered pane
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            clearHighlighting(); // Clear highlighting when mouse released
            if (board.selectedPiece != null && selectedPos != null) {
                int col = mapToBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
                int row = mapToBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());

                // Only attempt to move if the release position is different from the press position
                if (col != selectedPos.getX() || row != selectedPos.getY()) {
                    if (model.movePiece(selectedPos.getX(), selectedPos.getY(), col, row)) {
                        board.refreshBoard(model);
                        
                        if(model.isGameOver())
                        {
                            JOptionPane.showMessageDialog(board, "Game Over! " + model.getCurrentPlayer() + " wins!");
                            model.resetGame();
                            board.refreshBoard(model);
                        }
                        else
                        {
                            model.processRound();
                        }
                    } else {
                        // If the move is invalid, restore the icon to the original label
                        board.boardLabels[selectedPos.getY()][selectedPos.getX()].setIcon(draggedPieceIcon);
                        board.boardLabels[selectedPos.getY()][selectedPos.getX()].repaint();
                    }
                } else {
                    // If the mouse was released on the same square, restore the icon
                    board.boardLabels[selectedPos.getY()][selectedPos.getX()].setIcon(draggedPieceIcon);
                    board.boardLabels[selectedPos.getY()][selectedPos.getX()].repaint();
                }

                // Remove the dragged piece label
                if (draggedPieceLabel != null) {
                    board.getLayeredPane().remove(draggedPieceLabel);
                    draggedPieceLabel = null;
                    draggedPieceIcon = null;
                    board.getLayeredPane().repaint();
                }

                board.selectedPiece = null;
                selectedPos = null;
            } else {
                // If no piece was selected
                if (draggedPieceLabel != null) {
                    board.getLayeredPane().remove(draggedPieceLabel);
                    draggedPieceLabel = null;
                    draggedPieceIcon = null;
                    board.getLayeredPane().repaint();
                }
                board.selectedPiece = null;
                selectedPos = null;
            }
        }

        /**
         * Maps a pixel coordinate to a board array index.
         *
         * @param pixel The pixel coordinate (x or y).
         * @param dimension The full width or height of the chessboard.
         * @param boardSize The number of columns or rows on the chessboard.
         * @return The board array index.
         */
        private int mapToBoardCoordinate(int pixel, int dimension, int boardSize) {
            return pixel * boardSize / dimension;
        }
    }
}
