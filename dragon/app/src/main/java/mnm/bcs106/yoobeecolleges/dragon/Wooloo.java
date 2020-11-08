package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Wooloo extends NPC{

    int boundry;

    public Wooloo(float speed, int maxHP, int width, int height, int SBoundry) {
        super(speed, maxHP, width, height);
        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        Rect r = SpriteManager.instance.getNPCSprite("Wooloo");
        npcBitmap =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());
        boundry = SBoundry;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Math.abs(GameView.instance.player.position.x-npcX)<300 && Math.abs(GameView.instance.player.position.y - npcY)< GameView.instance.screenHeight/2){
            flee = true;
            target.x = (int) (npcX+(-(Math.signum(GameView.instance.player.position.x-npcX))*1500));
            tempCreationPoint.x= target.x;
        }else if((Scene.instance.timeOfDay)/(Scene.instance.dayLength) > 0 && (Scene.instance.timeOfDay)/(Scene.instance.dayLength) < 0.5){
            idle(500,Math.abs(npcX-target.x) < 10);
            npcY = (int) GameView.instance.groundLevel-npcRect.height();
            npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),npcY);
        }
        else{
            npcY = (int) GameView.instance.groundLevel-npcRect.height()+npcRect.height()/8;
            npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),npcY);
        }

    }
}
