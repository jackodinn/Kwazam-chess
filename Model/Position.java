/* Position.java
 - To keep track of coordinates
 - Members invovlved: Lai Zi Xuan
*/
package Model;

import java.util.*;

public class Position {

    private int x;
    private int y;

    // Constructor
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Generates a hash code
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // Checks if two positions are equal
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Position pos = (Position) obj;
        return x == pos.x && y == pos.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
