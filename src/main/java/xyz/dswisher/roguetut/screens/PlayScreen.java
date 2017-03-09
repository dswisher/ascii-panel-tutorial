package xyz.dswisher.roguetut.screens;

import asciiPanel.AsciiPanel;
import xyz.dswisher.roguetut.Creature;
import xyz.dswisher.roguetut.CreatureFactory;
import xyz.dswisher.roguetut.World;
import xyz.dswisher.roguetut.WorldBuilder;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {
    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        messages = new ArrayList<>();
        createWorld();

        CreatureFactory creatureFactory = new CreatureFactory(world);
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        player = creatureFactory.newPlayer(messages, 0);

        for (int z = 0; z < world.depth(); z++) {
            for (int i = 0; i < 8; i++) {
                creatureFactory.newFungus(z);
            }
        }
    }

    private void createWorld() {
        world = new WorldBuilder(90, 31, 5)
                .makeCaves()
                .build();
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight));
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                Creature creature = world.creature(wx, wy, player.z);
                if (creature != null) {
                    terminal.write(creature.glyph(), creature.x - left, creature.y - top, creature.color());
                } else {
                    terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
                }
            }
        }
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.writeCenter(messages.get(i), top + i);
        }
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();

        displayTiles(terminal, left, top);

        String stats = String.format(" %3d/%3d hp", player.hp(), player.maxHp());
        terminal.write(stats, 1, 23);

        displayMessages(terminal, messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                return new LoseScreen();

            case KeyEvent.VK_ENTER:
                return new WinScreen();

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_H:
            case KeyEvent.VK_NUMPAD6:
                player.moveBy(-1, 0, 0);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_L:
            case KeyEvent.VK_NUMPAD4:
                player.moveBy(1, 0, 0);
                break;

            case KeyEvent.VK_UP:
            case KeyEvent.VK_K:
            case KeyEvent.VK_NUMPAD8:
                player.moveBy(0, -1, 0);
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_J:
            case KeyEvent.VK_NUMPAD2:
                player.moveBy(0, 1, 0);
                break;

            case KeyEvent.VK_Y:
            case KeyEvent.VK_NUMPAD7:
                player.moveBy(-1, -1, 0);
                break;

            case KeyEvent.VK_U:
            case KeyEvent.VK_NUMPAD9:
                player.moveBy(1, -1, 0);
                break;

            case KeyEvent.VK_B:
            case KeyEvent.VK_NUMPAD1:
                player.moveBy(-1, 1, 0);
                break;

            case KeyEvent.VK_N:
            case KeyEvent.VK_NUMPAD3:
                player.moveBy(1, 1, 0);
                break;
        }

        switch (key.getKeyChar()){
            case '<':
                player.moveBy( 0, 0, -1);
                break;

            case '>':
                player.moveBy( 0, 0, 1);
                break;
        }

        world.update();

        return this;
    }
}
