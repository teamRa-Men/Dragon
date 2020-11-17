package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Wooloo extends NPC{

    int boundry;

    /*
    Down bellow i added some important variables into main constructor of the Wooloo class.
    Such as boundaries of farms.
     */

    public Wooloo(float speed, int maxHP, int width, int height, int SBoundry) {
        super(speed, maxHP, width, height);

        npcBitmap =SpriteManager.instance.getNPCSprite("Wooloo");
        boundry = SBoundry;
        npcType = 1;
    }


    /*
    In uodated update method i made Wooloo to walk around fields and run from the dragon once they see it
    though they might get lost once they run away from the fields.
     */
    int fleeSoundID;
    boolean fleeingSound = false;
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (alive) {
            if (Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.screenHeight/4 && GameView.instance.player.position.y > GameView.instance.screenHeight/3) {

                flee = true;
                target.x = (int) (npcX + (-(Math.signum(GameView.instance.player.position.x - npcX)) * 1500));
                tempCreationPoint.x = target.x;
                if(!fleeingSound){
                    fleeSoundID = SoundEffects.instance.play(SoundEffects.SHEEPFLEEING,-1,1);
                    fleeingSound = true;
                }

            } else if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) > 0 && (Scene.instance.timeOfDay) / (Scene.instance.dayLength) < 0.5) {
                idle(500, Math.abs(npcX - target.x) < 10);

                npcY = (int) GameView.instance.groundLevel - npcRect.height();
                npcRect.offsetTo((int) (npcX + GameView.instance.cameraDisp.x), (int)npcY);
            } else {
                if (!flee) {
                    npcY = (int) GameView.instance.groundLevel - npcRect.height() + npcRect.height() / 8;
                } else {
                    npcY = (int) GameView.instance.groundLevel - npcRect.height();
                }
                npcRect.offsetTo((int) (npcX + GameView.instance.cameraDisp.x), (int)npcY);
            }
            if(fleeingSound ){
                SoundEffects.instance.setVolume(fleeSoundID,GameView.instance.cameraSize/2/Math.abs(npcX - GameView.instance.player.position.x));
                if( Math.abs(npcX - target.x) < 10 ) {
                    fleeingSound = false;
                    SoundEffects.instance.stop(fleeSoundID);
                }
            }
        }
        else{
            if(fleeingSound ) {
                fleeingSound = false;
                SoundEffects.instance.stop(fleeSoundID);
            }
        }
    }
}
