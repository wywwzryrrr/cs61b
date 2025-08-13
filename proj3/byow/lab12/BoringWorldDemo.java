package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        // define the width
        for (int x = 20; x < 40; x += 1) {
            // define the height
            for (int y = 5; y < 10; y += 1) {
                world[x][y] = Tileset.WALL;
            }
            for (int z = 10; z < 30; z += 1) {
                world[x][z] = Tileset.FLOOR;
            }
            for (int z = 30; z < 50; z += 1) {
                world[x][z] = Tileset.FLOWER;
            }
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }


}
