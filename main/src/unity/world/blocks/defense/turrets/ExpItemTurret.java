package unity.world.blocks.defense.turrets;

import java.util.EnumMap;
import arc.util.io.*;
import arc.audio.Sound;
import arc.struct.ObjectSet;
import mindustry.gen.*;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import unity.world.blocks.ExpBlockBase;
import unity.world.blocks.ExpBuildBase;
import unity.world.blocks.ExpField;
import unity.world.meta.ExpType;

public class ExpItemTurret extends ItemTurret implements ExpBlockBase{
    protected final int maxLevel;
    protected final EnumMap<ExpType, ObjectSet<ExpField>> expFields = new EnumMap<>(ExpType.class);
    protected Effect levelUpFx = Fx.upgradeCore;
    protected Sound levelUpSound = Sounds.message;
    public final boolean hasCustomUpdate;
    {
        for(ExpType type : ExpType.values()) expFields.put(type, new ObjectSet<>(4));
    }

    public ExpItemTurret(String name, int maxLevel, boolean hasCustomUpdate){
        super(name);
        this.maxLevel = maxLevel;
        this.hasCustomUpdate = hasCustomUpdate;
    }

    public ExpItemTurret(String name, boolean hasCustomUpdate){
        this(name, 20, hasCustomUpdate);
    }

    public ExpItemTurret(String name, int maxLevel){
        this(name, maxLevel, false);
    }

    public ExpItemTurret(String name){
        this(name, 20, false);
    }

    @Override
    public int getMaxLevel(){
        return maxLevel;
    }

    @Override
    public void setBars(){
        super.setBars();
        bars.add("level", (ExpItemTurretBuild build) -> levelBar(build));
        bars.add("exp", (ExpItemTurretBuild build) -> expBar(build));
    }

    @Override
    public void setStats(){
        super.setStats();
        expSetStats(stats);
    }

    @Override
    public EnumMap<ExpType, ObjectSet<ExpField>> getExpFields(){
        return expFields;
    }

    public class ExpItemTurretBuild extends ItemTurretBuild implements ExpBuildBase{
        private float exp = 0f;
        private int level = 0;

        @Override
        public void updateTile(){
            setExpStats();
            if(hasCustomUpdate) expUpdate();
            else super.updateTile();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            expWrite(write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            expRead(read, revision);
        }

        @Override
        public ExpItemTurret getExpBlock(){
            return (ExpItemTurret)block;
        }

        @Override
        public void levelUpEffect(){
            levelUpFx.at(x, y, size);
            levelUpSound.at(x, y);
        }

        @Override
        public float totalExp(){
            return exp;
        }

        @Override
        public void setExp(float a){
            exp = a;
        }

        @Override
        public int getBlockMaxLevel(){
            return maxLevel;
        }

        @Override
        public int getLevel(){
            return level;
        }

        @Override
        public void setLevel(int a){
            level = a;
        }
    }
}
