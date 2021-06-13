package unity.ui;

import arc.graphics.*;
import arc.scene.ui.Label.*;
import arc.scene.ui.TextButton.*;
import mindustry.ui.*;

import static arc.Core.*;

public class UnityStyles{
    public static TextButtonStyle creditst;
    public static LabelStyle speecht, speechtitlet;

    public static void load(){
        creditst = new TextButtonStyle(){{
            font = Fonts.def;
            fontColor = Color.white;

            up = atlas.drawable("unity-credits-banner-up");
            down = atlas.drawable("unity-credits-banner-down");
            over = atlas.drawable("unity-credits-banner-over");
        }};

        speecht = new LabelStyle(){{
            fontColor = Color.white;
            background = Styles.black6;
        }};

        speechtitlet = new LabelStyle(){{
            fontColor = Color.white;
            background = Styles.black6;
        }};
    }
}
