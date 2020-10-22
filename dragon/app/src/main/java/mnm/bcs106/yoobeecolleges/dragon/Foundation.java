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
    int goldRate = 0;

    //current
    protected int health;

    protected int maxHealth=100;

    protected GameView activity;

    int buildingType;
    // 0 = fortress
    // 1 = house
    // 2 = Farm
    // 3 = tower

    int currentInhabitants;

    // if health 0 = false;
    boolean isStanding;

    public int x,y;
    Rect collider;

    Bitmap buildingImage;
    public ActionController damagePeriod;
    float rebuildTime = 0;


    public Foundation(int x, int y, int tileNr, boolean isStanding, GameView activity){
        tilesize =GameView.instance.cameraSize/9;
        this.tileNr=tileNr;
        this.activity = activity;
        this.isStanding = true;

        buildingType = 0;

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
         // Log.i("gmg","phy");
        if (GameView.instance.player.fireBreath.collision(collider)){
           // Log.i("gmg","hit fire");
            OnDamage();
            //System.out.println(health);
        }
    }

    public void update(float deltaTime){
        damagePeriod.update(deltaTime);
        repair(deltaTime);
    }

    public void OnDamage () {
        if(isStanding){
            damagePeriod.triggerAction();

            Log.i("gmg",damagePeriod.time+"");

            if(damagePeriod.charging){
                health-=3;
                health = Math.max(health,0);
                Log.i("gmg","dmg");
                //System.out.println("health "+ health);
                damagePeriod.cooling=true;
            }

        }
    }

    public void repair(float fixedDeltaTime){

        if(!isStanding){    // && currentInhabitants > 1
            rebuildTime+=(fixedDeltaTime);

            if( rebuildTime > 4){
                health+=5;
                rebuildTime = 0;
            }
            if(health == maxHealth) {
                isStanding = true;
                System.out.println(isStanding);
                System.out.println(health);

                if(buildingType == 1){
                    buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.fortress);
                    this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,width,height,false);
                }
                else if(buildingType == 2){
                    double rh = (Math.random()*3);

                    if(rh < 1){
                        this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.house1);
                    }
                    if(rh >= 1 && rh < 2){
                        this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.house2);
                    }
                    if(rh >= 2){
                        this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.house3);
                    }

                    this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,width, height,false);
                }

                else if(buildingType == 3){
                    this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.barn);
                    this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,width/3,height,false);
                }

                else if(buildingType == 4){
                    this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.house);
                    this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,width,height*2,false);
                }
            }
        }
    }
}


