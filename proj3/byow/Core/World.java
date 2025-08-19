package byow.Core;

import byow.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.Utilities;
import byow.Core.Room;
import java.util.Random;

public class World {
    private int width;
    private int height;
    private TETile[][] tiles;
    private Random random;

    World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new TETile[width][height];
    }

    public void initializeWorld(long seed) {
        Random random = new Random(seed);
        fillWithNothing();
    }

    private void fillWithNothing() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
}
