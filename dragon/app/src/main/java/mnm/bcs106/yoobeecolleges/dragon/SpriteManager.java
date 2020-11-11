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
    Bitmap buildingSheet, NPCSheet, fireSheet, dragonSheet, environmentSheet;
    GameView gameView;
    public static SpriteManager instance;

    public SpriteManager() {
        instance = this;
        gameView = GameView.instance;
        //buildingSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.building_sheet_2);
        // buildingSheet = Bitmap.createScaledBitmap(buildingSheet,1152,1024,true);
        buildingSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.building_sheet_low);
        buildingSheet = Bitmap.createScaledBitmap(buildingSheet,1152/2,512,true);

        //NPCSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.npc_sheet);
        //NPCSheet = Bitmap.createScaledBitmap(NPCSheet,392,768,true);

        NPCSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.npc_sheet_low);
        NPCSheet = Bitmap.createScaledBitmap(NPCSheet,392/2,768/2,true);

        fireSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.fire_sheet);
        fireSheet = Bitmap.createScaledBitmap(fireSheet,64,96,true);

        //dragonSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.dragon_sheet_refined);
        //dragonSheet = Bitmap.createScaledBitmap(dragonSheet,128*2,192*2,true);
        dragonSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.dragon_sheet_low);
        dragonSheet = Bitmap.createScaledBitmap(dragonSheet,128,192,true);


        environmentSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.environment_sheet);
        environmentSheet = Bitmap.createScaledBitmap(environmentSheet,1024,1024,true);
    }

    private final HashMap<String,Rect> buildingSprites = new HashMap<String,Rect>() {{
        int d = 64;
        put("House1",spriteRect(0,0,d,d));
        put("House2",spriteRect(d,0,d,d));
        put("House3",spriteRect(d*2,0,d,d));
        put("HouseRuin",spriteRect(d*3,0,d,d));

        put("Smoke1",spriteRect(d*6,0,d,d));
        put("Smoke2",spriteRect(d*7,0,d,d));
        put("Smoke3",spriteRect(d*8,0,d,d));

        put("Farm11",spriteRect(0,d,d*3,d));
        put("Farm12",spriteRect(d*3,d,d*3,d));
        put("Farm13",spriteRect(d*6,d,d*3,d));

        put("Farm21",spriteRect(0,d*2,d*3,d));
        put("Farm22",spriteRect(d*3,d*2,d*3,d));
        put("Farm23",spriteRect(d*6,d*2,d*3,d));

        put("Farm31",spriteRect(0,d*3,d*3,d));
        put("Farm32",spriteRect(d*3,d*3,d*3,d));
        put("Farm33",spriteRect(d*6,d*3,d*3,d));

        put("FarmRuin",spriteRect(0,d*4,d*3,d));

        put("Fortress1",spriteRect(d*3,d*4,d*3,d*2));
        put("Fortress2",spriteRect(d*6,d*4,d*3,d*2));
        put("Fortress3",spriteRect(d*3,d*6,d* 3,d*2));
        put("FortressRuin",spriteRect(d*6,d*6+2,d*3,d*2));

        put("Flag",spriteRect(0,d*5,d,d));

        put("Tower1",spriteRect(0,d*6,d,d*2));
        put("Tower2",spriteRect(d,d*6,d,d*2));
        put("TowerRuin",spriteRect(d*2,d*6,d,d*2));


    }};

    private final HashMap<String,Rect> NPCSprites = new HashMap<String,Rect>() {{
        int d = 64;
        put("Wooloo",spriteRect(0,0,d,d));

        put("Farmer1",spriteRect(0,d,d,d));
        put("Farmer2",spriteRect(d,d,d,d));

        put("Tribute",spriteRect(d*2,d/2,d,(int)(1.5*d)));

        put("Wizard1",spriteRect(0,d*2,d,d));
        put("Wizard2",spriteRect(d,d*2,d,d));
        put("Wizard3",spriteRect(d*2,d*2,d,d));

        put("Slayer1",spriteRect(0,d*3,d,d*2));
        put("Slayer2",spriteRect(d,d*3,d,d*2));
        put("Slayer3",spriteRect(d*2,d*3,d,d*2));

        put("Thief1",spriteRect(0,d*5,d,d));
        put("Thief2",spriteRect(d,d*5,d,d));

        put("Arrow",spriteRect(d,d/8,d/4,d/8));
        put("Magic",spriteRect(d,d/4,d/4,d/4));
        put("Spear",spriteRect(d,0,2*d,d/8));

    }};

    private final HashMap<String,Rect> environmentSprites = new HashMap<String,Rect>() {{
        put("LairBack",spriteRect(0,0,1024,256));
        put("LairMiddle",spriteRect(0,256,1024,256));
        put("LairFront",spriteRect(0,256*2,1024,256));
        put("Mum",spriteRect(0,256*3,256,64));
        put("GoldCoin",spriteRect(256,256*3,8,8));
        put("Mountains",spriteRect(0,832,1024,64));
        put("Ground",spriteRect(0,896,1024,32));
    }};

    private final HashMap<String,Rect> fireSprites = new HashMap<String,Rect>() {{
        put("Fire0",spriteRect(0,0,32,32));
        put("Fire1",spriteRect(0,32,32,32));
        put("Fire2",spriteRect(0,32*2,32,32));
        put("Fire3",spriteRect(32,0,32,32));
        put("Fire4",spriteRect(32,32,32,32));
        put("Fire5",spriteRect(32,32*2,32,32));
    }};

    private final HashMap<String,Rect> dragonSprites = new HashMap<String,Rect>() {{
        int d = 32;
        put("Head",spriteRect(0,0,d*2,d*2));
        put("Jaw",spriteRect(d*2,0,d*2,d*2));
        put("HeadSleeping",spriteRect(d*2,d*2,d*2,d*2));
        put("Segment",spriteRect(d,d*5,d,d));
        put("SpikedSegment",spriteRect(0,d*5,d,d));
        put("Arm",spriteRect(0,d*4,d,d));
        put("BackArm",spriteRect(d, d*4,d,d));
        put("Leg",spriteRect(d*2,d*4,d,d));
        put("BackLeg",spriteRect(d*2,d*5,d,d));
        put("LegFlying",spriteRect(d*3,d*4,d,d));
        put("BackLegFlying",spriteRect(d*3,d*5,d,d));
        put("Wing",spriteRect(0,d*2,d,d*2));
        put("BackWing",spriteRect(d,d*2,d,d*2));
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
    public Rect getFireSprite( String spriteName) {
        return fireSprites.get(spriteName);
    }
    public Rect getDragonSprite( String spriteName) {
        return dragonSprites.get(spriteName);
    }
    public Rect getEnvironmentSprite( String spriteName) {
        return environmentSprites.get(spriteName);
    }
}
