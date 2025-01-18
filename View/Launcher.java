package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Launcher extends JFrame {

    private static JFrame popupframe;
    private JButton launchButton;
    private JButton rulesButton;

    public Launcher() {
        setTitle("Kwazam Chess Launcher");
        JPanel frame = new JPanel(new GridLayout(3, 1));
        add(frame);
        setSize(500, 500);
        setLocation(800, 350);

        launchButton = new JButton("Play Kwazam Chess");
        frame.add(launchButton);

        rulesButton = new JButton("Rules");
        frame.add(rulesButton);
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRulesWindow();
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static void showRulesWindow() {
        popupframe = new JFrame("Rules");
        popupframe.setSize(600, 600); // Increased size for more content
        popupframe.setLayout(new BorderLayout());

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
        popupframe.setVisible(true);
    }

    public JButton getLaunchButton() {
        return launchButton;
    }
}