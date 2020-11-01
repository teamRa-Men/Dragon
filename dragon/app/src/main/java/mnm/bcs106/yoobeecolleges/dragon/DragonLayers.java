package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class DragonLayers extends RangedNPC {
    public DragonLayers(Bitmap bitmap, float speed, int maxHP, int width, int height, int damage) {
        super(bitmap, speed, maxHP, width, height, damage);
        target.x = npcX;
        arrowRechargeTime = new ActionController(1000, (float) 0.01,5000);
        dmg = damage;
    }

    @Override
    public void update(float deltaTime) {
        arrowRechargeTime.update(deltaTime);
        countdown+=deltaTime;
        if (Math.abs(target.x-npcX)>1){
            direction = (int) Math.signum(target.x-npcX);
        }
        moveToTarget(deltaTime);
        npcY = (int) GameView.instance.groundLevel-npcRect.height();
        npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),npcY);

        if (Math.abs(GameView.instance.player.position.x-npcX) > 1000 ){
            target.x = (int) GameView.instance.player.position.x-direction*200;
        }
        idle(500,Math.abs(npcX-target.x) < 10);
        arrowRechargeTime.triggerAction();
        if (arrowRechargeTime.performing){
            shoot();
        }
    }

    @Override
    public void shoot() {
        Vector2 target = GameView.instance.player.aimFor();
        hitX = (target.x-npcX)*(((float)Math.random()-0.5f)/4+1);
        hitY = (target.y-npcY)*(((float)Math.random()-0.5f)/4+1);
        GameView.instance.projectilePool.shootArrow(npcX,npcY,2f, hitX , hitY, dmg);
    }
}
