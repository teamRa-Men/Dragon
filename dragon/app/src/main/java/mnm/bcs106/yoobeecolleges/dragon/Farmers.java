package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Farmers extends NPC {
    public int farmX;
    public int farmY;
    public Farmers(Bitmap bitmap, float speed, int maxHP, int width, int height,int FX,int FY) {
        super(bitmap, speed, maxHP, width, height);
        farmX = FX;
    }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);
    }
}
