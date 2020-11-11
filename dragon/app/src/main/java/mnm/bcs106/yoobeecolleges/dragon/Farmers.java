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

    public Farmers( float speed, int maxHP, int width, int height,int FX) {
        super(speed, maxHP, width, height);
        farmX = (int)npcX ;

        Bitmap npcSheet = SpriteManager.instance.NPCSheet;
        Rect r = SpriteManager.instance.getNPCSprite("Farmer1");
        idleSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getNPCSprite("Farmer2");
        workingSprite =Bitmap.createBitmap(npcSheet,r.left,r.top,r.width(),r.height());

        npcBitmap = idleSprite;
    }

    /*
    Down bellow I made so once villager gets spawned he has no work, to resolve some of the issues.
     */

    @Override
    public void spawn(int spawnX, int spawnY) {
        super.spawn(spawnX, spawnY);
        work = false;
    }

    /*
    Down bellow I've added method for villagers when they are busy on the fields they start moving around the farm and work.
     */

    public void doStuff() {
        if (countdown >= Math.random()*5000+8000){
                flee = false;
                double targetDistance = (Math.random()-0.5f) * Farm.tileNr*GameView.instance.cameraSize/9;
                target.x = (int) (farmX+ targetDistance);
                countdown = 0;
        }

    }

    /*
    Down bellow I updated update method giving villagers logic of going to work when it's morning and run away from the dragon
    if they see that he's close, or run back home once it's night back to safety keeping them protected from the dragon.
     */


    @Override
    public void update(float deltaTime) {
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
                } else {
                    idle(500, true);//Math.abs(npcX - target.x) < 10);
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
                for (int j = 0; j < GameView.instance.fortress.currentBuildingsRight.size(); j++) {
                    if (GameView.instance.fortress.currentBuildingsRight.get(j).buildingType == 3) {
                        if (Math.abs(npcX - GameView.instance.fortress.currentBuildingsRight.get(j).x) < closestFarm.y) {
                            farmX = (int) (GameView.instance.fortress.currentBuildingsRight.get(j).x+GameView.instance.fortress.tilesize*1.5);
                            closestFarm.y = Math.abs((int)npcX - GameView.instance.fortress.currentBuildingsRight.get(j).x);
                        }
                    }
                }
                for (int j = 0; j < GameView.instance.fortress.currentBuildingsLeft.size(); j++) {
                    if (GameView.instance.fortress.currentBuildingsLeft.size() > j) {
                        if (GameView.instance.fortress.currentBuildingsLeft.get(j).buildingType == 3) {
                            if (Math.abs(npcX - GameView.instance.fortress.currentBuildingsLeft.get(j).x) < closestFarm.y) {
                                farmX = (int) (GameView.instance.fortress.currentBuildingsLeft.get(j).x+GameView.instance.fortress.tilesize*1.5);
                                closestFarm.y = Math.abs((int)npcX - GameView.instance.fortress.currentBuildingsLeft.get(j).x);
                            }
                        }
                    }
                }
                if (farmX != npcX) {
                    whereFarm = true;
                }
            }

            super.update(deltaTime);
        }else{
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
                if(wasAttacked = true){
                    int r = (int)(Math.random()*3);
                    GoldPool.instance.spawnGold((int)npcX, (int)npcY,r);}
                wasAttacked = false;
                super.update(deltaTime);
            }
        }
        if ((Scene.instance.timeOfDay)/(Scene.instance.dayLength)>0.5 && !flee) {
            target.x = tempCreationPoint.x;
            atFarm = false;
            workTime = 0;
            work = false;

            npcBitmap = idleSprite;
        }
        if (work  && !flee) {
            workTime += deltaTime;
            npcBitmap = workingSprite;
        }
        else{
            npcBitmap = idleSprite;
        }
    }
}
