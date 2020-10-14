package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class RangedNPC extends NPC {
    public boolean lockTarget = false;
    public Bitmap attackTexture;
    public Projectile projectile;

    public RangedNPC(Bitmap bitmap, float speed, int maxHP, int width, int height, int damage) {
        super(bitmap, speed, maxHP, width, height);
        target.x = npcX;
        attackTexture = BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.arrow);
        attackTexture = Bitmap.createScaledBitmap(attackTexture,100,100,false);
        projectile = new Projectile(attackTexture,npcX,npcY);
        projectile.damage = 10;
        projectile.setPush(1,1);
        projectile.onCollision(GameView.instance.player);
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
            projectile.shoot(1,GameView.instance.player.position.x,GameView.instance.player.position.x);
        }
        projectile.onGrounded(GameView.instance.getGroundLevel());
    }

    public void shoot(){

    }

    @Override
    public void physics(float deltaTime) {
        super.physics(deltaTime);
        projectile.physics(deltaTime);
        projectile.setCoolDown(1000,deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        projectile.draw(canvas);
    }
}
