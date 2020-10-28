package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.HashMap;

public class SpriteManager {
    Bitmap buildingSheet, NPCSheet, dragonSheet, environmentSheet;
    GameView gameView;
    public static SpriteManager instance;

    public SpriteManager() {
        instance = this;
        gameView = GameView.instance;
        buildingSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.building_sheet);
        buildingSheet = Bitmap.createScaledBitmap(buildingSheet,1152,1024,true);

        NPCSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.npc_sheet);
        NPCSheet = Bitmap.createScaledBitmap(NPCSheet,768,768,true);

        dragonSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.dragon_sheet);
        dragonSheet = Bitmap.createScaledBitmap(dragonSheet,128,192,true);

        environmentSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.environment_sheet);
    }

    private final HashMap<String,Rect> buildingSprites = new HashMap<String,Rect>() {{
        put("House1",spriteRect(0,0,128,128));
        put("House2",spriteRect(128,0,128,128));
        put("House3",spriteRect(128*2,0,128,128));
        put("HouseRuin",spriteRect(128*3,0,128,128));

        put("Smoke1",spriteRect(128*6,0,128,128));
        put("Smoke2",spriteRect(128*7,0,128,128));
        put("Smoke3",spriteRect(128*8,0,128,128));

        put("Farm11",spriteRect(0,128,128*3,128));
        put("Farm12",spriteRect(128*3,128,128*3,128));
        put("Farm13",spriteRect(128*6,128,128*3,128));

        put("Farm21",spriteRect(0,128*2,128*3,128));
        put("Farm22",spriteRect(128*3,128*2,128*3,128));
        put("Farm23",spriteRect(128*6,128*2,128*3,128));

        put("Farm31",spriteRect(0,128*3,128*3,128));
        put("Farm32",spriteRect(128*3,128*3,128*3,128));
        put("Farm33",spriteRect(128*6,128*3,128*3,128));

        put("FarmRuin",spriteRect(0,128*4,128*3,128));

        put("Fortress1",spriteRect(128*3,128*4,128*3,128*2));
        put("Fortress2",spriteRect(128*6,128*4,128*3,128*2));
        put("Fortress3",spriteRect(128*3,128*6,128* 3,128*2));
        put("FortressRuin",spriteRect(128*6,128*6,128*3,128*2));

        put("Flag",spriteRect(0,128*5,128,128));

        put("Tower1",spriteRect(0,128*6,128,256));
        put("Tower2",spriteRect(128,128*6,128,256));
        put("Tower2",spriteRect(128*2,128*6,128,128));


    }};

    private final HashMap<String,Rect> NPCSprites = new HashMap<String,Rect>() {{
        put("Wooloo1",spriteRect(0,0,128,128));
        put("Wooloo2",spriteRect(128,0,128,128));
        put("Wooloo3",spriteRect(128*2,0,128,128));

        put("Cow1",spriteRect(128*3,0,128,128));
        put("Cow2",spriteRect(128*4,0,128,128));
        put("Cow3",spriteRect(128*5,0,128,128));

        put("Farmer1",spriteRect(0,128,128,128));
        put("Farmer2",spriteRect(128,128,128,128));
        put("Farmer3",spriteRect(128*2,128,128,128));

        put("Farming1",spriteRect(128*3,0,128,128));
        put("Farming2",spriteRect(128*4,0,128,128));
        put("Farming3",spriteRect(128*5,0,128,128));

        put("Archer1",spriteRect(0,128*2,128,128));
        put("Archer2",spriteRect(128,128*2,128,128));
        put("Archer3",spriteRect(128*2,128*2,128,128));

        put("Shooting1",spriteRect(128*3,128*2,128,128));
        put("Shooting2",spriteRect(128*4,128*2,128,128));
        put("Shooting3",spriteRect(128*5,128*2,128,128));

        put("Wizard1",spriteRect(0,128*3,128,128));
        put("Wizard2",spriteRect(128,128*3,128,128));
        put("Wizard3",spriteRect(128*2,128*3,128,128));

        put("Casting1",spriteRect(128*3,128*3,128,128));
        put("Casting2",spriteRect(128*4,128*3,128,128));
        put("Casting3",spriteRect(128*5,128*3,128,128));

        put("Slayer1",spriteRect(0,128*4,128,128));
        put("Slayer2",spriteRect(128,128*4,128,128));
        put("Slayer3",spriteRect(128*2,128*4,128,128));

        put("Attack1",spriteRect(128*3,128*4,128,128));
        put("Attack2",spriteRect(128*4,128*4,128,128));
        put("Attack3",spriteRect(128*5,128*4,128,128));

        put("Thief1",spriteRect(0,128*5,128,128));
        put("Thief2",spriteRect(128,128*5,128,128));
        put("Thief3",spriteRect(128*2,128*5,128,128));

        put("Stealing1",spriteRect(128*3,128*5,128,128));
        put("Stealing2",spriteRect(128*4,128*5,128,128));
        put("Stealing3",spriteRect(128*5,128*5,128,128));
    }};

    private final HashMap<String,Rect> environmentSprites = new HashMap<String,Rect>() {{
        put("Carpet A",spriteRect(0,0,0,0));
    }};

    private final HashMap<String,Rect> dragonSprites = new HashMap<String,Rect>() {{
        put("Head",spriteRect(0,0,64,64));
        put("Eye",spriteRect(0,0,0,0));
        put("Jaw",spriteRect(64,32,32,32));
        put("Segment",spriteRect(96,0,16,16));
        put("SpikedSegment",spriteRect(64,0,32,32));
        put("Arm",spriteRect(0,128,32,32));
        put("BackArm",spriteRect(32,128,32,32));
        put("Leg",spriteRect(64,64,32,32));
        put("BackLeg",spriteRect(64,96,32,32));
        put("LegFlying",spriteRect(96,64,32,32));
        put("BackLegFlying",spriteRect(96,96,32,32));
        put("Wing",spriteRect(0,64,32,64));
        put("BackWing",spriteRect(32,64,32,64));
        put("Fire0",spriteRect(64,128,32,32));
        put("Fire1",spriteRect(96,128,32,32));
        put("Fire2",spriteRect(0,160,32,32));
        put("Fire3",spriteRect(32,160,32,32));
        put("Fire4",spriteRect(64,160,32,32));
        put("Fire5",spriteRect(96,160,32,32));
    }};

    private Rect spriteRect(int left, int top, int width, int height) {
        return new Rect(left, top, left+width, top+height);
    }


    public Rect getBuildingSprite( String spriteName) {
        return buildingSprites.get(spriteName);
    }
    public Rect getNPCSprite( String spriteName) {
        return NPCSprites.get(spriteName);
    }
    public Rect getDragonSprite( String spriteName) {
        return dragonSprites.get(spriteName);
    }
    public Rect getEnvironmentSprite( String spriteName) {
        return environmentSprites.get(spriteName);
    }
}
