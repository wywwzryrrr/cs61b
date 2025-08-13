package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 030225;
    private static final Random RANDOM = new Random(SEED);

    /**
     * draw a row of tiles to the board, anchored at a given position
     */
    public static void drawRow(Position p, TETile tile, TETile[][] tiles, int length) {
        for (int x = 0; x < length; x++) {
            tiles[p.x + x][p.y] = tile;
        }
    }

    /**
     * Fills the given 2D array of tiles with NOTHING tiles.
     * @param tiles
     */
    public static void fillBoardWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * adds the hexagon to the world at position p with the given size
     */
    public static void addHexagon(Position p, TETile[][] tiles, int size, TETile tile) {
        if (size < 2) return;
        addHexagonHelper(p, tiles, size - 1, size, tile);
    }

    /**
     * helper method to draw hexagon
     */
    private static void addHexagonHelper(Position p, TETile[][] tiles, int b, int t, TETile tile) {
        // draw the first row
        Position start = p.shift(b, 0);
        drawRow(start, tile, tiles, t);

        // draw the remaining rows recursively
        if (b > 0) {
            Position next = p.shift(0, -1);
            addHexagonHelper(next, tiles, b - 1, t + 2, tile);
        }
        // draw the last row
        Position end = start.shift(0, -(2 * b + 1));
        drawRow(end, tile, tiles, t);
    }

    /**
     * private class to deal with the position
     */
    private static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position shift(int x, int y) {
            return new Position(this.x + x, this.y + y);
        }
    }

    /**
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(9);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.NOTHING;
            case 3: return Tileset.WATER;
            case 4: return Tileset.MOUNTAIN;
            case 5: return Tileset.AVATAR;
            case 6: return Tileset.SAND;
            case 7: return Tileset.GRASS;
            case 8: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    /**
     * draw the hexagonal world
     */
    public static void drawWorld(TETile[][] tiles) {
        fillBoardWithNothing(tiles);
        Position p = new Position(20, 20);
        addHexagon(p, tiles, 3, Tileset.FLOWER);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        drawWorld(world);
        ter.renderFrame(world);
    }
}
