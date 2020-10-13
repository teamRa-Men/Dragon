package mnm.bcs106.yoobeecolleges.lifeofdragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Fortress extends Foundation {


    //All the stuff the fortress needs and can do;

    int lv = 0;

    int maxHealth = 500;

    int currentGold;
    int maxGold;
    public int goldRate = 1;

    int currentTilesLeft;
    int currentTilesRight;
    int maxTiles = 8;

    ArrayList<Foundation> currentBuildingsRight = new ArrayList<Foundation>();
    ArrayList<Foundation> currentBuildingsLeft = new ArrayList<Foundation>();

//    HashMap<Foundation, Integer> currentBuildingsLeft = new HashMap<Foundation, Integer>();
//    HashMap<Foundation, Integer> currentBuildingsRight = new HashMap<Foundation, Integer>();



    //number of buildings in the village/town
    int currentBuilding;

    public int currentTownInhabitants;
    public int maxTownInhabitants; // defined by the houses/farms

    public Foundation[] buildings = new Foundation[7];


    // attack function


    //Fortress constructor, used when calling Fortress();

    //this specific Fortress
    public Fortress(Bitmap buildingImage, int x, int y, int tileNr, boolean isStanding, GameView activity) {
        super(buildingImage, x, y, tileNr, isStanding, activity);

        Random r = new Random();
        x = r.nextInt();

        tileNr = 4;

        // lv up conditions
        /*if(((currentTilesLeft+currenttTilesRight)-2 >= maxTiles) && (currentGold >=((maxGold/10)*9)
                &&(currentTownInhabitants == maxTownInhabitants-2))){

            lv++;
            maxGold = maxGold * lv + 300;
            maxTiles += 3;
        }


        maxTiles = 5;
        */


    }


    //Working system, no arraylist unknown what if building destroyed
    /*
    public void update(int deltaTime){
        currentGold += goldRate * deltaTime;
        if(currentGold >= 240 && currentBuilding < 7 && (currentTilesLeft+currentTilesRight) < maxTiles){

            //left or right
            double lr = (Math.random()-0.5f);
            //int direction = (lr < 0 ? currentTilesRight : currentTilesLeft);

            //type of house
            double rh = (Math.random()-0.5f);

            if(lr < 0 ){

                if(rh < 0)
                {buildings[currentBuilding] = new House(buildingImage, x - (currentTilesLeft+1)*100,y,1,true, activity);}


                else
                    {buildings[currentBuilding] = new Farm(buildingImage, x - (currentTilesLeft+1)*100,y,2,true, activity);}


                currentTilesLeft+=buildings[currentBuilding].getTileNr();
                currentBuilding++;
                currentGold -= 150;}


            else{

                if(rh < 0)
                {buildings[currentBuilding] = new House(buildingImage, x + (currentTilesRight+1)*100,y,1,true, activity);}

                else
                {buildings[currentBuilding] = new Farm(buildingImage, x + (currentTilesRight+1)*100,y,2,true, activity);}


                currentTilesRight+=buildings[currentBuilding].getTileNr();
                currentBuilding++;
                currentGold -= 150;}

        }
    }



    @Override
    public void draw(Canvas c){
        super.draw(c);
        for(int i = 0 ;i < 7; i++){

            if(buildings[i] != null){

                buildings[i].draw(c);
            }
        }
    }

    public void position(Fortress f){

    }
}
    */

    //new test with arraylists works pretty much, tiles and buildings still individual from each other


    public void update(int deltaTime) {
        currentGold += goldRate * deltaTime;
        if (currentGold >= 240 && (currentBuildingsRight.size() + currentBuildingsLeft.size()) < 7) {
            System.out.println("Money aquired");
            //left or right
            double lr = (Math.random() - 0.5f);
            //int direction = (lr < 0 ? currentTilesRight : currentTilesLeft);

            //type of house
            double rh = (Math.random() - 0.5f);

            if (lr < 0) {

                if (rh < 0) {
                    currentBuildingsLeft.add(new House(buildingImage, x - (currentTilesLeft + 1) * 100, y, 1, true, activity));
                    currentTilesLeft+=1;
                } else {
                    currentBuildingsLeft.add(new Farm(buildingImage, x - (currentTilesLeft + 1) * 100, y, 2, true, activity));
                    currentTilesLeft+=3;
                }


                currentBuilding++;
                currentGold -= 150;

            } else {

                if (rh < 0) {
                    currentBuildingsRight.add(new House(buildingImage, x + (currentTilesRight + 1) * 100, y, 1, true, activity));
                    currentTilesRight+=1;
                } else {
                    currentBuildingsRight.add(new Farm(buildingImage, x + (currentTilesRight + 1) * 100, y, 2, true, activity));
                    currentTilesRight+=3;
                }


                currentBuilding++;
                currentGold -= 150;
            }

//            System.out.println("Left Buildings :" + currentBuildingsLeft.size());
//            System.out.println("Right Buildings :" + currentBuildingsRight.size());
//
//            System.out.println("Left Tiles :" +currentTilesLeft);
//            System.out.println("Right Tiles :" +currentTilesRight);

        }
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

    /*
    //HASMAPS

    public void update(int deltaTime) {
        currentGold += goldRate * deltaTime;
        if (currentGold >= 240 && (currentBuildingsRight.size() + currentBuildingsLeft.size()) < 7) {
            System.out.println("Money aquired");

            //left or right
            double lr = (Math.random() - 0.5f);
            //int direction = (lr < 0 ? currentTilesRight : currentTilesLeft);

            //type of house
            double rh = (Math.random() - 0.5f);

            if (lr < 0) {

                if (rh < 0) {
                    currentBuildingsLeft.put(new House(buildingImage, x - (currentTilesLeft + 1) * 100, y, 1, true, activity), 1);
                } else {
                    currentBuildingsLeft.put(new Farm(buildingImage, x - (currentTilesLeft + 1) * 100, y, 2, true, activity), 3);
                }

                currentBuilding++;
                currentGold -= 150;

            }
            else {

                if (rh < 0) {
                    currentBuildingsRight.put(new House(buildingImage, x + (currentTilesRight + 1) * 100, y, 1, true, activity), 1);
                } else {
                    currentBuildingsRight.put(new Farm(buildingImage, x + (currentTilesRight + 1) * 100, y, 2, true, activity), 3);
                }


                currentBuilding++;
                currentGold -= 150;
            }

            currentTilesLeft = 0;
            currentTilesRight = 0;

            System.out.println("Left Buildings :" + currentBuildingsLeft.size());
            for (Integer i : currentBuildingsLeft.values()) {
                System.out.println(i);
                currentTilesLeft += i;
            }
            System.out.println("Tiles left :" + currentTilesLeft);

            System.out.println("Right Buildings :" + currentBuildingsRight.size());
            for (Integer i : currentBuildingsRight.values()) {
                System.out.println(i);
                currentTilesRight += i;
            }
            for (Foundation i : currentBuildingsRight.keySet()) {
                System.out.println(i);
            }
            System.out.println("Tiles Right :" + currentTilesRight);

            System.out.println("\n\t");

        }
    }}

    /*
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


    */

}






//TODO: Building TileSize : done
//      Conditional Building output :
//      Corresponding Gold Rate increase depending on Inhabitants :
//      Conditional LV-up mechanic :
//      Adding prio. to farms at start of Village:
//      Slightly offset BUILDINGS:
//      Fortress size = 4 tiles:

/*
 if(lr < 0 ){

        if(rh < 0)
        {buildings[currentBuilding] = new House(buildingImage, x + (currentTilesRight+1)*tileSize,y,1,true, activity);}

        else
        {buildings[currentBuilding] = new Farm(buildingImage, x + (currentTilesRight+1)*tileSize,y,2,true, activity);}

        currentTilesRight++;
        currentBuilding++;

        scenario 1:
-------------------------------------------
        currentTilesRight = 0
        currentBuildings = 0

        remember, x, in itself, does not change




        new House(houseimage, x(0) + (0+1)*100 (100), y) (having only one tile it is fine)

        currentTilesRight++; (+1)
        currentBuildings++; (+1)

        currentTilesRight = 1
        currentBuildings = 1




        new Farm(houseimage, x(0) + (1+1)*100 (200), y) (having 3 tiles, the PLACEMENT is fine)

        currentTilesRight++; (+1) (should instead be +3 instead of +1)
        currentBuildings++; (+1)

        currentTilesRight = 2 (should be 4)
        currentBuildings = 2




         new House(houseimage, x(0) + (2+1)*100 (300), y) (where errors pile up)

        currentTilesRight++; (+1)
        currentBuildings++; (+1)

        currentTilesRight = 3
        currentBuildings = 3
-------------------------------------------





*/
