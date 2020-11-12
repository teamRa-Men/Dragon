package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.MacAddress;

import java.util.ArrayList;

public class NPC_Pool {
    int maxSlayers = 10;
    int maxTheives= 3;
    int maxWizards = 10;
    int maxFarmers = 20;
    int maxWooloo = 20;
int maxTributes = 3;

    ArrayList<Wooloo> npcWooloo = new ArrayList<Wooloo>();
    ArrayList<DragonLayers>  npcDragonLayers = new ArrayList<DragonLayers>();
    ArrayList<Wizard>  npcWizard = new ArrayList<Wizard>();
    ArrayList<Farmers>  npcFarmers = new ArrayList<Farmers>();
    ArrayList<Thief>  npcThiefs = new ArrayList<Thief>();
    ArrayList<Tribute>  tributes = new ArrayList<Tribute>();
    public NPC_Pool(){
        int size = GameView.instance.cameraSize / 20;
        for(int i = 0 ;i < maxWooloo; i++) {
            npcWooloo.add(new Wooloo((float) GameView.instance.cameraSize / 35000, 200, size, size, 500));
        }
        for(int i = 0 ;i < maxSlayers; i++) {
            npcDragonLayers.add(new DragonLayers((float) GameView.instance.cameraSize / 25000, 800, size, size*2, 30));
        } for(int i = 0 ;i < maxWizards; i++) {
            npcWizard.add(new Wizard((float) GameView.instance.cameraSize / 45000, 350, size, size, 50));
        } for(int i = 0 ;i < maxTheives; i++) {
            npcThiefs.add(new Thief((float) GameView.instance.cameraSize / 25000, 250, size, size, 30));
        }
        for(int i = 0 ;i < maxFarmers; i++) {
            npcFarmers.add(new Farmers( (float) GameView.instance.cameraSize / 25000, 250, size, size));
        }
        for(int i = 0; i < maxTributes; i++) {
            tributes.add(new Tribute((float) GameView.instance.cameraSize / 35000, 200, size, (int)(size*1.5f)));
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
        for (int i = 0;i<npcWooloo.size();i++){
            if (!npcWooloo.get(i).active) {
                npcWooloo.get(i).spawn((int) (spawnX+f.tilesize*1.5),spawnY, f);
                return npcWooloo.get(i);
            }
        }
        return null;
    }

    public DragonLayers spawnDragonLayers (int spawnX, int spawnY, Fortress f){
        int d = 0;
        for (int i = 0;i< npcDragonLayers.size();i++){
            if (!npcDragonLayers.get(i).active) {
                npcDragonLayers.get(i).spawn(spawnX,spawnY, f);
                return npcDragonLayers.get(i);
            }
        }
        return  null;
    }

    public void spawnWizard (int spawnX, int spawnY, int ammount, Fortress f){
        int d = 0;
        for (int i = 0;i<npcWizard.size();i++){
            if (!npcWizard.get(i).active && d < ammount) {
                npcWizard.get(i).spawn(spawnX,spawnY,f);
                d++;
            }
        }
    }public Farmers spawnFarmers (int spawnX, int spawnY, Fortress f){
        for (int i = 0;i<npcFarmers.size();i++){
            if (!npcFarmers.get(i).active) {
            npcFarmers.get(i).spawn(spawnX,spawnY, f);
            return npcFarmers.get(i);}
        }
        return null;
    }public void spawnThiefs (int spawnX, int spawnY, int ammount, Fortress f){
        int d = 0;
        for (int i = 0;i<npcThiefs.size();i++){
            if (!npcThiefs.get(i).active && d < ammount) {
                npcThiefs.get(i).spawn(spawnX,spawnY, f);
                d++;
            }
        }
    }

    public void draw (Canvas canvas){
        for (int i = 0; i<npcWooloo.size();i++) {
            if (npcWooloo.get(i).active) {
                npcWooloo.get(i).draw(canvas);
            }
        }
        for (int i = 0; i<npcWizard.size();i++) {
            if (npcWizard.get(i).active) {
                npcWizard.get(i).draw(canvas);
            }
        }
        for (int i = 0; i<npcWooloo.size();i++) {
            if (npcFarmers.get(i).active) {
                npcFarmers.get(i).draw(canvas);
            }
        }
        for (int i = 0; i<npcThiefs.size();i++){
            if (npcThiefs.get(i).active){
                npcThiefs.get(i).draw(canvas);
            }
        }
        for (int i = 0; i<npcDragonLayers.size();i++){
            if (npcDragonLayers.get(i).active){
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
        for (int i = 0; i<npcWooloo.size();i++) {
            if (npcWooloo.get(i).active) {
                npcWooloo.get(i).update(deltaTime);
            }
        }
        for (int i = 0; i<npcWizard.size();i++) {
            if (npcWizard.get(i).active) {
                npcWizard.get(i).update(deltaTime);
            }
        }
        for (int i = 0; i<npcFarmers.size();i++) {
            if (npcFarmers.get(i).active) {
                npcFarmers.get(i).update(deltaTime);
            }
        }
        for (int i = 0; i<npcThiefs.size();i++) {
            if (npcThiefs.get(i).active) {
                npcThiefs.get(i).update(deltaTime);
            }
        }
        for (int i = 0; i<npcDragonLayers.size();i++) {
            if (npcDragonLayers.get(i).active){
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
        for (int i = 0; i<npcWooloo.size();i++) {
            if (npcWooloo.get(i).active) {
                npcWooloo.get(i).physics(deltaTime);
            }
        }
        for (int i = 0; i<npcWizard.size();i++) {
            if (npcWizard.get(i).active) {
                npcWizard.get(i).physics(deltaTime);
            }
        }
        for (int i = 0; i<npcFarmers.size();i++) {
            if (npcFarmers.get(i).active) {
                npcFarmers.get(i).physics(deltaTime);
            }
        }
        for (int i = 0; i<npcThiefs.size();i++) {
            if (npcThiefs.get(i).active) {
                npcThiefs.get(i).physics(deltaTime);
            }
        }
        for (int i = 0; i<npcDragonLayers.size();i++) {
            if (npcDragonLayers.get(i).active){
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
