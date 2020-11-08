package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Thief extends NPC {
    public int howManySteal;
    public int maxSteal;
    public boolean stole = true;
    Bitmap idleSprite, stolenSprite;

    public Thief(float speed, int maxHP, int width, int height, int stealGold) {
        super(speed, maxHP, width, height);
        target.x = (int) GameView.instance.lair.position.x;
        maxSteal = stealGold;

        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        Rect r = SpriteManager.instance.getNPCSprite("Thief1");
        idleSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getNPCSprite("Thief2");
        stolenSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        npcBitmap = idleSprite;
    }
    @Override
    public void death() {
        GoldPool.instance.spawnGold(npcX,npcY,howManySteal);
        howManySteal = 0;
        npcBitmap = idleSprite;
        super.death();
    }

    @Override
    public void spawn(int spawnX, int spawnY) {
        super.spawn(spawnX, spawnY);

    }

    @Override
    public void update(float deltaTime) {

        //System.out.println("gold" + howManySteal);
        super.update(deltaTime);
        if (Math.abs(npcX - tempCreationPoint.x) < 7 && stole){
            target.x = (int) GameView.instance.lair.position.x;
            GameView.instance.fortress.currentGold+=howManySteal;
            howManySteal = 0;
            stole = false;
            npcBitmap = idleSprite;
        }
        if (Math.abs(npcX - GameView.instance.lair.position.x) < 7 && !stole){
            if (GameView.instance.lair.depositedGold>maxSteal){
                GameView.instance.lair.stealGold(maxSteal);
                howManySteal = maxSteal;
            }else {
                howManySteal = GameView.instance.lair.depositedGold;
                GameView.instance.lair.stealGold(GameView.instance.lair.depositedGold);
            }
            npcBitmap = stolenSprite;
            stole = true;
            target.x = tempCreationPoint.x;
        }

        if (Math.abs(GameView.instance.player.position.x-npcX) < 500 ){
            flee = true;
        }else {
            flee = false;
        }

    }

}
