package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Farmers extends NPC {

    public int farmX;
    public boolean wasAttacked = false;
    public boolean atFarm = false;
    public boolean finished;

    public Farmers(Bitmap bitmap, float speed, int maxHP, int width, int height,int FX) {
        super(bitmap, speed, maxHP, width, height);
        farmX = creationPoint.x+500;
    }
    public void doStuff(){
         finished = false;
    }

    @Override
    public void update(float deltaTime) {
        if (!wasAttacked){
            if (atFarm && target.x == npcX){
                if (countdown>=2000) {
                    doStuff();
                    if (finished == true){
                        target.x = creationPoint.x;
                        atFarm = false;
                    }
                }
            }else if (npcX == target.x && !atFarm){
                target.x = farmX;
                atFarm = true;
            }
        }else {

        }
        super.update(deltaTime);
    }
}
