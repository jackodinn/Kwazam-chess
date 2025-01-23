/* Sau.java
 - The Biz piece and their movement settings
 - Members invovlved: Lai Zi Xuan
*/
package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Sau extends Chesspiece {

    protected String name = "Sau";

    // Constructor - Lai Zi Xuan
    public Sau(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
    }

    public String getName() {
        return name;
    }

    // Movement logic ONLY, actual moving or capturing will not be implemented here - Lai Zi Xuan
    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
        
        // Implement movement
        int[][] movement = {{0, 1},
         {0, -1}, {1, 0}, {-1, 0}, {-1, 1}, {-1, -1}, {1, -1}, {1, 1}};

        for (int[] moves : movement) {
            int targetX = currentX + moves[0];
            int targetY = currentY + moves[1];
            
            if (targetX >= 0 && targetY >= 0 && targetX < model.getBoardWidth() && targetY < model.getBoardHeight()) {
                Chesspiece target = model.getPiece(targetX, targetY);
                if (target != null) {
                    if (!target.getColor().equals(getColor())) {
                        validMoves.add(new Position(targetX, targetY));
                    }
                } else {
                    validMoves.add(new Position(targetX, targetY));
                }
            }
        }
        return validMoves;
    }
}
