package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Wizard extends RangedNPC {
    public Wizard(Bitmap bitmap, float speed, int maxHP, int width, int height, int damage) {
        super(bitmap, speed, maxHP, width, height, damage);
        arrowRechargeTime = new ActionController(1000, (float) 0.01,5000);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void shoot() {
        Vector2 target = GameView.instance.player.aimFor();
        hitX = target.x-npcX;
        hitY = target.y-npcY;
        GameView.instance.projectilePool.shootMagic(npcX,npcY,1f, hitX , hitY,dmg);
    }
}
