package unity;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.scene.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.mod.*;
import mindustry.world.blocks.environment.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import unity.ai.kami.*;
import unity.async.*;
import unity.cinematic.*;
import unity.content.*;
import unity.gen.*;
import unity.gen.Regions.*;
import unity.graphics.*;
import unity.mod.*;
import unity.sync.*;
import unity.ui.*;
import unity.ui.dialogs.*;
import unity.util.*;
import younggamExperimental.Parts;

import static mindustry.Vars.*;

@SuppressWarnings("unchecked")
public class Unity extends Mod{
    public static MusicHandler music;
    public static TapHandler tap;
    public static AntiCheat antiCheat;
    public static DevBuild dev;

    private static final ContentList[] content = {
        new UnityItems(),
        new UnityStatusEffects(),
        new UnityWeathers(),
        new UnityLiquids(),
        new UnityBullets(),
        new UnityWeaponTemplates(),
        new UnityUnitTypes(),
        new UnityBlocks(),
        new UnityPlanets(),
        new UnitySectorPresets(),
        new UnityTechTree(),
        new Parts(),
        new OverWriter()
    };

    public Unity(){
        ContributorList.init();

        Core.assets.setLoader(WavefrontObject.class, new WavefrontObjectLoader(tree));

        KamiPatterns.load();
        KamiBulletDatas.load();

        Events.on(ContentInitEvent.class, e -> {
            if(!headless){
                Regions.load();
                KamiRegions.load();
            }

            UnityFonts.load();
            UnityStyles.load();
        });

        Events.on(FileTreeInitEvent.class, e -> {
            UnityObjs.load();
            UnitySounds.load();
            UnityShaders.load();
        });

        Events.on(DisposeEvent.class, e ->
            UnityShaders.dispose()
        );

        Events.on(ClientLoadEvent.class, e -> {
            addCredits();

            UnitySettings.init();
            SpeechDialog.init();

            var mod = mods.getMod(Unity.class);

            Func<String, String> stringf = value -> Core.bundle.get("mod." + mod.name + "." + value);
            mod.meta.displayName = stringf.get("name");
            mod.meta.description = stringf.get("description");

            Core.settings.getBoolOnce("unity-install", () -> Time.runTask(5f, CreditsDialog::showList));
        });

        try{
            Class<? extends DevBuild> impl = (Class<? extends DevBuild>)Class.forName("unity.mod.DevBuildImpl");
            dev = impl.getDeclaredConstructor().newInstance();

            print("Dev build class implementation found and instantiated.");
        }catch(Throwable e){
            print("Dev build class implementation not found; defaulting to regular user implementation.");
            dev = new DevBuild(){};
        }

        music = new MusicHandler(){};
        tap = new TapHandler();
        antiCheat = new AntiCheat();

        asyncCore.processes.add(new LightProcess());
    }

    @Override
    public void init(){
        music.setup();
        antiCheat.setup();
        dev.setup();

        UnityCall.init();
        BlockMovement.init();

        dev.init();
    }

    @Override
    public void loadContent(){
        for(ContentList list : content){
            list.load();
            print("Loaded content list: " + list.getClass().getSimpleName());
        }

        FactionMeta.init();
        UnityEntityMapping.init();

        logContent();

        //this internal implementation is used for loading Regions' outlines and such
        new UnlockableContent("internal-implementation"){
            @Override
            public void loadIcon(){
                fullIcon = Core.atlas.find("clear");
                uiIcon = Core.atlas.find("clear");
            }

            @Override
            public boolean isHidden(){
                return false;
            }

            @Override
            public boolean logicVisible(){
                return false;
            }

            @Override
            public void unlock(){
                //does nothing
            }

            @Override
            public void quietUnlock(){
                //does nothing
            }

            @Override
            public void createIcons(MultiPacker packer){
                Regions.load(); //load shadowed regions first

                var color = new Color();
                for(var field : Regions.class.getDeclaredFields()){
                    if(!TextureRegion.class.isAssignableFrom(field.getType()) || !field.getName().endsWith("OutlineRegion")) continue;

                    var raw = ReflectUtils.findField(Regions.class, field.getName().replace("Outline", ""), false);
                    AtlasRegion region = ReflectUtils.getField(null, raw);

                    if(region.found()){
                        var meta = raw.getAnnotation(Outline.class);
                        var outlineColor = Color.valueOf(color, meta == null ? "464649" : meta.color());
                        var outlineRadius = meta == null ? 4 : meta.radius();

                        var pix = GraphicUtils.get(packer, region);
                        var out = Pixmaps.outline(pix, outlineColor, outlineRadius);

                        if(Core.settings.getBool("linear")){
                            Pixmaps.bleed(out);
                        }

                        packer.add(PageType.main, region.name + "-outline", out);
                    }
                }
            }

            @Override
            public ContentType getContentType(){
                return ContentType.typeid_UNUSED;
            }
        };
    }

    public void logContent(){
        for(Faction faction : Faction.all){
            var array = FactionMeta.getByFaction(faction, Object.class);
            print(Strings.format("Faction @ has @ contents.", faction.localizedName, array.size));
        }

        Seq<Class<?>> ignored = Seq.with(Floor.class, Prop.class);
        Cons<Seq<? extends Content>> checker = list -> {
            for(var cont : list){
                if(
                    !(cont instanceof UnlockableContent ucont) ||
                        (cont.minfo.mod == null || !cont.minfo.mod.name.equals("unity"))
                ) continue;

                if(Core.bundle.getOrNull(ucont.getContentType() + "." + ucont.name + ".name") == null){
                    print(Strings.format("@ has no bundle entry for name", ucont));
                }

                if(!ignored.contains(c -> c.isAssignableFrom(ucont.getClass())) && Core.bundle.getOrNull(ucont.getContentType() + "." + ucont.name + ".description") == null){
                    print(Strings.format("@ has no bundle entry for description", ucont));
                }
            }
        };

        checker.get(Vars.content.blocks());
        checker.get(Vars.content.items());
        checker.get(Vars.content.liquids());
        checker.get(Vars.content.planets());
        checker.get(Vars.content.sectors());
        checker.get(Vars.content.statusEffects());
        checker.get(Vars.content.units());
    }

    protected void addCredits(){
        try{
            CreditsDialog credits = new CreditsDialog();
            Group group = (Group)ui.menuGroup.getChildren().first();

            if(mobile){
                //TODO button for mobile
            }else{
                group.fill(c ->
                    c.bottom().left()
                        .button("", UnityStyles.creditst, credits::show)
                        .size(84, 45)
                        .name("unity credits")
                );
            }
        }catch(Throwable t){
            Log.err("Couldn't create Unity's credits button", t);
        }
    }

    //TODO support for LogLevel too
    public static void print(Object... args){
        StringBuilder builder = new StringBuilder();
        if(args == null){
            builder.append("null");
        }else{
            for(int i = 0; i < args.length; i++){
                builder.append(args[i]);
                if(i < args.length - 1) builder.append(", ");
            }
        }

        Log.info("&lm&fb[unity]&fr @", builder.toString());
    }
}
