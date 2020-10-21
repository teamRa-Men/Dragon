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
        NPCSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.npc_sheet);

        dragonSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.dragon_sheet);
        dragonSheet = Bitmap.createScaledBitmap(dragonSheet,128,192,true);

        environmentSheet = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.environment_sheet);
    }

    private final HashMap<String,Rect> buildingSprites = new HashMap<String,Rect>() {{
        put("Carpet A",spriteRect(0,0,0,0));
    }};

    private final HashMap<String,Rect> NPCSprites = new HashMap<String,Rect>() {{
        put("Carpet A",spriteRect(0,0,0,0));
    }};

    private final HashMap<String,Rect> environmentSprites = new HashMap<String,Rect>() {{
        put("Carpet A",spriteRect(0,0,0,0));
    }};

    private final HashMap<String,Rect> dragonSprites = new HashMap<String,Rect>() {{
        put("Head",spriteRect(0,0,64,64));
        put("Eye",spriteRect(0,0,0,0));
        put("Jaw",spriteRect(64,32,32,32));
        put("Segment",spriteRect(96,0,32,32));
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
