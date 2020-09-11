package com.example.buildings_prototype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Foundation {

    int tileNr, tileSize = 100;

    //current
    int health;

    int maxHealth;

    MainActivity activity;

    //current
    int inhabitantsSize;

    // matthews Villager function as an array,
    // to tell the building how many villagers live inside

    // if health 0 = false;
    boolean isStanding;

    int x,y;
    Rect collision;

    Bitmap buildingImage;

    public Foundation(Bitmap buildingImage,int x, int y, int tileNr, boolean isStanding){

        this.isStanding = true;

        health = maxHealth;

        this.x = x;
        this.y = y;
        collision = new Rect(x,y,x+tileSize,y + tileSize);

        this.buildingImage = buildingImage;



    }

    public void draw(Canvas c){
        c.drawBitmap(buildingImage,x,y,null);
    }
}


    //public newSlime[] Puddle = new newSlime[12];
    //int puddleSize = 0;


