package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Thief extends NPC {
    public int howManySteal;
    public int maxSteal;
    public boolean stole = true;
    public Thief(Bitmap bitmap, float speed, int maxHP, int width, int height, int stealGold) {
        super(bitmap, speed, maxHP, width, height);
        target.x = (int) GameView.instance.lair.position.x;
        maxSteal = stealGold;
    }
    @Override
    public void death() {
        GoldPool.instance.spawnGold(npcX,npcY,howManySteal);
        howManySteal = 0;
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
        }
        if (Math.abs(npcX - GameView.instance.lair.position.x) < 7 && !stole){
            if (GameView.instance.lair.depositedGold>maxSteal){
                GameView.instance.lair.stealGold(maxSteal);
                howManySteal = maxSteal;
            }else {
                howManySteal = GameView.instance.lair.depositedGold;
                GameView.instance.lair.stealGold(GameView.instance.lair.depositedGold);
            }
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
