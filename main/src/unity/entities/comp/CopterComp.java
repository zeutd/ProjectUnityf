package unity.entities.comp;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import unity.annotations.Annotations.*;
import unity.type.*;

@EntityComponent
abstract class CopterComp implements Unitc{
    FloatSeq rotorRot = new FloatSeq();
    float rotorSpeedScl;

    @Import boolean dead;
    @Import float health, rotation;
    @Import int id;

    @Override
    public void add(){
        rotorRot.setSize(rotorCount());
    }

    @Override
    public void update(){
        UnityUnitType type = (UnityUnitType)type();
        if(dead || health < 0f){
            rotation = rotation + type.fallRotateSpeed * Mathf.signs[id % 2];
            rotorSpeedScl = Mathf.lerpDelta(rotorSpeedScl, 0f, type.rotorDeathSlowdown);
        }else{ //In case heal() is run while it's dying.
            rotorSpeedScl = Mathf.lerpDelta(rotorSpeedScl, 1f, type.rotorDeathSlowdown);
        }

        for(Rotor rotor : type.rotors){
            int index = type.rotors.indexOf(rotor);
            rotorRot.set(index, (rotorRot.get(index) + rotor.speed * rotorSpeedScl * Time.delta) % 360);
        }
    }

    public int rotorCount(){
        return ((UnityUnitType)type()).rotors.size;
    }
}
