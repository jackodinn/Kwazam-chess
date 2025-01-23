/* Ram.java
 - The Biz piece and their movement settings
 - Members invovlved: Andrew Wee
*/
package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Ram extends Chesspiece {

    protected String name = "Ram";
    private int moveDirection;

    // Constructor - Andrew Wee
    public Ram(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
        this.moveDirection = (color == Color.BLUE) ? -1 : 1;
    }

    // Getter and setter - Andrew Wee
    public int getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    // Movement logic ONLY, actual moving or capturing will not be implemented here - Andrew Wee
    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
        int nextY = currentY + moveDirection;
        int nextnextY = nextY + moveDirection;
        Chesspiece targetPiece = model.getPiece(currentX, nextY);

        if (nextY >= 0 && nextY < model.getBoardHeight()) {
            if (targetPiece != null) {
                if (!targetPiece.getColor().equals(getColor()))
                    validMoves.add(new Position(currentX, nextY));
            } else {
                validMoves.add(new Position(currentX, nextY));
            }
        } else {
            moveDirection *= -1;
            nextY = currentY + moveDirection;
            if (nextY >= 0 && nextY < model.getBoardHeight()) {
                if (model.getPiece(currentX, nextY) == null) {
                    validMoves.add(new Position(currentX, nextY));
                }
            }
        }
        return validMoves;
    }

    public String getName() {
        return name;
    }
}