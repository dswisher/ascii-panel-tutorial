package xyz.dswisher.roguetut;

import java.awt.*;

public class World {
    private Tile[][] tiles;
    private int width;
    public int width() { return width; }    // TODO - change to getWidth()

    private int height;
    public int height() { return height; }  // TODO - change to getHeight()

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
    }

    public Tile tile(int x, int y) {    // TODO - change to getTile()
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public char glyph(int x, int y) {   // TODO - change to getGlyph()
        return tile(x, y).glyph();
    }

    public Color color(int x, int y) {  // TODO - change to getColor()
        return tile(x, y).color();
    }
}