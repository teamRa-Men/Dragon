package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class GoldController {
    int maxGold = 50;
    ArrayList<Gold> goldPool = new ArrayList<>();
    ArrayList<Gold> activeGold = new ArrayList<>();
    Bitmap goldSprite;

    public GoldController(){
        goldSprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.circle);
        for (int i = 0; i < maxGold; i++) {
            Gold newGold = new Gold(goldSprite,0.5f,0.5f);
            goldPool.add(newGold);
        }
    }
    public void spawnGold(Vector2 position, int amount){
        for (int i = 0; i < amount; i++) {
            Gold goldSpawned = goldPool.get(0);
            goldPool.remove(goldSpawned);
            activeGold.add(goldSpawned);
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
