package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class ProjectilePool {

    int maxArrows = 10;
    ArrayList<Projectile> arrowPool = new ArrayList<Projectile>();
    ArrayList<Projectile> activeArrows = new ArrayList<Projectile>();
    Bitmap arrowSprite;
    public static ProjectilePool instance;

    public ProjectilePool(){
        if(instance == null){
            instance = this;
        }
        arrowSprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.gold_coin);
        for (int i = 0; i < maxArrows; i++) {
            Projectile newArrow = new Projectile(arrowSprite,0f,0.5f);
            arrowPool.add(newArrow);
        }
    }
    public void shootArrow(int x, int y, float speed, float dx, float dy){

        if(arrowPool.size()>0) {
            Projectile arrow = arrowPool.get(0);
            arrowPool.remove(0);
            activeArrows.add(arrow);
            arrow.shoot(x,y,speed,dx,dy);

        }

    }

    public void update(float deltaTime){
        for (int i = 0; i < activeArrows.size(); i++) {
            activeArrows.get(i).update(deltaTime);
        }
    }

    public void physics(float deltaTime){
        for (int i = 0; i < activeArrows.size(); i++) {
            Projectile arrow = activeArrows.get(i);
            if(arrow.visible) {
                arrow.physics(deltaTime);
            }
            if(arrow.returnToPool){
                activeArrows.remove(arrow);
                arrowPool.add(arrow);
                arrow.returnToPool=false;
            }

        }
    }

    public void draw(Canvas canvas){
        for (int i = 0; i < activeArrows.size(); i++) {
            activeArrows.get(i).draw(canvas);
        }
    }
}

