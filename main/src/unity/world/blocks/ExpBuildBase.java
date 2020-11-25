package unity.world.blocks;

import java.util.EnumMap;
import arc.struct.ObjectSet;
import arc.math.Mathf;
import arc.util.io.*;
import unity.world.meta.ExpType;

public interface ExpBuildBase{
    ExpBlockBase getExpBlock();

    default void setExpStats(){
        int lvl = getLevel();
        ExpBlockBase expBlock = getExpBlock();
        EnumMap<ExpType, ObjectSet<ExpField>> expFields = expBlock.getExpFields();
        expFields.get(ExpType.linear).each(f -> {
            try{
                f.field.set(expBlock, Math.max(f.start + f.intensity[0] * lvl, 0));
            }catch(Exception e){
                //Log.log(LogLevel.info, "[@]: @", "E", e.toString());
            }
        });
        expFields.get(ExpType.exp).each(f -> {
            try{
                f.field.set(expBlock, Math.max(f.start + Mathf.pow(f.intensity[0], lvl), 0));
            }catch(Exception e){
                //Log.log(LogLevel.info, "[@]: @", "E", e.toString());
            }
        });
        expFields.get(ExpType.root).each(f -> {
            try{
                f.field.set(expBlock, Math.max(f.start + Mathf.sqrt(f.intensity[0] * lvl), 0));
            }catch(Exception e){
                //Log.log(LogLevel.info, "[@]: @", "E", e.toString());
            }
        });
        expFields.get(ExpType.bool).each(f -> {
            try{
                f.field.set(expBlock, f.start > 0 ? lvl < f.intensity[0] : lvl >= f.intensity[0]);
            }catch(Exception e){
                //Log.log(LogLevel.info, "[@]: @", "E", e.toString());
            }
        });
        expFields.get(ExpType.list).each(f -> {
            try{
                f.field.set(expBlock, f.intensity[Math.min(lvl, f.intensity.length - 1)]);
            }catch(Exception e){
                //Log.log(LogLevel.info, "[@]: @", "E", e.toString());
            }
        });
    }

    float totalExp();

    void setExp(float a);

    default void incExp(float a){
        int lvl = getLevel();
        float current = totalExp() + a, max = getRequiredExp(lvl);
        if(lvl == getBlockMaxLevel()) return;
        if(current >= max){
            setExp(current - max);
            setLevel(lvl + 1);
            levelUpEffect();
        }else setExp(current);
    }

    int getBlockMaxLevel();

    int getLevel();

    void setLevel(int a);

    default float getRequiredExp(int lvl){
        return (2f * lvl + 1f) * 10f;
    }

    default float getLvlf(){
        if(getLevel() == getBlockMaxLevel()) return 1f;
        return totalExp() / (float)getRequiredExp(getLevel());
    }

    default void levelUpEffect(){

    }

    default void expUpdate(){

    }

    default void expWrite(Writes write){
        write.i((int)totalExp());
        write.i(getLevel());
    }

    default void expRead(Reads read, byte revision){
        setExp(read.i());
        setLevel(read.i());
    }
}
