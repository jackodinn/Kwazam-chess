/* Chessboard.java
 - GUI for the chessboard
 - Members invovlved: Andrew Wee & Lai Zi Xuan
*/
package View;

import Model.*;
import java.awt.*;
import javax.swing.*;

public class Chessboard extends JFrame {

    public JLabel[][] boardLabels;
    private int height = 8; // Number of rows
    private int width = 5;
    private boolean isFlipped = false; // Track if the board is flipped
    public Chesspiece selectedPiece;
    private JMenuItem saveGameItem;

    // Constructor - Andrew Wee
    public Chessboard() {
        this.boardLabels = new JLabel[height][width];
        setTitle("Kwazam Chess");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int squareSize = 100;
        setSize(width * squareSize, height * squareSize);
        setLayout(new GridLayout(height, width));
        setResizable(false);
        setLocationByPlatform(true);

        // Add the menu bar
        addMenuBar();
    }

    // Top menu bar config - Lai Zi Xuan
    private void addMenuBar() {
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create the "File" menu
        JMenu fileMenu = new JMenu("File");
        saveGameItem = new JMenuItem("Save Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add action listeners to the menu items
        saveGameItem.addActionListener(e -> {
            System.out.println("Save Game & Exit");
            // Add save game logic here
        });

        exitItem.addActionListener(e -> {
            System.out.println("Exit clicked");
            System.exit(0); // Exit the application
        });

        // Add items to the "File" menu
        fileMenu.add(saveGameItem);
        fileMenu.addSeparator(); // Add a separator line
        fileMenu.add(exitItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);

        // Set the menu bar to the JFrame
        setJMenuBar(menuBar);
    }

    // Initialize the chessboard with checkered design - Andrew Wee
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

    // Refresh GUI every time an event happens - Andrew Wee
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

    // Flip the board when a player moves - Lai Zi Xuan
    public void flipBoard(ChessModel model) {
        isFlipped = !isFlipped;
        rotatePieceImages(model);
        refreshBoard(model);
    }

    // Check if board needs to be flipped - Lai Zi Xuan
    public boolean isFlipped() {
        return isFlipped;
    }

    // Rotate piece images when flipping the board - Lai Zi Xuan
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

    // Menu button for save game in menu bar
    public JMenuItem getSaveGameMenuItem()
    {
        return saveGameItem;
    }

    // Keep track of whose turn it is
    public void updateTitle(String turn, int round) {
        round = (round+2)/2;
        setTitle("Kwazam Chess - " + turn + "'s Turn (Turn " + round + ")");
    }
}