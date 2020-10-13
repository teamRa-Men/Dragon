package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Wooloo extends NPC{

    int boundry;
    int countdown;
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
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        countdown+=deltaTime;
        if (Math.abs(GameView.instance.player.position.x-npcX)<300){
            flee = true;
            target.x = (int) (npcX+(-(Math.signum(GameView.instance.player.position.x-npcX))*1500));
            creationPoint.x= target.x;
        }else if (Math.abs(npcX-target.x) < 5){
            npcX = target.x;
            if (countdown >= Math.random()*1000+1000){
                flee = false;
                double targetDistance = (Math.random()-0.5)*boundry;
                if (Math.abs(targetDistance) > boundry/5){
                    target.x = (int) (creationPoint.x+targetDistance);
                }
                countdown = 0;
            }
        }
        super.update(deltaTime);
    }
}
