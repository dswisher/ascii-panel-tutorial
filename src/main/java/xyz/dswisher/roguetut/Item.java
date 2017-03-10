package xyz.dswisher.roguetut;

import java.awt.*;

public class Item {
    private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }

    private String name;
    public String name() { return name; }

    public Item(char glyph, Color color, String name){
        this.glyph = glyph;
        this.color = color;
        this.name = name;
    }
}
