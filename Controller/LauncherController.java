/* LauncherController.java
 - To launch main menu and handle their functions
 - Members invovlved: Andrew Wee & Lai Zi Xuan
 */
package Controller;

import Model.*;
import View.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class LauncherController implements WindowFocusListener {

    private ChessModel model;
    private Chessboard view;
    private final Launcher launcher;
    private static JFrame popupframe;
    private Clip menuMusicClip;

    private static LauncherController instance;

    // Private Constructor - Andrew Wee
    private LauncherController(Launcher launcher) {
        this.launcher = launcher;
        launcher.getLaunchButton().addActionListener(e -> handleLaunch());
        launcher.getLoadButton().addActionListener(event -> handleLoad());
        launcher.addWindowFocusListener(this);
        playMenu("menumusic.wav"); // Start playing music when the controller is created
    }

    // Singleton method instance - Andrew wee
    public static LauncherController getInstance(Launcher launcher) {
        if (instance == null) {
            instance = new LauncherController(launcher);
        }
        return instance;
    }

    // When launcher is focused - Andrew Wee
    @Override
    public void windowGainedFocus(WindowEvent e) {
        playMenu("menumusic.wav");
    }

    // When launcher is not in focus - Andrew Wee
    @Override
    public void windowLostFocus(WindowEvent e) {
        stopMenuMusic();
    }

    // Launch chessboard - Andrew Wee
    private void handleLaunch() {
        stopMenuMusic();
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

    // Load button menu selection - Lai Zi Xuan
    private void handleLoad() {
        playMenu("menumusic.wav");

        if (popupframe != null && popupframe.isVisible()) {
            popupframe.dispose();
        }

        popupframe = new JFrame("Load old games...");
        popupframe.setSize(250, 150);
        popupframe.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Select a game to load:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JComboBox<String> gameComboBox = new JComboBox<>();
        gameComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Scan the "saves" directory for saved games
        File savesDir = new File("saves");
        if (savesDir.exists() && savesDir.isDirectory()) {
            File[] savedGames = savesDir.listFiles((dir, name) -> name.endsWith(".txt"));
            if (savedGames != null) {
                for (File file : savedGames) {
                    String gameName = file.getName().replace(".txt", "").replace("_", " ");
                    gameComboBox.addItem(gameName);
                }
            }
        } else {
            gameComboBox.addItem("No saved games found");
        }

        panel.add(gameComboBox);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton loadButton = new JButton("Load Game");
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loadButton);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopMenuMusic();
                String selectedGame = (String) gameComboBox.getSelectedItem();
                if (selectedGame != null && !selectedGame.equals("No saved games found")) {
                    loadGame(selectedGame);
                    popupframe.dispose();
                }
            }
        });

        popupframe.add(panel, BorderLayout.CENTER);
        popupframe.setLocation(750, 250);
        popupframe.setVisible(true);
    }

    // Load chessboard from a saved game - Lai Zi Xuan
    private void loadGame(String gameName) {
        String fileName = "saves/" + gameName.toLowerCase().replace(" ", "_") + ".txt";
        System.out.println("Attempting to load game from: " + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            this.model = new ChessModel();
            this.view = new Chessboard();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CurrentPlayer:")) {
                    String player = line.split(":")[1];
                    model.setCurrentPlayer(player.equals("Blue") ? Color.BLUE : Color.RED);
                } else if (line.startsWith("Round:")) {
                    int round = Integer.parseInt(line.split(":")[1]);
                    model.setRound(round);
                } else if (line.startsWith("Board:")) {
                    continue;
                } else {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String pieceType = parts[0];
                        String color = parts[1];
                        int col = Integer.parseInt(parts[2]);
                        int row = Integer.parseInt(parts[3]);

                        if (!pieceType.equals("Empty")) {
                            Chesspiece piece = createPieceFromString(pieceType, color, col, row);
                            model.setPiece(col, row, piece);
                        }
                    }
                }
            }

            view.setVisible(true);
            view.initializeBoard(model);
            InputHandler inputHandler = new InputHandler(view, model);
            view.addMouseListener(inputHandler);
            view.addMouseMotionListener(inputHandler);
            addSaveGameListener();
            addResetGameListener();

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

    // Take data from save file and create pieces on board - Lai Zi Xuan
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

    // Play menu music - Andrew Wee & Tan Ee Hang
    private void playMenu(String soundFileName) {
        try {
            if (menuMusicClip == null) {
                File soundFile = new File("sounds/" + soundFileName);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                menuMusicClip = AudioSystem.getClip();
                menuMusicClip.open(audioIn);
                menuMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            } else if (!menuMusicClip.isRunning()) {
                menuMusicClip.start(); // Resume if it was paused
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error playing menu music: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Could not play audio, line unavailable: " + e.getMessage());
        }
    }

    // Stop menu music - Andrew Wee & Tan Ee Hang
    private void stopMenuMusic() {
        if (menuMusicClip != null) { // Add null check here
            if (menuMusicClip.isRunning()) {
                menuMusicClip.stop();
            }
        }
    }

    // Save game button - Lai Zi Xuan
    private void addSaveGameListener() {
        JMenuItem saveGameItem = view.getSaveGameMenuItem();
        saveGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
    }

    //reset game listener - Lai zi xuan
    private void addResetGameListener() {
        JMenuItem resetGameButton = view.getResetGameItem();
        resetGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
    }

    //Reset game function - Lai zi xuan
    private void resetGame() {
        model.clearChessPiece();
        model.initializeChesspiece();
        model.setRound(0);
        model.setCurrentPlayer(Color.BLUE);
        if (view.isFlipped()) {
            view.flipBoard(model);
            view.resetPieceImage(model);
        }
        model.clearLogs();
        view.refreshBoard(model);
    }

    // Save game function - Lai Zi Xuan
    private void saveGame() {
        // Ask user to enter a name for the saved game
        String gameName = JOptionPane.showInputDialog(view, "Enter a name for the saved game:");
        if (gameName == null || gameName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Game name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(view, "Game saved as: " + gameName, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error saving the game.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
