/* ChessController.java
 - Middle person between the ChessModel.java and the Chessboard.java
 - Members invovlved: Andrew Wee & Lai Zi Xuan
*/
package Controller;

import Model.*;
import View.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;

public class ChessController {

    private ChessModel model;
    private Chessboard board;
    private Position selectedPos = null;
    private JLabel draggedPieceLabel = null;
    private ImageIcon draggedPieceIcon = null;
    private static final Border border = BorderFactory.createEmptyBorder();

    // Constructor - Andrew Wee
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
        addResetGameListener();
    }

    // Save game button - Lai Zi Xuan
    private void addSaveGameListener() {
        JMenuItem saveGameItem = board.getSaveGameMenuItem();
        saveGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
    }

    //reset game listener - Lai zi xuan
    private void addResetGameListener()
    {
        JMenuItem resetGameButton = board.getResetGameItem();
        resetGameButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                resetGame();
            }
        });
    }

    //Reset game function - Lai zi xuan
    private void resetGame()
    {
        model.clearChessPiece();
        model.initializeChesspiece(); 
        model.setRound(0);
        model.setCurrentPlayer(Color.BLUE);
        if(board.isFlipped())
        {
            board.flipBoard(model);
            board.resetPieceImage(model);
        }
        model.clearLogs();
        board.refreshBoard(model);
    }

    // Save game function - Lai Zi Xuan
    private void saveGame() {
        // Ask user to enter a name for the saved game
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

        // Generate the file and save board state
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
