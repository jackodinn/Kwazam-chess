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
        String fileName = "game_1.txt"; // File to save the game state
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            String dimensionState = model.getDimensionAsString();
            // Get the game state as a string
            String gameState = model.getGameStateAsString();
            // Write the game state to the file
            writer.write(dimensionState);
            writer.write(gameState);
            System.out.println("Game saved successfully to " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving the game.");
        }
    }
}
