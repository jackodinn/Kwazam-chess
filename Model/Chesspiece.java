/* Chesspiece.java
 - To keep track of coordinates
 - Members invovlved: Andrew Wee & Lai Zi Xuan
*/
package Model;

import java.awt.*;
import java.util.*;
import javax.swing.ImageIcon;

public abstract class Chesspiece {

    protected ImageIcon images;
    protected Position position;
    protected Color color;

    // Constructor - Andrew Wee
    public Chesspiece(Color color, String imagePath, Position pos) {
        this.color = color;
        this.images = resizeImageIcon(new ImageIcon(getClass().getResource(imagePath)), 90, 90); // Resize to 60x60 pixels
        this.position = pos;
    }

    // Getters and setters - Andrew Wee
    public Color getColor() {
        return color;
    }

    private String getColorString() {
        return (color == Color.BLUE) ? "Blue" : "Red";
    }

    public ImageIcon getImagePath() {
        return images;
    }

    public Position getPos() {
        return position;
    }

    public void setPos(Position pos) {
        this.position = pos;
    }

    public void setImageIcon(ImageIcon icon) {
        this.images = icon;
    }

    // Get image icon to put it through rotateImage() (Template Method) - Lai Zi Xuan
    public ImageIcon rotateImageIcon(ImageIcon icon) {
        Image image = icon.getImage();
        Image rotatedImage = rotateImage(image, 180); // Rotate by 180 degrees
        return new ImageIcon(rotatedImage);
    }

    // Rotate the image from rotateImageIcon() - Lai Zi Xuan
    private Image rotateImage(Image image, double degrees) {
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);

        // Create a new buffered image
        java.awt.image.BufferedImage rotated = new java.awt.image.BufferedImage(newWidth, newHeight, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.translate((newWidth - width) / 2, (newHeight - height) / 2);
        g2d.rotate(Math.toRadians(degrees), width / 2, height / 2);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    // Abstract function to calculate valid moves for the chesspiece - Andrew Wee
    public abstract Set<Position> ifValidMove(ChessModel cboard);

    // Resize image icon to make icon fit inside each space - Andrew Wee
    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int heightreal) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, heightreal, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    @Override
    public String toString() {
        Position pos = getPos();
        return getColorString() + " " + getClass().getSimpleName();
    }

}
