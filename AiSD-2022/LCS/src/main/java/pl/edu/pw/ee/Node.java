package pl.edu.pw.ee;

enum Direction {
    DIAGONALLY,
    LEFT,
    UP
}

public class Node {
    private int value;
    private Direction direction;
    private boolean onPath;

    public Node(int value) {
        this.value = value;
        this.onPath = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isOnPath() {
        return onPath;
    }

    public void setOnPath(boolean onPath) {
        this.onPath = onPath;
    }
}
