package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Wooloo extends NPC{

    int boundry;

    /*
    Down bellow i added some important variables into main constructor of the Wooloo class.
    Such as boundaries of farms.
     */

    public Wooloo(float speed, int maxHP, int width, int height, int SBoundry) {
        super(speed, maxHP, width, height);
        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        Rect r = SpriteManager.instance.getNPCSprite("Wooloo");
        npcBitmap =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());
        boundry = SBoundry;
    }
    @Override

    /*
    In uodated update method i made Wooloo to walk around fields and run from the dragon once they see it
    though they might get lost once they run away from the fields.
     */

    public void update(float deltaTime) {
        super.update(deltaTime);
        if (alive) {
            if (Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.screenHeight/4 && GameView.instance.player.position.y > GameView.instance.screenHeight/3) {
                flee = true;
                target.x = (int) (npcX + (-(Math.signum(GameView.instance.player.position.x - npcX)) * 1500));
                tempCreationPoint.x = target.x;
            } else if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) > 0 && (Scene.instance.timeOfDay) / (Scene.instance.dayLength) < 0.5) {
                idle(500, Math.abs(npcX - target.x) < 10);
                npcY = (int) GameView.instance.groundLevel - npcRect.height();
                npcRect.offsetTo((int) (npcX + GameView.instance.cameraDisp.x), (int)npcY);
            } else {
                if (!flee) {
                    npcY = (int) GameView.instance.groundLevel - npcRect.height() + npcRect.height() / 8;
                } else {
                    npcY = (int) GameView.instance.groundLevel - npcRect.height();
                }
                npcRect.offsetTo((int) (npcX + GameView.instance.cameraDisp.x), (int)npcY);
            }
        }
    }
}
