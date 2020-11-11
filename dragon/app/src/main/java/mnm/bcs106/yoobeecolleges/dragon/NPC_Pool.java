package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.MacAddress;

import java.util.ArrayList;

public class NPC_Pool {
    int size = 20;

    ArrayList<Wooloo> npcWooloo = new ArrayList<Wooloo>();
    ArrayList<DragonLayers>  npcDragonLayers = new ArrayList<DragonLayers>();
    ArrayList<Wizard>  npcWizard = new ArrayList<Wizard>();
    ArrayList<Farmers>  npcFarmers = new ArrayList<Farmers>();
    ArrayList<Thief>  npcThiefs = new ArrayList<Thief>();
    ArrayList<Tribute>  tributes = new ArrayList<Tribute>();
    public NPC_Pool(){




        for(int i = 0 ;i < size; i++){
            npcWooloo.add(new Wooloo((float)GameView.instance.cameraSize/35000,100,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,500));
            npcDragonLayers.add(new DragonLayers((float)GameView.instance.cameraSize/25000,500,GameView.instance.cameraSize/20,GameView.instance.cameraSize/10,10));
            npcWizard.add(new Wizard((float)GameView.instance.cameraSize/45000,250,GameView.instance.cameraSize/30,GameView.instance.cameraSize/30,25));
            npcThiefs.add(new Thief((float)GameView.instance.cameraSize/25000,250,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,30));
            npcFarmers.add(new Farmers( (float) GameView.instance.cameraSize / 25000, 200, GameView.instance.cameraSize / 20, GameView.instance.cameraSize / 20));

        }
        for(int i = 0; i < 3; i++) {
            tributes.add(new Tribute((float) GameView.instance.cameraSize / 35000, 200, GameView.instance.cameraSize / 20, GameView.instance.cameraSize / 12));
        }
    }
    public Tribute spawnTribute(int spawnX, int spawnY, int tributeSize, Fortress f){
        for (int i = 0;i<tributes.size();i++){
            if (!tributes.get(i).active) {
                tributes.get(i).spawn(spawnX,spawnY, tributeSize, f);
                return tributes.get(i);
            }
        }
        return null;
    }

    public Wooloo spawnWooloo (int spawnX, int spawnY, Fortress f){
        for (int i = 0;i<size;i++){
            if (!npcWooloo.get(i).alive) {
                npcWooloo.get(i).spawn((int) (spawnX+f.tilesize*1.5),spawnY, f);
                return npcWooloo.get(i);
            }
        }
        return null;
    }

    public void spawnDragonLayers (int spawnX, int spawnY, int ammount, Fortress f){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcDragonLayers.get(i).alive && d < ammount) {
                npcDragonLayers.get(i).spawn(spawnX,spawnY, f);
                d++;
            }
        }
    }

    public void spawnWizard (int spawnX, int spawnY, int ammount, Fortress f){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcWizard.get(i).alive && d < ammount) {
                npcWizard.get(i).spawn(spawnX,spawnY,f);
                d++;
            }
        }
    }public Farmers spawnFarmers (int spawnX, int spawnY, Fortress f){
        for (int i = 0;i<size;i++){
            if (!npcFarmers.get(i).alive) {
            npcFarmers.get(i).spawn(spawnX,spawnY, f);
            return npcFarmers.get(i);}
        }
        return null;
    }public void spawnThiefs (int spawnX, int spawnY, int ammount, Fortress f){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcThiefs.get(i).alive && d < ammount) {
                npcThiefs.get(i).spawn(spawnX,spawnY, f);
                d++;
            }
        }
    }

    public void draw (Canvas canvas){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).active){
                npcWooloo.get(i).draw(canvas);
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

        for (int i = 0;i<tributes.size();i++){
            if (tributes.get(i).active) {
                tributes.get(i).draw(canvas);
            }
        }
    }
    public void update(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).active){
                npcWooloo.get(i).update(deltaTime);
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
        for (int i = 0;i<tributes.size();i++){
            if (tributes.get(i).active) {
                tributes.get(i).update(deltaTime);
            }
        }
    }
    public void physics(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo.get(i).active){
                npcWooloo.get(i).physics(deltaTime);
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
        for (int i = 0;i<tributes.size();i++){
            if (tributes.get(i).active) {
                tributes.get(i).physics(deltaTime);
            }
        }
    }
}
