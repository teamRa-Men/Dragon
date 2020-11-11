package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Wizard extends NPC {
    public boolean lockTarget = false;
    public ActionController arrowRechargeTime;
    public float hitX;
    public float hitY;
    public int dmg;
    boolean shot=false;
    Bitmap idleSprite, shootSprite, shootingSprite;


    public Wizard(float speed, int maxHP, int width, int height, int damage) {
        super(speed, maxHP, width, height);
        target.x = (int)npcX;
        arrowRechargeTime = new ActionController(3000*(1+(float)Math.random()-0.5f), 500,2000*(1+(float)Math.random()-0.5f));
        dmg = damage;

        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        Rect r = SpriteManager.instance.getNPCSprite("Wizard1");
        idleSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getNPCSprite("Wizard2");
        shootSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getNPCSprite("Wizard3");
        shootingSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        npcBitmap = idleSprite;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        arrowRechargeTime.update(deltaTime);
        if (!lockTarget){
            if (Math.abs(GameView.instance.player.position.x-CreationPoint.x)<GameView.instance.screenWidth/2){
                lockTarget = true;
                target.x = (int) GameView.instance.player.position.x+direction*200;
                flee = true;
            }
        }else if (lockTarget){
            direction = (int) Math.signum(GameView.instance.player.aimFor().x -npcX);
            if (Math.abs(GameView.instance.player.position.x-npcX) > GameView.instance.screenWidth/4 ){
                target.x = (int) GameView.instance.player.position.x-direction*200;
            }
            arrowRechargeTime.triggerAction();
            if(arrowRechargeTime.charging){
                shot = false;
                npcBitmap = shootSprite;
                float dy = npcRect.height()/8 * ((float) Math.random() - 0.5f);
                float dx = npcRect.width()/8 * ((float) Math.random() - 0.5f);
                npcRect.offset((int)dx,(int)dy);
            }
            else if (arrowRechargeTime.performing){
                npcBitmap = shootingSprite;
                if(!shot) {
                    shoot();
                    shot = true;
                }
            }
            else {
                npcBitmap = idleSprite;
            }

            if (Math.abs(GameView.instance.player.position.x-npcX)> GameView.instance.screenWidth/2){
                target.x = CreationPoint.x;
                flee = false;
                lockTarget = false;
                arrowRechargeTime.ready = true;
                npcBitmap = idleSprite;
            }

        }

    }

    public void shoot() {
        GameView.instance.projectilePool.shootMagic((int)(npcX+npcWidth/2),(int)(npcY+npcHeight/2), 1f / 2, 0, 1, dmg);

    }
}
