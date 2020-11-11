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
        target.x = (int)npcX;
        arrowRechargeTime = new ActionController(1000*(1+(float)Math.random()-0.5f), (float) 1000,2000*(1+(float)Math.random()-0.5f));
        dmg = damage;


        idleSprite =SpriteManager.instance.getNPCSprite("Slayer1");
        shootSprite =SpriteManager.instance.getNPCSprite("Slayer2");
        shootingSprite =SpriteManager.instance.getNPCSprite("Slayer3");

        npcBitmap = idleSprite;
    }

    @Override
    public void spawn(int spawnX, int spawnY, Fortress fortress) {
        super.spawn(spawnX, spawnY, fortress);
        lockTarget = false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(alive) {
            arrowRechargeTime.update(deltaTime);

            if(lockTarget) {
                direction = (int) Math.signum(GameView.instance.player.aimFor().x - (npcX+npcWidth/2));
                if (Math.abs(GameView.instance.player.position.x - npcX) > GameView.instance.cameraSize / 4) {
                    target.x = (int) GameView.instance.player.position.x - direction * GameView.instance.cameraSize / 4;
                    moveToTarget(deltaTime);
                    npcY = (int) GameView.instance.groundLevel - npcRect.height();
                    npcRect.offsetTo((int) (npcX + GameView.instance.cameraDisp.x), (int)npcY);
                }
                if (Math.abs(GameView.instance.player.aimFor().x - npcX) < GameView.instance.cameraSize / 2) {
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
                idle(GameView.instance.screenWidth/4,Math.abs(npcX - target.x) < 10);
                if (Math.abs(GameView.instance.player.aimFor().x  - npcX) < GameView.instance.cameraSize / 3){
                    lockTarget = true;
                }
            }
        }
    }

    public void shoot() {
        Vector2 target = GameView.instance.player.aimFor();
        float dx = target.x-(npcX+npcWidth/2);
        float dy = target.y-(npcY+npcHeight/2);
        float l= (float)Math.sqrt(dx*dx+dy*dy);

        hitX = dx/l;
        hitY = dy/l-(float)(Math.random())/8;
        GameView.instance.projectilePool.shootSpear((int)(npcX+npcWidth/2),(int)(npcY+npcHeight/2),1f, hitX , hitY, dmg);
    }
}
