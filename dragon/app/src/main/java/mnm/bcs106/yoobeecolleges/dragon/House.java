package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.util.ArrayList;

public class House extends Foundation{


    int maxInhabitants;



    boolean createdVillager = false;
    boolean beenEmptied = false;

    public House(int x, int y, boolean isStanding, GameView activity){
        super(x, y,1, isStanding, activity );
        maxHealth = 200;

        health = maxHealth;
        buildingType = 2;
        maxInhabitants = 5;

        double rh = (Math.random()*3);
        double flipp = (Math.random() - 0.5f);

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

        if(flipp < 0){

        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //===================
        // ALIVE
        //=======================

        if(isStanding == true){
            //System.out.println("gmg"+ health);
            if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) < 0.2
                    && (currentInhabitants.size() < maxInhabitants)
                    && (createdVillager == false)){

                Farmers newFarmer = GameView.instance.npc_pool.spawnFarmers(x, (int) GameView.instance.groundLevel);

                if (newFarmer != null) {
                    currentInhabitants.add(newFarmer);
                }

                createdVillager = true;
            }

            if((Scene.instance.timeOfDay) / (Scene.instance.dayLength) > 0.7) {
                createdVillager = false;
            }

            goldRate = currentInhabitants.size() * 3;
            beenEmptied = false;

        }

        //===========================
        // IS DEAD
        //============================

        else{

            buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.houseruin);
            buildingImage = Bitmap.createScaledBitmap(buildingImage,width,height,false);

            if(beenEmptied == false){
                GoldPool.instance.spawnGold(x, y-3,goldRate/5);
                beenEmptied = true;}

            goldRate = 0;
        }
    }

    @Override
    public int getTileNr() {
        return 1;
    }

    @Override
    public void OnDamage() {
        super.OnDamage();

        if(health == 0 && isStanding){
            isStanding = false;
            Log.i("ouch","damaged");
        }
    }
}
