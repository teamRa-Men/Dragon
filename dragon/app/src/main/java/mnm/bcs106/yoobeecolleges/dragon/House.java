package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class House extends Foundation{

    int currentInhabitants;
    int maxInhabitants;
    int maxHealth = 100;
    public House(int x, int y, boolean isStanding, GameView activity){
        super(x, y,1, isStanding, activity );


        health = maxHealth;

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

    public void spawnVillager(){
        // some other requirements
        if(currentInhabitants < maxInhabitants){

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
            buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.houseruin);
            buildingImage = Bitmap.createScaledBitmap(buildingImage,width,height,false);
            Log.i("ouch","damaged");
        }
    }
}
