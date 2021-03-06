package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

public class GoldPool {
    int maxGold = 150;
    ArrayList<Gold> goldPool = new ArrayList<Gold>();
    ArrayList<Gold> activeGold = new ArrayList<Gold>();
    Bitmap goldSprite;
    public static GoldPool instance;

    public GoldPool(){
        instance = this;
        Bitmap sheet = SpriteManager.instance.environmentSheet;
        Rect r = SpriteManager.instance.getEnvironmentSpriteRect("GoldCoin");
        goldSprite =Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        for (int i = 0; i < maxGold; i++) {
            Gold newGold = new Gold(goldSprite,0.5f,0.5f);
            goldPool.add(newGold);
        }
    }
    public void spawnGold(int x, int y, int amount) {
        spawnGold(x, y, amount, false);
    }
    public void spawnGold(int x, int y, int amount, boolean fromDragon){
        //amount*=10;
        for (int i = 0; i < amount; i++) {
            if(goldPool.size()>0) {
                Gold goldSpawned = goldPool.get(0);
                goldPool.remove(0);
                activeGold.add(goldSpawned);
                goldSpawned.spawn(new Vector2(x,y), fromDragon);

            }
        }
        if(Math.random()<0.1){
            Music.instance.playThemeMusic(false);

        }
    }

    public void collectedGold(Gold collected){
        activeGold.remove(collected);
        goldPool.add(collected);
    }

    public void update(float deltaTime){
        for (int i = 0; i < activeGold.size(); i++) {
            activeGold.get(i).update(deltaTime);
        }
    }

    public void physics(float deltaTime){
        for (int i = 0; i < activeGold.size(); i++) {
            activeGold.get(i).physics(deltaTime);
        }
    }

    public void draw(Canvas canvas){
        for (int i = 0; i < activeGold.size(); i++) {
            activeGold.get(i).draw(canvas);
        }
    }
}
