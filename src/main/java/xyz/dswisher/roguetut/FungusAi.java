package xyz.dswisher.roguetut;

public class FungusAi extends CreatureAi {

    private CreatureFactory factory;
    private int spreadCount;

    public FungusAi(Creature creature, CreatureFactory factory) {
        super(creature);

        this.factory = factory;
    }

    public void onUpdate() {
        if (spreadCount < 5 && Math.random() < 0.02) {
            spread();
        }
    }

    private void spread() {
        int x = creature.x + (int)(Math.random() * 11) - 5;
        int y = creature.y + (int)(Math.random() * 11) - 5;

        if (!creature.canEnter(x, y, creature.z)) {
            return;
        }

        Creature child = factory.newFungus(creature.z);
        child.x = x;
        child.y = y;
        child.z = creature.z;
        spreadCount += 1;

        creature.doAction("spawn a child");
    }
}
