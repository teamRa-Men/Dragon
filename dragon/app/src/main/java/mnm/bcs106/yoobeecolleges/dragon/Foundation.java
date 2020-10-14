package mnm.bcs106.yoobeecolleges.dragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

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
        health = maxHealth;

        this.x = x;
        this.y = y;

        width = TILE_SIZE*tileNr;
        height = width;
        this.buildingImage = Bitmap.createScaledBitmap(buildingImage,width, height,false);
        collider = new Rect(x,y-height,x+width,height);
        damagePeriod = new ActionController(0,5000,5000);



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
        health-=1;


        if (health<=0){
            isStanding = false;
        }
    }
}


