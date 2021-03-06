package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.HashMap;

public class SpriteManager {
    Bitmap buildingSheet, NPCSheet, fireSheet, dragonSheet, startDragonSheet, environmentSheet;
    HashMap<String,Bitmap> buildingSprites, NPCSprites;

    public static SpriteManager instance;
    final int cameraSize;

    public SpriteManager(Resources res,int size) {
        this.cameraSize = size;
        instance = this;

        buildingSheet = BitmapFactory.decodeResource(res, R.drawable.building_sheet_low);
        buildingSheet = Bitmap.createScaledBitmap(buildingSheet,1152/2,512,true);

        NPCSheet = BitmapFactory.decodeResource(res,R.drawable.npc_sheet);
        NPCSheet = Bitmap.createScaledBitmap(NPCSheet,392,768,true);

        //NPCSheet = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.npc_sheet_low);
        //NPCSheet = Bitmap.createScaledBitmap(NPCSheet,392/2,768/2,true);

        fireSheet = BitmapFactory.decodeResource(res,R.drawable.fire_sheet);
        fireSheet = Bitmap.createScaledBitmap(fireSheet,64,96,true);

        startDragonSheet = BitmapFactory.decodeResource(res,R.drawable.dragon_sheet_refined);
        startDragonSheet = Bitmap.createScaledBitmap(startDragonSheet,128*2,192*2,true);
        //dragonSheet = BitmapFactory.decodeResource(res,R.drawable.dragon_sheet_low);
        //dragonSheet = Bitmap.createScaledBitmap(dragonSheet,128,192,true);
        dragonSheet = BitmapFactory.decodeResource(res,R.drawable.dragon_sheet_refined);
        dragonSheet = Bitmap.createScaledBitmap(dragonSheet,128*2,192*2,true);

        environmentSheet = BitmapFactory.decodeResource(res, R.drawable.environment_sheet_low);
        environmentSheet = Bitmap.createScaledBitmap(environmentSheet,1024,1088,true);

        NPCSprites = new HashMap<String,Bitmap>() {{
            int size = cameraSize / 20;
            put("Wooloo",getSprite(getNPCSpriteRect("Wooloo"),NPCSheet,size,size));

            put("Farmer1",getSprite(getNPCSpriteRect("Farmer1"),NPCSheet,size,size));
            put("Farmer2",getSprite(getNPCSpriteRect("Farmer2"),NPCSheet,size,size));

            put("Tribute",getSprite(getNPCSpriteRect("Tribute"),NPCSheet,size,(int)(size*1.5f)));

            put("Wizard1",getSprite(getNPCSpriteRect("Wizard1"),NPCSheet,size,size));
            put("Wizard2",getSprite(getNPCSpriteRect("Wizard2"),NPCSheet,size,size));
            put("Wizard3",getSprite(getNPCSpriteRect("Wizard3"),NPCSheet,size,size));

            put("Slayer1",getSprite(getNPCSpriteRect("Slayer1"),NPCSheet,size,size*2));
            put("Slayer2",getSprite(getNPCSpriteRect("Slayer2"),NPCSheet,size,size*2));
            put("Slayer3",getSprite(getNPCSpriteRect("Slayer3"),NPCSheet,size,size*2));

            put("Thief1",getSprite(getNPCSpriteRect("Thief1"),NPCSheet,size,size));
            put("Thief2",getSprite(getNPCSpriteRect("Thief2"),NPCSheet,size,size));

            put("Arrow",getSprite(getNPCSpriteRect("Arrow"),NPCSheet,size/4,size/8));
            put("Magic",getSprite(getNPCSpriteRect("Magic"),NPCSheet,size/2,size/2));
            put("Spear",getSprite(getNPCSpriteRect("Spear"),NPCSheet,size*2,size/8));
        }};

        buildingSprites = new HashMap<String,Bitmap>() {{
            int tileSize = cameraSize/12;
            put("House1",getSprite(getBuildingSpriteRect("House1"),buildingSheet,tileSize,tileSize));
            put("House2",getSprite(getBuildingSpriteRect("House2"),buildingSheet,tileSize,tileSize));
            put("House3",getSprite(getBuildingSpriteRect("House3"),buildingSheet,tileSize,tileSize));
            put("HouseRuin",getSprite(getBuildingSpriteRect("HouseRuin"),buildingSheet,tileSize,tileSize));
            put("Tower1",getSprite(getBuildingSpriteRect("Tower1"),buildingSheet,tileSize,2*tileSize));
            put("Tower2",getSprite(getBuildingSpriteRect("Tower2"),buildingSheet,tileSize,2*tileSize));
            put("TowerRuin",getSprite(getBuildingSpriteRect("TowerRuin"),buildingSheet,tileSize,2*tileSize));

            put("Background",getSprite(getBuildingSpriteRect("Background"),buildingSheet,(int)(tileSize*1.05),tileSize));

            put("WindMill",getSprite(getBuildingSpriteRect("WindMill"),buildingSheet,tileSize,tileSize));
            tileSize*=4f/3;

            put("Farm11",getSprite(getBuildingSpriteRect("Farm11"),buildingSheet,3*tileSize,tileSize));

            put("Farm21",getSprite(getBuildingSpriteRect("Farm21"),buildingSheet,3*tileSize,tileSize));

            put("Farm31",getSprite(getBuildingSpriteRect("Farm31"),buildingSheet,3*tileSize,tileSize));
            put("Farm32",getSprite(getBuildingSpriteRect("Farm32"),buildingSheet,3*tileSize,tileSize));
            put("Farm33",getSprite(getBuildingSpriteRect("Farm33"),buildingSheet,3*tileSize,tileSize));

            put("FarmRuin",getSprite(getBuildingSpriteRect("FarmRuin"),buildingSheet,3*tileSize,tileSize));

            put("Fortress1",getSprite(getBuildingSpriteRect("Fortress1"),buildingSheet,3*tileSize,2*tileSize));
            put("Fortress2",getSprite(getBuildingSpriteRect("Fortress2"),buildingSheet,3*tileSize,2*tileSize));
            put("Fortress3",getSprite(getBuildingSpriteRect("Fortress3"),buildingSheet,3*tileSize,2*tileSize));
            put("FortressRuin",getSprite(getBuildingSpriteRect("FortressRuin"),buildingSheet,3*tileSize,2*tileSize));


        }};

    }

    private final HashMap<String,Rect> buildingSpriteRects = new HashMap<String,Rect>() {{
        int d = 64;
        put("House1",spriteRect(0,0,d,d));
        put("House2",spriteRect(d,0,d,d));
        put("House3",spriteRect(d*2,0,d,d));
        put("HouseRuin",spriteRect(d*3,0,d,d));

        put("Smoke1",spriteRect(d*6,0,d,d));
        put("Smoke2",spriteRect(d*7,0,d,d));
        put("Smoke3",spriteRect(d*8,0,d,d));

        put("WindMill",spriteRect(d*5,0,d,d));

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
        put("FortressRuin",spriteRect(d*6,d*6,d*3,d*2));

        put("Flag",spriteRect(0,d*5,d,d));

        put("Tower1",spriteRect(0,d*6,d,d*2));
        put("Tower2",spriteRect(d,d*6,d,d*2));
        put("TowerRuin",spriteRect(d*2,d*6,d,d*2));

        put("Background",spriteRect(d*4,0,d,d));
    }};

    private final HashMap<String,Rect> NPCSpriteRects = new HashMap<String,Rect>() {{
        int d = 128;
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
        put("Magic",spriteRect(d,d/4,d/2,d/2));
        put("Spear",spriteRect(d,0,2*d,d/8));

    }};

    private final HashMap<String,Rect> environmentSpriteRects = new HashMap<String,Rect>() {{

        put("LairBack",spriteRect(0,0,512,128));
        put("LairMiddle",spriteRect(0,256,512,128));
        put("LairFront",spriteRect(0,256*2,512,128));
        put("Mum",spriteRect(0,128,256,64));
        put("GoldCoin",spriteRect(256,256*3,8,8));
        put("Mountains",spriteRect(0,(256*3+8),1024,64));
        put("Hills",spriteRect(0,1024+1,1024,64-1));
        put("Ground",spriteRect(0,896,1024,24));
        put("Tree1",spriteRect(0,(1024-64),64,64));
        put("Tree2",spriteRect(64,(1024-64),64,64));
        put("Tree3",spriteRect(64*2,(1024-64),64,64));
        put("Tree4",spriteRect(64*3,(1024-64),64,64));
        put("Tree5",spriteRect(64*4,1024-64,64,64));
        put("Tree6",spriteRect(64*5,1024-64,64,64));
        put("Tree7",spriteRect(64*6,1024-64,64,64));
        put("Tree8",spriteRect(64*7,1024-64,64,64));
        put("ForestLeft",spriteRect(64*8,1024-64,128,64));
        put("Forest",spriteRect(64*10,1024-64,128,64));
        put("ForestRight",spriteRect(64*12,1024-64,128,64));
        put("Bush",spriteRect(0,1024-64-32,64,32));
        put("Trunk",spriteRect(64,1024-64-32,64,32));
        put("Tiny1",spriteRect(128,1024-64-32,32,32));
        put("Tiny2",spriteRect(128+32,1024-64-32,32,32));
        put("Tiny3",spriteRect(128+32*2,1024-64-32,32,32));
        put("Tiny4",spriteRect(128+32*3,1024-64-32,32,32));
        put("Tiny5",spriteRect(128+32*4,1024-64-32,32,32));
        put("Tiny6",spriteRect(128+32*5,1024-64-32,32,32));
        put("Tiny7",spriteRect(128+32*6,1024-64-32,32,32));
        put("Tiny8",spriteRect(128+32*7,1024-64-32,32,32));
        put("Tiny9",spriteRect(128+32*8,1024-64-32,32,32));

        put("Cloud1",spriteRect(1024-128,1024-64-32,128,32));
        put("Cloud2",spriteRect(1024-256,1024-64-32,128,32));
        put("Cloud3",spriteRect(1024-128,1024-64,128,64));
    }};

    private final HashMap<String,Rect> fireSpriteRects = new HashMap<String,Rect>() {{
        put("Fire0",spriteRect(0,0,32,32));
        put("Fire1",spriteRect(0,32,32,32));
        put("Fire2",spriteRect(0,32*2,32,32));
        put("Fire3",spriteRect(32,0,32,32));
        put("Fire4",spriteRect(32,32,32,32));
        put("Fire5",spriteRect(32,32*2,32,32));
    }};

    private final HashMap<String,Rect> dragonSpriteRects = new HashMap<String,Rect>() {{
        int d = 64;
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

    private final HashMap<String,Rect> startDragonSpriteRects = new HashMap<String,Rect>() {{
        int d = 64;
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


    public Rect getBuildingSpriteRect( String spriteName) {
        return buildingSpriteRects.get(spriteName);
    }
    public Rect getNPCSpriteRect( String spriteName) {
        return NPCSpriteRects.get(spriteName);
    }
    public Rect getFireSpriteRect( String spriteName) {
        return fireSpriteRects.get(spriteName);
    }
    public Rect getDragonSpriteRect( String spriteName) {
        return dragonSpriteRects.get(spriteName);
    }
    public Rect getStartDragonSpriteRect( String spriteName) {
        return startDragonSpriteRects.get(spriteName);
    }
    public Rect getEnvironmentSpriteRect( String spriteName) {
        return environmentSpriteRects.get(spriteName);
    }

    private Bitmap getSprite(Rect r, Bitmap sheet, int width, int height) {
        Bitmap sprite = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        sprite = Bitmap.createScaledBitmap(sprite,width,height,true);
        return sprite;
    }

    public Bitmap getBuildingSprite( String spriteName) {
        return buildingSprites.get(spriteName);
    }
    public Bitmap getNPCSprite( String spriteName) {
        return NPCSprites.get(spriteName);
    }


    public static Bitmap flip(Bitmap bitmap, int type){
        Matrix matrix =new Matrix();
        if(type==0){
            matrix.preScale(-1.0f, 1.0f);
        }else if(type==1){
            matrix.preScale(1.0f, -1.0f);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
