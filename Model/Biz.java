package Model;

import java.awt.Color; // Make sure to import Color
import java.util.HashSet;
import java.util.Set;

public class Biz extends Chesspiece {

    protected String name = "Biz";

    public Biz(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
    }

    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
    
        // Define the knight's possible moves
        int[][] movement = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
    
        for (int[] move : movement) {
            int nextX = currentX + move[0];
            int nextY = currentY + move[1];
    
            // Validate the position is on the board
            if (nextX >= 0 && nextX < model.getBoardWidth() &&
                nextY >= 0 && nextY < model.getBoardHeight()) {
                
                Chesspiece targetPiece = model.getPiece(nextX, nextY);
    
                // Add the position if it's empty or an opponent's piece
                if (targetPiece == null || !targetPiece.getColor().equals(this.getColor())) {
                    validMoves.add(new Position(nextX, nextY));
                }
            }
        }
    
        return validMoves;
    }
}
