package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import java.util.Collection;

public class Gold extends GameObject{
    float spawnSpeed = 1f/2;
    float time;
    float fadeTime = 40;
    boolean fromDragon = false;

    double phase;
    public Gold(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);
        simulated = true;
        bounce = 0.6f;
        friction = 0.8f;
        width = GameView.instance.screenWidth/70;
        height = width;
        phase = Math.random()*Math.PI*2;
        gravity = 1f/2;
    }

    public  void spawn(Vector2 p) {
        spawn(p,false);
    }
    public  void spawn(Vector2 p, boolean fromDragon){
        this.fromDragon = fromDragon;
        position = p;
        if(!fromDragon) {
            direction = new Vector2((float) (Math.random() - 0.5f) / 10, (float) (Math.random() - 0.5f)).getNormal();
        }
        else {
            direction = new Vector2((float) (Math.random() - 0.5f) / 10,0).getNormal();
        }
        speed = spawnSpeed*((float)Math.random());
        time = 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        scaleX = (float) Math.abs(Math.cos(time +phase)/2)+0.5f;
        scaleY = 1;
        time+=deltaTime/800;
        int c = (int) (255);
        c *= (1+scaleX)/2;
        paint.setColorFilter(new LightingColorFilter(Color.rgb(c,c,c),0));

        if(time > fadeTime*0.75){
            paint.setAlpha((int)(Math.random()*255));
            if(time > fadeTime){
                GoldPool.instance.collectedGold(this);
            }
        }
    }

    @Override
    public void physics(float deltaTime) {
        super.physics(deltaTime);
        if (GameView.instance.player.inReach(position) && !fromDragon) {
            GoldPool.instance.collectedGold(this);
            GameView.instance.player.collectedGold();
            System.out.println("collected");

        }
        if(fromDragon) {
            GameView.instance.lair.attractGold(this);
        }

    }


}
