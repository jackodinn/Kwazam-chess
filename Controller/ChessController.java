// ChessController.java
package Controller;

import Model.*;
import View.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.*;

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
        addSaveGameListener();
    }

    private void addSaveGameListener() {
        JMenuItem saveGameItem = board.getSaveGameMenuItem(); // You need to add this method to Chessboard
        saveGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
    }

    private void saveGame() {
        // Prompt the user to enter a name for the saved game
        String gameName = JOptionPane.showInputDialog(board, "Enter a name for the saved game:");
        if (gameName == null || gameName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(board, "Game name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the "saves" directory if it doesn't exist
        File savesDir = new File("saves");
        if (!savesDir.exists()) {
            savesDir.mkdir();
        }

        // Generate the filename
        String fileName = "saves/" + gameName.toLowerCase().replace(" ", "_") + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            String dimensionState = model.getDimensionAsString();
            String gameState = model.getGameStateAsString();
            writer.write(dimensionState);
            writer.write(gameState);
            System.out.println("Game saved successfully to " + fileName);
            JOptionPane.showMessageDialog(board, "Game saved as: " + gameName, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(board, "Error saving the game.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
