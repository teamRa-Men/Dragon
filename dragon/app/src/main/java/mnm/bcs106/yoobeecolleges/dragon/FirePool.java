package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;

public class FirePool {
    int maxFire = 10;
    ArrayList<Fire> firePool = new ArrayList<Fire>();
    ArrayList<Fire> activeFire = new ArrayList<Fire>();




    public static FirePool instance;

    public FirePool(){
        instance = this;
        Bitmap sheet = SpriteManager.instance.fireSheet;
        Rect r = SpriteManager.instance.getEnvironmentSprite("Flame1");
        for (int i = 0; i < maxFire; i++) {
            Fire newFire = new Fire();
            firePool.add(newFire);
        }
    }

    public void spawnFire(float x, float y){

            if(firePool.size()>0) {
                Fire fireSpawned = firePool.get(0);
                firePool.remove(0);
                activeFire.add(fireSpawned);
               fireSpawned.spawn(new Vector2(x,y));

            }

    }

    public void extinguished(Fire ex){
        activeFire.remove(ex);
        firePool.add(ex);
    }

    public void update(float deltaTime){
        for (int i = 0; i < activeFire.size(); i++) {
            activeFire.get(i).update(deltaTime);
        }
    }


    public void draw(Canvas canvas){
        for (int i = 0; i < activeFire.size(); i++) {
            activeFire.get(i).draw(canvas);
        }
    }
}

class Fire{
    Rect src[] = new Rect[6];
    Bitmap fireSheet;
    Paint paint = new Paint();
    Paint backPaint = new Paint();
    Vector2 position;
    int alpha;
    boolean active;

    public Fire (){
        fireSheet = SpriteManager.instance.fireSheet;
        src[0] = SpriteManager.instance.getFireSprite("Fire0");
        src[1] = SpriteManager.instance.getFireSprite("Fire1");
        src[2] = SpriteManager.instance.getFireSprite("Fire2");
        src[3] = SpriteManager.instance.getFireSprite("Fire3");
        src[4] = SpriteManager.instance.getFireSprite("Fire4");
        src[5] = SpriteManager.instance.getFireSprite("Fire5");
        paint.setColorFilter(new LightingColorFilter(Game.instance.getResources().getColor(R.color.colorFire),0));
        backPaint.setColorFilter(new LightingColorFilter(Game.instance.getResources().getColor(R.color.colorFireCold),0));
        active = false;
    }

    public void draw(Canvas canvas){
        if(active) {
            //p.setColorFilter(new LightingColorFilter(Color.rgb(health/maxHealth*255, health/maxHealth*255, health/maxHealth*255),0));
            float left = position.x + GameView.instance.cameraDisp.x;
            float top = position.y - GameView.instance.cameraSize/20;
            float bottom = position.y;
            float right = left +GameView.instance.cameraSize/20;
            Rect dst = new Rect((int) left, (int) top, (int) right, (int) bottom);
            canvas.drawBitmap(SpriteManager.instance.fireSheet, src[(int) (Math.random() * 5.9)], dst, backPaint);

            left = position.x + GameView.instance.cameraDisp.x+GameView.instance.cameraSize/30/4;
            top = position.y - GameView.instance.cameraSize/30;
            bottom = position.y;
            right = left +GameView.instance.cameraSize/30;
            dst = new Rect((int) left, (int) top, (int) right, (int) bottom);
            canvas.drawBitmap(SpriteManager.instance.fireSheet, src[(int) (Math.random() * 5.9)], dst, paint);
        }

    }
    public void update(float deltaTime){
        alpha-=5;
        if(alpha<0){
            FirePool.instance.extinguished(this);
            active = false;
        }
        paint.setAlpha(alpha);
        backPaint.setAlpha(alpha);
        System.out.println("fireUpdate" + alpha);
    }

    public void spawn(Vector2 p){
        position = p;
        alpha = 255;
        active = true;
        System.out.println("fireSpawn");
    }
}
