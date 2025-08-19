package byow.Core;

import java.util.Random;

public class Room {
    Position topLeft;
    Position bottomRight;

    public Room(Position topLeft, Position bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Room(Position topLeft, Position bottomRight, int height, int width) {
        this.topLeft = topLeft;
        this.bottomRight.x = topLeft.x + width;
        this.bottomRight.y = topLeft.y - height;
    }
}
