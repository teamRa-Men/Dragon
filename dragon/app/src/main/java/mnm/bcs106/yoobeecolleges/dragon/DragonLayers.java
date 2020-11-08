package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class DragonLayers extends NPC {
    public boolean lockTarget = false;
    public ActionController arrowRechargeTime;
    public float hitX;
    public float hitY;
    public int dmg;
    boolean shot=false;
    Bitmap idleSprite, shootSprite, shootingSprite;

    public DragonLayers(float speed, int maxHP, int width, int height, int damage) {
        super(speed, maxHP, width, height);
        target.x = npcX;
        arrowRechargeTime = new ActionController(1000, (float) 1000,2000);
        dmg = damage;

        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        Rect r = SpriteManager.instance.getNPCSprite("Slayer1");
        idleSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getNPCSprite("Slayer2");
        shootSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getNPCSprite("Slayer3");
        shootingSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        npcBitmap = idleSprite;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(alive) {
            arrowRechargeTime.update(deltaTime);
            direction = (int) Math.signum(GameView.instance.player.position.x - npcX);
            if(lockTarget) {
                if (Math.abs(GameView.instance.player.position.x - npcX) > GameView.instance.cameraSize / 4) {
                    target.x = (int) GameView.instance.player.position.x - direction * GameView.instance.cameraSize / 4;
                    moveToTarget(deltaTime);
                    npcY = (int) GameView.instance.groundLevel - npcRect.height();
                    npcRect.offsetTo((int) (npcX + GameView.instance.cameraDisp.x), npcY);
                }
                if (Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.cameraSize / 2) {
                    arrowRechargeTime.triggerAction();
                    if (arrowRechargeTime.charging) {
                        shot = false;
                        npcBitmap = shootSprite;
                    } else if (arrowRechargeTime.performing) {
                        if (!shot) {
                            shoot();
                            shot = true;
                            npcBitmap = shootingSprite;
                        }
                    } else {
                        npcBitmap = idleSprite;
                    }
                }
            }
            else {
                idle(GameView.instance.screenWidth/4,true);
                if (Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.cameraSize / 3){
                    lockTarget = true;
                }
            }
        }
    }

    public void shoot() {
        Vector2 target = GameView.instance.player.aimFor();
        float dx = target.x-npcX;
        float dy = target.y-npcY;
        float l= (float)Math.sqrt(dx*dx+dy*dy);
        hitX = dx/l;
        hitY = dy/l-(float)Math.random()/3;
        GameView.instance.projectilePool.shootSpear(npcX+npcWidth/2,npcY+npcHeight/2,1f, hitX , hitY, dmg);
    }
}
