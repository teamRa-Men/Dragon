package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class House extends Foundation{

    int currentInhabitants;
    int maxInhabitants;

    public House(Bitmap buildingImage, int x, int y, int tileNr, boolean isStanding, GameView activity){
        super(buildingImage, x, y, tileNr, isStanding, activity );

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

        if(flipp < 0){

        }

        this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,128,128,false);

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
}
