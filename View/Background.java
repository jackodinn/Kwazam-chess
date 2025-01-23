/* Background.java
 - Set background of the launcher
 - Members invovlved: Andrew Wee, Lai Zi Xuan & Tan Ee Hang
*/
package View;

import java.awt.*;
import javax.swing.*;

class Background extends JPanel {
    private Image backgroundImage;

    public Background(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}