package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

public class Farm extends Foundation{

    ArrayList<Object> currentCattle = new ArrayList<Object>();
    int maxCattle;
    static int cost;

    Bitmap cattle;
    Rect cattleBorder;
    Bitmap[] farmBuildings = new Bitmap[3];
    int[] farmBuildingImage = new int[3];
    ArrayList<Integer> Pos = new ArrayList<Integer>();
    int[] spritePosition = new int[3];

    boolean createdWooloo = false;

    SpriteAnimation spriteAnim;

    public static int tileNr = 3;
    //   int[] spritePosition = new int[]{1,2,3}; // 0=1, 1=2 and so on.

    public Farm(int x, int y, boolean isStanding, GameView activity){
        super( x, y,tileNr, isStanding, activity);
        int buildingSprite = (int) (Math.random()*2.9+1);
        cost = 110;

        if(buildingSprite == 3) {
            spriteAnim = new SpriteAnimation(new Rect[]{SpriteManager.instance.getBuildingSprite("Farm" + buildingSprite + "1"),
                    SpriteManager.instance.getBuildingSprite("Farm" + buildingSprite + "2"),
                    SpriteManager.instance.getBuildingSprite("Farm" + buildingSprite + "3")}, 1000);
        }
        else{
            spriteAnim = new SpriteAnimation(new Rect[]{SpriteManager.instance.getBuildingSprite("Farm" + buildingSprite + "1")}, 10000);
        }
        buildingImage = spriteAnim.getFrame(0);
        height = width*buildingImage.height()/buildingImage.width();

        buildingType = 3;

        maxHealth = 200;
        health = maxHealth;

        maxCattle = 4;
        collider = new Rect(x,y-height/2,x+width,y);
    }

    public void update(float fixedDeltaTime) {
        super.update(fixedDeltaTime);

        //===================
        // ALIVE
        //=======================

        if(isStanding == true){
            if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) < 0.2
                    && (currentCattle.size() < maxCattle)
                    && (createdWooloo == false)) {

                Wooloo newWooloo = GameView.instance.npc_pool.spawnWooloo(x, (int) GameView.instance.groundLevel);

                if (newWooloo != null) {
                    currentCattle.add(newWooloo);
                }

                createdWooloo = true;
            }

            if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) > 0.7) createdWooloo = false;

            goldRate = currentCattle.size() * 10;
            beenEmptied = false;
            buildingImage = spriteAnim.getFrame(fixedDeltaTime,1);
            fearCooldown();
        }

        //===========================

        // IS DEAD

        //============================

        else{
            if(beenEmptied == false){
            GoldPool.instance.spawnGold(collider.centerX(), collider.centerY(),goldRate/4);
            beenEmptied = true;}
            this.buildingImage = SpriteManager.instance.getBuildingSprite("FarmRuin");
            goldRate = 0;
        }

    }

    @Override
    public void OnDamage() {
        super.OnDamage();

        if(health == 0 && isStanding){
            isStanding = false;
        }
    }



    @Override
    public void draw(Canvas c) {
        super.draw(c);
    }


    @Override
    public int getTileNr() {
        return 3;
    }
}


// TODO:
//      Farms supposed to spawn random sprites on position, one should always be the main building:
//      sprites to individual objects?
//      Cattle and sheep spawn. (when Villagers present?)

