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
    public int shootSpellID;
    public boolean shootBoolean;
    boolean shot=false;
    Bitmap idleSprite, shootSprite, shootingSprite;


    public Wizard(float speed, int maxHP, int width, int height, int damage) {
        super(speed, maxHP, width, height);
        target.x = (int)npcX;
        arrowRechargeTime = new ActionController(3000*(1+(float)Math.random()-0.5f), 500,2000*(1+(float)Math.random()-0.5f));
        dmg = damage;

        idleSprite = SpriteManager.instance.getNPCSprite("Wizard1");
        shootSprite = SpriteManager.instance.getNPCSprite("Wizard2");
        shootingSprite = SpriteManager.instance.getNPCSprite("Wizard3");

        npcBitmap = idleSprite;
        npcType = 5;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        arrowRechargeTime.update(deltaTime);
        if(shootBoolean){
            SoundEffects.instance.setVolume(shootSpellID,GameView.instance.cameraSize/2/(Math.abs(npcX - GameView.instance.player.position.x)+1));
            if(Math.abs(npcX - GameView.instance.player.position.x)>GameView.instance.cameraSize || !alive) {
                shootBoolean = false;
                SoundEffects.instance.stop(shootSpellID);
            }
        }
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
                if((!shootBoolean && Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.cameraSize/2) ) {
                    shootBoolean = true;
                    shootSpellID = SoundEffects.instance.play(SoundEffects.WIZARD_CHARGE, -1, 1);
                }

            }
            else if (arrowRechargeTime.performing){
                npcBitmap = shootingSprite;
                if(shootBoolean){

                        shootBoolean = false;
                        SoundEffects.instance.stop(shootSpellID);

                }
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
        GameView.instance.projectilePool.shootMagic((int)(npcX+npcWidth/2),(int)npcY, 0.45f, 0, 1, dmg);

    }
}
