package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Thief extends NPC {
    public int holdCapacity;
    public int howManySteal;
    public boolean stole = false;
    public Thief(Bitmap bitmap, float speed, int maxHP, int width, int height, int stealGold) {
        super(bitmap, speed, maxHP, width, height);
        holdCapacity = stealGold;
        target.x = (int) GameView.instance.lair.position.x;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Math.abs(npcX - creationPoint.x) < 7){
            target.x = (int) GameView.instance.lair.position.x;
            GameView.instance.fortress.currentGold+=howManySteal;
            howManySteal = 0;
            stole = false;
        }
        if (Math.abs(npcX - GameView.instance.lair.position.x) < 7){
            if (GameView.instance.lair.depositedGold>100){
                GameView.instance.lair.depositedGold-=100;
                howManySteal = 100;
            }else {
                howManySteal = GameView.instance.lair.depositedGold;
                GameView.instance.lair.depositedGold=0;
            }

            stole = true;
            target.x = creationPoint.x;
        }
    }
}
