//launcherController.java
package Controller;

import Model.*;
import View.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class LauncherController {

    private ChessModel model;
    private Chessboard view;
    private final Launcher launcher;
    private static JFrame popupframe;
    private static JFrame settingsFrame;

    public LauncherController(Launcher launcher) {
        this.launcher = launcher;
        launcher.getLaunchButton().addActionListener(e -> handleLaunch());
        launcher.getLoadButton().addActionListener(event -> handleLoad());
    }

    private void handleLaunch() {
        System.out.println("Launching...");
        this.view = new Chessboard();
        this.model = new ChessModel();
        ChessController chessController = new ChessController(model, view);
        view.setVisible(true);
        System.out.println("Launched!");
        model.displayRound();
        System.out.println(model.getCurrentPlayer() + "'s Turn");
        launcher.setVisible(true);

    }

    private void handleLoad() {
        if (popupframe != null && popupframe.isVisible()) {
            popupframe.dispose();
        }

        popupframe = new JFrame("Load old games...");
        popupframe.setSize(400, 150);
        popupframe.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Add a label above the combo box
        JLabel label = new JLabel("Select a game to load:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        // Add some vertical spacing
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create and add the combo box
        JComboBox<String> gameComboBox = new JComboBox<>();
        gameComboBox.addItem("Game 1");
        gameComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(gameComboBox);

        // Add some vertical spacing
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create and add the button
        JButton loadButton = new JButton("Load Game");
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loadButton);

        // Add action listener to the load button
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGame = (String) gameComboBox.getSelectedItem();
                if (selectedGame != null) {
                    loadGame(selectedGame);
                    popupframe.dispose(); // Close the popup after loading
                }
            }
        });

        // Add the panel to the frame
        popupframe.add(panel, BorderLayout.CENTER);
        popupframe.setLocation(800, 300);
        popupframe.setVisible(true);
    }

    private void loadGame(String gameName) {
        String fileName = gameName.toLowerCase().replace(" ", "_") + ".txt"; // Example: "game_1.txt"
        System.out.println("Attempting to load game from: " + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Initialize a new model
            this.model = new ChessModel();

            // Initialize the Chessboard with the correct dimensions
            this.view = new Chessboard();

            // Load the game state from the file
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Reading line: " + line); // Debugging

                if (line.startsWith("CurrentPlayer:")) {
                    String player = line.split(":")[1];
                    System.out.println("Setting current player: " + player); // Debugging
                    model.setCurrentPlayer(player.equals("Blue") ? Color.BLUE : Color.RED);
                } else if (line.startsWith("Round:")) {
                    int round = Integer.parseInt(line.split(":")[1]);
                    System.out.println("Setting round: " + round); // Debugging
                    model.setRound(round);
                } else if (line.startsWith("Board:")) {
                    // Skip the "Board:" line
                    System.out.println("Skipping Board header"); // Debugging
                } else {
                    // Parse the board state
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String pieceType = parts[0];
                        String color = parts[1];
                        int col = Integer.parseInt(parts[2]);
                        int row = Integer.parseInt(parts[3]);

                        System.out.println("Parsing piece: " + pieceType + ", " + color + ", " + col + ", " + row); // Debugging

                        if (!pieceType.equals("Empty")) {
                            Chesspiece piece = createPieceFromString(pieceType, color, col, row);
                            model.setPiece(col, row, piece);
                        }
                    } else {
                        System.out.println("Invalid line format: " + line); // Debugging
                    }
                }
            }

            // Initialize the Chessboard with the loaded model
            view.setVisible(true);
            view.initializeBoard(model);
            // Create a new ChessController to handle the loaded game
            InputHandler inputHandler = new InputHandler(view, model);
            view.addMouseListener(inputHandler);
            view.addMouseMotionListener(inputHandler);

            System.out.println("Game loaded successfully: " + gameName);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + fileName);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the file: " + fileName);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error loading the game: " + gameName);
            e.printStackTrace();
        }
    }

    private Chesspiece createPieceFromString(String pieceType, String color, int col, int row) {
        Color pieceColor = color.equals("Blue") ? Color.BLUE : Color.RED;
        switch (pieceType) {
            case "Tor":
                return new Tor(pieceColor, "/images/Tor" + color + ".png", new Position(col, row));
            case "Biz":
                return new Biz(pieceColor, "/images/Biz" + color + ".png", new Position(col, row));
            case "Sau":
                return new Sau(pieceColor, "/images/Sau" + color + ".png", new Position(col, row));
            case "Xor":
                return new Xor(pieceColor, "/images/Xor" + color + ".png", new Position(col, row));
            case "Ram":
                return new Ram(pieceColor, "/images/Ram" + color + ".png", new Position(col, row));
            default:
                throw new IllegalArgumentException("Unknown piece type: " + pieceType);
        }
    }
}
