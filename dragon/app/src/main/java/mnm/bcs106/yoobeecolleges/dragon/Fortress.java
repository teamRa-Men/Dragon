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
    public int goldRate = 15;

    int currentTilesLeft;
    int currentTilesRight;
    int maxTiles = 8;

    public Point creationPoint = new Point();
    float attackRange = (1f/2);

    ArrayList<Foundation> currentBuildingsRight = new ArrayList<Foundation>();
    ArrayList<Foundation> currentBuildingsLeft = new ArrayList<Foundation>();
    int maxBuildings;

    //number of buildings in the village/town

    public int currentTownInhabitants;
    public int maxTownInhabitants; // defined by the houses/farms

    //public Foundation[] buildings = new Foundation[5];

    ArcherTower archertower;
    boolean hasTaxed = false;

    boolean hasFarm = false;
    // attack function
    public static int tileNr = 3;

    float townFear;

    //Fortress constructor, used when calling Fortress();

    //this specific Fortress
    public Fortress(int x, int y, boolean isStanding, GameView activity) {
        super(x, y,tileNr, isStanding, activity);
        buildingImage = SpriteManager.instance.getBuildingSprite("Fortress1");
        height = width*buildingImage.height()/buildingImage.width();

        new ArcherTower(x,y,true,activity);
        new ArcherTower(x+tilesize*3,y,true, activity);



        Random r = new Random();
        x = r.nextInt();
        buildingType = 1;
        currentGold = 150;

        maxBuildings = 5;
        maxGold = 400;
        lv = 0;

        creationPoint.x = x+(width/2);
        creationPoint.y = (int)GameView.instance.groundLevel - height;

        Farmers newFarmer = GameView.instance.npc_pool.spawnFarmers(x, (int) GameView.instance.groundLevel);
    }

    //new test with arraylists works pretty much, tiles and buildings still individual from each other

    public void update(float deltaTime) {

        int currentGold1 = currentGold;

        if(currentGold < maxGold){

            //Money income

            if((Scene.instance.timeOfDay)/(Scene.instance.dayLength)<0.2
                    && (!hasTaxed)){

                goldRate = 15;

                for(int i = 0; i < currentBuildingsRight.size(); i++){
                    goldRate = currentBuildingsRight.get(i).goldRate + goldRate;
                }

                for(int i = 0; i < currentBuildingsLeft.size(); i++){
                    goldRate = currentBuildingsLeft.get(i).goldRate + goldRate;
                }


                currentGold = currentGold + (int)(goldRate*1.2);


                if(currentGold > maxGold){
                    currentGold = currentGold - (currentGold-maxGold);
                }

                hasTaxed = true;
            }

            if((Scene.instance.timeOfDay)/(Scene.instance.dayLength) > 0.7) hasTaxed = false;

            if(currentGold != currentGold1){
                System.out.println("Goldrate : " + goldRate);
                System.out.println("Gold : " + currentGold);
            }
        }

        //=======================================================================================//

        //Buildings

        //=======================================================================================//

        if (((currentBuildingsRight.size() + currentBuildingsLeft.size()) < maxBuildings)
                && (currentGold > 5)) {
            spawnRandomBuilding();
        }

        //===================================================================================//

        //Lv up conditions

        //===================================================================================//

        if((((currentBuildingsRight.size()+currentBuildingsLeft.size()) >= maxBuildings) || (currentTilesLeft + currentTilesRight >= 8))
                && (currentGold >= (maxGold/10*9))
                && lv == 0){

            lv++;
            maxGold = maxGold * 4 + 300;
            maxBuildings = 12;


            currentBuildingsRight.add(new ArcherTower(x + (tilesize*tileNr) +(currentTilesRight) * tilesize, y, true, activity));
            currentTilesRight+=1;

            currentBuildingsLeft.add(new ArcherTower(x - (currentTilesLeft) * tilesize-ArcherTower.tileNr*tilesize, y, true, activity));
            currentTilesLeft+=1;
        }

        float tempfear;
        if(!isStanding){
            repair((int)(currentTownInhabitants/5)+1, deltaTime);
        }

        else {
            boolean repairingRight = false, repairingLeft = false;
            tempfear = 0;

            for (int i = 0; i < currentBuildingsRight.size(); i++) {
                currentBuildingsRight.get(i).update(deltaTime);

                //repairing
                if (!repairingRight && !currentBuildingsRight.get(i).isStanding) {
                    currentBuildingsRight.get(i).repair((int) (currentTownInhabitants / 5) + 1, deltaTime);
                    repairingRight = true;

                }

                //gathering fear
                tempfear += currentBuildingsRight.get(i).fear;
            }

            for (int i = 0; i < currentBuildingsLeft.size(); i++) {
                currentBuildingsLeft.get(i).update(deltaTime);
                if (!repairingLeft && !currentBuildingsLeft.get(i).isStanding) {
                    currentBuildingsLeft.get(i).repair((int) (currentTownInhabitants / 5) + 1, deltaTime);
                    repairingLeft = true;
                }
                tempfear += currentBuildingsLeft.get(i).fear;
            }

            townFear = (tempfear/(currentBuildingsLeft.size()+currentBuildingsRight.size()));

        }
    }

    public void spawnRandomBuilding(){
        double lr = (Math.random() - 0.5f);
        int offset = 0;//tilesize/2;

        ArrayList<Foundation> direction;
        int directionTiles;

        if(lr < 0){
            direction = currentBuildingsLeft;
        }
        else{
            direction = currentBuildingsRight;
        }

        int position;
        if(direction ==  currentBuildingsLeft){
            position = x-(currentTilesLeft*tilesize+offset);
        }
        else position = x + (tilesize*Fortress.tileNr) +currentTilesRight*tilesize + offset;

        //deciding Building

        Foundation building;

        if(currentBuildingsRight.size() == 0 && currentBuildingsLeft.size() == 0){
            if(direction == currentBuildingsLeft) {
                position -= Farm.tileNr*tilesize;
            }
            building = new Farm(position,y,true,activity);
        }

        else if(townFear > 30){
            double rh = (Math.random() - 0.5f);
            if(rh < 0){
                if(direction == currentBuildingsLeft) {
                    position -= House.tileNr*tilesize;
                }
                building = new House(position,y,true,activity);
                currentGold -= 240;
            }
            else {
                if(direction == currentBuildingsLeft) {
                    position -= ArcherTower.tileNr*tilesize;
                }
                building = new ArcherTower(position,y,true,activity);
                currentGold -= 240;}
        }

        else{
            double rh = (Math.random() - 0.5f);
            if(rh < 0 ){
                if(direction == currentBuildingsLeft) {
                    position -= House.tileNr*tilesize;
                }
                building = new House(position,y,true,activity);
                currentGold -= 240;
            }
            else{
                if(direction == currentBuildingsLeft) {
                    position -= Farm.tileNr*tilesize;
                }
                building = new Farm(position,y,true,activity);
                currentGold -= 240;
            }
        }



        //giving feedback to the Tiles right and Tiles left
        if(direction == currentBuildingsLeft) {
            currentTilesLeft += building.tileNr;
        }
        else {
            currentTilesRight += building.tileNr;
        }
        direction.add(building);
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

    public void spawnFarm(){

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






//TODO:
//      Conditional Building output :
//      Fortress size : 4 tiles: done
//      Fortress Attacking :

