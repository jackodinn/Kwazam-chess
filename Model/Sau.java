package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Sau extends Chesspiece {

    protected String name = "Sau";

    public Sau(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
    }

    public String getName() {
        return name;
    }

    @Override
    public Set<Position> ifValidMove(ChessModel cboard) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
        
        // Implement movement
        int[][] moveSets = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {-1, 1}, {-1, -1}, {1, -1}, {1, 1}};

        for (int[] moveSet : moveSets) {
            int targetX = currentX + moveSet[0];
            int targetY = currentY + moveSet[1];
            
            if (targetX >= 0 && targetY >= 0 && targetX < cboard.getBoardWidth() && targetY < cboard.getBoardHeight()) {
                Chesspiece target = cboard.getPiece(targetX, targetY);
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
