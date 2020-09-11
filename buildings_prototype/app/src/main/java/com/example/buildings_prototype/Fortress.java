package com.example.buildings_prototype;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Fortress extends Foundation{



    //All the stuff the fortress needs and can do;

    int lv = 0;

    int maxHealth = 500;

    int currentGold;
    int maxGold;
    int goldRate = 1;

    int currentTilesLeft;
    int currentTilesRight;
    int maxTiles;

    //number of buildings in the village/town
    int currentBuilding;

    int currentTownInhabitants;
    int maxTownInhabitants; // defined by the houses/farms

    public Foundation[] buildings = new Foundation[7];


    // attack function



    //Fortress constructor, used when calling Fortress();

    //this specific Fortress
    public Fortress(Bitmap buildingImage, int x, int y, int tileNr, boolean isStanding) {
        super(buildingImage, x, y, tileNr, isStanding);

        Random r= new Random();
        x = r.nextInt();


        tileNr = 4;

        // lv up conditions
        /*if(((currentTilesLeft+currenttTilesRight)-2 >= maxTiles) && (currentGold >=((maxGold/10)*9)
                &&(currentTownInhabitants == maxTownInhabitants-2))){

            lv++;
            maxGold = maxGold * lv + 300;
            maxTiles += 3;
        }


        maxTiles = 5;*/



    }

    public void update(int deltaTime){
        currentGold += goldRate * deltaTime;
        if(currentGold >= 240 && currentBuilding<7){
            System.out.println("spawn!!!!!");

            double lr = (Math.random()-0.5f);

            if(lr < 0 ){buildings[currentBuilding] = new Foundation(buildingImage, x - (currentTilesLeft+1)*tileSize,y,1,true);
                currentTilesLeft++;
                currentBuilding++;
                currentGold -= 150;}


            else{buildings[currentBuilding] = new Foundation(buildingImage, x + (currentTilesRight+1)*tileSize,y,1,true);
                currentTilesRight++;
                currentBuilding++;
                currentGold -= 150;}

        }
    }

    @Override
    public void draw(Canvas c){
        super.draw(c);
        for(int i = 0 ;i < 7; i++){

            //System.out.println("sdfsdfsdfsdf");
            if(buildings[i] != null){
                System.out.println(buildings[i].x);
                buildings[i].draw(c);
            }
        }

    }



}


