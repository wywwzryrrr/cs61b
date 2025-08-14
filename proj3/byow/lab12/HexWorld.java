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
     * adds a column of NUM hexagons with the given size to the given position p,
     * each of which biome is random
     * @param p
     * @param tiles
     * @param size
     */
    public static void addHexColumn(Position p, TETile[][] tiles, int size, int num) {
        if (num < 1) return;
        // draw the first hexagon
        addHexagon(p, tiles, size, randomTile());
        // recursively draw the remaining hexagons
        if (num > 1) {
            Position bottomNeighbour = getBottomNeighbour(p, size);
            addHexColumn(bottomNeighbour, tiles, size, num - 1);
        }
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

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(9);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.WATER;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.AVATAR;
            case 5: return Tileset.SAND;
            case 6: return Tileset.GRASS;
            case 7: return Tileset.TREE;
            case 8: return Tileset.FLOOR;
            default: return Tileset.NOTHING;
        }
    }

    /**
     * get the position of the bottom neighbour of a hexagon at Position P
     * SIZE is the size of the hexagon
     * @param p
     * @param size
     * @return
     */
    public static Position getBottomNeighbour(Position p, int size) {
        return p.shift(0, -2 * size);
    }

    /**
     * get the position of the top right neighbour of a hexagon at Position P
     * SIZE is the size of the hexagon
     * @param p
     * @param size
     * @return
     */
    public static Position getTopRightNeighbour(Position p, int size) {
        return p.shift(2 * size - 1, size);
    }

    /**
     * get the position of the bottom right neighbour of a hexagon at Position P
     * SIZE is the size of the hexagon
     * @param p
     * @param size
     * @return
     */
    public static Position getBottomRightNeighbour(Position p, int size) {
        return p.shift(2 * size - 1, -size);
    }

    /**
     * draw the hexagonal world
     */
    public static void drawWorld(TETile[][] tiles, Position p, int hexSize, int tessSize) {
        // draw the first hexColumn
        addHexColumn(p, tiles, hexSize, tessSize);
        // expand up and to the right
        for(int i = 1; i < tessSize; i++) {
            p = getTopRightNeighbour(p, hexSize);
            addHexColumn(p, tiles, hexSize, tessSize + i);
        }
        // expand down and to the right
        for (int i = tessSize - 2; i >= 0 ; i--) {
            p = getBottomRightNeighbour(p, hexSize);
            addHexColumn(p, tiles, hexSize, tessSize + i);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        Position p = new Position(5, 30);
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillBoardWithNothing(world);
        drawWorld(world, p,3, 3);
        ter.renderFrame(world);
    }
}
