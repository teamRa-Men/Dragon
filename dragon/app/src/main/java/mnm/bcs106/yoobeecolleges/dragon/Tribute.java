package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tribute extends NPC{
    public int tributeSize;
    public boolean given = false;
    Flag flag;

    public Tribute(float speed, int maxHP, int width, int height) {
        super(speed, maxHP, width, height);
        target.x = (int) GameView.instance.lair.position.x;
        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        Rect r = SpriteManager.instance.getNPCSprite("Tribute");
        npcBitmap =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());
        flag = new Flag();
        flag.setSurrender(true);
    }

    public void spawn(int spawnX, int spawnY, int tributeSize) {
        super.spawn(spawnX, spawnY);
        this.tributeSize = tributeSize;
        given = false;
        System.out.println("tribute spawwn");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        flag.y = npcRect.top;
        flag.x = (int)(npcX+npcWidth/2);
        flag.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {

        //System.out.println("gold" + howManySteal);
        super.update(deltaTime);
        flee = true;
        if (!given){
            target.x = (int) GameView.instance.lair.position.x - direction*GameView.instance.screenWidth/4;
            if(Math.abs(target.x-npcX) < 7 || Math.abs(GameView.instance.player.position.x - npcX)< GameView.instance.screenWidth/4){
                GoldPool.instance.spawnGold((int)npcX,(int)npcY,tributeSize);
                given = true;
            }

        }
        else {
            target.x = tempCreationPoint.x;
            if(Math.abs(tempCreationPoint.x-npcX) < 7){
                active = false;
            }
        }
    }

}
