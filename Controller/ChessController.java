package Controller;

import Model.*;
import View.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.Border;

public class ChessController {

    private final ChessModel model;
    private final Chessboard board;
    private Position selectedPos = null;
    private JLabel draggedPieceLabel = null;
    private ImageIcon draggedPieceIcon = null;
    private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(); // Default border for board cells

    // Constructor to initialize the ChessController with the model and board
    public ChessController(ChessModel model, Chessboard board) {
        System.out.println("Initializing ChessController...");
        this.model = model;
        this.board = board;
        initializeGame();
    }

    // Method to initialize the game, set up the board, and set mouse listeners
    private void initializeGame() {
        model.initializeChesspiece(); // Initialize pieces
        board.initializeBoard(model);  // Set up the board visually
        board.setVisible(true);
        
        // Register mouse event handlers for dragging and dropping pieces
        InputHandler inputHandler = new InputHandler();
        board.addMouseListener(inputHandler);
        board.addMouseMotionListener(inputHandler);
    }

    // InputHandler listens for mouse events
    private class InputHandler extends MouseAdapter {
        
        // Called when mouse is pressed down on the board
        @Override
        public void mousePressed(MouseEvent e) {
            handleMousePressed(e);
        }

        // Called when mouse is dragged over the board
        @Override
        public void mouseDragged(MouseEvent e) {
            handleMouseDragged(e);
        }

        // Called when mouse is released on the board
        @Override
        public void mouseReleased(MouseEvent e) {
            handleMouseReleased(e);
        }
    }

    // Handle mouse press: Select piece and start dragging it
    private void handleMousePressed(MouseEvent e) {
        clearHighlighting();  // Reset any previous highlights

        // Calculate the column and row on the board where the mouse was clicked
        int col = mapToBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
        int row = mapToBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());
        
        selectedPos = new Position(col, row);  // Store the selected position
        Chesspiece piece = model.getPiece(col, row);  // Get the piece at the clicked position

        // If a piece of the correct color is selected, start dragging
        if (piece != null && piece.getColor() == model.getCurrentColor()) {
            preparePieceForDragging(e, piece, row, col);
            highlightValidMoves(piece);  // Highlight valid moves for the selected piece
        } else {
            // If no piece is selected or it’s the wrong color
            System.out.println(piece == null ? "No piece selected." : "Cannot move enemy piece.");
            selectedPos = null;  // Deselect
        }
    }

    // Prepare a piece for dragging by creating a draggable label
    private void preparePieceForDragging(MouseEvent e, Chesspiece piece, int row, int col) {
        draggedPieceIcon = piece.getImagePath();  // Get the image of the piece
        draggedPieceLabel = new JLabel(draggedPieceIcon);  // Create label for dragged piece
        draggedPieceLabel.setOpaque(false);  // Make the label transparent
        board.getLayeredPane().add(draggedPieceLabel, JLayeredPane.DRAG_LAYER); // Add the piece to the layered pane

        // Convert mouse coordinates to board coordinates and set the position of the dragged piece
        Point point = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane());
        draggedPieceLabel.setBounds(
            point.x - draggedPieceIcon.getIconWidth() / 2,
            point.y - draggedPieceIcon.getIconHeight() / 2,
            draggedPieceIcon.getIconWidth(),
            draggedPieceIcon.getIconHeight()
        );

        // Temporarily remove the piece from the board
        board.boardLabels[row][col].setIcon(null);
        board.repaint();  // Update the board view
        System.out.println("Piece selected at: " + col + ", " + row);
    }

    // Handle mouse drag: Move the dragged piece across the board
    private void handleMouseDragged(MouseEvent e) {
        if (draggedPieceLabel != null) {
            // Update the position of the dragged piece label as the mouse moves
            Point point = SwingUtilities.convertPoint(board, e.getX(), e.getY(), board.getLayeredPane());
            draggedPieceLabel.setLocation(
                point.x - draggedPieceIcon.getIconWidth() / 2,
                point.y - draggedPieceIcon.getIconHeight() / 2
            );
            board.getLayeredPane().repaint();  // Repaint the layered pane to show the updated position
        }
    }

    // Handle mouse release: Drop the piece in a valid or invalid position
    private void handleMouseReleased(MouseEvent e) {
        if (selectedPos == null || draggedPieceLabel == null) {
            cleanupDraggedPiece();  // If no piece was selected, clean up
            return;
        }

        // Calculate the release position on the board
        int col = mapToBoardCoordinate(e.getX(), board.getWidth(), model.getBoardWidth());
        int row = mapToBoardCoordinate(e.getY(), board.getHeight(), model.getBoardHeight());

        // Attempt to move the piece if the release position is valid
        if (model.movePiece(selectedPos.getX(), selectedPos.getY(), col, row)) {
            board.refreshBoard(model);  // Refresh the board after the move

            // If the game is over, notify the user and reset the game
            if (model.isGameOver()) {
                JOptionPane.showMessageDialog(board, "Game Over! " + model.getCurrentPlayer() + " wins!");
                model.resetGame();
                board.refreshBoard(model);
            } else {
                model.processRound();  // Process the round if the game isn't over
                board.refreshBoard(model);
            }
        } else {
            // If the move is invalid, restore the piece to its original position
            restoreDraggedPieceToOriginalPosition();
        }

        cleanupDraggedPiece();  // Clean up after the move
    }

    // Restore the dragged piece to its original position if the move was invalid
    private void restoreDraggedPieceToOriginalPosition() {
        if (selectedPos != null) {
            board.boardLabels[selectedPos.getY()][selectedPos.getX()].setIcon(draggedPieceIcon);
            board.boardLabels[selectedPos.getY()][selectedPos.getX()].repaint();
        }
    }

    // Clean up the dragged piece label after a move or cancel
    private void cleanupDraggedPiece() {
        if (draggedPieceLabel != null) {
            board.getLayeredPane().remove(draggedPieceLabel);  // Remove the dragged piece from the screen
            draggedPieceLabel = null;  // Reset the label reference
            draggedPieceIcon = null;  // Reset the icon reference
            board.getLayeredPane().repaint();  // Repaint the layered pane to reflect the changes
        }
        selectedPos = null;  // Deselect the piece
    }

    // Highlight the valid moves of the selected piece
    private void highlightValidMoves(Chesspiece piece) {
        if (piece != null) {
            Set<Position> validMoves = piece.ifValidMove(model);  // Get the valid moves for the piece
            for (Position move : validMoves) {
                int row = move.getY();
                int col = move.getX();
                Chesspiece targetPiece = model.getPiece(col, row);  // Check if there's an enemy piece

                // Highlight valid squares with green for empty squares and red for enemy pieces
                board.boardLabels[row][col].setBorder(BorderFactory.createLineBorder(
                    targetPiece != null ? Color.RED : Color.GREEN, 3
                ));
            }
        }
    }

    // Clear the highlighting from all board squares
    private void clearHighlighting() {
        for (int r = 0; r < model.getBoardHeight(); r++) {
            for (int c = 0; c < model.getBoardWidth(); c++) {
                board.boardLabels[r][c].setBorder(DEFAULT_BORDER);  // Reset the border to default
            }
        }
    }

    // Convert pixel coordinates to board indices
    private int mapToBoardCoordinate(int pixel, int dimension, int boardSize) {
        return pixel * boardSize / dimension;  // Calculate the board index based on the pixel position
    }
}