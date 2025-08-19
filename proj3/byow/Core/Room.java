package byow.Core;

import java.util.Random;

public class Room {
    private final Position topLeft;
    private final int width;
    private final int height;

    public Room(Position topLeft, int width, int height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
    }

    public Position getTopLeft() {
        return topLeft;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getBottomRight() {
        // Y轴向下增加，所以是 topLeft.y + height - 1
        return new Position(topLeft.x + width - 1, topLeft.y + height - 1);
    }
}
