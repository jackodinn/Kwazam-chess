/* InputHandler.java
 - To handle input events on the chessboard
 - MousePressed event to handle the initial click
 - MouseDragged event to handle the dragging of a piece
 - MouseReleased event to handle the final release of a piece
 - Members invovlved: Andrew Wee & Lai Zi Xuan
*/
package Controller;

import Model.*;
import View.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.Border;

// Handle drag/drop events - Andrew Wee & Lai Zi Xuan
public class InputHandler extends MouseAdapter {

    private ChessModel model;
    private Chessboard board;
    private Position selectedPos = null;
    private JLabel draggedPieceLabel = null;
    private ImageIcon draggedPieceIcon = null;
    private static final Border border = BorderFactory.createEmptyBorder();

    // Constructor - Andrew Wee
    public InputHandler(Chessboard board, ChessModel model) {
        this.board = board;
        this.model = model;
    }

    // Event where somewhere is clicked on the board - Andrew Wee & Lai Zi Xuan
    @Override
    public void mousePressed(MouseEvent e) {
        clearHighlighting();
        // To pinpoint where has been clicked
        int col = mapToBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
        int row = mapToBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());

        // When flipped, reverse the coordinates tracking
        if (board.isFlipped()) {
            col = model.getBoardWidth() - 1 - col;
            row = model.getBoardHeight() - 1 - row;
        }

        selectedPos = new Position(col, row);
        Chesspiece piece = model.getPiece(col, row);
        if (piece != null) {
            // If a piece is clicked
            if (piece.getColor() == model.getCurrentColor()) {
                board.selectedPiece = piece;
                draggedPieceIcon = piece.getImagePath();
                draggedPieceLabel = new JLabel(draggedPieceIcon);
                draggedPieceLabel.setOpaque(false);
                board.getLayeredPane().add(draggedPieceLabel, JLayeredPane.DRAG_LAYER); // Take selected piece and throw into layered pane level
                Point p = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane()); // Pinpoint cursor
                draggedPieceLabel.setBounds(p.x - draggedPieceIcon.getIconWidth() / 2, 
                  p.y - draggedPieceIcon.getIconHeight() / 2, // Centering the icon
                draggedPieceIcon.getIconWidth(), draggedPieceIcon.getIconHeight()); // Make dragged icon follow cursor
                board.repaint();
                // Temporarily clear the icon from the original board label
                if (board.isFlipped()) {
                    col = model.getBoardWidth() - 1 - col;
                    row = model.getBoardHeight() - 1 - row;
                    JLabel pieceOnBoard = board.boardLabels[row][col]; // When dragging piece, hide the original icon
                    pieceOnBoard.setIcon(null);
                    pieceOnBoard.repaint();
                } else {
                    JLabel pieceOnBoard = board.boardLabels[row][col];
                    pieceOnBoard.setIcon(null);
                    pieceOnBoard.repaint();
                }
                // While dragging, highlight available moves
                highlightValidMoves(piece);
            } else {
                // If current player try to move enemy piece
                System.out.println("WARNING! Cannot move enemy piece");
            }
        } else {
            // If nothing is clicked
            System.out.println("No piece selected.");
        }
    }

    // Directly use piece movement logic as parameter to highlight valid moves - Andrew Wee & Lai Zi Xuan
    private void highlightValidMoves(Chesspiece piece) {
        if (piece != null) {
            Set<Position> validMoves = piece.ifValidMove(model);
            for (Position move : validMoves) {
                int row = move.getY();
                int col = move.getX();
                if (board.isFlipped()) {
                    row = model.getBoardHeight() - 1 - row;
                    col = model.getBoardWidth() - 1 - col;
                }
                Chesspiece checkEnemy = model.getPiece(move.getX(), move.getY());
                if (checkEnemy != null) {
                    board.boardLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                } else {
                    board.boardLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                }
            }
        }
    }

    // Clear highlighting after completing an action - Andrew Wee
    private void clearHighlighting() {
        for (int r = 0; r < model.getBoardHeight(); r++) {
            for (int c = 0; c < model.getBoardWidth(); c++) {
                board.boardLabels[r][c].setBorder(border); // Reset to default border
            }
        }
    }

    // Event where a piece is dragged on the board - Andrew Wee
    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedPieceLabel != null) {
            // If a piece is currently being dragged
            Point p = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane());
            draggedPieceLabel.setLocation(p.x - draggedPieceIcon.getIconWidth() / 2, 
              p.y - draggedPieceIcon.getIconHeight() / 2);
            board.getLayeredPane().repaint(); // Repaint the layered pane
        }
    }

    // Event where a piece is released on the board - Andrew Wee & Lai Zi Xuan
    @Override
    public void mouseReleased(MouseEvent e) {
        clearHighlighting(); // Clear highlighting when mouse released
        if (board.selectedPiece != null && selectedPos != null) {
            // If a piece is selected and released
            int col = mapToBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
            int row = mapToBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());

            if (board.isFlipped()) {
                col = model.getBoardWidth() - 1 - col;
                row = model.getBoardHeight() - 1 - row;
            }

            if (col != selectedPos.getX() || row != selectedPos.getY()) {
                // If release position is different from the initial position
                if (model.movePiece(selectedPos.getX(), selectedPos.getY(), col, row)) {
                    // If move is successful
                    board.refreshBoard(model);
                    if (model.isGameOver()) {
                        // If somebody won
                        JOptionPane.showMessageDialog(board, "Game Over! " + model.getCurrentPlayer() + " wins!");
                        model.closeGame(board);
                    } else {
                        // If nobody won yet
                        model.processRound(board.selectedPiece);
                        board.updateTitle(model.getCurrentPlayer(), model.getRound());
                        board.flipBoard(model); // Flip the board after each turn
                        board.refreshBoard(model);
                    }
                } else {
                    // If invalid move
                    board.boardLabels[selectedPos.getY()][selectedPos.getX()].setIcon(draggedPieceIcon);
                    board.boardLabels[selectedPos.getY()][selectedPos.getX()].repaint();
                    board.refreshBoard(model);
                }
            } else {
                // If same square
                board.boardLabels[selectedPos.getY()][selectedPos.getX()].setIcon(draggedPieceIcon);
                board.boardLabels[selectedPos.getY()][selectedPos.getX()].repaint();
                board.refreshBoard(model);
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

    // To convert a pixel on the board to a corresponding space on the board
    private int mapToBoardCoordinate(int pixel, int dimension, int boardSize) {
        return pixel * boardSize / dimension;
    }
}