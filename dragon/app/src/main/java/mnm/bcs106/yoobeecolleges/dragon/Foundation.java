package mnm.bcs106.yoobeecolleges.dragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.net.PasswordAuthentication;

public class Foundation {
    public int tilesize;
    int tileNr = 1;
    int width, height;

    //current
    protected int health;

    protected int maxHealth=100;

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


    public Foundation(int x, int y, int tileNr, boolean isStanding, GameView activity){
        tilesize =GameView.instance.cameraSize/9;
        this.tileNr=tileNr;
        this.activity = activity;
        this.isStanding = true;

        this.health = maxHealth;

        this.x = x;
        this.y = (int)GameView.instance.groundLevel-3;

        width = (tilesize-(tilesize/10))*tileNr;
        height = width;



        collider = new Rect(x,y-height,x+width,y);
        damagePeriod = new ActionController(10,0,2000);

    }

    public void draw(Canvas c){
        Paint p = new Paint();
        //p.setColorFilter(new LightingColorFilter(Color.rgb(health/maxHealth*255, health/maxHealth*255, health/maxHealth*255),0));
        c.drawBitmap(buildingImage,x+GameView.instance.cameraDisp.x,y-buildingImage.getHeight(),p);

    }

    public int getTileNr(){
        return 1;
    }

    public void physics(float deltaTime){
        //Log.i("gmg","phy");
        if (GameView.instance.player.fireBreath.collision(collider)){
            Log.i("gmg","hit fire");
            OnDamage();
        }
    }

    public void update(float deltaTime){
        damagePeriod.update(deltaTime);
    }

    public  void OnDamage () {
        if(isStanding){
            damagePeriod.triggerAction();
           // Log.i("gmg","standing");

            if(damagePeriod.charging){
                health-=3;
                health = Math.max(health,0);
                //Log.i("gmg","dmg");
                System.out.println("health "+ health);
                damagePeriod.cooling=true;
            }

        }


    }
}


