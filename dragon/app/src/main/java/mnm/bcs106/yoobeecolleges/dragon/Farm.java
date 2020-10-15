package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

public class Farm extends Foundation{

    int currentCattle;
    int maxCattle;

    Bitmap cattle;
    Rect cattleBorder;

    Bitmap[] farmBuildings = new Bitmap[3];

    int[] farmBuildingImage = new int[3];

    ArrayList<Integer> Pos = new ArrayList<Integer>();
    int[] spritePosition = new int[3];

    boolean beenEmptied = false;

    //   int[] spritePosition = new int[]{1,2,3}; // 0=1, 1=2 and so on.


    public Farm(Bitmap buildingImage, int x, int y, boolean isStanding, GameView activity){
        super( x, y,3, isStanding, activity);

        //working simple farmimplementation, 3 tiles big, allways placed thw same.
           this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.barn);
           this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,100,100,false);

           maxCattle = 4;


           GameView.instance.npc_pool.spawnWooloo(x, (int) GameView.instance.groundLevel ,4);







           //randomised spriteposition for farmobjects, currently on Hold
//            farmBuildings[0] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.barn);
//            farmBuildings[0] = Bitmap.createScaledBitmap(farmBuildings[0],100,100,false);
//
//            farmBuildings[1] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.filds);
//            farmBuildings[1] = Bitmap.createScaledBitmap(farmBuildings[1],100,100,false);
//
//            farmBuildings[2] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.filds);
//            farmBuildings[2] = Bitmap.createScaledBitmap(farmBuildings[1],100,100,false);
//
//            //Placing a random sprite in a random position, then deleting that position from the array
//
//            Pos.add(1);
//            Pos.add(2);
//            Pos.add(0);
//
////           while (Pos.size() != 0 && beenEmptied == false){
//        if(beenEmptied != true) {
//
//            for(int i = 0; i < 3; i++) {
//                float random = (float) Math.random() * Pos.size();
//                int random1 = (int) random;
//                System.out.println("float :" + random);
//
//                System.out.println("int : " +random1);
//
//                spritePosition[i] = Pos.get(random1);
//                System.out.println("spritePosition : " + spritePosition[i]);
//
//                Pos.remove(random1);
//                System.out.println("leftover positions : " + Pos);
//                System.out.println("\n \n \t \t");
//            }
//
//            if(Pos.size() == 0) {
//                beenEmptied = true;
//            }
//        }


//                if(spritePosition.size() == 3){
//                    farmBuildingImage[0] = random1;
//                }
//
//                else {
//                    farmBuildingImage[1] = random1;
// //                   farmBuildingImage[2] = random1;
//                }



//                if(Pos.size() == 0){
//                    beenEmptied = true;
//
//                    spritePosition.add(1);
//                    spritePosition.add(2);
//                    spritePosition.add(3);
//                    System.out.println(spritePosition);
//                }

            }



//    @Override
//    public void draw(Canvas c) {

//        for(int i= 0; i < farmBuildingImage.length ; i++) {
//
//            c.drawBitmap(farmBuildings[farmBuildingImage[i]], spritePosition[i]*100 + x, y, null);
//            //needs to know where X is, so needs to have partial access to the fortress position data
//        }
//        c.drawBitmap(buildingImage, x, y, null);

//    }

//    class Field{
//
//
//    }
//
//    class Farmhouse{
//
//    }
    @Override
    public void draw(Canvas c) {
        super.draw(c);
    }


    @Override
    public int getTileNr() {
        return 3;
    }
}


// TODO:
//      Farms supposed to spawn random sprites on position, one should always be the main building:
//      sprites to individual objects?
//      Cattle and sheep spawn. (when Villagers present?)

