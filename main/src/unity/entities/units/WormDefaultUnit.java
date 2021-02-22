package unity.entities.units;

import arc.util.*;
import arc.math.*;
import arc.math.geom.Vec2;
import arc.graphics.g2d.Draw;
import mindustry.gen.*;
import mindustry.type.UnitType;
import unity.ai.*;
import unity.content.UnityUnitTypes;
import unity.type.*;
import unity.util.*;

import java.util.*;

public class WormDefaultUnit extends UnitEntity{
    public UnityUnitType wormType;
    public WormSegmentUnit[] segmentUnits;
    public float repairTime = 0f;
    protected Vec2[] segments, segmentVelocities;
    protected boolean addSegments = true;
    protected final Vec2 lastVelocityC = new Vec2(), lastVelocityD = new Vec2();

    public int getSegmentLength(){
        return wormType.segmentLength;
    }

    @Override
    public void type(UnitType type){
        super.type(type);
        if(type instanceof UnityUnitType w) wormType = w;
        else throw new ClassCastException("you set this unit's type in a sneaky way");
    }

    @Override
    public void setType(UnitType type){
        super.setType(type);
        if(type instanceof UnityUnitType w) wormType = w;
        else throw new ClassCastException("you set this unit's type in a sneaky way");
    }

    protected void setEffects(){
        segmentUnits = new WormSegmentUnit[wormType.segmentLength];
        segments = new Vec2[wormType.segmentLength];
        segmentVelocities = new Vec2[wormType.segmentLength];
        for(int i = 0; i < getSegmentLength(); i++){
            segments[i] = new Vec2(x, y);
            segmentVelocities[i] = new Vec2();
        }
    }

    @Override
    public void update(){
        lastVelocityD.set(lastVelocityC);
        lastVelocityC.set(vel);
        super.update();
        updateSegmentVLocal(lastVelocityC);
        updateSegmentsLocal();
        if(regenAvailable()){
            if(repairTime >= wormType.regenTime){
                float damage = (health / segmentUnits.length) / 2f;
                damage(damage);
                addSegment();
                repairTime = 0f;
            }else{
                repairTime += Time.delta;
            }
        }
    }

    public boolean regenAvailable(){
        return wormType.splittable && segmentUnits.length < wormType.segmentLength && wormType.regenTime > 0f;
    }

    protected void updateSegmentVLocal(Vec2 vec){
        //int len = getSegmentLength();
        int len = segmentUnits.length;
        for(int i = 0; i < len; i++){
            Vec2 seg = segments[i];
            Vec2 segV = segmentVelocities[i];
            segV.limit(type.speed);
            float angleB = i != 0 ? Angles.angle(seg.x, seg.y, segments[i - 1].x, segments[i - 1].y) : Angles.angle(seg.x, seg.y, x, y);
            float velocity = i != 0 ? segmentVelocities[i - 1].len() : vec.len();

            Tmp.v1.set(vel);
            Tmp.v1.add(vec);
            Tmp.v1.add(lastVelocityD);
            Tmp.v1.scl(1f / 3f);

            float trueVel = Math.max(Math.max(velocity, segV.len()), Tmp.v1.len());
            Tmp.v1.trns(angleB, trueVel);
            segV.add(Tmp.v1);
            segV.setLength(trueVel);
            if(wormType.counterDrag) segV.scl(1f + drag);
            segmentUnits[i].vel.set(segV);
        }
        //for(int i = 0; i < len; i++) segmentVelocities[i].scl(Time.delta);
    }

    protected void updateSegmentsLocal(){
        float segmentOffset = wormType.segmentOffset;
        float angleC = Utils.clampedAngle(Angles.angle(segments[0].x, segments[0].y, x, y), rotation, wormType.angleLimit) + 180f;
        Tmp.v1.trns(angleC, segmentOffset);
        Tmp.v1.add(x, y);
        segments[0].set(Tmp.v1);
        //int len = getSegmentLength();
        int len = segmentUnits.length;
        for(int i = 1; i < len; i++){
            Vec2 seg = segments[i];
            float angle = Utils.clampedAngle(Angles.angle(seg.x, seg.y, segments[i - 1].x, segments[i - 1].y), segmentUnits[i - 1].rotation, wormType.angleLimit);
            Tmp.v1.trns(angle, segmentOffset);
            seg.set(segments[i - 1]);
            seg.sub(Tmp.v1);
        }
        for(int i = 0; i < segmentUnits.length; i++){
            Vec2 seg = segments[i];
            Vec2 segV = segmentVelocities[i];
            WormSegmentUnit segU = segmentUnits[i];
            seg.add(segV);
            float angleD = i == 0 ? Angles.angle(seg.x, seg.y, x, y) : Angles.angle(seg.x, seg.y, segments[i - 1].x, segments[i - 1].y);
            segV.scl(Mathf.clamp(1f - drag * Time.delta));
            segU.set(seg.x, seg.y);
            segU.rotation = angleD;
            segU.wormSegmentUpdate();
        }
    }

    @Override
    public int classId(){
        return UnityUnitTypes.getClassId(1);
    }

    @Override
    public float clipSize(){
        return getSegmentLength() * wormType.segmentOffset * 2f;
    }

    public void drawShadow(){
        float originZ = Draw.z();
        for(int i = 0, len = segmentUnits.length; i < len; i++){
            Draw.z(originZ - (i + 1) / 10000f);
            segmentUnits[i].drawShadow();
        }
        Draw.z(originZ);
    }

    public WormSegmentUnit newSegment(){
        return new WormSegmentUnit();
    }

    @Override
    public void remove(){
        if(!added) return;
        super.remove();
        for(WormSegmentUnit segmentUnit : segmentUnits){
            segmentUnit.remove();
        }
    }

    @Override
    public int count(){
        return Math.max(super.count() / wormType.segmentLength, 1);
    }

    public void addSegment(){
        int index = segments.length;
        Unit parent = segmentUnits[index - 1];
        Tmp.v1.trns(segmentUnits[index - 1].rotation + 180f, wormType.segmentOffset).add(segmentUnits[index - 1]);
        segmentUnits[index - 1].segmentType = 0;
        segmentUnits = Arrays.copyOf(segmentUnits, segmentUnits.length + 1);
        segments = Arrays.copyOf(segments, segments.length + 1);
        segmentVelocities = Arrays.copyOf(segmentVelocities, segmentVelocities.length + 1);

        WormSegmentUnit segment = newSegment();
        segment.elevation = elevation;
        segment.segmentType = 1;
        segment.setType(type);
        segment.parentUnit = parent;
        segment.trueParentUnit = this;
        segment.set(Tmp.v1);
        segment.team = team;
        segment.health = health;
        segment.maxHealth = maxHealth;
        segment.segmentHealth = health;
        segment.dead = false;
        segment.add();
        segmentUnits[segmentUnits.length - 1] = segment;
        segments[segments.length - 1] = new Vec2(Tmp.v1);
        segmentVelocities[segmentVelocities.length - 1] = new Vec2(segmentVelocities[segmentVelocities.length - 2]);
    }

    @Override
    public void add(){
        if(added) return;
        super.add();
        if(!addSegments) return;
        setEffects();
        Unit parent = this;
        for(int i = 0, len = getSegmentLength(); i < len; i++){
            int typeS = i == len - 1 ? 1 : 0;
            segments[i].set(x, y);
            WormSegmentUnit temp = newSegment();

            temp.elevation = elevation;
            temp.setSegmentType(typeS);
            temp.type(type);
            temp.controller = type.createController();
            temp.controller.unit(temp);
            temp.team = team;
            temp.setTrueParent(this);
            temp.setParent(parent);
            temp.add();
            temp.afterSync();
            temp.heal();
            parent = temp;
            segmentUnits[i] = temp;
        }
    }

    /* seems uselss because multiple setStats() does nothing at end.
    @Override
    public void read(Reads read){
    	super.read(read);
    	for (int i = 0, len = getSegmentLength(); i < len; i++){
    		segments[i].x = read.f();
    		segments[i].y = read.f();
    	}
    }
    
    @Override
    public void write(Writes write){
    	super.write(write);
    	for (int i = 0, len = getSegmentLength(); i < len; i++){
    		write.f(segments[i].x);
    		write.f(segments[i].y);
    	}
    }*/

    public void handleCollision(Hitboxc originUnit, Hitboxc other, float x, float y){
        if(controller instanceof AdvanceWormAI f && other instanceof Bullet b){
            float damage = Utils.getBulletDamage(b.type);
            f.setTarget(b.x, b.y, damage);
        }
    }
}
