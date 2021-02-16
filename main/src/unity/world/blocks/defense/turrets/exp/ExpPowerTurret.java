package unity.world.blocks.defense.turrets.exp;

import arc.Graphics.*;
import arc.graphics.*;
import arc.scene.ui.layout.Table;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.turrets.*;
import unity.entities.comp.*;
import unity.graphics.*;

public class ExpPowerTurret extends PowerTurret{
    /** Color of shoot effects. Shifts to second color as the turret levels up. */
    public Color fromColor = Pal.lancerLaser, toColor = Pal.sapBullet;
    /** Increase in range with each level. I don't know how to get the expFields from here, if possible. */
    // ^ regarding that, you use ExpMeta#map(UnlockableContent), or you can just do block.addField(...) ~glen
    public float rangeInc = -1f;

    public ExpPowerTurret(String name){
        super(name);
        shootCone = 1;
        inaccuracy = 0;
    }

    public class ExpPowerTurretBuild extends PowerTurretBuild implements ExpBuildc{
        public float exp = 0f;
        public boolean checked = false;

        public Color getShootColor(float lvl){
            return Tmp.c1.set(fromColor).lerp(toColor, lvl).cpy();
        }

        @Override
        public void drawSelect(){
            Drawf.dashCircle(x, y, range, team.color);
            int lvl = level();
            if(lvl > 0 && rangeInc > 0) Drawf.dashCircle(x, y, range + rangeInc * lvl, UnityPal.expColor);
        }

        @Override
        public boolean shouldShowConfigure(Player player){
            return
                super.shouldShowConfigure(player) &&
                ExpBuildc.super.shouldShowConfigure(player);
        }

        @Override
        public Cursor getCursor(){
            return ExpBuildc.super.getCursor();
        }

        @Override
        public float exp(){
            return exp;
        }

        @Override
        public void exp(float exp){
            this.exp = exp;
        }

        @Override
        public boolean checked(){
            return checked;
        }

        @Override
        public void checked(boolean checked){
            this.checked = checked;
        }

        @Override
        public void killed() {
            super.killed();
            ExpBuildc.super.killed();
        }

        @Override
        public void update(){
            ExpBuildc.super.update();
            super.update();
        }

        @Override
        public void buildConfiguration(Table table){
            ExpBuildc.super.buildConfiguration(table);
            super.buildConfiguration(table);
        }

        @Override
        public void write(Writes write){
            super.write(write);
            ExpBuildc.super.write(write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            ExpBuildc.super.read(read, revision);
        }

        @Override
        public String toString(){
            return getClass().getSimpleName() + "#" + id;
        }
    }
}
