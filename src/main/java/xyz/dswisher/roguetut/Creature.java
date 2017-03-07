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

    public Creature(World world, char glyph, Color color) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
    }

    private CreatureAi ai;
    public void setCreatureAi(CreatureAi ai) {
        this.ai = ai;
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public void moveBy(int mx, int my) {
        ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
    }
}
