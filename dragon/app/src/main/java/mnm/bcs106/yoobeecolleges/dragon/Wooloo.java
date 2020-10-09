package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Wooloo extends NPC{

    int boundry;
    Point creationPoint = new Point();

    public Wooloo(Bitmap bitmap, int x, int y, float speed, int maxHP, int width, int height, int SBoundry) {
        super(bitmap, x, y, speed, maxHP, width, height);
        creationPoint.x = x;
        creationPoint.y = y;
        boundry = SBoundry;
        target.x = npcX;
    }

    public void setBoundry(int Boundry){
        boundry = Boundry;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (npcX == target.x){
            target.x = (int) (creationPoint.x+(Math.random()-0.5)*boundry);
        }
    }
}
