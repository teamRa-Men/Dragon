package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class RangedNPC extends NPC {
    public boolean lockTarget = false;
    public Bitmap attackTexture;
    public ActionController arrowRechargeTime;
    public float hitX;
    public float hitY;
    public int dmg;

    public RangedNPC(Bitmap bitmap, float speed, int maxHP, int width, int height, int damage) {
        super(bitmap, speed, maxHP, width, height);
        target.x = npcX;
        arrowRechargeTime = new ActionController(1000, (float) 0.01,2000);
        dmg = damage;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        arrowRechargeTime.update(deltaTime);
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
            arrowRechargeTime.triggerAction();
            if (arrowRechargeTime.performing){
               shoot();
            }
            if (Math.abs(GameView.instance.player.position.x-npcX)>1000){
                creationPoint.x = npcX;
                target.x = npcX;
                flee = false;
                lockTarget = false;
            }

        }

    }

    public void shoot(){
        hitX = (float) (GameView.instance.player.position.x-npcX+Math.random()/4);
        hitY = (float) (GameView.instance.player.position.y-npcX+Math.random()/4);
        GameView.instance.projectilePool.shootArrow(npcX,npcY,1f, hitX , hitY, dmg);
    }

    @Override
    public void physics(float deltaTime) {
        super.physics(deltaTime);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }
}
