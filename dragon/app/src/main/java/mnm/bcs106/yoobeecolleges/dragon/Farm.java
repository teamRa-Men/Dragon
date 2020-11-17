package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

public class Farm extends Foundation{

    ArrayList<Object> currentCattle = new ArrayList<Object>();
    int maxCattle;
    static int cost;
    int buildingSprite = 3;
    boolean createdWooloo = false;
    Bitmap windMill;
    float windmillDegrees = 0;
Matrix matrix;
    //SpriteAnimation spriteAnim;

    public static int tileNr = 4;
    //   int[] spritePosition = new int[]{1,2,3}; // 0=1, 1=2 and so on.

    public Farm(int x, int y, Fortress fortress){
        super( x, y,tileNr, fortress);
        double r = Math.random();

        if(r<0.25){
            buildingSprite = 2;
        }
        else if(r<0.5){
            buildingSprite=1;
        }


        buildingImage = SpriteManager.instance.getBuildingSprite("Farm" + buildingSprite + "1");
        windMill = SpriteManager.instance.getBuildingSprite("WindMill");
        matrix = new Matrix();

        cost = 110;
        height = tilesize*tileNr/3;

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

                Wooloo newWooloo = GameView.instance.npc_pool.spawnWooloo(x, (int) GameView.instance.groundLevel, fortress);

                if (newWooloo != null) {
                    currentCattle.add(newWooloo);
                }

                createdWooloo = true;
            }

            if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) > 0.7) createdWooloo = false;

            goldRate = currentCattle.size() * 10;

            fearCooldown();

            if(buildingSprite ==3) {
                windmillDegrees += fixedDeltaTime/100;
                if (windmillDegrees >= 360) {
                    windmillDegrees = 0;
                }

                int top = (int)(y - height - windMill.getHeight()*0.4);
                int left = (int) (x + width * 3 / 4 - tilesize / 6 + GameView.instance.cameraDisp.x);
                int right = left + tilesize;
                int bottom = top + tilesize;
                RectF src = new RectF(0, 0, windMill.getWidth(), windMill.getHeight());
                RectF dst = new RectF(left, top, right, bottom);
                matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
                matrix.postRotate(windmillDegrees, dst.centerX(), dst.centerY());
            }
        }

        //===========================

        // IS DEAD

        //============================

        else{
            int r = (int)(2+Math.random()*5);
            if(beenEmptied == false){
            GoldPool.instance.spawnGold(collider.centerX(), collider.centerY(),goldRate/2 + r);
            beenEmptied = true;}
            this.buildingImage = SpriteManager.instance.getBuildingSprite("FarmRuin");
            goldRate = 0;
        }

    }





    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if(isStanding && buildingSprite == 3) {
            c.drawBitmap(windMill, matrix, null);
        }
    }


    @Override
    public int getTileNr() {
        return 3;
    }



// TODO:
//      Farms supposed to spawn random sprites on position, one should always be the main building:
//      sprites to individual objects?
//      Cattle and sheep spawn. (when Villagers present?)

    @Override
    public void repair(int repairRate, float deltaTime){

        if(!isStanding || buildingType == 1){    // && currentInhabitants > 1
            rebuildTime+=deltaTime;

            if( rebuildTime > 1000){
                health+=repairRate;
                rebuildTime = 0;
            }


            if(health == maxHealth) {
                double r = Math.random();

                if(r<0.25){
                    buildingSprite = 2;
                }
                else if(r<0.5){
                    buildingSprite=1;
                }
                buildingImage = SpriteManager.instance.getBuildingSprite("Farm" + buildingSprite + "1");
            }
        }
    }
}
