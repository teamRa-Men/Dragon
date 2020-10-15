package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class NPC_Pool {
    int size = 24;

    Wooloo[] npcWooloo = new Wooloo[size];
    RangedNPC[] npcRangedNPC = new RangedNPC[size];
    public NPC_Pool(){
        for(int i = 0 ;i < size; i++){
            npcWooloo[i] = new Wooloo(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),0.1f,100,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,500);
            npcRangedNPC[i] = new RangedNPC(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),0.1f,300,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,100);
        }
    }
    public void spawnWooloo (int spawnX, int spawnY, int ammount){
        for (int i = 0;i<ammount;i++){
            if (!npcWooloo[i].alive) {
                npcWooloo[i].alive = true;
                npcWooloo[i].npcX = spawnX;
                npcWooloo[i].npcY = spawnY;
            }
        }
    }
    public void spawnArcher (int spawnX, int spawnY, int ammount){
        for (int i = 0;i<ammount;i++){
            if (!npcRangedNPC[i].alive) {
                npcRangedNPC[i].alive = true;
                npcRangedNPC[i].npcX = spawnX;
                npcRangedNPC[i].npcY = spawnY;
            }
        }
    }
    public void draw (Canvas canvas){
        for (int i = 0; i<size;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].draw(canvas);
            }if (npcRangedNPC[i].alive){
                npcRangedNPC[i].draw(canvas);
            }
        }
    }
    public void update(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].update(deltaTime);
            }if (npcRangedNPC[i].alive){
                npcRangedNPC[i].update(deltaTime);
            }
        }
    }
    public void physics(float deltaTime){
        for (int i = 0; i<size;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].physics(deltaTime);
            }if (npcRangedNPC[i].alive){
                npcRangedNPC[i].physics(deltaTime);
            }
        }
    }
}
