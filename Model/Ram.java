package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Ram extends Chesspiece {

    protected String name = "Ram";
    private int moveDirection;

    public Ram(Color color, String imagePath, Position pos) {
        super(color, imagePath, pos);
        this.moveDirection = (color == Color.BLUE) ? -1 : 1;
    }

    public int getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    // Movement logic ONLY, actual moving or capturing will not be implemented here
    @Override
    public Set<Position> ifValidMove(ChessModel model) {
        Set<Position> validMoves = new HashSet<>();
        int currentX = position.getX();
        int currentY = position.getY();
        int nextY = currentY + moveDirection;
        int nextnextY = nextY + moveDirection;
        Chesspiece targetPiece = model.getPiece(currentX, nextY);
        System.out.println("Current X: " + currentX + " Current Y: " + currentY);
        System.out.println("Next Y: " + nextY);

        if (nextY >= 0 && nextY < model.getBoardHeight()) {
            System.out.println("Next column within borders");
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