package mnm.bcs106.yoobeecolleges.lifeofdragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class House extends Foundation{

    int currentInhabitants;
    int maxInhabitants;

    public House(Bitmap buildingImage, int x, int y, int tileNr, boolean isStanding, GameView activity){
        super(buildingImage, x, y, tileNr, isStanding, activity );

        this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.house);
        this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,100,100,false);

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
