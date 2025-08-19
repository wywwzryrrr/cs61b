package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.Utilities;
import byow.Core.Room;
import java.util.Random;

public class World {
    int width;
    int height;
    TETile[][] tiles;
    Random random;

    World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new TETile[width][height];
    }

    public void generateWorld(long seed) {
        random = new Random(seed);
        fillWithNothing();
        createRoom();
    }

    private void fillWithNothing() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void createRoom() {
        int roomWidth = this.random.nextInt(4, 10); // 最小宽度为4
        int roomHeight = this.random.nextInt(4, 10); // 最小高度为4
        int xPos = this.random.nextInt(1, this.width - roomWidth - 1); // 离世界边缘至少有1格
        int yPos = this.random.nextInt(1, this.height - roomHeight - 1);
        Room newRoom = new Room(new Position(xPos, yPos), roomWidth, roomHeight);
        drawRoom(newRoom);
    }

    private void drawRoom(Room room) {
        int startX = room.getTopLeft().x;
        int startY = room.getTopLeft().y;
        int roomWidth = room.getWidth();
        int roomHeight = room.getHeight();

        for (int x = startX; x < startX + roomWidth; x++) {
            for (int y = startY; y < startY + roomHeight; y++) {
                tiles[x][y] = Tileset.FLOOR;
            }
        }
    }
}
