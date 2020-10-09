package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Point;

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
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Math.abs(GameView.instance.player.position.x-npcX)<500){
            flee = true;
            target.x = (int) (npcX+(-(Math.signum(GameView.instance.player.position.x-npcX))*1000));
            creationPoint.x= target.x;
        }else if (npcX == target.x && Math.abs(GameView.instance.player.position.x-npcX)>500){
            countdown+=deltaTime;
            if (countdown >= Math.random()*1000+1000){
                flee = false;
                target.x = (int) (creationPoint.x+(Math.random()-0.5)*boundry);
                countdown = 0;
            }
        }
    }
}
