package xyz.dswisher.roguetut;

import java.awt.*;

public class Creature {
    private World world;

    public int x;
    public int y;
    public int z;

    private char glyph;
    public char glyph() { return glyph; }   // TODO - rename to getGlyph()

    private Color color;
    public Color color() { return color; }  // TODO - rename to getColor()

    private int maxHp;
    public int maxHp() { return maxHp; }    // TODO - rename to getMaxHp()

    private int hp;
    public int hp() { return hp; }          // TODO - rename to getHp()

    private int attackValue;
    public int attackValue() { return attackValue; }    // TODO - rename

    private int defenseValue;
    public int defenseValue() { return defenseValue; }  // TODO - rename

    private int visionRadius;
    public int visionRadius() { return visionRadius; }  // TODO - rename

    private String name;
    public String name() { return name; }   // TODO - rename

    private Inventory inventory;
    public Inventory inventory() { return inventory; }

    public Creature(World world, char glyph, Color color, int maxHp, int attack, int defense, String name) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = 9;
        this.name = name;
        this.inventory = new Inventory(20);
    }

    private CreatureAi ai;
    public void setCreatureAi(CreatureAi ai) {
        this.ai = ai;
    }

    public void dig(int wx, int wy, int wz) {
        world.dig(wx, wy, wz);
    }

    public void moveBy(int mx, int my, int mz) {
        if (mx==0 && my==0 && mz==0) {
            return;
        }

        Tile tile = world.tile(x+mx, y+my, z+mz);

        if (mz == -1){
            if (tile == Tile.STAIRS_DOWN) {
                doAction("walk up the stairs to level %d", z+mz+1);
            } else {
                doAction("try to go up but are stopped by the cave ceiling");
                return;
            }
        } else if (mz == 1) {
            if (tile == Tile.STAIRS_UP) {
                doAction("walk down the stairs to level %d", z+mz+1);
            } else {
                doAction("try to go down but are stopped by the cave floor");
                return;
            }
        }

        Creature other = world.creature(x + mx, y + my, z + mz);

        if (other == null) {
            ai.onEnter(x + mx, y + my, z + mz, tile);
        } else {
            attack(other);
        }
    }

    public void attack(Creature other) {
        int amount = Math.max(0, attackValue() - other.defenseValue());

        amount = (int)(Math.random() * amount) + 1;

        other.modifyHp(-amount);

        doAction("attack the %s for %d damage", other.name(), amount);
    }

    public void doAction(String message, Object ... params) {
        int r = 9;
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox * ox + oy * oy > r * r) {
                    continue;
                }

                Creature other = world.creature(x + ox, y + oy, z);

                if (other == null) {
                    continue;
                }

                if (other == this) {
                    other.notify("You " + message + ".", params);
                } else if (other.canSee(x, y, z)) {
                    other.notify(String.format("The %s %s.", name(), makeSecondPerson(message)), params);
                }
            }
        }
    }

    // TODO - clean this up - hacky!
    private String makeSecondPerson(String text) {
        String[] words = text.split(" ");
        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    public void modifyHp(int amount) {
        hp += amount;

        if (hp < 1) {
            doAction("die");
            world.remove(this);
        }
    }

    public void update() {
        ai.onUpdate();
    }

    public void notify(String message, Object ... params) {
        ai.onNotify(String.format(message, params));
    }

    public boolean canEnter(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
    }

    public boolean canSee(int wx, int wy, int wz) {
        return ai.canSee(wx, wy, wz);
    }

    public Tile tile(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz);
    }

    public Creature creature(int wx, int wy, int wz) {
        return world.creature(wx, wy, wz);
    }

    public void pickup() {
        Item item = world.item(x, y, z);

        if (inventory.isFull() || item == null){
            doAction("grab at the ground");
        } else {
            doAction("pickup a %s", item.name());
            world.remove(x, y, z);
            inventory.add(item);
        }
    }

    public void drop(Item item) {
        if (world.addAtEmptySpace(item, x, y, z)) {
            doAction("drop a " + item.name());
            inventory.remove(item);
        } else {
            notify("There's nowhere to drop the %s.", item.name());
        }
    }
}
