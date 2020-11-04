package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Wooloo extends NPC{

    int boundry;

    public Wooloo(Bitmap bitmap,float speed, int maxHP, int width, int height, int SBoundry) {
        super(bitmap,speed, maxHP, width, height);
        boundry = SBoundry;
    }
    @Override
    public void update(float deltaTime) {
        if (Math.abs(GameView.instance.player.position.x-npcX)<300){
            flee = true;
            target.x = (int) (npcX+(-(Math.signum(GameView.instance.player.position.x-npcX))*1500));
            tempCreationPoint.x= target.x;
        }else idle(500,Math.abs(npcX-target.x) < 10);
        super.update(deltaTime);
    }
}
