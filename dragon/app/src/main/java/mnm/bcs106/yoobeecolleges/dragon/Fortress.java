package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Fortress extends Foundation {


    //All the stuff the fortress needs and can do;

    int lv;

    int maxHealth = 500;

    int currentGold;
    int maxGold;
    public int goldRate = 20;

    int currentTilesLeft;
    int currentTilesRight;
    int maxTiles = 8;

    public Point creationPoint = new Point();

    ArrayList<Foundation> currentBuildingsRight = new ArrayList<Foundation>();
    ArrayList<Foundation> currentBuildingsLeft = new ArrayList<Foundation>();
    int maxBuildings;

    //number of buildings in the village/town

    public int currentTownInhabitants;
    public int maxTownInhabitants; // defined by the houses/farms

    //public Foundation[] buildings = new Foundation[5];

    ArcherTower archertower;
    boolean hasTowers = false;


    // attack function


    //Fortress constructor, used when calling Fortress();

    //this specific Fortress
    public Fortress(int x, int y, boolean isStanding, GameView activity) {
        super(x, y,4, isStanding, activity);

        buildingImage = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.fortress);
        buildingImage = Bitmap.createScaledBitmap(buildingImage,width,height,false);
        Random r = new Random();
        x = r.nextInt();
        buildingType = 1;

        maxBuildings = 5;
        maxGold = 400;
        lv = 0;
    }

    //new test with arraylists works pretty much, tiles and buildings still individual from each other

    public void update(float deltaTime) {

        if(currentGold < maxGold){
            deltaTime = deltaTime/200;
            currentGold += goldRate * deltaTime;
        }

        if (currentGold >= 240 && (currentBuildingsRight.size() + currentBuildingsLeft.size()) < maxBuildings) {
            System.out.println("Money aquired");

            //left or right
            double lr = (Math.random() - 0.5f);
            //int direction = (lr < 0 ? currentTilesRight : currentTilesLeft);

            //type of house
            double rh = (Math.random() - 0.5f);

            int offset = tilesize;
            //(int)(tilesize+((Math.random()*(tilesize-((tilesize/10)*9)))));

            if (lr < 0) {

                if (rh < 0) {
                    currentBuildingsLeft.add(new House(x - (currentTilesLeft + 1) * offset, y, true, activity));
                    currentTilesLeft+=1;
                }
                else {
                    currentBuildingsLeft.add(new Farm(x - (currentTilesLeft + 1) * tilesize, y, true, activity));
                    currentTilesLeft+=3;
                }

                currentGold -= 150;

            } else {

                if (rh < 0) {
                    currentBuildingsRight.add(new House(x + (tilesize*3) +(currentTilesRight + 1)*offset, y, true, activity));
                    currentTilesRight+=1;
                }
                else {
                    currentBuildingsRight.add(new Farm(x + (tilesize*3) +(currentTilesRight + 1) * tilesize, y, true, activity));
                    currentTilesRight+=3;
                }

                currentGold -= 150;
            }

//            System.out.println("Left Buildings :" + currentBuildingsLeft.size());
//            System.out.println("Right Buildings :" + currentBuildingsRight.size());
//
//            System.out.println("Left Tiles :" +currentTilesLeft);
//            System.out.println("Right Tiles :" +currentTilesRight);

        }

        //Lv up conditions
        if((((currentBuildingsRight.size()+currentBuildingsLeft.size()) >= maxBuildings) || (currentTilesLeft + currentTilesRight >= 8))
                && (currentGold >= (maxGold/10*9))
                && lv == 0){

            lv++;
            maxGold = maxGold * 4 + 300;
            maxBuildings = 12;

            currentBuildingsRight.add(new ArcherTower(x + (tilesize*3) +(currentTilesRight + 1) * tilesize, y, true, activity));
            currentTilesRight+=2;

            currentBuildingsLeft.add(new ArcherTower(x - (currentTilesLeft + 1) * tilesize, y, true, activity));
            currentTilesLeft+=2;
        }

        for(int i = 0; i < currentBuildingsRight.size(); i++){
            currentBuildingsRight.get(i).update(deltaTime);
        }

        for(int i = 0; i < currentBuildingsLeft.size(); i++){
            currentBuildingsLeft.get(i).update(deltaTime);
        }


        //

        if(inRange()) {
            if(Math.random() < 0.01){
                Attack();}
        }
    }

    public boolean inRange(){
        System.out.println("calling inRange");
        if (Math.abs(GameView.instance.player.position.y-y)<500){
            return true;}
        return false;
    }

    //shooting an arrow at target
    public void Attack(){
        System.out.println("calling Attack");
        float randomx = (float)(Math.random()-0.5)*15;
        float randomy = (float)(Math.random()-0.5)*15;

        float dx = GameView.instance.player.position.x-x;
        float dy =GameView.instance.player.position.y-y;
        ProjectilePool.instance.shootArrow(creationPoint.x, creationPoint.y, 1, dx+randomx, dy+randomy, 2);
    }


    @Override
    public void draw(Canvas c) {
        super.draw(c);
        for (int i = 0; i < currentBuildingsLeft.size(); i++) {

            if (currentBuildingsLeft.get(i) != null) {

                currentBuildingsLeft.get(i).draw(c);

            }
        }

        for (int i = 0; i < currentBuildingsRight.size(); i++) {

            if (currentBuildingsRight.get(i) != null) {

                currentBuildingsRight.get(i).draw(c);
            }
        }
    }

    public void position(Fortress f) {

    }

    public void physics(float deltaTime){
        super.physics(deltaTime);

        for(int i = 0; i < currentBuildingsLeft.size(); i++){
            currentBuildingsLeft.get(i).physics(deltaTime);
        }

        for(int i = 0; i < currentBuildingsRight.size(); i++){
            currentBuildingsRight.get(i).physics(deltaTime);
        }
    }
}






//TODO: Building TileSize : done
//      Conditional Building output : deleted
//      Conditional LV-up mechanic : done
//      Adding prio. to farms at start of Village : deleted
//      Slightly offset BUILDINGS : done
//      Fortress size : 4 tiles: done
//      Fortress Attacking :
//      Corresponding Gold Rate increase depending on Inhabitants :

