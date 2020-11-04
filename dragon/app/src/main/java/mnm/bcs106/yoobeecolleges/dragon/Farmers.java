 package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Farmers extends NPC {

    public int farmX;
    public int workTime = 0;
    public boolean wasAttacked = false;
    public boolean atFarm = true;
    public boolean whereFarm = false;
    public boolean work = false;

    public Farmers(Bitmap bitmap, float speed, int maxHP, int width, int height,int FX) {
        super(bitmap, speed, maxHP, width, height);
        farmX = npcX ;
    }

    @Override
    public void spawn(int spawnX, int spawnY) {
        super.spawn(spawnX, spawnY);
    }

    public void doStuff() {

    }

    public Boolean atHome = false;
    @Override
    public void update(float deltaTime) {
        if(((Scene.instance.timeOfDay)/(Scene.instance.dayLength) > 0) && ((Scene.instance.timeOfDay)/(Scene.instance.dayLength) < 0.5) && alive) {
//            System.out.println(workTime);
            if (atHome){
                npcY = CreationPoint.y;
                atHome = false;
                flee = false;
            }
            if (work) {
                workTime += deltaTime;
            }
            if (!whereFarm) {
                Point closestFarm = new Point();
                closestFarm.y = 100000;
                for (int j = 0; j < GameView.instance.fortress.currentBuildingsRight.size(); j++) {
                    if (GameView.instance.fortress.currentBuildingsRight.get(j).buildingType == 3) {
                        if (Math.abs(npcX - GameView.instance.fortress.currentBuildingsRight.get(j).x) < closestFarm.y) {
                            farmX = (int) (GameView.instance.fortress.currentBuildingsRight.get(j).x+GameView.instance.fortress.tilesize*1.5);
                            closestFarm.y = Math.abs(npcX - GameView.instance.fortress.currentBuildingsRight.get(j).x);
                        }
                    }
                }
                for (int j = 0; j < GameView.instance.fortress.currentBuildingsLeft.size(); j++) {
                    if (GameView.instance.fortress.currentBuildingsLeft.size() > j) {
                        if (GameView.instance.fortress.currentBuildingsLeft.get(j).buildingType == 3) {
                            if (Math.abs(npcX - GameView.instance.fortress.currentBuildingsLeft.get(j).x) < closestFarm.y) {
                                farmX = (int) (GameView.instance.fortress.currentBuildingsLeft.get(j).x+GameView.instance.fortress.tilesize*1.5);
                                closestFarm.y = Math.abs(npcX - GameView.instance.fortress.currentBuildingsLeft.get(j).x);
                            }
                        }
                    }
                }
                if (farmX != npcX) {
                    whereFarm = true;
                }
            }
            if (!wasAttacked) {
                if (Math.abs(GameView.instance.player.position.x - npcX) < 300) {
                    flee = true;
                    target.x = (int) (npcX + (-(Math.signum(GameView.instance.player.position.x - npcX)) * 1500));
                    tempCreationPoint.x = target.x;
                    wasAttacked = true;
                }
                if (atFarm && Math.abs(target.x - npcX) < 7) {
                    work = true;
                    if (workTime >= 3000) {
                        target.x = tempCreationPoint.x;
                        atFarm = false;
                        workTime = 0;
                        work = false;
                    } else {
                        doStuff();
                    }
                } else if (Math.abs(target.x - npcX) < 7 && !atFarm) {
                    target.x = farmX;
                    atFarm = true;
                }
            } else {
                if (Math.abs(GameView.instance.player.position.x - npcX) < 300) {
                    flee = true;
                    target.x = (int) (npcX + (-(Math.signum(GameView.instance.player.position.x - npcX)) * 1500));
                    tempCreationPoint.x = target.x;
                } else idle(500, Math.abs(npcX - target.x) < 10);
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
                    flee = true;
                    target.x = CreationPoint.x;
                    super.update(deltaTime);
                }else {
                    wasAttacked = false;
                    npcY += npcRect.height()*7;
                    npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),npcY);
            }
            }else {
                wasAttacked = false;
                super.update(deltaTime);
            }
        }
    }
}
