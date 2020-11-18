 package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

 public class Farmers extends NPC {

    public int farmX;
    public int workTime = 0;
    public boolean wasAttacked = false;
    public boolean atFarm = false;
    public boolean whereFarm = false;
    public boolean work = false;
    Bitmap idleSprite, workingSprite;
    public Boolean atHome = false;
    public int scaredID;
    public int idleID;
    public int wellAnywayID;
    public boolean runForestRun = false;
    public boolean idleBoolean = false;
    public boolean wellAnywayBoolean = false;

    public Farmers( float speed, int maxHP, int width, int height) {
        super(speed, maxHP, width, height);

        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        idleSprite = SpriteManager.instance.getNPCSprite("Farmer1");
        workingSprite =SpriteManager.instance.getNPCSprite("Farmer2");

        npcBitmap = idleSprite;
        npcType = 2;
        idleID = SoundEffects.FARMER_IDLING;
    }

    /*
    Down bellow I made so once villager gets spawned he has no work, to resolve some of the issues.
     */

    @Override
    public void spawn(int spawnX, int spawnY, Fortress f) {
        super.spawn(spawnX, spawnY, f);
        work = false;
        farmX = (int)npcX ;
        whereFarm = false;
    }

    /*
    Down bellow I've added method for villagers when they are busy on the fields they start moving around the farm and work.
     */

    public void doStuff() {
        if((!idleBoolean && Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.cameraSize/2) ) {
            idleBoolean = true;


            idleID = SoundEffects.instance.play(SoundEffects.FARMER_IDLING, -1, 1);
            SoundEffects.instance.setVolume(idleID,SoundEffects.instance.volumeMul/3);

        }

        countdown = 0;
        if (countdown >= Math.random()*5000+8000){
            flee = false;
            double targetDistance = (Math.random()-0.5f) * Farm.tileNr*GameView.instance.cameraSize/9;
            target.x = (int) (farmX+ targetDistance);


        }
    }

    /*
    Down bellow I updated update method giving villagers logic of going to work when it's morning and run away from the dragon
    if they see that he's close, or run back home once it's night back to safety keeping them protected from the dragon.
     */

    @Override
    public void update(float deltaTime) {
        if(runForestRun){
            SoundEffects.instance.setVolume(scaredID,GameView.instance.cameraSize/2/Math.abs(npcX - GameView.instance.player.position.x+1));
            if(Math.abs(npcX - target.x) < 10 || !alive) {
                runForestRun = false;
                SoundEffects.instance.stop(scaredID);
            }
        }
        if(idleBoolean){
            SoundEffects.instance.setVolume(idleID,GameView.instance.cameraSize/8/(Math.abs(npcX - GameView.instance.player.position.x)+1));
            if(Math.abs(npcX - GameView.instance.player.position.x)>GameView.instance.cameraSize || !alive) {
                idleBoolean = false;
                SoundEffects.instance.stop(idleID);
            }
        }
        if(((Scene.instance.timeOfDay)/(Scene.instance.dayLength) > 0) && ((Scene.instance.timeOfDay)/(Scene.instance.dayLength) < 0.5) && alive) {
            if (!wasAttacked) {
                if (Math.abs(GameView.instance.player.position.x - npcX) < GameView.instance.screenHeight/4 && GameView.instance.player.position.y > GameView.instance.screenHeight/3) {
                    flee = true;
                    work = false;
                    target.x = (int) (npcX + (-(Math.signum(GameView.instance.player.position.x - npcX)) * 1500));
                    tempCreationPoint.x = target.x;
                    wasAttacked = true;
                }
                if(whereFarm){
                    if(!atFarm) {
                        target.x = farmX;
                        work = false;
                    }
                    else {
                        work = true;
                        doStuff();
                    }
                    if(Math.abs(target.x - npcX) < 7){
                        atFarm = true;
                    }
                }
            } else {
                if (Math.abs(GameView.instance.player.position.x - npcX) < 300) {
                    flee = true;
                    work = false;
                    target.x = (int) (npcX + (-(Math.signum(GameView.instance.player.position.x - npcX)) * 1500));
                    tempCreationPoint.x = target.x;
                    if (!runForestRun){
                        scaredID = SoundEffects.instance.play(SoundEffects.SCARED_WILLAGERS,-1,1);
                        SoundEffects.instance.setVolume(scaredID,SoundEffects.instance.volumeMul/3);
                        runForestRun = true;
                    }

                } else {
                    idle(500, Math.abs(npcX - target.x) < 10);

                }

            }
            if (atHome){
                npcY = CreationPoint.y;
                atHome = false;
                flee = false;
            }
            if (!whereFarm) {
                Point closestFarm = new Point();
                closestFarm.y = 100000;
                for (int j = 0; j < fortress.currentBuildingsRight.size(); j++) {
                    if (fortress.currentBuildingsRight.get(j).buildingType == 3) {
                        if (Math.abs(npcX - fortress.currentBuildingsRight.get(j).x) < closestFarm.y) {
                            farmX = (int) (fortress.currentBuildingsRight.get(j).x+fortress.tilesize*1.5);
                            closestFarm.y = Math.abs((int)npcX - fortress.currentBuildingsRight.get(j).x);
                        }
                    }
                }
                for (int j = 0; j < fortress.currentBuildingsLeft.size(); j++) {
                    if ( fortress.currentBuildingsLeft.get(j).buildingType == 3) {
                        if (Math.abs(npcX -  fortress.currentBuildingsLeft.get(j).x) < closestFarm.y) {
                            farmX = (int) ( fortress.currentBuildingsLeft.get(j).x+ fortress.tilesize*1.5);
                            closestFarm.y = Math.abs((int)npcX -  fortress.currentBuildingsLeft.get(j).x);
                        }
                    }
                }
                if (farmX != npcX) {
                    whereFarm = true;
                }
            }


            super.update(deltaTime);
        }else{
            idleBoolean = false;
            if (alive){
                if (Math.abs(npcX-CreationPoint.x) < 7 && !atHome){
//                    System.out.println("CP = " + CreationPoint.x + " Target = " + target.x + " TempCP = " + tempCreationPoint.x);
                    atHome = true;
                    tempCreationPoint.x = CreationPoint.x;
                    tempCreationPoint.y = CreationPoint.y;
                }
                if (!atHome){
                    target.x = CreationPoint.x;
                    super.update(deltaTime);
                }else {
                    wasAttacked = false;
                    npcY += npcRect.height()*7;
                    npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),(int)npcY);
            }
            }else {
                if(wasAttacked){
                    int r = (int)(Math.random()*3);
                    GoldPool.instance.spawnGold((int)npcX, (int)npcY,r);
                    wasAttacked = false;
                }
                super.update(deltaTime);
            }
        }
        if ((Scene.instance.timeOfDay)/(Scene.instance.dayLength)>0.5) {
            target.x = tempCreationPoint.x;
            atFarm = false;
            workTime = 0;
            work = false;
            flee = true;
            npcBitmap = idleSprite;
        }
        if (work  && !flee) {
            workTime += deltaTime;
            npcBitmap = workingSprite;
        }
        else{
            npcBitmap = idleSprite;
        }
//        if(idleBoolean ){
//            SoundEffects.instance.setVolume(idleID,GameView.instance.cameraSize/2/Math.abs(npcX - GameView.instance.player.position.x));
//        }
        if(runForestRun ){
            SoundEffects.instance.setVolume(scaredID,GameView.instance.cameraSize/2/Math.abs(npcX - GameView.instance.player.position.x));
        }
    }
}
