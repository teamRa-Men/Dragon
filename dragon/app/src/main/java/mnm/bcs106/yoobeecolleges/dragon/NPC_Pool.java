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
    ArrayList<DragonLayers>  npcDragonLayers = new ArrayList<DragonLayers>();
    ArrayList<Wizard>  npcWizard = new ArrayList<Wizard>();
    ArrayList<Farmers>  npcFarmers = new ArrayList<Farmers>();
    ArrayList<Thief>  npcThiefs = new ArrayList<Thief>();
    public NPC_Pool(){
        for(int i = 0 ;i < size; i++){
            npcWooloo.add(new Wooloo(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),(float)GameView.instance.cameraSize/45000,100,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,500));
            npcRangedNPC.add(new RangedNPC(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.ottwizard2),(float)GameView.instance.cameraSize/25000,300,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,1));
            npcDragonLayers.add(new DragonLayers(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.ottwizard2),(float)GameView.instance.cameraSize/25000,500,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,10));
            npcWizard.add(new Wizard(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.ottwizard2),(float)GameView.instance.cameraSize/65000,250,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,25));
            npcThiefs.add(new Thief(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.ottwizard2),(float)GameView.instance.cameraSize/45000,250,GameView.instance.cameraSize/40,GameView.instance.cameraSize/40,100));

//            if(i%2==0) {
  //              npcFarmers.add(new Farmers(BitmapFactory.decodeResource(GameView.instance.getResources(), R.drawable.villager), (float) GameView.instance.cameraSize / 25000, 500, GameView.instance.cameraSize / 20, GameView.instance.cameraSize / 20, 0));
    //        }
      //      else {
                npcFarmers.add(new Farmers(BitmapFactory.decodeResource(GameView.instance.getResources(), R.drawable.farmer), (float) GameView.instance.cameraSize / 25000, 500, GameView.instance.cameraSize / 60, GameView.instance.cameraSize / 30, 0));
        //    }
        }
    }
    public Wooloo spawnWooloo (int spawnX, int spawnY){
        for (int i = 0;i<size;i++){
            if (!npcWooloo.get(i).alive) {
                npcWooloo.get(i).spawn((int) (spawnX+GameView.instance.fortress.tilesize*1.5),spawnY);
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
    }public void spawnDragonLayers (int spawnX, int spawnY, int ammount){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcDragonLayers.get(i).alive && d < ammount) {
                npcDragonLayers.get(i).spawn(spawnX,spawnY);
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
            if (!npcFarmers.get(i).alive) {
            npcFarmers.get(i).spawn(spawnX,spawnY);
            return npcFarmers.get(i);}
        }
        return null;
    }public void spawnThiefs (int spawnX, int spawnY, int ammount){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcThiefs.get(i).alive && d < ammount) {
                npcThiefs.get(i).spawn(spawnX,spawnY);
                d++;
            }
        }
    }

    public void draw (Canvas canvas){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).active){
                npcWooloo.get(i).draw(canvas);
            }if (npcRangedNPC.get(i).active){
                npcRangedNPC.get(i).draw(canvas);
            }if (npcWizard.get(i).active){
                npcWizard.get(i).draw(canvas);
            }if (npcFarmers.get(i).active){
                npcFarmers.get(i).draw(canvas);
            }if (npcThiefs.get(i).active){
                npcThiefs.get(i).draw(canvas);
            }if (npcDragonLayers.get(i).active){
                npcDragonLayers.get(i).draw(canvas);
            }
        }
    }
    public void update(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).active){
                npcWooloo.get(i).update(deltaTime);
            }if (npcRangedNPC.get(i).active){
                npcRangedNPC.get(i).update(deltaTime);
            }if (npcWizard.get(i).active){
                npcWizard.get(i).update(deltaTime);
            }if (npcFarmers.get(i).active){
                npcFarmers.get(i).update(deltaTime);
            }if (npcThiefs.get(i).active){
                npcThiefs.get(i).update(deltaTime);
            }if (npcDragonLayers.get(i).active){
                npcDragonLayers.get(i).update(deltaTime);
            }
        }
    }
    public void physics(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).active){
                npcWooloo.get(i).physics(deltaTime);
            }if (npcRangedNPC.get(i).active){
                npcRangedNPC.get(i).physics(deltaTime);
            }if (npcWizard.get(i).active){
                npcWizard.get(i).physics(deltaTime);
            }if (npcFarmers.get(i).active){
                npcFarmers.get(i).physics(deltaTime);
            }if (npcThiefs.get(i).active){
                npcThiefs.get(i).physics(deltaTime);
            }if (npcDragonLayers.get(i).active){
                npcDragonLayers.get(i).physics(deltaTime);
            }
        }
    }
}
