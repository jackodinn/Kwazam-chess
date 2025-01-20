//Launcher.java
package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Launcher extends JFrame {

    private static JFrame popupframe;
    private JButton launchButton;
    private JButton rulesButton;
    private JButton loadGameButton;
    private JButton button4;
    private JButton button5;

    public Launcher() {
        setTitle("Kwazam Chess Launcher");
        JPanel frame = new JPanel();
        frame.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));
        add(frame);
        setSize(500, 350);
        setLocation(750, 250);
        setResizable(false);
        frame.setBorder(BorderFactory.createEmptyBorder(25, 100, 25, 100));
        ImageIcon logoIcon = new ImageIcon("images/KwazamLogo.png");
        Image image = logoIcon.getImage();
        Image resizedImage = image.getScaledInstance(250, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(resizedImage);

        JLabel logoLabel = new JLabel(resizedLogo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the image
        frame.add(logoLabel);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));

        launchButton = new JButton("Play Kwazam Chess");
        launchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(launchButton);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));

        rulesButton = new JButton("Rules");
        frame.add(rulesButton);
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRulesWindow();
            }
        });
        rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));

        loadGameButton = new JButton("Load Game");
        loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(loadGameButton);
        frame.add(Box.createRigidArea(new Dimension(0, 10)));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static void showRulesWindow() {
        if (popupframe != null && popupframe.isVisible()) {
            popupframe.dispose();
        }
        popupframe = new JFrame("Rules");
        popupframe.setSize(750, 500); // Increased size for more content
        popupframe.setLayout(new BorderLayout());
        popupframe.setResizable(false);
        popupframe.setLocationByPlatform(true);

        // Game rules as a string - UPDATED WITH FULL RULES
        String rulesText = """
                Welcome to Kwazam Chess!

                1. Players alternate turns moving their pieces.
                2. Objective: Capture the opponent's Sau piece.
                3. Piece Movements:
                    - Ram: Can only move forward 1 step. If it reaches the end of the board, it turns around and starts heading back the other way. It cannot skip over other pieces.
                    - Biz: Moves in a 3x2 L shape in any orientation (like the knight in standard chess). This is the only piece that can skip over other pieces.
                    - Tor: Can move orthogonally any distance. It cannot skip over other pieces. After 2 turns, it transforms into the Xor piece.
                    - Xor: Can move diagonally any distance. It cannot skip over other pieces. After 2 turns, it transforms into the Tor piece.
                    - Sau: Can move only one step in any direction. The game ends when the Sau is captured.

                4. Piece Transformation: After 2 turns (counting one blue move and one red move as one turn), all Tor pieces will turn into Xor pieces, and all Xor pieces will turn into Tor pieces.

                None of the pieces are allowed to skip over other pieces, except for the Biz.
                """;

        JTextArea rules = new JTextArea(rulesText);
        rules.setEditable(false);
        rules.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        rules.setBackground(popupframe.getBackground());
        rules.setLineWrap(true);
        rules.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(rules); // Added scroll pane
        popupframe.add(scrollPane, BorderLayout.CENTER);
        popupframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close to allow reopening
        popupframe.setVisible(true);

    }

    public JButton getLaunchButton() {
        return launchButton;
    }

    public JButton getLoadButton() {
        return loadGameButton;
    }

}
