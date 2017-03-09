package xyz.dswisher.roguetut;

import asciiPanel.AsciiPanel;

import java.util.List;

public class CreatureFactory {
    private World world;
    private FieldOfView fov;

    public CreatureFactory(World world, FieldOfView fov) {
        this.world = world;
        this.fov = fov;
    }

    public Creature newPlayer(List<String> messages, int z) {
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite, 100, 20, 5);
        world.addAtEmptyLocation(player, z);
        new PlayerAi(player, messages, fov);
        return player;
    }

    public Creature newFungus(int z) {
        Creature fungus = new Creature(world, 'f', AsciiPanel.green, 10, 0, 0);
        world.addAtEmptyLocation(fungus, z);
        new FungusAi(fungus, this);
        return fungus;
    }
}
