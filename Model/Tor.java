/* Tor.java
 - The Biz piece and their movement settings
 - Members invovlved: Tan Ee Hang
*/
package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Tor extends Chesspiece {

    protected String name = "Tor";

    public Tor(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
    }

    // Movement logic ONLY, actual moving or capturing will not be implemented here - Tan Ee Hang
    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
    
        int[][] movement = {
            {1, 0},  // East
            {-1, 0}, // West
            {0, 1},  // South
            {0, -1}  // North
        };
    
        for (int[] moves : movement) {
            int tempX = moves[0];
            int tempY = moves[1];
            int nextX = currentX + tempX;
            int nextY = currentY + tempY;
    
            // Move in the current moves until hitting the edge or a piece
            while (nextX >= 0 && nextX < model.getBoardWidth() &&
                   nextY >= 0 && nextY < model.getBoardHeight()) {
    
                Chesspiece targetPiece = model.getPiece(nextX, nextY);
    
                if (targetPiece != null) { 
                    // If an opponent's piece, capture is possible
                    if (!targetPiece.getColor().equals(this.getColor())) {
                        validMoves.add(new Position(nextX, nextY));
                    }
                    break; // Stop moving in this moves after hitting a piece
                }
    
                // No piece, add the move and continue in the moves
                validMoves.add(new Position(nextX, nextY));
                nextX += tempX;
                nextY += tempY;
            }
        }
    
        return validMoves;
    }   
}