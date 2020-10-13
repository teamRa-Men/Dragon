package mnm.bcs106.yoobeecolleges.lifeofdragon;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.lang.reflect.Array;

public class NPC_Pool {
    Wooloo[] npcWooloo = new Wooloo[10];
    public NPC_Pool(){
        for(int i = 0 ;i < 10; i++){
            npcWooloo[i] = new Wooloo(BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.wooloo),0,0,0.1f,100,GameView.instance.cameraSize/20,GameView.instance.cameraSize/20,500);
        }
    }
    public void spawnWooloo (int spawnX, int spawnY){
        for (int i = 0;i<10;i++){
            if (!npcWooloo[i].alive) {
                npcWooloo[i].alive = true;
                npcWooloo[i].npcX = spawnX;
                npcWooloo[i].npcY = spawnY;
            }
        }
    }
    public void draw (Canvas canvas){
        for (int i = 0; i<10;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].draw(canvas);
            }
        }
    }
    public void update(float deltaTime){
        for (int i = 0; i<10;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].update(deltaTime);
            }
        }
    }
    public void physics(float deltaTime){
        for (int i = 0; i<10;i++){
            if (npcWooloo[i].alive){
                npcWooloo[i].physics(deltaTime);
            }
        }
    }
}
