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
    public int idleID;
    public int shootID;
    public boolean idleBoolean;
    public boolean shootBoolean;
    Bitmap idleSprite, shootSprite, shootingSprite;

    public DragonLayers(float speed, int maxHP, int width, int height, int damage) {
        super(speed, maxHP, width, height);
        target.x = (int)npcX;
        arrowRechargeTime = new ActionController(1000*(1+(float)Math.random()/4), (float) 500,2000*(1+(float)Math.random()/4));
        dmg = damage;

        idleSprite =SpriteManager.instance.getNPCSprite("Slayer1");
        shootSprite =SpriteManager.instance.getNPCSprite("Slayer2");
        shootingSprite =SpriteManager.instance.getNPCSprite("Slayer3");

        npcBitmap = idleSprite;
        npcType = 3;
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

                boolean insideLair = Math.abs(npcX-GameView.instance.lair.position.x)<GameView.instance.lair.width/2;
                boolean aboveLair = GameView.instance.player.aimFor().y<GameView.instance.lair.colliderLeft.top && insideLair;

                if (Math.abs(GameView.instance.player.position.x - npcX) > GameView.instance.cameraSize / 4) {
                    if(!aboveLair) {
                        direction = (int) Math.signum(GameView.instance.player.aimFor().x - (npcX+npcWidth/2));
                        target.x = (int) GameView.instance.player.position.x - direction * GameView.instance.cameraSize / 4;
                    }
                    else{
                        target.x = (int) GameView.instance.lair.position.x+direction* GameView.instance.lair.width;
                    }
                }
                if (Math.abs(GameView.instance.player.aimFor().x - npcX) < GameView.instance.cameraSize / 2) {
                    if(!aboveLair){
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
                moveToTarget(deltaTime);
                npcY = (int) GameView.instance.groundLevel - npcRect.height();
                npcRect.offsetTo((int) (npcX + GameView.instance.cameraDisp.x), (int)npcY);
            }
            else {
                idle(GameView.instance.screenWidth/4,Math.abs(npcX - target.x) < 10);
                if (Math.abs(GameView.instance.player.aimFor().x  - npcX) < GameView.instance.cameraSize / 3){
                    lockTarget = true;
                }
            }
            if((!idleBoolean && Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.cameraSize/2) ) {
                idleBoolean = true;
                idleID = SoundEffects.instance.play(SoundEffects.DRAGONLAYER_IDLING, -1, 1);
            }
            if(idleBoolean){
                SoundEffects.instance.setVolume(idleID,GameView.instance.cameraSize/2/(Math.abs(npcX - GameView.instance.player.position.x)+1));
                if(Math.abs(npcX - GameView.instance.player.position.x)>GameView.instance.cameraSize) {
                    idleBoolean = false;
                    SoundEffects.instance.stop(idleID);
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
        hitY = dy/l-(float)(Math.random())/10;
        GameView.instance.projectilePool.shootSpear((int)(npcX+npcWidth/2),(int)(npcY+npcHeight/2),1f, hitX , hitY, dmg);
        SoundEffects.instance.play(SoundEffects.THROW_DA_HO);
    }
}
