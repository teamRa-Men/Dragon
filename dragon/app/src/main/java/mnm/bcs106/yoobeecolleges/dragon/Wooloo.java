package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Wooloo extends NPC{

    int boundry;

    public Wooloo(Bitmap bitmap,float speed, int maxHP, int width, int height, int SBoundry) {
        super(bitmap,speed, maxHP, width, height);
        boundry = SBoundry;
        target.x = npcX;
    }

    public void setBoundry(int Boundry){
        boundry = Boundry;
    }

    @Override
    public void update(float deltaTime) {
        if (Math.abs(GameView.instance.player.position.x-npcX)<300){
            flee = true;
            target.x = (int) (npcX+(-(Math.signum(GameView.instance.player.position.x-npcX))*1500));
            creationPoint.x= target.x;
        }else if (Math.abs(npcX-target.x) < 5){
            idle(boundry);
        }
        super.update(deltaTime);
    }
}
