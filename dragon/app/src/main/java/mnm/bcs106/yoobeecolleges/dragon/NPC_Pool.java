package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.MacAddress;

import java.util.ArrayList;

public class NPC_Pool {
    int size = 20;

    ArrayList<Wooloo> npcWooloo = new ArrayList<Wooloo>();
    ArrayList<RangedNPC>  npcRangedNPC = new ArrayList<RangedNPC>();
    ArrayList<Wizard>  npcWizard = new ArrayList<Wizard>();
    ArrayList<Farmers>  npcFarmers = new ArrayList<Farmers>();
    public NPC_Pool(){
        for(int i = 0 ;i < size; i++){
            npcWooloo.add(new Wooloo(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),(float)GameView.instance.cameraSize/45000,100,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,500));
            npcRangedNPC.add(new RangedNPC(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),(float)GameView.instance.cameraSize/25000,300,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,1));
            npcWizard.add(new Wizard(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.ottwizard),(float)GameView.instance.cameraSize/65000,500,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,25));

//            if(i%2==0) {
  //              npcFarmers.add(new Farmers(BitmapFactory.decodeResource(GameView.instance.getResources(), R.drawable.villager), (float) GameView.instance.cameraSize / 25000, 500, GameView.instance.cameraSize / 20, GameView.instance.cameraSize / 20, 0));
    //        }
      //      else {
                npcFarmers.add(new Farmers(BitmapFactory.decodeResource(GameView.instance.getResources(), R.drawable.farmer_paper), (float) GameView.instance.cameraSize / 25000, 500, GameView.instance.cameraSize / 60, GameView.instance.cameraSize / 30, 0));
        //    }
        }
    }
    public Wooloo spawnWooloo (int spawnX, int spawnY){
        for (int i = 0;i<size;i++){
            if (!npcWooloo.get(i).alive) {
                npcWooloo.get(i).spawn(spawnX,spawnY);
                return npcWooloo.get(i);
            }
        }

        return null;
    }

    public void spawnArcher (int spawnX, int spawnY, int ammount){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcRangedNPC.get(i).alive && d < ammount) {
                npcRangedNPC.get(i).spawn(spawnX,spawnY);
                d++;
            }
        }
    }public void spawnWizard (int spawnX, int spawnY, int ammount){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcWizard.get(i).alive && d < ammount) {
                npcWizard.get(i).spawn(spawnX,spawnY);
                d++;
            }
        }
    }public Farmers spawnFarmers (int spawnX, int spawnY){
        for (int i = 0;i<size;i++){
            Point closestFarm = new Point();
            closestFarm.y = 100000;
            if (!npcFarmers.get(i).alive) {
            npcFarmers.get(i).spawn(spawnX,spawnY);
            return npcFarmers.get(i);}
        }
        return null;
    }

    public void draw (Canvas canvas){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).alive){
                npcWooloo.get(i).draw(canvas);
            }if (npcRangedNPC.get(i).alive){
                npcRangedNPC.get(i).draw(canvas);
            }if (npcWizard.get(i).alive){
                npcWizard.get(i).draw(canvas);
            }if (npcFarmers.get(i).alive){
                npcFarmers.get(i).draw(canvas);
            }
        }
    }
    public void update(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).alive){
                npcWooloo.get(i).update(deltaTime);
            }if (npcRangedNPC.get(i).alive){
                npcRangedNPC.get(i).update(deltaTime);
            }if (npcWizard.get(i).alive){
                npcWizard.get(i).update(deltaTime);
            }if (npcFarmers.get(i).alive){
                npcFarmers.get(i).update(deltaTime);
            }
        }
    }
    public void physics(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).alive){
                npcWooloo.get(i).physics(deltaTime);
            }if (npcRangedNPC.get(i).alive){
                npcRangedNPC.get(i).physics(deltaTime);
            }if (npcWizard.get(i).alive){
                npcWizard.get(i).physics(deltaTime);
            }if (npcFarmers.get(i).alive){
                npcFarmers.get(i).physics(deltaTime);
            }
        }
    }
}
