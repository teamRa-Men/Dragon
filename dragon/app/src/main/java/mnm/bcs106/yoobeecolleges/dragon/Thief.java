package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Thief extends NPC {
    public int howManySteal;
    public boolean stole = false;
    public Thief(Bitmap bitmap, float speed, int maxHP, int width, int height, int stealGold) {
        super(bitmap, speed, maxHP, width, height);
        target.x = (int) GameView.instance.lair.position.x;
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
        if (Math.abs(npcX - tempCreationPoint.x) < 7){
            target.x = (int) GameView.instance.lair.position.x;
            GameView.instance.fortress.currentGold+=howManySteal;
            howManySteal = 0;
            stole = false;
        }
        if (Math.abs(npcX - GameView.instance.lair.position.x) < 7){
            if (GameView.instance.lair.depositedGold>100){
                GameView.instance.lair.stealGold(100);
                howManySteal = 100;
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
