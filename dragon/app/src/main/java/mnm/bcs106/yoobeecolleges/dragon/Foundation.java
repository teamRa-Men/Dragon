package mnm.bcs106.yoobeecolleges.dragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Foundation {

    int tileSize = 1;
    int width, height;
    public final int TILE_SIZE = 100;

    //current
    int health;

    int maxHealth;

    protected GameView activity;

    //current
    int inhabitantsSize;

    // matthews Villager function as an array,
    // to tell the building how many villagers live inside

    // if health 0 = false;
    boolean isStanding;

    public int x,y;
    Rect collider;

    Bitmap buildingImage;
    public ActionController damagePeriod;


    public Foundation(Bitmap buildingImage,int x, int y, int tileNr, boolean isStanding, GameView activity){

        this.activity = activity;
        this.isStanding = true;

        maxHealth = 200;
        this.health = maxHealth;

        this.x = x;
        this.y = (int)GameView.instance.groundLevel-3;

        width = TILE_SIZE*tileNr;
        height = width;

        this.buildingImage = Bitmap.createScaledBitmap(buildingImage,width, height,false);
        this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,GameView.instance.cameraSize*6/10,GameView.instance.cameraSize*6/10,false);

        collider = new Rect(x,y-height,x+width,height);
        damagePeriod = new ActionController(0,5000,5000);

        if (health<=0){
            Log.i("ouch", "Foundation: ");
            this.isStanding = false;
        }
    }

    public void draw(Canvas c){
        c.drawBitmap(buildingImage,x+GameView.instance.cameraDisp.x,y-buildingImage.getHeight(),null);
    }

    public int getTileNr(){
        return 1;
    }

    public void physics(float deltaTime){


        if (GameView.instance.player.fireBreath.collision(new Vector2(x,y), width)){
            OnDamage();
        }
        if (GameView.instance.player.fireBreath.collision(collider)){
            OnDamage();
        }
    }

    public void update(float deltaTime){

    }

    public  void OnDamage () {
        damagePeriod.triggerAction();
        this.health-=1;
    }
}


