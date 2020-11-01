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
        farmX = npcX + 500;
    }

    @Override
    public void spawn(int spawnX, int spawnY) {
        super.spawn(spawnX, spawnY);
    }

    public void doStuff() {

    }

    @Override
    public void update(float deltaTime) {
        if (work){
            workTime+=deltaTime;
        }
        if (!whereFarm){
            Point closestFarm = new Point();
            closestFarm.y = 100000;
            for (int j = 0; j < GameView.instance.fortress.currentBuildingsRight.size(); j++) {
                if (GameView.instance.fortress.currentBuildingsRight.get(j).buildingType == 3) {
                    if (Math.abs(npcX - GameView.instance.fortress.currentBuildingsRight.get(j).x) < closestFarm.y) {
                        farmX = GameView.instance.fortress.currentBuildingsRight.get(j).x;
                        closestFarm.y = Math.abs(npcX - GameView.instance.fortress.currentBuildingsRight.get(j).x);
                    }
                }
                if (GameView.instance.fortress.currentBuildingsLeft.size() > j) {
                    if (GameView.instance.fortress.currentBuildingsLeft.get(j).buildingType == 3) {
                        if (Math.abs(npcX - GameView.instance.fortress.currentBuildingsLeft.get(j).x) < closestFarm.y) {
                            farmX = GameView.instance.fortress.currentBuildingsLeft.get(j).x;
                            closestFarm.y = Math.abs(npcX - GameView.instance.fortress.currentBuildingsLeft.get(j).x);
                        }
                    }
                }
            }
        }
        whereFarm = true;
        if (!wasAttacked){
            if (Math.abs(GameView.instance.player.position.x-npcX)<300){
                flee = true;
                target.x = (int) (npcX+(-(Math.signum(GameView.instance.player.position.x-npcX))*1500));
                creationPoint.x= target.x;
                wasAttacked = true;
            }
            if (atFarm && Math.abs(target.x-npcX) < 7){
                work = true;
                if (workTime>=5000) {
                    target.x = creationPoint.x;
                    atFarm = false;
                    workTime = 0;
                    work = false;
                }else {
                    doStuff();
                }
            }else if (Math.abs(target.x-npcX) < 7 && !atFarm){
                target.x = farmX;
                atFarm = true;
            }
        }else {
            if (Math.abs(GameView.instance.player.position.x-npcX)<300){
                flee = true;
                target.x = (int) (npcX+(-(Math.signum(GameView.instance.player.position.x-npcX))*1500));
                creationPoint.x= target.x;
            }else idle(500,Math.abs(npcX-target.x) < 10);
        }
        super.update(deltaTime);
    }
}
