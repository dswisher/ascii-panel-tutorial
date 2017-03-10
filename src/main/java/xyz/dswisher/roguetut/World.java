package xyz.dswisher.roguetut;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    private Tile[][][] tiles;
    private Item[][][] items;
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
        this.items = new Item[width][height][depth];
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
        if (creature != null) {
            return creature.glyph();
        }

        Item item = item(x,y,z);
        if (item != null) {
            return item.glyph();
        }

        return tile(x, y, z).glyph();
    }

    public Color color(int x, int y, int z) {  // TODO - change to getColor()
        Creature creature = creature(x, y, z);
        if (creature != null) {
            return creature.color();
        }

        Item item = item(x, y, z);
        if (item != null) {
            return item.color();
        }

        return tile(x, y, z).color();
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

    public void addAtEmptyLocation(Item item, int z) {
        int x;
        int y;

        do {
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
        } while (!tile(x,y,z).isGround() || item(x,y,z) != null);

        items[x][y][z] = item;
    }

    public Creature creature(int x, int y, int z) {
        for (Creature c : creatures) {
            if (c.x == x && c.y == y && c.z == z) {
                return c;
            }
        }

        return null;
    }

    public boolean addAtEmptySpace(Item item, int x, int y, int z){
        if (item == null) {
            return false;
        }

        List<Point> points = new ArrayList<>();
        List<Point> checked = new ArrayList<>();

        points.add(new Point(x, y, z));

        while (!points.isEmpty()){
            Point p = points.remove(0);
            checked.add(p);

            if (!tile(p.x, p.y, p.z).isGround())
                continue;

            if (items[p.x][p.y][p.z] == null){
                items[p.x][p.y][p.z] = item;
                Creature c = this.creature(p.x, p.y, p.z);
                if (c != null) {
                    c.notify("A %s lands between your feet.", item.name());
                }
                return true;
            } else {
                List<Point> neighbors = p.neighbors8();
                neighbors.removeAll(checked);
                points.addAll(neighbors);
            }
        }
        return false;
    }

    public void remove(Creature other) {
        creatures.remove(other);
    }

    public void remove(int x, int y, int z) {
        items[x][y][z] = null;
    }

    public void update() {
        List<Creature> toUpdate = new ArrayList<>(creatures);

        toUpdate.forEach(Creature::update);
    }

    public Item item(int x, int y, int z) {
        return items[x][y][z];
    }
}

