package unity.world.blocks.logic;

import static arc.Core.*;

/** @deprecated Use the blocks in the {@link unity.world.blocks.light} package instead! */
public class LightDivisor extends LightReflector{
    /** change this */
    private static final String spriteName = "unity-light-divisor";

    public LightDivisor(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();
        angleRegions[0] = atlas.find(spriteName + (diagonal ? "" : "-1"));
        angleRegions[1] = atlas.find(spriteName + (diagonal ? "-2" : "-3"));
    }
}
