package xyz.dswisher.roguetut;

import java.awt.*;

public class Creature {
    private World world;

    public int x;
    public int y;

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

    public Creature(World world, char glyph, Color color, int maxHp, int attack, int defense) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackValue = attack;
        this.defenseValue = defense;
    }

    private CreatureAi ai;
    public void setCreatureAi(CreatureAi ai) {
        this.ai = ai;
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public void moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);

        if (other == null) {
            ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        } else {
            attack(other);
        }
    }

    public void attack(Creature other) {
        int amount = Math.max(0, attackValue() - other.defenseValue());

        amount = (int)(Math.random() * amount) + 1;

        other.modifyHp(-amount);

        doAction("attack the '%s' for %d damage", other.glyph, amount);
    }

    public void doAction(String message, Object ... params) {
        int r = 9;
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox * ox + oy * oy > r * r) {
                    continue;
                }

                Creature other = world.creature(x + ox, y + oy);

                if (other == null) {
                    continue;
                }

                if (other == this) {
                    other.notify("You " + message + ".", params);
                } else {
                    other.notify(String.format("The '%s' %s.", glyph, makeSecondPerson(message)), params);
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

    public boolean canEnter(int wx, int wy) {
        return world.tile(wx, wy).isGround() && world.creature(wx, wy) == null;
    }
}
