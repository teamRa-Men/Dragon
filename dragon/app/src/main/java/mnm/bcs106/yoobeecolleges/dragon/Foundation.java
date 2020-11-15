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
import java.util.ArrayList;
import java.util.Random;

public class Foundation {
    public int tilesize;
    int tileNr = 1;
    int width, height;
    int goldRate = 0;

    //current
    protected int health;

    protected int maxHealth=100;



    int buildingType;


    // 1 = fortress
    // 2 = house
    // 3 = Farm
    // 4 = tower

    ArrayList <NPC> currentInhabitants = new ArrayList<NPC>();

    // if health 0 = false;
    boolean isStanding;

    public int x,y;
    Rect collider;

    int fixedx;

    Rect  dst;
    Bitmap buildingImage;
    public ActionController damagePeriod;
    float rebuildTime = 0;

    float fear = 0;
    boolean beenAttacked;
    boolean beenEmptied = false;
    int fearTime = 0;
    boolean noAttackDay = false;
    boolean fearRemoved = false;
    protected Fortress fortress;

    public Foundation(int x, int y, int tileNr, Fortress fortress){
        this.fortress = fortress;
        tilesize = GameView.instance.cameraSize/12;
        this.tileNr=tileNr;

        this.isStanding = true;

        buildingType = 0;

        this.health = maxHealth;

        this.x = x;
        this.y = (int)GameView.instance.groundLevel-3;

        fixedx = x;

        width = tilesize*tileNr;//(tilesize-(tilesize/10))*tileNr;
        height = width;



        collider = new Rect(x,y-height,x+width,y);
        damagePeriod = new ActionController(100,0,2000);


    }

    public void draw(Canvas c){
        /*Paint p = Scene.instance.frontPaint;
        if(isStanding) {
            int d = (int)((float) health / maxHealth*125)+130;
            p.setColorFilter(new LightingColorFilter(Color.rgb(d,d,d), 0));
        }*/
        if(buildingImage!=null) {
            c.drawBitmap(buildingImage, dst.left, dst.top, null);
        }
    }

    public int getTileNr(){
        return 1;
    }

    public void physics(float deltaTime){
         // Log.i("gmg","phy");
        if(buildingType == 0){
            System.out.println("fortress");
        }
        try {
            if(buildingType == 0){
                System.out.println("fortress sttack check");
            }
            if (GameView.instance.player.fireBreath.collision(collider)) {
                OnDamage();
                System.out.println(health);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void update(float deltaTime){
        //System.out.println(deltaTime);
        damagePeriod.update(deltaTime);

        if(!isStanding)
            y = (int)GameView.instance.groundLevel-3;
        else {
            if (health == maxHealth) {
                y = (int) GameView.instance.groundLevel - 3;

            } else if (health < (maxHealth / 4 * 3) && health > (maxHealth / 2)) {
                y = ((int) GameView.instance.groundLevel - 3) + (height / 4)/2/2;

            } else if (health < (maxHealth / 2) && health > maxHealth / 4) {
                y = ((int) GameView.instance.groundLevel - 3) + (height / 2)/2/2;

            } else if (health < (maxHealth / 4) && health != 0){
                y = ((int) GameView.instance.groundLevel - 3) + (height/4*3)/2/2;
            }
        }
        float left = x+GameView.instance.cameraDisp.x;
        float top = y-height;
        float bottom = y;
        float right = left + width;
        dst = new Rect ((int)left, (int)top, (int)right, (int)bottom);

    }

    public void OnDamage () {


        if(isStanding){
            beenEmptied = false;
            if(Math.random() < 0.1) {
                FirePool.instance.spawnFire(x + (float) Math.random() * width, y - (float) Math.random() * height / 4);
            }

            damagePeriod.triggerAction();
            //System.out.println("damaging");
            beenAttacked = true;
            fearTime = 0;

            if(damagePeriod.charging){
                //


                health-=1*GameView.instance.player.attack;
                health = Math.max(health,0);

                fear+=0.15f;
                if(buildingType == 1){
                    System.out.println("fortress attacked" + fear);
                }
                damagePeriod.cooling=true;
            }
        }

        if(health == 0 && isStanding){
            isStanding = false;
        }
    }

    public void fearCooldown(){
        if(beenAttacked
                && !noAttackDay
                && (Scene.instance.timeOfDay)/(Scene.instance.dayLength)<0.2){

            fearTime+=1;
            noAttackDay = true;
        }

        if(beenAttacked
                &&(Scene.instance.timeOfDay)/(Scene.instance.dayLength)>0.7){

            noAttackDay = false;
        }

        if(fearTime > 2
                && ((Scene.instance.timeOfDay)/(Scene.instance.dayLength)<0.2)
                && !fearRemoved
                && fear >= 0){

            fear-=5;
            fearRemoved = true;
        }

        if((Scene.instance.timeOfDay)/(Scene.instance.dayLength)>0.7){
            fearRemoved = false;
        }

        if(fear <= 0){
            fear = 0;
            beenAttacked = false;
        }
    }


    public void repair(int repairRate, float deltaTime){

        if(!isStanding || buildingType == 1){    // && currentInhabitants > 1
            rebuildTime+=deltaTime;

            if( rebuildTime > 1000){
                health+=repairRate;
                rebuildTime = 0;
            }


            if(health == maxHealth) {
                isStanding = true;
                beenEmptied = false;

                if(buildingType == 1){
                    buildingImage = SpriteManager.instance.getBuildingSprite("Fortress1");
                }

                else if(buildingType == 2){
                    double rh = (Math.random()*3);
                    if(rh < 1){
                        buildingImage = SpriteManager.instance.getBuildingSprite("House1");
                    }
                    if(rh >= 1 && rh < 2){
                        buildingImage = SpriteManager.instance.getBuildingSprite("House2");
                    }
                    if(rh >= 2){
                        buildingImage = SpriteManager.instance.getBuildingSprite("House3");
                    }

                }

                else if(buildingType == 3){
                    double r = Math.random();
                    int buildingSprite = 3;
                    if(r<0.25){
                        buildingSprite = 2;
                    }
                    else if(r<0.5){
                        buildingSprite=1;
                    }

                    buildingImage = SpriteManager.instance.getBuildingSprite("Farm" + buildingSprite + "1");
                }

                else if(buildingType == 4){
                    buildingImage = SpriteManager.instance.getBuildingSprite("Tower1");
                }
            }
        }
    }
}



