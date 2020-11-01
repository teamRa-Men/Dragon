package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Thief extends NPC {
    public Thief(Bitmap bitmap, float speed, int maxHP, int width, int height) {
        super(bitmap, speed, maxHP, width, height);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Math.abs(npcX - creationPoint.x) < 7){
            target.x = (int) GameView.instance.lair.position.x;
        }
        if (Math.abs(npcX - GameView.instance.lair.position.x) < 7){
            target.x = creationPoint.x;
        }
    }
}
