package unity.content;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.production.*;
import mindustry.type.*;
import mindustry.ctype.*;
import mindustry.content.*;
import unity.annotations.Annotations.*;
import unity.entities.bullet.*;
import unity.gen.*;
import unity.graphics.*;
import unity.mod.*;
import unity.type.exp.*;
import unity.util.*;
import unity.world.blocks.*;
import unity.world.blocks.defense.*;
import unity.world.blocks.defense.turrets.*;
import unity.world.blocks.distribution.*;
import unity.world.blocks.logic.*;
import unity.world.blocks.power.*;
import unity.world.blocks.production.*;
import unity.world.blocks.sandbox.*;
import unity.world.blocks.units.*;
import unity.world.blocks.effect.*;
import unity.world.blocks.environment.*;
import unity.world.consumers.*;
import unity.world.draw.*;
import unity.world.graphs.*;
import younggamExperimental.*;
import younggamExperimental.blocks.*;

import static arc.Core.*;
import static mindustry.Vars.*;
import static mindustry.type.ItemStack.*;

public class UnityBlocks implements ContentList{
    public static Block
    distributionDrill,

    recursiveReconstructor,

    irradiator,

    superCharger;

    public static @FactionDef("dark") Block
    oreUmbrium,

    apparition, ghost, banshee, fallout, catastrophe, calamity, extinction,

    darkAlloyForge,

    darkWall, darkWallLarge;

    public static @FactionDef("light") Block
    oreLuminum,

    photon, //graviton, gluon, higgsBoson, singularity,
    electron, zBoson, /*,
    proton, zBoson,
    neutron, wBoson;*/

    lightLamp, oilLamp, lightLaser, lightLampInfi, lightReflector, lightReflector1, lightOmnimirror, lightFilter, lightInvertedFilter, lightDivisor, lightDivisor1, lightItemFilter, lightPanel, lightInfluencer,

    metaglassWall, metaglassWallLarge;

    public static @FactionDef("imber") Block
    oreImberium,

    orb, shockwire, current, plasma, electrobomb, shielder,

    sparkAlloyForge,

    absorber,

    electroTile;

    public static @FactionDef("koruh")
    Block solidifier, steelSmelter, liquifier,

    stoneWall, denseWall, steelWall, steelWallLarge, diriumWall, diriumWallLarge,

    steelConveyor, diriumConveyor,
    
    bufferPad, omegaPad, cachePad,

    convertPad,

    teleporter, teleunit; //expOutput, expUnloader, expTank, expChest, expFountain, expVoid;

    public static
    @FactionDef("koruh")
    @LoadRegs("bt-laser-turret-top")
    @Merge(base = PowerTurret.class, value = {Expc.class, Turretc.class})
    Block laser, laserCharge, laserBranch, laserFractal, laserBreakthrough;

    public static
    @FactionDef("koruh")
    @Merge(base = LiquidTurret.class, value = {Expc.class, Turretc.class})
    Block laserFrost, laserKelvin;

    public static
    @FactionDef("koruh")
    @Merge(base = ItemTurret.class, value = {Expc.class, Turretc.class})
    Block inferno;

    public static
    @FactionDef("monolith")
    @LoadRegs({
        "debris-extractor-heat1",
        "debris-extractor-heat2"
    })
    Block
    //environments
    oreMonolite,
    sharpslate, sharpslateWall,
    infusedSharpslate, infusedSharpslateWall,
    archSharpslate, archEnergy,
    loreMonolith,

    //effects
    deflectorAura,

    //production
    debrisExtractor, monolithAlloyForge,

    //defense
    electrophobicWall, electrophobicWallLarge;

    //turrets
    public static
    @FactionDef("monolith")
    @LoadRegs(value = {
        "supernova-head",
        "supernova-core",
        "supernova-wing-left", "supernova-wing-right",
        "supernova-wing-left-bottom", "supernova-wing-right-bottom",
        "supernova-bottom"
    }, outline = true)
    Block

    ricochet, shellshock, purge,
    lifeStealer, absorberAura,
    recluse, blackout,
    heatRay,
    diviner, mage, oracle,
    supernova;

    public static @FactionDef("youngcha")
    Block

    //environments
    oreNickel, concreteBlank, concreteFill, concreteNumber, concreteStripe, concrete, stoneFullTiles, stoneFull,
    stoneHalf, stoneTiles,

    //turrets
    smallTurret, medTurret, chopper,

    //production
    augerDrill, mechanicalExtractor, sporeFarm, 
    
    //distribution
    mechanicalConveyor, heatPipe, driveShaft, inlineGearbox, shaftRouter, simpleTransmission,

    //crafting
    crucible, holdingCrucible, cruciblePump, castingMold, sporePyrolyser, 
    
    //power
    smallRadiator, thermalHeater, combustionHeater, solarCollector, solarReflector, nickelStator, nickelStatorLarge,
    nickelElectromagnet, electricRotorSmall, electricRotor, handCrank, windTurbine, waterTurbine, electricMotor,

    //defense
    cupronickelWall, cupronickelWallLarge,
    
    //sandbox
    infiHeater, infiCooler, infiTorque, neodymiumStator;

    public static @FactionDef("advance")
    Block celsius, kelvin, xenoCorruptor, cube;

    public static
    @FactionDef("end")
    @LoadRegs({
        "end-forge-lights",
        "end-forge-top",
        "end-forge-top-small",

        "terminal-crucible-lights",
        "terminal-crucible-top"
    })
    Block terminalCrucible, endForge;

    public static
    @FactionDef("end")
    @LoadRegs(value = {
        "tenmeikiri-base"
    }, outline = true)
    Block endGame, tenmeikiri;

    @Override
    public void load(){
        //region global

        distributionDrill = new DistributionDrill("distribution-drill"){{
            requirements(Category.production, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 20));
            tier = 3;
            drillTime = 450;
            size = 2;

            consumes.liquid(Liquids.water, 0.06f).boost();
        }};

        recursiveReconstructor = new SelectableReconstructor("recursive-reconstructor"){{
            requirements(Category.units, with(Items.graphite, 1600, Items.silicon, 2000, Items.metaglass, 900, Items.thorium, 600, Items.lead, 1200, Items.plastanium, 3600));
            size = 11;
            liquidCapacity = 360f;
            configurable = true;
            constructTime = 20000f;
            minTier = 6;
            upgrades.add(
                new UnitType[]{UnitTypes.toxopid, UnityUnitTypes.araneidae},

                new UnitType[]{UnitTypes.corvus, UnityUnitTypes.orion},

                new UnitType[]{UnityUnitTypes.rex, UnityUnitTypes.excelsus},

                new UnitType[]{UnityUnitTypes.monument, UnityUnitTypes.colossus}
            );
            otherUpgrades.add(
                new UnitType[]{UnityUnitTypes.araneidae, UnityUnitTypes.theraphosidae},

                new UnitType[]{UnityUnitTypes.colossus, UnityUnitTypes.bastion}
            );
            consumes.power(5f);
            consumes.items(with(Items.silicon, 1200, Items.metaglass, 800, Items.thorium, 700, Items.surgeAlloy, 400, Items.plastanium, 600, Items.phaseFabric, 350));
            consumes.liquid(Liquids.cryofluid, 7f);
        }};

        irradiator = new Press("irradiator"){{
            requirements(Category.crafting, with(Items.lead, 120, Items.silicon, 80, Items.titanium, 30));
            outputItem = new ItemStack(UnityItems.irradiantSurge, 3);
            size = 3;
            movementSize = 29f;
            fxYVariation = 25f / tilesize;
            craftTime = 50f;
            consumes.power(1.2f);
            consumes.items(with(Items.thorium, 5, Items.titanium, 5, Items.surgeAlloy, 1));
        }};

        superCharger = new Reinforcer("supercharger"){{
            requirements(Category.effect, with(Items.titanium, 60, Items.lead, 20, Items.silicon, 30));
            size = 2;
            itemCapacity = 15;
            laserColor = Items.surgeAlloy.color;
            consumes.power(0.4f);
            consumes.items(with(UnityItems.irradiantSurge, 10));
        }};

        lightLamp = new LightSource("light-lamp"){{
            consumes.power(1f);
            requirements(Category.logic, with(Items.lead, 5, Items.metaglass, 10));
            drawer = new DrawLightSource();
            lightLength = 30;
        }};

        oilLamp = new LightSource("oil-lamp", true){{
            size = 3;
            health = 240;
            consumes.power(1.8f);
            consumes.liquid(Liquids.oil, 0.1f);
            requirements(Category.logic, with(Items.lead, 20, Items.metaglass, 20, Items.titanium, 15));
            drawer = new DrawLightSource();
            lightLength = 150;
            lightStrength = 750;
        }};

        lightLaser = new LightSource("light-laser"){{
            health = 60;
            consumes.power(1.5f);
            requirements(Category.logic, BuildVisibility.sandboxOnly, with(Items.metaglass, 10, Items.silicon, 5, Items.titanium, 5));
            alwaysUnlocked = true;
            drawer = new DrawLightSource();
            lightLength = 30;
            lightInterval = 0;
        }};

        lightLampInfi = new LightSource("light-lamp-infi"){{
            hasPower = false;
            consumesPower = false;
            requirements(Category.logic, BuildVisibility.sandboxOnly, with());
            alwaysUnlocked = true;
            drawer = new DrawLightSource();
            lightLength = 150;
            lightStrength = 600000;
            scaleStatus = false;
            maxLightLength = 7500;
        }};

        lightReflector = new LightReflector("light-reflector"){{
            requirements(Category.logic, with(Items.metaglass, 10));
        }};

        lightReflector1 = new LightReflector("light-reflector-1"){{
            diagonal = false;
            requirements(Category.logic, with(Items.metaglass, 10));
        }};

        lightOmnimirror = new LightOmniReflector("light-omnimirror"){{
            health = 80;
            requirements(Category.logic, with(Items.metaglass, 10, Items.silicon, 5));
        }};

        lightFilter = new LightFilter("light-filter"){{
            health = 60;
            requirements(Category.logic, with(Items.graphite, 10, Items.metaglass, 10));
        }};

        lightInvertedFilter = new LightFilter("light-inverted-filter", true){{
            health = 60;
            requirements(Category.logic, with(Items.graphite, 10, Items.metaglass, 10));
        }};

        lightDivisor = new LightDivisor("light-divisor"){{
            health = 80;
            requirements(Category.logic, with(Items.metaglass, 10, Items.titanium, 2));
        }};

        lightDivisor1 = new LightDivisor("light-divisor-1"){{
            diagonal = false;
            health = 80;
            requirements(Category.logic, with(Items.metaglass, 10, Items.titanium, 2));
        }};

        lightItemFilter = new LightRouter("light-item-filter"){{
            health = 60;
            requirements(Category.logic, with(Items.graphite, 5, Items.metaglass, 20, Items.silicon, 10));
        }};

        lightPanel = new LightGenerator("light-panel"){{
            health = 100;
            lightStrength = 80f;
            scaleStatus = true;
            powerProduction = 1f;
            requirements(Category.logic, with(Items.copper, 15, Items.graphite, 10, Items.silicon, 15));
        }};

        lightInfluencer = new LightInfluencer("light-influencer"){{
            health = 60;
            lightStrength = 1f;
            scaleStatus = true;
            powerProduction = 1f;
            requirements(Category.logic, with(Items.lead, 15, Items.metaglass, 10, Items.silicon, 5));
        }};

        metaglassWall = new LightWall("metaglass-wall"){{
            health = 350;
            requirements(Category.defense, with(Items.lead, 6, Items.metaglass, 6));
        }};

        metaglassWallLarge = new LightWall("metaglass-wall-large"){{
            size = 2;
            health = 1400;
            requirements(Category.defense, with(Items.lead, 24, Items.metaglass, 24));
        }};

        oreNickel = new UnityOreBlock(UnityItems.nickel){{
            oreScale = 24.77f;
            oreThreshold = 0.913f;
            oreDefault = true;
        }};

        oreUmbrium = new UnityOreBlock(UnityItems.umbrium){{
            oreScale = 23.77f;
            oreThreshold = 0.813f;
            oreDefault = true;
        }};

        oreLuminum = new UnityOreBlock(UnityItems.luminum){{
            oreScale = 23.77f;
            oreThreshold = 0.81f;
            oreDefault = true;
        }};

        oreImberium = new UnityOreBlock(UnityItems.imberium){{
            oreScale = 23.77f;
            oreThreshold = 0.807f;
            oreDefault = true;
        }};

        //endregion
        //region dark

        apparition = new ItemTurret("apparition"){
            @Override
            public void load(){
                super.load();
                baseRegion = atlas.find("unity-block-" + size);
            }

            {
                requirements(Category.turret, with(Items.copper, 350, Items.graphite, 380, Items.silicon, 360, Items.plastanium, 200, Items.thorium, 220, UnityItems.umbrium, 370, Items.surgeAlloy, 290));
                size = 5;
                health = 3975;
                range = 235f;
                reloadTime = 6f;
                coolantMultiplier = 0.5f;
                restitution = 0.09f;
                inaccuracy = 3f;
                spread = 12f;
                shots = 2;
                alternate = true;
                recoilAmount = 3f;
                rotateSpeed = 4.5f;
                ammo(Items.graphite, UnityBullets.standardDenseLarge, Items.silicon, UnityBullets.standardHomingLarge, Items.pyratite, UnityBullets.standardIncendiaryLarge, Items.thorium, UnityBullets.standardThoriumLarge);
            }
        };

        ghost = new BarrelsItemTurret("ghost"){{
            size = 8;
            health = 9750;
            range = 290f;
            reloadTime = 9f;
            coolantMultiplier = 0.5f;
            restitution = 0.08f;
            inaccuracy = 3f;
            shots = 2;
            alternate = true;
            recoilAmount = 5.5f;
            rotateSpeed = 3.5f;
            spread = 21f;
            addBarrel(8f, 18.75f, 6f);
            ammo(Items.graphite, UnityBullets.standardDenseHeavy, Items.silicon, UnityBullets.standardHomingHeavy, Items.pyratite, UnityBullets.standardIncendiaryHeavy, Items.thorium, UnityBullets.standardThoriumHeavy);
            requirements(Category.turret, with(Items.copper, 1150, Items.graphite, 1420, Items.silicon, 960, Items.plastanium, 800, Items.thorium, 1230, UnityItems.darkAlloy, 380));
        }};

        banshee = new unity.world.blocks.defense.turrets.BarrelsItemTurret("banshee"){{
            size = 12;
            health = 22000;
            range = 370f;
            reloadTime = 12f;
            coolantMultiplier = 0.5f;
            restitution = 0.08f;
            inaccuracy = 3f;
            shots = 2;
            alternate = true;
            recoilAmount = 5.5f;
            rotateSpeed = 3.5f;
            spread = 37f;
            focus = true;
            addBarrel(23.5f, 36.5f, 9f);
            addBarrel(8.5f, 24.5f, 6f);
            ammo(Items.graphite, UnityBullets.standardDenseMassive, Items.silicon, UnityBullets.standardHomingMassive, Items.pyratite, UnityBullets.standardIncendiaryMassive, Items.thorium, UnityBullets.standardThoriumMassive);
            requirements(Category.turret, with(Items.copper, 2800, Items.graphite, 2980, Items.silicon, 2300, Items.titanium, 1900, Items.phaseFabric, 1760, Items.thorium, 1780, UnityItems.darkAlloy, 1280));
        }};

        fallout = new LaserTurret("fallout"){
            @Override
            public void load(){
                super.load();
                baseRegion = atlas.find("unity-block-" + size);
            }

            {
                size = 5;
                health = 3975;
                range = 215f;
                reloadTime = 110f;
                coolantMultiplier = 0.8f;
                shootCone = 40f;
                shootDuration = 230f;
                // shootLength = 5f;
                powerUse = 19f;
                shootShake = 3f;
                firingMoveFract = 0.2f;
                shootEffect = Fx.shootBigSmoke2;
                recoilAmount = 4f;
                shootSound = Sounds.laserbig;
                heatColor = Color.valueOf("e04300");
                rotateSpeed = 3.5f;
                loopSound = Sounds.beam;
                loopSoundVolume = 2.1f;
                requirements(Category.turret, with(Items.copper, 450, Items.lead, 350, Items.graphite, 390, Items.silicon, 360, Items.titanium, 250, UnityItems.umbrium, 370, Items.surgeAlloy, 360));
                shootType = UnityBullets.falloutLaser;
                consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.58f)).update(false);
            }
        };

        catastrophe = new BigLaserTurret("catastrophe"){{
            size = 8;
            health = 9750;
            range = 300f;
            reloadTime = 190f;
            coolantMultiplier = 0.6f;
            shootCone = 40f;
            shootDuration = 320f;
            // shootLength = 12f;
            powerUse = 39f;
            shootShake = 4f;
            firingMoveFract = 0.16f;
            shootEffect = Fx.shootBigSmoke2;
            recoilAmount = 7f;
            cooldown = 0.012f;
            heatColor = Color.white;
            rotateSpeed = 1.9f;
            shootSound = Sounds.laserbig;
            loopSound = Sounds.beam;
            loopSoundVolume = 2.2f;
            expanded = true;
            requirements(Category.turret, with(Items.copper, 1250, Items.lead, 1320, Items.graphite, 1100, Items.titanium, 1340, Items.surgeAlloy, 1240, Items.silicon, 1350, Items.thorium, 770, UnityItems.darkAlloy, 370));
            // chargeBeginEffect = UnityFx.catastropheCharge;
            // chargeTime = UnityFx.catastropheCharge.lifetime;
            shootType = UnityBullets.catastropheLaser;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.4f && liquid.flammability < 0.1f, 1.3f)).update(false);
        }};

        calamity = new BigLaserTurret("calamity"){{
            size = 12;
            health = 22000;
            range = 420f;
            reloadTime = 320f;
            coolantMultiplier = 0.6f;
            shootCone = 23f;
            shootDuration = 360f;
            // shootLength = 14f;
            powerUse = 87f;
            shootShake = 4f;
            firingMoveFract = 0.09f;
            shootEffect = Fx.shootBigSmoke2;
            recoilAmount = 7f;
            cooldown = 0.009f;
            heatColor = Color.white;
            rotateSpeed = 0.97f;
            shootSound = Sounds.laserbig;
            loopSound = Sounds.beam;
            loopSoundVolume = 2.6f;
            expanded = true;
            requirements(Category.turret, with(Items.copper, 2800, Items.lead, 2970, Items.graphite, 2475, Items.titanium, 3100, Items.surgeAlloy, 2790, Items.silicon, 3025, Items.thorium, 1750, UnityItems.darkAlloy, 1250));
            // chargeBeginEffect = UnityFx.calamityCharge;
            // chargeTime = UnityFx.calamityCharge.lifetime;
            shootType = UnityBullets.calamityLaser;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.3f && liquid.flammability < 0.1f, 2.1f)).update(false);
        }};

        extinction = new BigLaserTurret("extinction"){{
            requirements(Category.turret, with(Items.copper, 3800, Items.lead, 4100, Items.graphite, 3200, Items.titanium, 4200, Items.surgeAlloy, 3800, Items.silicon, 4300, Items.thorium, 2400, UnityItems.darkAlloy, 1700, UnityItems.terminum, 900, UnityItems.terminaAlloy, 500));
            size = 14;
            health = 29500;
            range = 520f;
            reloadTime = 380f;
            coolantMultiplier = 0.4f;
            shootCone = 12f;
            shootDuration = 360f;
            // shootLength = 10f;
            powerUse = 175f;
            shootShake = 4f;
            firingMoveFract = 0.09f;
            shootEffect = Fx.shootBigSmoke2;
            recoilAmount = 7f;
            cooldown = 0.003f;
            heatColor = Color.white;
            rotateSpeed = 0.82f;
            shootSound = UnitySounds.extinctionShoot;
            loopSound = UnitySounds.beamIntenseHighpitchTone;
            loopSoundVolume = 2f;
            expanded = true;
            chargeBeginEffect = UnityFx.extinctionCharge;
            // chargeTime = UnityFx.extinctionCharge.lifetime;
            shootType = UnityBullets.extinctionLaser;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.27f && liquid.flammability < 0.1f, 2.5f)).update(false);
        }};

        darkAlloyForge = new StemGenericSmelter("dark-alloy-forge"){{
            requirements(Category.crafting, with(Items.copper, 30, Items.lead, 25));

            outputItem = new ItemStack(UnityItems.darkAlloy, 3);
            craftTime = 140f;
            size = 4;
            ambientSound = Sounds.respawning;
            ambientSoundVolume = 0.6f;

            consumes.items(with(Items.lead, 2, Items.silicon, 3, Items.blastCompound, 1, Items.phaseFabric, 1, UnityItems.umbrium, 2));
            consumes.power(3.2f);

            update = e -> {
                if(e.consValid() && Mathf.chanceDelta(0.76f)){
                    UnityFx.craftingEffect.at(e.getX(), e.getY(), Mathf.random(360f));
                }
            };
        }};

        darkWall = new Wall("dark-wall"){{
            requirements(Category.defense, with(UnityItems.umbrium, 6));
            health = 120 * 4;
        }};

        darkWallLarge = new Wall("dark-wall-large"){{
            requirements(Category.defense, with(UnityItems.umbrium, 24));
            health = 120 * 4 * 4;
            size = 2;
        }};

        //endregion
        //region light

        photon = new LaserTurret("photon"){{
            requirements(Category.turret, with(Items.lead, 50, Items.silicon, 35, UnityItems.luminum, 65, Items.titanium, 65));
            size = 2;
            health = 1280;
            reloadTime = 100f;
            shootCone = 30f;
            range = 120f;
            powerUse = 4.5f;
            heatColor = UnityPal.lightHeat;
            loopSound = Sounds.respawning;
            shootType = new ContinuousLaserBulletType(16f){{
                incendChance = -1f;
                length = 130f;
                width = 4f;
                colors = new Color[]{Pal.lancerLaser.cpy().a(3.75f), Pal.lancerLaser, Color.white};
                strokes = new float[]{0.92f, 0.6f, 0.28f};
                lightColor = hitColor = Pal.lancerLaser;
            }};
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.2f)).update(false);
        }};

        electron = new PowerTurret("electron"){{
            requirements(Category.turret, with(Items.lead, 110, Items.silicon, 75, UnityItems.luminum, 165, Items.titanium, 135));
            size = 3;
            health = 2540;
            reloadTime = 60f;
            coolantMultiplier = 2f;
            range = 170f;
            powerUse = 6.6f;
            heatColor = UnityPal.lightHeat;
            shootSound = Sounds.pew;
            shootType = new BasicBulletType(6f, 26f, "unity-electric-shell"){{
                lifetime = 30f;
                width = 12f;
                height = 19f;
                backColor = lightColor = hitColor = Pal.lancerLaser;
                frontColor = Color.white;
            }

                @Override
                public void update(Bullet b){
                    super.update(b);
                    if(b.timer(0, 1f + b.fslope() * 2f)){
                        UnityFx.blueTriangleTrail.at(b.x, b.y);
                    }
                }
            };
        }};

        zBoson = new RampupPowerTurret("z-boson"){
            @Override
            public void load(){
                super.load();
                baseRegion = atlas.find("unity-block-" + size);
            }

            {
                requirements(Category.turret, with(Items.silicon, 290, UnityItems.luminum, 430, Items.titanium, 190, Items.thorium, 120, Items.surgeAlloy, 20));
                health = 4000;
                size = 5;
                reloadTime = 40f;
                range = 230f;
                shootCone = 20f;
                heatColor = UnityPal.lightHeat;
                coolantMultiplier = 1.9f;
                rotateSpeed = 2.7f;
                recoilAmount = 2f;
                restitution = 0.09f;
                cooldown = 0.008f;
                powerUse = 3.6f;
                targetAir = true;
                shootSound = UnitySounds.zbosonShoot;
                alternate = true;
                shots = 2;
                spread = 14f;
                inaccuracy = 2.3f;

                lightning = true;
                lightningThreshold = 12f;
                baseLightningLength = 16;
                lightningLengthDec = 1;
                baseLightningDamage = 18f;
                lightningDamageDec = 1f;

                barBaseY = -10.75f;
                barLength = 20f;

                shootType = UnityBullets.zBosonBolt;
            }
        };

        //endregion
        //region imber

        orb = new PowerTurret("orb"){{
            requirements(Category.turret, with(Items.copper, 55, Items.lead, 30, Items.graphite, 25, Items.silicon, 35, UnityItems.imberium, 20));
            size = 2;
            health = 1320;
            range = 145f;
            reloadTime = 130f;
            coolantMultiplier = 2f;
            shootCone = 0.1f;
            shots = 1;
            inaccuracy = 12f;
            chargeTime = 65f;
            chargeEffects = 5;
            chargeMaxDelay = 25f;
            powerUse = 4.2069f;
            targetAir = false;
            shootType = UnityBullets.orb;
            shootSound = Sounds.laser;
            heatColor = Pal.turretHeat;
            shootEffect = UnityFx.orbShoot;
            smokeEffect = Fx.none;
            chargeEffect = UnityFx.orbCharge;
            chargeBeginEffect = UnityFx.orbChargeBegin;
        }};

        shockwire = new LaserTurret("shockwire"){{
            requirements(Category.turret, with(Items.copper, 150, Items.lead, 145, Items.titanium, 160, Items.silicon, 130, UnityItems.imberium, 70));
            size = 2;
            health = 860;
            range = 125f;
            reloadTime = 140f;
            coolantMultiplier = 2f;
            shootCone = 1f;
            inaccuracy = 0f;
            powerUse = 6.9420f;
            targetAir = false;
            shootType = UnityBullets.shockBeam;
            shootSound = Sounds.thruster;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability <= 0.1f, 0.4f)).update(false);
        }};

        current = new PowerTurret("current"){{
            requirements(Category.turret, with(Items.copper, 280, Items.lead, 295, Items.silicon, 260, UnityItems.sparkAlloy, 65));
            size = 3;
            health = 2400;
            range = 220f;
            reloadTime = 120f;
            coolantMultiplier = 2;
            shootCone = 0.01f;
            inaccuracy = 0f;
            chargeTime = 60f;
            chargeEffects = 4;
            chargeMaxDelay = 260;
            powerUse = 6.8f;
            shootType = UnityBullets.currentStroke;
            shootSound = Sounds.laserbig;
            chargeEffect = UnityFx.currentCharge;
            chargeBeginEffect = UnityFx.currentChargeBegin;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability <= 0.1f, 0.52f)).boost();
        }};

        plasma = new PowerTurret("plasma"){{
            requirements(Category.turret, with(Items.copper, 580, Items.lead, 520, Items.graphite, 410, Items.silicon, 390, Items.surgeAlloy, 180, UnityItems.sparkAlloy, 110));
            size = 4;
            health = 2800;
            range = 200f;
            reloadTime = 360f;
            recoilAmount = 4f;
            coolantMultiplier = 1.2f;
            liquidCapacity = 20f;
            shootCone = 1f;
            inaccuracy = 0f;
            powerUse = 8.2f;
            shootType = UnityBullets.plasmaTriangle;
            shootSound = Sounds.shotgun;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability <= 0.1f, 0.52f)).boost();
        }};

        electrobomb = new ItemTurret("electrobomb"){            
            {
                requirements(Category.turret, with(Items.titanium, 360, Items.thorium, 630, Items.silicon, 240, UnityItems.sparkAlloy, 420));
                health = 3650;
                size = 5;
                range = 400f;
                minRange = 60f;
                reloadTime = 320f;
                coolantMultiplier = 2f;
                shootCone = 20f;
                shots = 2;
                spread = 60f;
                inaccuracy = 0f;
                targetAir = false;
                ammo(UnityItems.sparkAlloy, UnityBullets.surgeBomb);
                shootSound = Sounds.laser;
                shootEffect = Fx.none;
                smokeEffect = Fx.none;
                consumes.powerCond(10f, TurretBuild::isActive);
            }
            
            @Override
            public void load(){
                super.load();
                baseRegion = atlas.find("unity-block-" + size);
            }
        };

        shielder = new ShieldTurret("shielder"){{
            requirements(Category.turret, with(Items.copper, 300, Items.lead, 100, Items.titanium, 160, Items.silicon, 240, UnityItems.sparkAlloy, 90));
            size = 3;
            health = 900;
            range = 260;
            reloadTime = 800;
            coolantMultiplier = 2;
            shootCone = 60;
            inaccuracy = 0;
            powerUse = 6.4f;
            targetAir = false;
            shootType = UnityBullets.shielderBullet;
            shootSound = /*test*/Sounds.pew;
            chargeEffect = new Effect(38f, e -> {
                Draw.color(Pal.accent);
                Angles.randLenVectors(e.id, 2, 1 + 20 * e.fout(), e.rotation, 120, (x, y) -> Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3 + 1));
            });
            chargeBeginEffect = Fx.none;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability <= 0.1f, 0.4f)).update(false);
        }};

        sparkAlloyForge = new StemGenericSmelter("spark-alloy-forge"){{
            requirements(Category.crafting, with(Items.lead, 160, Items.graphite, 340, UnityItems.imberium, 270, Items.silicon, 250, Items.thorium, 120, Items.surgeAlloy, 100));

            outputItem = new ItemStack(UnityItems.sparkAlloy, 4);
            size = 4;
            craftTime = 160f;
            ambientSound = Sounds.machine;
            ambientSoundVolume = 0.6f;
            craftEffect = UnityFx.imberCircleSparkCraftingEffect;

            consumes.power(2.6f);
            consumes.items(with(Items.surgeAlloy, 3, Items.titanium, 4, Items.silicon, 6, UnityItems.imberium, 3));

            update = e -> {
                if(e.consValid()){
                    if(Mathf.chanceDelta(0.3f)){
                        UnityFx.imberSparkCraftingEffect.at(e.getX(), e.getY(), Mathf.random(360f));
                    }else if(Mathf.chanceDelta(0.02f)){
                        Lightning.create(e.team(), UnityPal.imberColor, 5f, e.x(), e.y(), Mathf.random(360f), 5);
                    }
                }
            };
        }};

        absorber = new Absorber("absorber"){{
            requirements(Category.power, with(UnityItems.sparkAlloy, 20, Items.lead, 20));
            size = 2;
        }};

        electroTile = new Floor("electro-tile"){{

        }};

        //endregion
        //region koruh

        solidifier = new LiquidsSmelter("solidifier"){{
            requirements(Category.crafting, with(Items.copper, 20, UnityItems.denseAlloy, 30));

            health = 150;
            hasItems = true;
            liquidCapacity = 12f;
            updateEffect = Fx.fuelburn;
            craftEffect = UnityFx.rockFx;
            craftTime = 60f;
            flameColor = Color.valueOf("ffb096");
            outputItem = new ItemStack(UnityItems.stone, 1);

            consumes.add(new ConsumeLiquids(new LiquidStack[]{new LiquidStack(UnityLiquids.lava, 0.1f), new LiquidStack(Liquids.water, 0.1f)}));

            preserveDraw = false;
            draw((StemSmelterBuild e) -> {
                Draw.rect(region, e.x, e.y);
                if(e.warmup > 0f){
                    Draw.color(liquids[0].color, e.liquids.get(liquids[0]) / liquidCapacity);
                    Draw.rect(topRegion, e.x, e.y);
                    Draw.color();
                }
            });
        }};

        steelSmelter = new StemGenericSmelter("steel-smelter"){{
            requirements(Category.crafting, with(Items.lead, 45, Items.silicon, 20, UnityItems.denseAlloy, 30));
            health = 140;
            itemCapacity = 10;
            craftEffect = UnityFx.craftFx;
            craftTime = 300f;
            outputItem = new ItemStack(UnityItems.steel, 1);

            consumes.power(2f);
            consumes.items(with(Items.coal, 2, Items.graphite, 2, UnityItems.denseAlloy, 3));

            preserveDraw = false;
            draw((StemSmelterBuild e) -> {
                Draw.rect(region, e.x, e.y);
                if(e.warmup > 0f){
                    Draw.color(1f, 1f, 1f, e.warmup * Mathf.absin(8f, 0.6f));
                    Draw.rect(topRegion, e.x, e.y);
                    Draw.color();
                }
            });
        }};

        liquifier = new BurnerSmelter("liquifier"){{
            requirements(Category.crafting, with(Items.titanium, 30, Items.silicon, 15, UnityItems.steel, 10));
            health = 100;
            hasLiquids = true;
            updateEffect = Fx.fuelburn;
            craftTime = 30f;
            outputLiquid = new LiquidStack(UnityLiquids.lava, 0.1f);

            configClear(b -> Fires.create(b.tile));
            consumes.power(3.7f);

            update((BurnerSmelterBuild e) -> {//eh is it chanceDelta?
                if(e.progress == 0f && e.warmup > 0.001f && !Vars.net.client() && Mathf.chance(0.2f)){
                    e.configureAny(null);
                }
            });

            preserveDraw = false;
            draw((BurnerSmelterBuild e) -> {
                Draw.rect(region, e.x, e.y);
                if(e.warmup > 0f){
                    Liquid liquid = outputLiquid.liquid;
                    Draw.color(liquid.color, e.liquids.get(liquid) / liquidCapacity);
                    Draw.rect(topRegion, e.x, e.y);
                    Draw.color();
                }
            });
        }};

        stoneWall = new LimitWall("ustone-wall"){{
            requirements(Category.defense, with(UnityItems.stone, 6));
            maxDamage = 40f;
            health = 200;
        }};

        denseWall = new LimitWall("dense-wall"){{
            requirements(Category.defense, with(UnityItems.denseAlloy, 6));
            maxDamage = 32f;
            health = 560;
        }};

        steelWall = new LimitWall("steel-wall"){{
            requirements(Category.defense, with(UnityItems.steel, 6));
            maxDamage = 24f;
            health = 810;
        }};

        steelWallLarge = new LimitWall("steel-wall-large"){{
            requirements(Category.defense, with(UnityItems.steel, 24));
            maxDamage = 48f;
            health = 3240;
            size = 2;
        }};

        diriumWall = new LimitWall("dirium-wall"){{
            requirements(Category.defense, with(UnityItems.dirium, 6));
            maxDamage = 76f;
            blinkFrame = 30f;
            health = 760;
        }};

        diriumWallLarge = new LimitWall("dirium-wall-large"){{
            requirements(Category.defense, with(UnityItems.dirium, 24));
            maxDamage = 152f;
            blinkFrame = 30f;
            health = 3040;
            size = 2;
        }};

        steelConveyor = new ExpConveyor("steel-conveyor"){{
            requirements(Category.distribution, with(UnityItems.stone, 1, UnityItems.denseAlloy, 1, UnityItems.steel, 1));
            health = 140;
            speed = 0.1f;
            displayedSpeed = 12.5f;
            drawMultiplier = 1.9f;
        }};

        diriumConveyor = new ExpConveyor("dirium-conveyor"){{
            requirements(Category.distribution, with(UnityItems.steel, 1, Items.phaseFabric, 1, UnityItems.dirium, 1));
            health = 150;
            speed = 0.16f;
            displayedSpeed = 20f;
            drawMultiplier = 1.3f;
        }};
        
        bufferPad = new MechPad("buffer-pad"){{
            requirements(Category.units, with(UnityItems.stone, 120, Items.copper, 170, Items.lead, 150, Items.titanium, 150, Items.silicon, 180));
            size = 2;
            craftTime = 100;
            consumes.power(0.7f);
            unitType = UnityUnitTypes.buffer;
        }};

        omegaPad = new MechPad("omega-pad"){{
            requirements(Category.units, with(UnityItems.stone, 220, Items.lead, 200, Items.silicon, 230, Items.thorium, 260, Items.surgeAlloy, 100));
            size = 3;
            craftTime = 300f;
            consumes.power(1.2f);
            unitType = UnityUnitTypes.omega;
        }};
        
        cachePad = new MechPad("cache-pad"){{
            requirements(Category.units, with(UnityItems.stone, 150, Items.lead, 160, Items.silicon, 100, Items.titanium, 60, Items.plastanium, 120, Items.phaseFabric, 60));
            size = 2;
            craftTime = 130f;
            consumes.power(0.8f);
            unitType = UnityUnitTypes.cache;
        }};

        convertPad = new ConversionPad("conversion-pad"){{
            requirements(Category.units, BuildVisibility.sandboxOnly, empty);
            size = 2;
            craftTime = 60f;
            consumes.power(1f);
            upgrades.add(
                new UnitType[]{UnitTypes.dagger, UnitTypes.mace},
                new UnitType[]{UnitTypes.flare, UnitTypes.horizon},
                new UnitType[]{UnityUnitTypes.cache, UnityUnitTypes.dijkstra},
                new UnitType[]{UnityUnitTypes.omega, UnitTypes.reign}
            );
        }};

        teleporter = new Teleporter("teleporter"){{
            requirements(Category.distribution, with(Items.lead, 22, Items.silicon, 10, Items.phaseFabric, 32, UnityItems.dirium, 32));
        }};

        teleunit = new TeleUnit("teleunit"){{
            requirements(Category.units, with(Items.lead, 180, Items.titanium, 80, Items.silicon, 90, Items.phaseFabric, 64, UnityItems.dirium, 48));
            size = 3;
            ambientSound = Sounds.techloop;
            ambientSoundVolume = 0.02f;
            consumes.power(3f);
        }};

        laser = new ExpTurretPowerTurret("laser-turret"){
            {
                requirements(Category.turret, with(Items.copper, 190, Items.silicon, 110, Items.titanium, 15));
                size = 2;
                health = 800;

                reloadTime = 35f;
                coolantMultiplier = 2f;
                range = 140f;
                targetAir = false;
                shootSound = Sounds.laser;

                powerUse = 7f;
                shootType = UnityBullets.laser;

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 10;

                addField(ExpFieldType.linear, "reloadTime", reloadTime, -2f);
                addField(ExpFieldType.bool, "targetAir", false, 5f);
            }

            @Override
            public void setUpgrades(){
                addUpgrade(laserCharge, 10, false);
                addUpgrade(laserFrost, 10, false);
            }
        };

        laserCharge = new ExpTurretPowerTurret("charge-laser-turret"){
            {
                category = Category.turret;
                size = 2;
                health = 1400;

                reloadTime = 60f;
                coolantMultiplier = 2f;
                range = 140f;

                chargeTime = 50f;
                chargeMaxDelay = 30f;
                chargeEffects = 4;
                recoilAmount = 2f;
                cooldown = 0.03f;
                targetAir = true;
                shootShake = 2f;

                powerUse = 7f;

                shootEffect = UnityFx.laserChargeShoot;
                smokeEffect = Fx.none;
                chargeEffect = UnityFx.laserCharge;
                chargeBeginEffect = UnityFx.laserChargeBegin;
                heatColor = Color.red;
                shootSound = Sounds.laser;

                shootType = UnityBullets.shardLaser;
                buildVisibility = BuildVisibility.sandboxOnly;

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 30;

                addField(ExpFieldType.linear, "reloadTime", reloadTime, -1f);
            }

            @Override
            public void setUpgrades(){
                addUpgrade(laserBranch, 15, false);
                addUpgrade(laserFractal, 15, false);
                addUpgrade(laserBreakthrough, 30, true);
            }
        };

        laserFrost = new ExpTurretLiquidTurret("frost-laser-turret"){
            {
                ammo(Liquids.cryofluid, UnityBullets.frostLaser);

                category = Category.turret;
                size = 2;
                health = 1000;

                range = 160f;
                reloadTime = 80f;
                targetAir = true;
                liquidCapacity = 10f;
                buildVisibility = BuildVisibility.sandboxOnly;

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 30;

                consumes.powerCond(1f, TurretBuild::isActive);
            }

            @Override
            public void setUpgrades(){
                addUpgrade(laserKelvin, 15, false);
                addUpgrade(laserBreakthrough, 30, true);
            }
        };

        laserFractal = new ExpTurretPowerTurret("fractal-laser-turret"){
            {
                category = Category.turret;
                size = 3;
                health = 2000;

                reloadTime = UnityBullets.distField.lifetime / 3f;
                coolantMultiplier = 2f;
                range = 140f;

                chargeTime = 50f;
                chargeMaxDelay = 40f;
                chargeEffects = 5;
                recoilAmount = 4f;

                cooldown = 0.03f;
                targetAir = true;
                shootShake = 5f;
                powerUse = 13f;

                shootEffect = UnityFx.laserChargeShoot;
                smokeEffect = Fx.none;
                chargeEffect = UnityFx.laserCharge;
                chargeBeginEffect = UnityFx.laserChargeBegin;
                heatColor = Color.red;
                shootSound = Sounds.laser;

                lerpColor = true;
                fromColor = Pal.lancerLaser.cpy().lerp(Pal.sapBullet, 0.5f);
                toColor = Pal.place;

                shootType = UnityBullets.fractalLaser;
                buildVisibility = BuildVisibility.sandboxOnly;

                basicFieldRadius = 85f;

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 30;

                addField(ExpFieldType.linear, "reloadTime", reloadTime, -2f);
                addField(ExpFieldType.linear, "range", range, 0.35f * 8f);
                addField(ExpFieldType.linear, "basicFieldRadius", basicFieldRadius, 0.2f * 8f);
            }
        };

        laserBranch = new ExpTurretPowerTurret("swarm-laser-turret"){
            {
                category = Category.turret;

                size = 3;
                health = 2400;
                
                reloadTime = 90f;
                coolantMultiplier = 2.25f;
                powerUse = 15f;
                targetAir = true;
                range = 150f;
                rangeColor = UnityPal.expColor;

                chargeTime = 50f;
                chargeMaxDelay = 30f;
                chargeEffects = 4;
                recoilAmount = 2f;

                cooldown = 0.03f;
                shootShake = 2f;
                shootEffect = UnityFx.laserChargeShoot;
                smokeEffect = Fx.none;
                chargeEffect = UnityFx.laserCharge;
                chargeBeginEffect = UnityFx.laserChargeBegin;
                heatColor = Color.red;
                lerpColor = true;
                fromColor = Pal.lancerLaser.cpy().lerp(Pal.sapBullet, 0.5f);
                shootSound = Sounds.laser;
                shootType = UnityBullets.branchLaser;
                buildVisibility = BuildVisibility.sandboxOnly;

                shootLength = size * tilesize / 2.7f;
                shots = 4;
                burstSpacing = 5f;
                inaccuracy = 10f;
                xRand = 6f;

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 30;

                addField(ExpFieldType.linear, "reloadTime", reloadTime, -2f);
                addField(ExpFieldType.linear, "range", range, 0.25f * 8f);
            }
        };

        laserKelvin = new ExpTurretLiquidTurret("kelvin-laser-turret"){
            {
                ammo(
                    Liquids.water, UnityBullets.kelvinWaterLaser,
                    Liquids.slag, UnityBullets.kelvinSlagLaser,
                    Liquids.oil, UnityBullets.kelvinOilLaser,
                    Liquids.cryofluid, UnityBullets.kelvinCryofluidLaser
                );

                category = Category.turret;
                size = 3;
                health = 2100;

                range = 180f;
                reloadTime = 120f;
                targetAir = true;
                liquidCapacity = 15f;

                omni = true;
                defaultBullet = UnityBullets.kelvinLiquidLaser;

                buildVisibility = BuildVisibility.sandboxOnly;

                consumes.powerCond(2.5f, TurretBuild::isActive);

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 30;
            }
        };

        laserBreakthrough = new ExpTurretPowerTurret("bt-laser-turret"){
            {
                category = Category.turret;
                size = 4;
                health = 2800;

                range = 500f;
                coolantMultiplier = 1.5f;
                targetAir = true;
                reloadTime = 500f;

                chargeTime = 100f;
                chargeMaxDelay = 100f;
                chargeEffects = 0;

                recoilAmount = 5f;
                cooldown = 0.03f;
                powerUse = 17f;

                shootShake = 4f;
                shootEffect = UnityFx.laserBreakthroughShoot;
                smokeEffect = Fx.none;
                chargeEffect = Fx.none;
                chargeBeginEffect = UnityFx.laserBreakthroughChargeBegin;

                heatColor = Pal.lancerLaser;
                lerpColor = true;
                toColor = UnityPal.expColor;
                shootSound = Sounds.laserblast;
                chargeSound = Sounds.lasercharge;
                shootType = UnityBullets.breakthroughLaser;
                buildVisibility = BuildVisibility.sandboxOnly;

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 1;

                ioPrecision = 20f;
                orbMultiplier = 0.07f;

                addField(ExpFieldType.list, "heatColor", Color.class, new Color[]{fromColor, toColor});

                drawer = b -> {
                    if(b instanceof ExpTurretPowerTurretBuild tile){
                        Draw.rect(region, tile.x + tr2.x, tile.y + tr2.y, tile.rotation - 90f);
                        if(tile.level() >= tile.maxLevel()){
                            //Draw.blend(Blending.additive);
                            Draw.color(tile.getShootColor(tile.levelf()));
                            Draw.alpha(Mathf.absin(Time.time, 20f, 0.6f));
                            Draw.rect(Regions.btLaserTurretTopRegion, tile.x + tr2.x, tile.y + tr2.y, tile.rotation - 90f);
                            Draw.color();
                            //Draw.blend();
                        }
                    }else{
                        throw new IllegalStateException("building isn't an instance of ExpTurretPowerTurretBuild");
                    }
                };
            }
        };

        inferno = new ExpTurretItemTurret("inferno"){
            {
                requirements(Category.turret, with(Items.copper, 150, Items.lead, 165, Items.graphite, 60));
                ammo(
                    Items.scrap, Bullets.slagShot,
                    Items.coal, UnityBullets.coalBlaze,
                    Items.pyratite, UnityBullets.pyraBlaze
                );

                size = 3;
                range = 80f;
                reloadTime = 6f;
                coolantMultiplier = 2f;
                recoilAmount = 0f;
                shootCone = 5f;
                shootSound = Sounds.flame;

                hasExp = true;
                condConfig = true;
                enableUpgrade = true;

                maxLevel = 10;

                addField(ExpFieldType.list, "shots", Integer.class, new Integer[]{
                    1, 1, 2, 2, 2, 3, 3, 4, 4, 5, 5
                });
                addField(ExpFieldType.list, "spread", Float.class, new Float[]{
                    0f, 0f, 5f, 10f, 15f, 7f, 14f, 8f, 10f, 6f, 9f
                });
            }
        };

        //endregion
        //region monolith

        oreMonolite = new UnityOreBlock(UnityItems.monolite){{
            oreScale = 23.77f;
            oreThreshold = 0.807f;
            oreDefault = true;
        }};

        sharpslate = new Floor("sharpslate"){{
            variants = 3;
        }};

        infusedSharpslate = new Floor("infused-sharpslate"){{
            variants = 3;
            emitLight = true;
            lightRadius = 8f;
            lightColor = UnityPal.monolith.cpy().a(0.1f);
            albedo = 0.25f;
        }};

        archSharpslate = new Floor("archaic-sharpslate"){{
            variants = 3;
            updateEffect = UnityFx.archaicEnergy;
            blendGroup = infusedSharpslate;
            emitLight = true;
            lightRadius = 12f;
            lightColor = UnityPal.monolith.cpy().a(0.16f);
            albedo = 0.4f;
        }};

        sharpslateWall = new StaticWall("sharpslate-wall"){{
            variants = 2;
            sharpslate.asFloor().wall = this;
        }};

        infusedSharpslateWall = new StaticWall("infused-sharpslate-wall"){{
            variants = 2;
            infusedSharpslate.asFloor().wall = this;
            archSharpslate.asFloor().wall = this;
        }};

        archEnergy = new OverlayFloor("archaic-energy"){{
            variants = 3;
            emitLight = true;
            lightRadius = 16f;
            lightColor = UnityPal.monolith.cpy().a(0.24f);
        }};

        loreMonolith = new LoreMessageBlock("lore-monolith", Faction.monolith);

        deflectorAura = new DeflectProjector("deflector-aura"){{
            requirements(Category.defense, with(Items.copper, 1));
            size = 4;
            radius = 20f * tilesize;

            consumes.power(4f);
        }};

        debrisExtractor = new FloorExtractor("debris-extractor",
            infusedSharpslate, 0.04f,
            archSharpslate, 0.08f,
            archEnergy, 1f
        ){{
            requirements(Category.crafting, with(UnityItems.monolite, 140, Items.surgeAlloy, 80, Items.thorium, 60));

            size = 2;
            outputItem = new ItemStack(UnityItems.archDebris, 1);
            craftTime = 84f;

            consumes.power(2.4f);
            consumes.liquid(Liquids.cryofluid, 0.08f);

            drawer = new DrawBlock(){
                @Override
                public void draw(GenericCrafterBuild e){
                    Draw.rect(region, e.x, e.y);

                    Draw.color(UnityPal.monolith, UnityPal.monolithLight, Mathf.absin(Time.time, 6f, 1f) * e.warmup);
                    Draw.alpha(e.warmup);
                    Draw.rect(Regions.debrisExtractorHeat1Region, e.x, e.y);

                    Draw.color(UnityPal.monolith, UnityPal.monolithLight, Mathf.absin(Time.time + 4f, 6f, 1f) * e.warmup);
                    Draw.alpha(e.warmup);
                    Draw.rect(Regions.debrisExtractorHeat2Region, e.x, e.y);

                    Draw.color();
                    Draw.alpha(1f);
                }
            };
        }};

        monolithAlloyForge = new StemGenericSmelter("monolith-alloy-forge"){
            final int effectTimer = timers++;

            {
                requirements(Category.crafting, with(Items.lead, 380, UnityItems.monolite, 240, Items.silicon, 400, Items.titanium, 240, Items.thorium, 90, Items.surgeAlloy, 160));

                outputItem = new ItemStack(UnityItems.monolithAlloy, 3);
                size = 4;
                flameColor = Pal.lancerLaser;
                ambientSound = Sounds.machine;
                ambientSoundVolume = 0.6f;

                consumes.power(3.6f);
                consumes.items(with(Items.silicon, 3, UnityItems.archDebris, 1, UnityItems.monolite, 2));
                consumes.liquid(Liquids.cryofluid, 0.1f);

                update((StemSmelterBuild e) -> {
                    if(e.consValid()){
                        e.fdata = Mathf.lerpDelta(e.fdata, e.efficiency(), 0.02f);
                    }else{
                        e.fdata = Mathf.lerpDelta(e.fdata, 0f, 0.02f);
                    }

                    float temp = e.fdata;
                    if(!Mathf.zero(temp)){
                        if(e.timer.get(effectTimer, 45f)){
                            UnityFx.effect.at(e.x, e.y, e.rotation, temp);
                        }

                        if(Mathf.chanceDelta(temp * 0.5f)){
                            Lightning.create(
                                e.team,
                                Pal.lancerLaser,
                                1f,
                                e.x, e.y,
                                Mathf.randomSeed((int)Time.time + e.id, 360f), (int)(temp * 4f) + Mathf.random(3)
                            );
                        }
                    }
                });
            }
        };

        electrophobicWall = new PowerWall("electrophobic-wall"){{
            requirements(Category.defense, with(UnityItems.monolite, 4, Items.silicon, 2));

            size = 1;
            health = 400;

            energyMultiplier.put(LightningBulletType.class, 15f);
            energyMultiplier.put(LaserBulletType.class, 9f);
            energyMultiplier.put(ContinuousLaserBulletType.class, 12f);
            energyMultiplier.put(LaserBoltBulletType.class, 9f);
        }};

        electrophobicWallLarge = new PowerWall("electrophobic-wall-large"){{
            requirements(Category.defense, with(UnityItems.monolite, 16, Items.silicon, 8));

            size = 2;
            health = 1600;
            powerProduction = 4f;
            damageThreshold = 300f;

            energyMultiplier.put(LightningBulletType.class, 15f);
            energyMultiplier.put(LaserBulletType.class, 9f);
            energyMultiplier.put(ContinuousLaserBulletType.class, 12f);
            energyMultiplier.put(LaserBoltBulletType.class, 9f);
        }};

        ricochet = new PowerTurret("ricochet"){{
            requirements(Category.turret, with(UnityItems.monolite, 40));

            size = 1;
            reloadTime = 60f;
            restitution = 0.03f;
            range = 180f;
            shootCone = 15f;
            ammoUseEffect = Fx.none;
            health = 200;
            inaccuracy = 2f;
            rotateSpeed = 12f;
            shootType = UnityBullets.ricochetSmall;
            shootSound = UnitySounds.energyBolt;
            powerUse = 1f;
        }};

        diviner = new PowerTurret("diviner"){{
            requirements(Category.turret, with(Items.lead, 15, UnityItems.monolite, 30));

            size = 1;
            reloadTime = 30f;
            health = 240;
            range = 70f;
            powerUse = 1.5f;
            targetGround = true;
            targetAir = false;
            shootSound = UnitySounds.energyBolt;
            shootType = new LaserBulletType(50f){{
                length = 80f;
            }};
        }};

        lifeStealer = new LifeStealerTurret("life-stealer"){{
            requirements(Category.turret, with(Items.silicon, 50, UnityItems.monolite, 25));

            size = 1;
            health = 320;
            powerUse = 1f;
        }};

        recluse = new ItemTurret("recluse"){{
            requirements(Category.turret, with(Items.lead, 15, UnityItems.monolite, 20));
            ammo(
                Items.lead, UnityBullets.stopLead,
                UnityItems.monolite, UnityBullets.stopMonolite,
                Items.silicon, UnityBullets.stopSilicon
            );

            size = 1;
            spread = 4f;
            reloadTime = 20f;
            restitution = 0.03f;
            range = 110f;
            shootCone = 3f;
            ammoUseEffect = Fx.none;
            health = 200;
            rotateSpeed = 12f;
        }};

        absorberAura = new AbsorberTurret("absorber-aura"){{
            requirements(Category.turret, with(Items.silicon, 75, UnityItems.monolite, 125));

            size = 2;
            health = 720;
            range = 150f;
            powerUse = 1f;
        }};

        mage = new PowerTurret("mage"){{
            requirements(Category.turret, with(Items.lead, 75, Items.silicon, 50, UnityItems.monolite, 25));

            size = 2;
            health = 600;
            range = 120f;
            reloadTime = 48f;
            shootCone = 15f;
            shots = 3;
            burstSpacing = 2f;
            shootSound = Sounds.spark;
            powerUse = 2.5f;
            recoilAmount = 2.5f;
            rotateSpeed = 10f;
            shootType = new LightningBulletType(){{
                lightningLength = 20;
                damage = 32f;
            }};
        }};

        blackout = new PowerTurret("blackout"){{
            requirements(Category.turret, with(Items.graphite, 85, Items.titanium, 25, UnityItems.monolite, 125));

            size = 2;
            reloadTime = 140f;
            range = 200f;
            health = 720;
            rotateSpeed = 10f;
            powerUse = 3f;
            recoilAmount = 3f;
            shootSound = Sounds.shootBig;
            targetGround = true;
            targetAir = false;
            shootType = new BasicBulletType(6f, 60f, "shell"){
                {
                    lifetime = 35f;
                    width = height = 20f;
                    frontColor = UnityPal.monolith;
                    backColor = UnityPal.monolithDark;
                    hitEffect = despawnEffect = Fx.blastExplosion;
                    splashDamage = 60f;
                    splashDamageRadius = 3.2f * tilesize;
                }

                @Override
                public void hitEntity(Bullet b, Hitboxc other, float initialHealth){
                    super.hitEntity(b, other, initialHealth);

                    float r = splashDamageRadius;
                    Units.nearbyEnemies(b.team, b.x - r, b.y - r, r * 2f, r * 2f, unit -> {
                        if(unit.within(b, r)){
                            unit.apply(StatusEffects.unmoving, 60f);
                            unit.apply(StatusEffects.disarmed, 60f);
                        }
                    });
                }
            };
        }};

        shellshock = new PowerTurret("shellshock"){{
            requirements(Category.turret, with(Items.lead, 90, Items.graphite, 100, UnityItems.monolite, 80));

            size = 2;
            reloadTime = 75f;
            range = 260f;
            shootCone = 3f;
            ammoUseEffect = Fx.none;
            health = 720;
            rotateSpeed = 10f;
            shootType = UnityBullets.ricochetMedium;
            shootSound = UnitySounds.energyBolt;
            powerUse = 2f;
        }};

        heatRay = new HeatRayTurret("heat-ray"){{
            requirements(Category.turret, with(Items.copper, 75, Items.lead, 50, Items.graphite, 25, Items.titanium, 45, UnityItems.monolite, 50));

            size = 2;
            range = 120f;
            targetGround = true;
            targetAir = false;
            damage = 120f;
            powerUse = 2f;
            shootSound = UnitySounds.heatRay;
        }};

        oracle = new BurstPowerTurret("oracle"){{
            requirements(Category.turret, with(Items.silicon, 175, Items.titanium, 150, UnityItems.monolithAlloy, 75));
            size = 3;
            health = 1440;
            range = 180f;
            reloadTime = 72f;
            chargeTime = 30f;
            chargeMaxDelay = 4f;
            chargeEffects = 12;
            shootCone = 5f;
            shots = 8;
            burstSpacing = 2f;
            shootSound = Sounds.spark;
            shootShake = 3f;
            powerUse = 3f;
            recoilAmount = 2.5f;
            rotateSpeed = 8f;
            shootType = new LightningBulletType(){{
                damage = 64f;
                shootEffect = Fx.lightningShoot;
            }};
            chargeEffect = UnityFx.oracleChage;
            chargeBeginEffect = UnityFx.oracleChargeBegin;
            subShots = 3;
            subBurstSpacing = 1f;
            subShootEffect = Fx.hitLancer;
            subShootSound = Sounds.laser;
            subShootType = new LaserBulletType(96f){{
                length = 180f;
                sideAngle = 45f;
                inaccuracy = 8f;
            }};
        }};

        purge = new PowerTurret("purge"){{
            requirements(Category.turret, with(Items.plastanium, 75, Items.lead, 350, UnityItems.monolite, 200, UnityItems.monolithAlloy, 75));

            size = 3;
            reloadTime = 90f;
            range = 360f;
            shootCone = 3f;
            ammoUseEffect = Fx.none;
            health = 1680;
            rotateSpeed = 8f;
            shootType = UnityBullets.ricochetBig;
            shootSound = UnitySounds.energyBolt;
            powerUse = 3f;
        }};

        supernova = new AttractLaserTurret("supernova"){
            /** Temporary vector array to be used in the drawing method */
            final Vec2[] phases = new Vec2[]{new Vec2(), new Vec2(), new Vec2(), new Vec2(), new Vec2(), new Vec2()};

            final float starRadius;
            final float starOffset;
            Cons<AttractLaserTurretBuild> effectDrawer;

            final int timerChargeStar = timers++;
            final Effect starEffect;
            final Effect chargeEffect;
            final Effect chargeStarEffect;
            final Effect chargeStar2Effect;
            final Effect chargeBeginEffect;
            final Effect starDecayEffect;
            final Effect heatWaveEffect;
            final Effect pullEffect;

            {
                requirements(Category.turret, with(Items.surgeAlloy, 500, Items.silicon, 650, UnityItems.archDebris, 350, UnityItems.monolithAlloy, 325));
                size = 7;
                health = 8100;

                shootLength = size * tilesize / 2f - 8f;
                rotateSpeed = 1f;
                recoilAmount = 4f;
                powerUse = 24f;
                cooldown = 0.006f;

                shootCone = 15f;
                range = 250f;
                starRadius = 8f;
                starOffset = -2.25f;

                chargeSound = UnitySounds.supernovaCharge;
                chargeSoundVolume = 1f;
                shootSound = UnitySounds.supernovaShoot;
                loopSound = UnitySounds.supernovaActive;
                loopSoundVolume = 1f;

                baseExplosiveness = 25f;
                shootDuration = 480f;
                shootType = UnityBullets.supernovaLaser;

                starEffect = UnityFx.supernovaStar;
                chargeEffect = UnityFx.supernovaCharge;
                chargeStarEffect = UnityFx.supernovaChargeStar;
                chargeStar2Effect = UnityFx.supernovaChargeStar2;
                chargeBeginEffect = UnityFx.supernovaChargeBegin;
                starDecayEffect = UnityFx.supernovaStarDecay;
                heatWaveEffect = UnityFx.supernovaStarHeatwave;
                pullEffect = UnityFx.supernovaPullEffect;

                drawer = b -> {
                    if(b instanceof AttractLaserTurretBuild tile){
                        //core
                        phases[0].trns(tile.rotation, -tile.recoil + Mathf.curve(tile.phase, 0f, 0.3f) * -2f);
                        //left wing
                        phases[1].trns(tile.rotation - 90,
                            Mathf.curve(tile.phase, 0.2f, 0.5f) * -2f,

                            -tile.recoil + Mathf.curve(tile.phase, 0.2f, 0.5f) * 2f +
                            Mathf.curve(tile.phase, 0.5f, 0.8f) * 3f
                        );
                        //left bottom wing
                        phases[2].trns(tile.rotation - 90,
                            Mathf.curve(tile.phase, 0f, 0.3f) * -1.5f +
                            Mathf.curve(tile.phase, 0.6f, 1f) * -2f,

                            -tile.recoil + Mathf.curve(tile.phase, 0f, 0.3f) * 1.5f +
                            Mathf.curve(tile.phase, 0.6f, 1f) * -1f
                        );
                        //bottom
                        phases[3].trns(tile.rotation, -tile.recoil + Mathf.curve(tile.phase, 0f, 0.6f) * -4f);
                        //right wing
                        phases[4].trns(tile.rotation - 90,
                            Mathf.curve(tile.phase, 0.2f, 0.5f) * 2f,

                            -tile.recoil + Mathf.curve(tile.phase, 0.2f, 0.5f) * 2f +
                            Mathf.curve(tile.phase, 0.5f, 0.8f) * 3f
                        );
                        //right bottom wing
                        phases[5].trns(tile.rotation - 90,
                            Mathf.curve(tile.phase, 0f, 0.3f) * 1.5f +
                            Mathf.curve(tile.phase, 0.6f, 1f) * 2f,

                            -tile.recoil + Mathf.curve(tile.phase, 0f, 0.3f) * 1.5f +
                            Mathf.curve(tile.phase, 0.6f, 1f) * -1f
                        );

                        Draw.rect(Regions.supernovaWingLeftBottomOutlineRegion, tile.x + phases[2].x, tile.y + phases[2].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaWingRightBottomOutlineRegion, tile.x + phases[5].x, tile.y + phases[5].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaWingLeftOutlineRegion, tile.x + phases[1].x, tile.y + phases[1].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaWingRightOutlineRegion, tile.x + phases[4].x, tile.y + phases[4].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaBottomOutlineRegion, tile.x + phases[3].x, tile.y + phases[3].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaHeadOutlineRegion, tile.x + tr2.x, tile.y + tr2.y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaCoreOutlineRegion, tile.x + phases[0].x, tile.y + phases[0].y, tile.rotation - 90);

                        Draw.rect(Regions.supernovaWingLeftBottomRegion, tile.x + phases[2].x, tile.y + phases[2].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaWingRightBottomRegion, tile.x + phases[5].x, tile.y + phases[5].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaWingLeftRegion, tile.x + phases[1].x, tile.y + phases[1].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaWingRightRegion, tile.x + phases[4].x, tile.y + phases[4].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaBottomRegion, tile.x + phases[3].x, tile.y + phases[3].y, tile.rotation - 90);
                        Draw.rect(Regions.supernovaHeadRegion, tile.x + tr2.x, tile.y + tr2.y, tile.rotation - 90);

                        float z = Draw.z();
                        Draw.z(z + 0.001f);

                        Draw.rect(Regions.supernovaCoreRegion, tile.x + phases[0].x, tile.y + phases[0].y, tile.rotation - 90);
                        Draw.z(z);

                        effectDrawer.get(tile);
                    }else{
                        throw new IllegalStateException("building isn't an instance of AttractLaserTurretBuild");
                    }
                };

                heatDrawer = tile -> {
                    if(tile.heat <= 0.00001f) return;

                    float r = Utils.pow6In.apply(tile.heat);
                    float g = Interp.pow3In.apply(tile.heat);
                    float b = Interp.pow2Out.apply(tile.heat);
                    float a = Interp.pow2In.apply(tile.heat);

                    Draw.color(Tmp.c1.set(r, g, b, a));
                    Draw.blend(Blending.additive);

                    Draw.rect(heatRegion, tile.x + tr2.x, tile.y + tr2.y, tile.rotation - 90f);

                    Draw.color();
                    Draw.blend();
                };

                effectDrawer = tile -> {
                    boolean notShooting = tile.bulletLife() <= 0f || tile.bullet() == null;
                    float ch = notShooting ? tile.charge : 1f;
                    Tmp.v1.trns(tile.rotation, -tile.recoil + starOffset + Mathf.curve(tile.phase, 0f, 0.3f) * -2f);

                    Draw.color(Pal.lancerLaser);
                    Fill.circle(
                        tile.x + Tmp.v1.x,
                        tile.y + Tmp.v1.y,
                        ch * starRadius
                    );

                    if(notShooting){
                        Fill.circle(
                            tile.x + Tmp.v1.x,
                            tile.y + Tmp.v1.y,
                            tile.charge * starRadius * 0.67f
                        );
                    }

                    if(!state.isPaused()){
                        float a = Mathf.random(360f);
                        float d = 0.3f;

                        starEffect.at(
                            tile.x + Tmp.v1.x + Angles.trnsx(a, d),
                            tile.y + Tmp.v1.y + Angles.trnsy(a, d),
                            tile.rotation, Float.valueOf(ch * starRadius)
                        );
                        chargeEffect.at(
                            tile.x + Angles.trnsx(tile.rotation, -tile.recoil + shootLength),
                            tile.y + Angles.trnsy(tile.rotation, -tile.recoil + shootLength),
                            tile.rotation, Float.valueOf(tile.charge * starRadius * 0.67f)
                        );

                        if(notShooting){
                            if(tile.charge > 0.1f && tile.timer(timerChargeStar, 20f)){
                                chargeStarEffect.at(
                                    tile.x + Tmp.v1.x,
                                    tile.y + Tmp.v1.y,
                                    tile.rotation, Float.valueOf(tile.charge)
                                );
                            }

                            if(Mathf.chanceDelta(tile.charge)){
                                chargeBeginEffect.at(
                                    tile.x + Angles.trnsx(tile.rotation, -tile.recoil + shootLength),
                                    tile.y + Angles.trnsy(tile.rotation, -tile.recoil + shootLength),
                                    tile.rotation, Float.valueOf(tile.charge)
                                );

                                chargeStar2Effect.at(
                                    tile.x + Tmp.v1.x,
                                    tile.y + Tmp.v1.y,
                                    tile.rotation, Float.valueOf(tile.charge)
                                );
                            }
                        }else{
                            starDecayEffect.at(
                                tile.x + Tmp.v1.x,
                                tile.y + Tmp.v1.y,
                                tile.rotation
                            );

                            if(tile.timer(timerChargeStar, 20f)){
                                heatWaveEffect.at(
                                    tile.x + Tmp.v1.x,
                                    tile.y + Tmp.v1.y,
                                    tile.rotation
                                );
                            }
                        }
                    }
                };

                attractor = tile -> {
                    if(Mathf.chanceDelta(tile.charge)){
                        Tmp.v1
                            .trns(tile.rotation, -tile.recoil + starOffset + Mathf.curve(tile.phase, 0f, 0.3f) * -2f)
                            .add(tile);

                        Lightning.create(
                            tile.team,
                            Pal.lancerLaser,
                            60f, Tmp.v1.x, Tmp.v1.y,
                            Mathf.randomSeed((long)(tile.id + Time.time), 360f),
                            Mathf.round(Mathf.randomTriangular(16f, 24f) * tile.charge)
                        );
                    }
                };

                attractUnit = (tile, unit) -> {
                    if(Mathf.chanceDelta(0.1f)){
                        Tmp.v1
                            .trns(tile.rotation, -tile.recoil + starOffset + Mathf.curve(tile.phase, 0f, 0.3f) * -2f)
                            .add(tile);

                        pullEffect.at(tile.x, tile.y, tile.rotation, new Float[]{unit.x, unit.y, Tmp.v1.x, Tmp.v1.y, tile.charge * (3f + Mathf.range(0.2f))});
                    }
                };
            }
        };

        /*monolithGroundFactory = new UnitFactory("monolith-ground-factory"){{
            requirements(Category.units, with());
            size = 3;
            plans = Seq.with(new UnitPlan(UnityUnitTypes.stele, 900f, with(Items.silicon, 10, UnityItems.monolite, 15)));
            consumes.power(1.2f);
        }};*/

        //endregion
        //region youngcha

        concreteBlank = new Floor("concrete-blank");

        concreteFill = new Floor("concrete-fill"){{
            variants = 0;
        }};

        concreteNumber = new Floor("concrete-number"){{
            variants = 10;
        }};

        concreteStripe = new Floor("concrete-stripe");

        concrete = new Floor("concrete");

        stoneFullTiles = new Floor("stone-full-tiles");

        stoneFull = new Floor("stone-full");

        stoneHalf = new Floor("stone-half");

        stoneTiles = new Floor("stone-tiles");

        smallTurret = new ModularTurret("small-turret-base"){{
            requirements(Category.turret, with(Items.graphite, 20, UnityItems.nickel, 30, Items.copper, 30));
            size = 2;
            health = 1200;
            setGridW(3);
            setGridH(3);
            spriteGridSize = 18;
            spriteGridPadding = 3;
            yScale = 0.8f;
            addGraph(new GraphTorque(0.03f, 50f).setAccept(1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
            addGraph(new GraphHeat(50f, 0.1f, 0.01f).setAccept(1, 1, 1, 1, 1, 1, 1, 1));
        }};

        medTurret = new ModularTurret("med-turret-base"){{
            requirements(Category.turret, with(Items.graphite, 25, UnityItems.nickel, 30, Items.titanium, 50, Items.silicon, 40));
            size = 3;
            health = 1200;
            acceptsItems = true;
            setGridW(5);
            setGridH(5);
            spriteGridSize = 16;
            spriteGridPadding = 4;
            yShift = 0.8f;
            yScale = 0.8f;
            partCostAccum = 0.12f;
            addGraph(new GraphTorque(0.05f, 150f).setAccept(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            addGraph(new GraphHeat(120f, 0.05f, 0.02f).setAccept(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        }};

        chopper = new Chopper("chopper"){{
            requirements(Category.turret, with(UnityItems.nickel, 50, Items.titanium, 50, Items.lead, 30));
            health = 650;
            setGridW(7);
            setGridH(1);
            addPart(
                bundle.get("part.unity.pivot.name"), bundle.get("part.unity.pivot.info"), PartType.blade, 4, 0, 1, 1, true, true,
                new Point2(0, 0), new ItemStack[0], new byte[]{1, 0, 0, 0}, new byte[]{0, 0, 0, 0},
                new PartStat(PartStatType.mass, 1), new PartStat(PartStatType.collides, false), new PartStat(PartStatType.hp, 10)
            );
            addPart(
                bundle.get("part.unity.blade.name"), bundle.get("part.unity.blade.info"), PartType.blade, 0, 0, 1, 1,
                with(UnityItems.nickel, 3, Items.titanium, 5), new byte[]{1, 0, 0, 0}, new byte[]{0, 0, 1, 0},
                new PartStat(PartStatType.mass, 2), new PartStat(PartStatType.collides, true), new PartStat(PartStatType.hp, 80), new PartStat(PartStatType.damage, 5)
            );
            addPart(
                bundle.get("part.unity.serrated-blade.name"), bundle.get("part.unity.serrated-blade.info"), PartType.blade, 2, 0, 2, 1,
                with(UnityItems.nickel, 8, Items.lead, 5), new byte[]{1, 0, 0, 0, 0, 0}, new byte[]{0, 0, 0, 1, 0, 0},
                new PartStat(PartStatType.mass, 6), new PartStat(PartStatType.collides, true), new PartStat(PartStatType.hp, 120), new PartStat(PartStatType.damage, 12)
            );
            addPart(
                bundle.get("part.unity.rod.name"), bundle.get("part.unity.rod.info"), PartType.blade, 1, 0, 1, 1,
                with(Items.titanium, 3), new byte[]{1, 0, 0, 0}, new byte[]{0, 0, 1, 0},
                new PartStat(PartStatType.mass, 1), new PartStat(PartStatType.collides, false), new PartStat(PartStatType.hp, 40)
            );
            addGraph(new GraphTorque(0.03f, 5f).setAccept(1, 0, 0, 0));
        }};

        augerDrill = new AugerDrill("auger-drill"){{
            requirements(Category.production, with(Items.lead, 100, Items.copper, 75));
            size = 3;
            health = 1000;
            tier = 3;
            drillTime = 400f;
            addGraph(new GraphTorqueConsume(45f, 8f, 0.03f, 0.15f).setAccept(0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0));
        }};

        mechanicalExtractor = new MechanicalExtractor("mechanical-extractor"){{
            requirements(Category.production, with(Items.lead, 100, Items.copper, 75));
            hasPower = false;
            size = 3;
            health = 1000;
            pumpAmount = 0.4f;
            
            addGraph(new GraphTorqueConsume(45f, 8f, 0.06f, 0.3f).setAccept(0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0));
        }};

        sporeFarm = new SporeFarm("spore-farm"){{
            requirements(Category.production, with(Items.lead, 5));
            health = 50;
            rebuildable = false;
            hasItems = true;
            itemCapacity = 2;
            buildCostMultiplier = 0.01f;
            breakSound = Sounds.splash;
        }};

        mechanicalConveyor = new ShadowedConveyor("mechanical-conveyor"){{
            requirements(Category.distribution, with(Items.copper, 3, UnityItems.nickel, 2));
            health = 250;
            speed = 0.1f;
        }};

        heatPipe = new HeatPipe("heat-pipe"){{
            requirements(Category.distribution, with(Items.copper, 15, UnityItems.cupronickel, 10, UnityItems.nickel, 5));
            health = 140;
            addGraph(new GraphHeat(5f, 0.7f, 0.008f).setAccept(1, 1, 1, 1));
        }};

        driveShaft = new DriveShaft("drive-shaft"){{
            requirements(Category.distribution, with(Items.copper, 10, Items.lead, 10));
            health = 150;
            addGraph(new GraphTorque(0.01f, 3f).setAccept(1, 0, 1, 0));
        }};

        inlineGearbox = new InlineGearbox("inline-gearbox"){{
            requirements(Category.distribution, with(Items.titanium, 20, Items.lead, 30, Items.copper, 30));
            size = 2;
            health = 700;
            addGraph(new GraphTorque(0.02f, 20f).setAccept(1, 1, 0, 0, 1, 1, 0, 0));
        }};

        shaftRouter = new GraphBlock("shaft-router"){{
            requirements(Category.distribution, with(Items.copper, 20, Items.lead, 20));
            health = 100;
            preserveDraw = true;
            addGraph(new GraphTorque(0.05f, 5f).setAccept(1, 1, 1, 1));
        }};

        simpleTransmission = new SimpleTransmission("simple-transmission"){{
            requirements(Category.distribution, with(Items.titanium, 50, Items.lead, 50, Items.copper, 50));
            size = 2;
            health = 500;
            addGraph(new GraphTorqueTrans(0.05f, 25f).setRatio(1f, 2.5f).setAccept(2, 1, 0, 0, 1, 2, 0, 0));
        }};

        crucible = new Crucible("crucible"){{
            requirements(Category.crafting, with(UnityItems.nickel, 10, Items.titanium, 15));
            health = 400;
            addGraph(new GraphCrucible().setAccept(1, 1, 1, 1));
            addGraph(new GraphHeat(75f, 0.2f, 0.006f).setAccept(1, 1, 1, 1));
        }};

        holdingCrucible = new HoldingCrucible("holding-crucible"){{
            requirements(Category.crafting, with(UnityItems.nickel, 50, UnityItems.cupronickel, 150, Items.metaglass, 150, Items.titanium, 30));
            size = 4;
            health = 2400;
            addGraph(new GraphCrucible(50f, false).setAccept(0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0));
            addGraph(new GraphHeat(275f, 0.05f, 0.01f).setAccept(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        }};

        cruciblePump = new CruciblePump("crucible-pump"){{
            requirements(Category.crafting, with(UnityItems.cupronickel, 50, UnityItems.nickel, 50, Items.metaglass, 15));
            size = 2;
            health = 500;
            consumes.power(1f);
            addGraph(new GraphCrucible(10f, false).setAccept(1, 1, 0, 0, 2, 2, 0, 0).multi());
            addGraph(new GraphHeat(50f, 0.1f, 0.003f).setAccept(1, 1, 1, 1, 1, 1, 1, 1));
        }};

        castingMold = new CastingMold("casting-mold"){{
            requirements(Category.crafting, with(Items.titanium, 70, UnityItems.nickel, 30));
            size = 2;
            health = 700;
            addGraph(new GraphCrucible(2f, false).setAccept(0, 0, 0, 0, 1, 1, 0, 0));
            addGraph(new GraphHeat(55f, 0.2f, 0.0f).setAccept(1, 1, 1, 1, 1, 1, 1, 1));
        }};

        sporePyrolyser = new SporePyrolyser("spore-pyrolyser"){{
            requirements(Category.crafting, with(UnityItems.nickel, 25, Items.titanium, 50, Items.copper, 50, Items.lead, 30));
            size = 3;
            health = 1100;
            craftTime = 50f;
            outputItem = new ItemStack(Items.coal, 3);
            ambientSound = Sounds.machine;
            ambientSoundVolume = 0.6f;
            consumes.item(Items.sporePod, 1);
            addGraph(new GraphHeat(60f, 0.4f, 0.008f).setAccept(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        }};

        smallRadiator = new GraphBlock("small-radiator"){{
            requirements(Category.power, with(Items.copper, 30, UnityItems.cupronickel, 20, UnityItems.nickel, 15));
            health = 200;
            solid = true;
            addGraph(new GraphHeat(10f, 0.7f, 0.05f).setAccept(1, 1, 1, 1));
        }};

        thermalHeater = new ThermalHeater("thermal-heater"){{
            requirements(Category.power, with(Items.copper, 150, UnityItems.nickel, 100, Items.titanium, 150));
            size = 2;
            health = 500;
            maxTemp = 1100f;
            mulCoeff = 0.11f;
            addGraph(new GraphHeat(40f, 0.6f, 0.004f).setAccept(1, 1, 0, 0, 0, 0, 0, 0));
        }};

        combustionHeater = new CombustionHeater("combustion-heater"){{
            requirements(Category.power, with(Items.copper, 100, UnityItems.nickel, 70, Items.graphite, 40, Items.titanium, 80));
            size = 2;
            health = 550;
            itemCapacity = 5;
            maxTemp = 1200f;
            mulCoeff = 0.45f;
            addGraph(new GraphHeat(40f, 0.6f, 0.004f).setAccept(1, 1, 0, 0, 0, 0, 0, 0));
        }};

        solarCollector = new SolarCollector("solar-collector"){{
            requirements(Category.power, with(UnityItems.nickel, 80, Items.titanium, 50, Items.lead, 30));
            size = 3;
            health = 1500;
            maxTemp = 800f;
            mulCoeff = 0.03f;
            addGraph(new GraphHeat(60f, 1f, 0.02f).setAccept(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0));
        }};

        solarReflector = new SolarReflector("solar-reflector"){{
            requirements(Category.power, with(UnityItems.nickel, 25, Items.copper, 50));
            size = 2;
            health = 800;
        }};

        nickelStator = new Magnet("nickel-stator"){{
            requirements(Category.power, with(UnityItems.nickel, 30, Items.titanium, 20));
            health = 450;
            addGraph(new GraphFlux(2f).setAccept(1, 0, 0, 0));
        }};

        nickelStatorLarge = new Magnet("nickel-stator-large"){{
            requirements(Category.power, with(UnityItems.nickel, 250, Items.titanium, 150));
            size = 2;
            health = 1800;
            addGraph(new GraphFlux(10f).setAccept(1, 1, 0, 0, 0, 0, 0, 0));
        }};

        nickelElectromagnet = new Magnet("nickel-electromagnet"){{
            requirements(Category.power, with(UnityItems.nickel, 250, Items.titanium, 200, Items.copper, 100, UnityItems.cupronickel, 50));
            size = 2;
            health = 1000;
            consumes.power(1.6f);
            addGraph(new GraphFlux(25f).setAccept(1, 1, 0, 0, 0, 0, 0, 0));
        }};

        electricRotorSmall = new RotorBlock("electric-rotor-small"){{
            requirements(Category.power, with(UnityItems.nickel, 30, Items.copper, 50, Items.titanium, 10));
            health = 120;
            powerProduction = 2f;
            fluxEfficiency = 10f;
            rotPowerEfficiency = 0.8f;
            torqueEfficiency = 0.7f;
            baseTorque = 1f;
            baseTopSpeed = 3f;
            consumes.power(1f);
            addGraph(new GraphFlux(false).setAccept(0, 1, 0, 1));
            addGraph(new GraphTorque(0.08f, 20f).setAccept(1, 0, 1, 0));
        }};

        electricRotor = new RotorBlock("electric-rotor"){{
            requirements(Category.power, with(UnityItems.nickel, 200, Items.copper, 200, Items.titanium, 150, Items.graphite, 100));
            size = 3;
            health = 1000;
            powerProduction = 32f;
            big = true;
            fluxEfficiency = 10f;
            rotPowerEfficiency = 0.8f;
            torqueEfficiency = 0.8f;
            baseTorque = 5f;
            baseTopSpeed = 15f;
            consumes.power(16f);
            addGraph(new GraphFlux(false).setAccept(0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1));
            addGraph(new GraphTorque(0.05f, 150f).setAccept(0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0));
        }};

        handCrank = new HandCrank("hand-crank"){{
            requirements(Category.power, with(UnityItems.nickel, 5, Items.lead, 20));
            health = 120;
            addGraph(new GraphTorque(0.01f, 3f).setAccept(1, 0, 0, 0));
        }};

        windTurbine = new WindTurbine("wind-turbine"){{
            requirements(Category.power, with(Items.titanium, 20, Items.lead, 80, Items.copper, 70));
            size = 3;
            health = 1200;
            addGraph(new GraphTorqueGenerate(0.03f, 20f, 5f, 5f).setAccept(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        }};

        waterTurbine = new WaterTurbine("water-turbine"){{
            requirements(Category.power, with(Items.metaglass, 50, UnityItems.nickel, 20, Items.lead, 150, Items.copper, 100));
            size = 3;
            health = 1100;
            liquidCapacity = 250f;
            liquidPressure = 0.3f;
            disableOgUpdate();
            addGraph(new GraphTorqueGenerate(0.3f, 20f, 7f, 15f).setAccept(0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0));
        }};

        electricMotor = new ElectricMotor("electric-motor"){{
            requirements(Category.power, with(Items.silicon, 100, Items.lead, 80, Items.copper, 150, Items.titanium, 150));
            size = 3;
            health = 1300;
            consumes.power(4.5f);
            addGraph(new GraphTorqueGenerate(0.1f, 25f, 10f, 16f).setAccept(0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0));
        }};

        cupronickelWall = new HeatWall("cupronickel-wall"){{
            requirements(Category.defense, with(UnityItems.cupronickel, 8, UnityItems.nickel, 5));
            health = 500;
            addGraph(new GraphHeat(50f, 0.5f, 0.03f).setAccept(1, 1, 1, 1));
        }};

        cupronickelWallLarge = new HeatWall("cupronickel-wall-large"){{
            requirements(Category.defense, with(UnityItems.cupronickel, 36, UnityItems.nickel, 20));
            size = 2;
            health = 2000;
            minStatusRadius = 8f;
            statusRadiusMul = 40f;
            minStatusDuration = 5f;
            statusDurationMul = 120f;
            statusTime = 120f;
            maxDamage = 40f;
            addGraph(new GraphHeat(200f, 0.5f, 0.09f).setAccept(1, 1, 1, 1, 1, 1, 1, 1));
        }};

        infiHeater = new HeatSource("infi-heater"){{
            requirements(Category.power, BuildVisibility.sandboxOnly, with());
            health = 200;
            addGraph(new GraphHeat(1000f, 1f, 0f).setAccept(1, 1, 1, 1));
        }};

        infiCooler = new HeatSource("infi-cooler"){{
            requirements(Category.power, BuildVisibility.sandboxOnly, with());
            health = 200;
            isVoid = true;
            addGraph(new GraphHeat(1000f, 1f, 0f).setAccept(1, 1, 1, 1));
        }};

        infiTorque = new TorqueGenerator("infi-torque"){{
            requirements(Category.power, BuildVisibility.sandboxOnly, with());
            health = 200;
            preserveDraw = true;
            rotate = false;
            addGraph(new GraphTorqueGenerate(0.001f, 1f, 999999f, 9999f).setAccept(1, 1, 1, 1));
        }};

        neodymiumStator = new Magnet("neodymium-stator"){{
            requirements(Category.power, BuildVisibility.sandboxOnly, with());
            health = 400;
            addGraph(new GraphFlux(200f).setAccept(1, 0, 0, 0));
        }};

        //endregion
        //region advance

        celsius = new PowerTurret("celsius"){{
            requirements(Category.turret, with(Items.silicon, 20, UnityItems.xenium, 15, Items.titanium, 30, UnityItems.advanceAlloy, 25));
            health = 780;
            size = 1;
            reloadTime = 3f;
            range = 47f;
            shootCone = 50f;
            heatColor = Color.valueOf("ccffff");
            ammoUseEffect = Fx.none;
            inaccuracy = 9.2f;
            rotateSpeed = 7.5f;
            shots = 2;
            recoilAmount = 1f;
            powerUse = 13.9f;
            hasPower = true;
            targetAir = true;
            shootSound = Sounds.flame;
            cooldown = 0.01f;
            shootType = UnityBullets.celsiusSmoke;
        }};

        kelvin = new PowerTurret("kelvin"){{
            requirements(Category.turret, with(Items.silicon, 80, UnityItems.xenium, 35, Items.titanium, 90, UnityItems.advanceAlloy, 50));
            health = 2680;
            size = 2;
            reloadTime = 3f;
            range = 100f;
            shootCone = 50f;
            heatColor = Color.valueOf("ccffff");
            ammoUseEffect = Fx.none;
            inaccuracy = 9.2f;
            rotateSpeed = 6.5f;
            shots = 2;
            spread = 6f;
            recoilAmount = 1f;
            powerUse = 13.9f;
            hasPower = true;
            targetAir = true;
            shootSound = Sounds.flame;
            cooldown = 0.01f;
            shootType = UnityBullets.kelvinSmoke;
        }};

        xenoCorruptor = new LaserTurret("xeno-corruptor"){{
                requirements(Category.turret, with(Items.lead, 640, Items.graphite, 740, Items.titanium, 560, Items.surgeAlloy, 650, Items.silicon, 720, Items.thorium, 400, UnityItems.xenium, 340, UnityItems.advanceAlloy, 640));
                health = 7900;
                size = 7;
                reloadTime = 230f;
                range = 290f;
                coolantMultiplier = 1.4f;
                shootCone = 40f;
                shootDuration = 310f;
                firingMoveFract = 0.16f;
                powerUse = 45f;
                shootShake = 3f;
                recoilAmount = 8f;
                shootSound = Sounds.laser;
                loopSound = UnitySounds.xenoBeam;
                loopSoundVolume = 2f;
                shootType = new ChangeTeamLaserBulletType(60f){{
                    length = 300f;
                    lifetime = 18f;
                    shootEffect = Fx.none;
                    smokeEffect = Fx.none;
                    hitEffect = Fx.hitLancer;
                    incendChance = -1f;
                    lightColor = Color.valueOf("59a7ff");
                    conversionStatusEffect = UnityStatusEffects.teamConverted;
                    convertBlocks = false;

                    colors = new Color[]{Color.valueOf("59a7ff55"), Color.valueOf("59a7ffaa"), Color.valueOf("a3e3ff"), Color.white};
                }};

                consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.4f && liquid.flammability < 0.1f, 2.1f)).update(false);
            }

            @Override
            public void load(){
                super.load();
                baseRegion = atlas.find("unity-block-" + size);
            }
        };

        cube = new ObjPowerTurret("the-cube"){{
            requirements(Category.turret, with(Items.copper, 3300, Items.lead, 2900, Items.graphite, 4400, Items.silicon, 3800, Items.titanium, 4600, UnityItems.xenium, 2300, Items.phaseFabric, 670, UnityItems.advanceAlloy, 1070));
            health = 22500;
            object = UnityObjs.cube;
            size = 10;
            range = 320f;
            reloadTime = 240f;
            powerUse = 260f;
            coolantMultiplier = 1.1f;
            shootSound = UnitySounds.cubeBlast;
            shootType = new PointBlastLaserBulletType(580f){{
                length = 320f;
                lifetime = 17f;
                pierce = true;
                auraDamage = 8000f;
                damageRadius = 120f;
                laserColors = new Color[]{UnityPal.advance};
            }};
        }};

        //endregion
        //region end

        terminalCrucible = new StemGenericSmelter("terminal-crucible"){{
            requirements(Category.crafting, with(Items.lead, 810, Items.graphite, 720, Items.silicon, 520, Items.phaseFabric, 430, Items.surgeAlloy, 320, UnityItems.plagueAlloy, 120, UnityItems.darkAlloy, 120, UnityItems.lightAlloy, 120, UnityItems.advanceAlloy, 120, UnityItems.monolithAlloy, 120, UnityItems.sparkAlloy, 120, UnityItems.superAlloy, 120));
            flameColor = UnityPal.scarColor;
            outputItem = new ItemStack(UnityItems.terminum, 1);
            size = 6;
            craftTime = 310f;
            ambientSound = Sounds.respawning;
            ambientSoundVolume = 0.6f;

            consumes.power(45.2f);
            consumes.items(with(UnityItems.plagueAlloy, 3, UnityItems.darkAlloy, 3, UnityItems.lightAlloy, 3, UnityItems.advanceAlloy, 3, UnityItems.monolithAlloy, 3, UnityItems.sparkAlloy, 3, UnityItems.superAlloy, 3));

            preserveDraw = false;
            draw((StemSmelterBuild e) -> {
                drawer.draw(e);
                if(e.warmup > 0f){
                    Draw.blend(Blending.additive);

                    Draw.color(1f, Mathf.absin(5f, 0.5f) + 0.5f, Mathf.absin(Time.time + 90f * Mathf.radDeg, 5f, 0.5f) + 0.5f, e.warmup);
                    Draw.rect(Regions.terminalCrucibleLightsRegion, e.x, e.y);

                    float b = (Mathf.absin(8f, 0.25f) + 0.75f) * e.warmup;
                    Draw.color(1f, b, b, b);

                    Draw.rect(topRegion, e.x, e.y);

                    Draw.blend();
                    Draw.color();
                }
            });
        }};

        endForge = new StemGenericSmelter("end-forge"){{
            requirements(Category.crafting, with(Items.silicon, 2300, Items.phaseFabric, 650, Items.surgeAlloy, 1350, UnityItems.plagueAlloy, 510, UnityItems.darkAlloy, 510, UnityItems.lightAlloy, 510, UnityItems.advanceAlloy, 510, UnityItems.monolithAlloy, 510, UnityItems.sparkAlloy, 510, UnityItems.superAlloy, 510, UnityItems.terminationFragment, 230));
            outputItem = new ItemStack(UnityItems.terminaAlloy, 2);
            size = 8;
            craftTime = 410f;
            ambientSoundVolume = 0.6f;

            consumes.power(86.7f);
            consumes.items(with(UnityItems.terminum, 3, UnityItems.darkAlloy, 5, UnityItems.lightAlloy, 5));

            update((StemSmelterBuild e) -> {
                if(e.consValid() && Mathf.chanceDelta(0.7f * e.warmup)){
                    UnityFx.forgeAbsorbEffect.at(e.x, e.y, Mathf.random(360f));
                }
            });

            preserveDraw = false;
            draw((StemSmelterBuild e) -> {
                drawer.draw(e);
                if(e.warmup <= 0.0001f) return;
                Draw.blend(Blending.additive);
                Draw.color(1f, Mathf.absin(5f, 0.5f) + 0.5f, Mathf.absin(Time.time + 90f * Mathf.radDeg, 5f, 0.5f) + 0.5f, e.warmup);
                Draw.rect(Regions.endForgeLightsRegion, e.x, e.y);
                float b = (Mathf.absin(8f, 0.25f) + 0.75f) * e.warmup;
                Draw.color(1f, b, b, b);
                Draw.rect(topRegion, e.x, e.y);
                for(int i = 0; i < 4; i++){
                    float ang = i * 90f;
                    for(int s = 0; s < 2; s++){
                        float offset = 360f / 8f * (i * 2 + s);
                        TextureRegion reg = Regions.endForgeTopSmallRegion;
                        int sign = Mathf.signs[s];
                        float colA = (Mathf.absin(Time.time + offset * Mathf.radDeg, 8f, 0.25f) + 0.75f) * e.warmup;
                        float colB = (Mathf.absin(Time.time + (90f + offset) * Mathf.radDeg, 8f, 0.25f) + 0.75f) * e.warmup;
                        Draw.color(1, colA, colB, e.warmup);
                        Draw.rect(reg, e.x, e.y, reg.width * sign * Draw.scl, reg.height * Draw.scl, -ang);
                    }
                }
                Draw.blend();
                Draw.color();
            });
        }};

        tenmeikiri = new EndLaserTurret("tenmeikiri"){{
            requirements(Category.turret, with(Items.phaseFabric, 3000, Items.surgeAlloy, 4000,
            UnityItems.darkAlloy, 1800, UnityItems.terminum, 1200, UnityItems.terminaAlloy, 200));

            health = 23000;
            range = 900f;
            size = 15;

            shootCone = 1.5f;
            reloadTime = 5f * 60f;
            coolantMultiplier = 0.5f;
            recoilAmount = 15f;
            powerUse = 350f;
            absorbLasers = true;
            shootLength = 8f;
            chargeTime = 158f;
            chargeEffects = 12;
            chargeMaxDelay = 80f;
            chargeEffect = UnityFx.tenmeikiriChargeEffect;
            chargeBeginEffect = UnityFx.tenmeikiriChargeBegin;
            chargeSound = UnitySounds.tenmeikiriCharge;
            shootSound = UnitySounds.tenmeikiriShoot;
            shootShake = 4f;
            shootType = new EndCutterLaserBulletType(7800f){{
                maxLength = 1200f;
                lifetime = 3f * 60f;
                width = 30f;
                laserSpeed = 80f;
                status = StatusEffects.melting;
                antiCheatScl = 5f;
                statusDuration = 200f;
                lightningColor = UnityPal.scarColor;
                lightningDamage = 85f;
                lightningLength = 15;
            }};

            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.25f && liquid.flammability < 0.1f, 3.1f)).update(false);
        }};

        endGame = new EndGameTurret("endgame"){{
            requirements(Category.turret, with(Items.phaseFabric, 9500, Items.surgeAlloy, 10500,
                UnityItems.darkAlloy, 2300, UnityItems.lightAlloy, 2300, UnityItems.advanceAlloy, 2300,
                UnityItems.plagueAlloy, 2300, UnityItems.sparkAlloy, 2300, UnityItems.monolithAlloy, 2300,
                UnityItems.superAlloy, 2300, UnityItems.terminum, 1600, UnityItems.terminaAlloy, 800, UnityItems.terminationFragment, 100
            ));

            shootCone = 360f;
            reloadTime = 430f;
            range = 820f;
            size = 14;
            coolantMultiplier = 0.6f;
            hasItems = true;
            itemCapacity = 10;

            shootType = new BulletType(){{
                //damage = Float.MAX_VALUE;
                damage = (float)Double.MAX_VALUE;
            }};
            consumes.item(UnityItems.terminum, 2);
        }};

        //endregion
    }
}
