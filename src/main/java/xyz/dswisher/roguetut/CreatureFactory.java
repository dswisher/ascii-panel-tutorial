package xyz.dswisher.roguetut;

import asciiPanel.AsciiPanel;

import java.util.List;

public class CreatureFactory {
    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages, int z) {
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite, 100, 20, 5);
        world.addAtEmptyLocation(player, z);
        new PlayerAi(player, messages);
        return player;
    }

    public Creature newFungus(int z) {
        Creature fungus = new Creature(world, 'f', AsciiPanel.green, 10, 0, 0);
        world.addAtEmptyLocation(fungus, z);
        new FungusAi(fungus, this);
        return fungus;
    }
}
