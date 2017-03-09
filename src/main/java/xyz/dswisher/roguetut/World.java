package xyz.dswisher.roguetut;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    private Tile[][][] tiles;
    private int width;
    public int width() { return width; }    // TODO - change to getWidth()

    private int height;
    public int height() { return height; }  // TODO - change to getHeight()

    private int depth;
    public int depth() { return depth; }    // TODO - change to getDepth()

    private List<Creature> creatures;

    public World(Tile[][][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.depth = tiles[0][0].length;
        this.creatures = new ArrayList<>();
    }

    public Tile tile(int x, int y, int z) {    // TODO - change to getTile()
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y][z];
        }
    }

    public char glyph(int x, int y, int z) {   // TODO - change to getGlyph()
        Creature creature = creature(x, y, z);
        return creature != null ? creature.glyph() : tile(x, y, z).glyph();
    }

    public Color color(int x, int y, int z) {  // TODO - change to getColor()
        Creature creature = creature(x, y, z);
        return creature != null ? creature.color() : tile(x, y, z).color();
    }

    public void dig(int x, int y, int z) {
        if (tile(x,y,z).isDiggable()) {
            tiles[x][y][z] = Tile.FLOOR;
        }
    }

    private void printLevel(int z) {
        int x;
        int y;

        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                System.out.print(tiles[x][y][z].glyph());
            }
            System.out.println();
        }
        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                Creature c = creature(x,y,z);
                if (c == null) {
                    System.out.print('.');
                } else {
                    System.out.print(c.glyph());
                }
            }
            System.out.println();
        }
    }

    public void addAtEmptyLocation(Creature creature, int z) {
        int x;
        int y;
        int count = 0;

        do {
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
            count += 1;
            if (count > 10000) {
                printLevel(z);
                throw new IllegalStateException("Could not find an empty spot for creature on level " + z + "!");
            }
        } while (!tile(x, y, z).isGround() || creature(x, y, z) != null);

        creature.x = x;
        creature.y = y;
        creature.z = z;
        creatures.add(creature);
    }

    public Creature creature(int x, int y, int z) {
        for (Creature c : creatures) {
            if (c.x == x && c.y == y && c.z == z) {
                return c;
            }
        }

        return null;
    }

    public void remove(Creature other) {
        creatures.remove(other);
    }

    public void update() {
        List<Creature> toUpdate = new ArrayList<>(creatures);

        toUpdate.forEach(Creature::update);
    }
}

