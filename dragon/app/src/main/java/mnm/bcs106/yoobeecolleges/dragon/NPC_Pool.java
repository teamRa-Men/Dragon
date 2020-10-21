package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class NPC_Pool {
    int size = 20;

    Wooloo[] npcWooloo = new Wooloo[size];
    RangedNPC[] npcRangedNPC = new RangedNPC[size];
    Wizard[] npcWizard = new Wizard[size];
    public NPC_Pool(){
        for(int i = 0 ;i < size; i++){
            npcWooloo[i] = new Wooloo(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),(float)GameView.instance.cameraSize/25000,100,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,500);
            npcRangedNPC[i] = new RangedNPC(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),(float)GameView.instance.cameraSize/25000,300,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,30);
            npcWizard[i] = new Wizard(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),(float)GameView.instance.cameraSize/25000,500,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,100);
        }
    }
    public Wooloo spawnWooloo (int spawnX, int spawnY){
        for (int i = 0;i<size;i++){
            if (!npcWooloo[i].alive) {
                npcWooloo[i].alive = true;
                npcWooloo[i].npcX = spawnX;
                npcWooloo[i].npcY = spawnY;

                return npcWooloo[i];
            }
        }

        return null;
    }

    public void spawnArcher (int spawnX, int spawnY, int ammount){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcRangedNPC[i].alive && d < ammount) {
                npcRangedNPC[i].alive = true;
                npcRangedNPC[i].npcX = spawnX;
                npcRangedNPC[i].npcY = spawnY;
                d++;
            }
        }
    }public void spawnWizard (int spawnX, int spawnY, int ammount){
        int d = 0;
        for (int i = 0;i<ammount;i++){
            if (!npcWizard[i].alive && d < ammount) {
                npcWizard[i].alive = true;
                npcWizard[i].npcX = spawnX;
                npcWizard[i].npcY = spawnY;
                d++;
            }
        }
    }
    public void draw (Canvas canvas){
        for (int i = 0; i<size;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].draw(canvas);
            }if (npcRangedNPC[i].alive){
                npcRangedNPC[i].draw(canvas);
            }if (npcWizard[i].alive){
                npcWizard[i].draw(canvas);
            }
        }
    }
    public void update(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].update(deltaTime);
            }if (npcRangedNPC[i].alive){
                npcRangedNPC[i].update(deltaTime);
            }if (npcWizard[i].alive){
                npcWizard[i].update(deltaTime);
            }
        }
    }
    public void physics(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].physics(deltaTime);
            }if (npcRangedNPC[i].alive){
                npcRangedNPC[i].physics(deltaTime);
            }if (npcWizard[i].alive){
                npcWizard[i].physics(deltaTime);
            }
        }
    }
}
