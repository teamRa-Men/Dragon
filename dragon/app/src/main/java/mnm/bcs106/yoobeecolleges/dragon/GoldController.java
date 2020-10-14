package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class GoldController {
    int maxGold = 10;
    ArrayList<Gold> goldPool = new ArrayList<Gold>();
    ArrayList<Gold> activeGold = new ArrayList<Gold>();
    Bitmap goldSprite;
    public static GoldController instance;

    public GoldController(){
        if(instance == null){
            instance = this;
        }
        goldSprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.gold_coin);
        for (int i = 0; i < maxGold; i++) {
            Gold newGold = new Gold(goldSprite,0.5f,0.5f);
            goldPool.add(newGold);
        }
    }
    public void spawnGold(int x, int y, int amount){
        for (int i = 0; i < amount; i++) {
            if(goldPool.size()>0) {
                Gold goldSpawned = goldPool.get(0);
                goldPool.remove(0);
                activeGold.add(goldSpawned);
                goldSpawned.spawn(new Vector2(x,y));

            }
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
