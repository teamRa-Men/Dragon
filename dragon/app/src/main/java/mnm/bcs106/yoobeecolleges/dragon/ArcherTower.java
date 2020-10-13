package com.example.buildings_prototype;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ArcherTower extends Foundation {

    float attackRange = (1/3);
    int[] Arrows = new int[15];

    public ArcherTower(Bitmap buildingImage, int x, int y, int tileNr, boolean isStanding, MainActivity activity){
        super(buildingImage, x, y, tileNr, isStanding, activity );

        this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.barn);
        this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,100,100,false);

    }


    //adding physics to the arrows
    public void Physics(float deltaTime){

    }

    // calculates if the dragon is in range
    public boolean inRange(){
        return false;
    }


    //shooting an arrow at target
    public void Attack(){

    }


    //
    public void Update(float deltaTime){
        if(inRange()){Attack();}
    }

    @Override
    public int getTileNr() {
        return 1;
    }
}
