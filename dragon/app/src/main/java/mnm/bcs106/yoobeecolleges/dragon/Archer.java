package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Archer extends NPC {
    public boolean lockTarget = false;

    public Archer(Bitmap bitmap, float speed, int maxHP, int width, int height,int damage) {
        super(bitmap, speed, maxHP, width, height);
        target.x = npcX;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!lockTarget){
            if (Math.abs(GameView.instance.player.position.x-npcX)<500){
                lockTarget = true;
                target.x = (int) GameView.instance.player.position.x;
                flee = true;
            }else {
                idle(500);
            }
        }else if (lockTarget){
            target.x = (int) GameView.instance.player.position.x;
            if (Math.abs(GameView.instance.player.position.x-npcX)>1000){
                creationPoint.x = npcX;
                target.x = npcX;
                flee = false;
                lockTarget = false;
            }
        }
    }
}
