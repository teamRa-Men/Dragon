package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class ProjectilePool {

    int maxArrows = 100;
    ArrayList<Projectile> arrowPool = new ArrayList<Projectile>();
    ArrayList<Projectile> activeArrows = new ArrayList<Projectile>();
    Bitmap arrowSprite;

    int maxMagic = 100;
    ArrayList<Projectile> magicPool = new ArrayList<Projectile>();
    ArrayList<Projectile> activeMagic = new ArrayList<Projectile>();
    Bitmap magicSprite;

    public static ProjectilePool instance;

    public ProjectilePool(){
        if(instance == null){
            instance = this;
        }
        arrowSprite =  BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.projectile);

        for (int i = 0; i < maxArrows; i++) {
            Projectile newArrow = new Projectile(arrowSprite,1f,0.5f);
            arrowPool.add(newArrow);
        }

        magicSprite =  BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.projectile);

        for (int i = 0; i < maxMagic; i++) {
            Projectile newMagic = new Projectile(magicSprite,1f,0.5f);
            magicPool.add(newMagic);
        }
    }
    public void shootArrow(int x, int y, float speed, float dx, float dy){

        if(arrowPool.size()>0) {
            Projectile arrow = arrowPool.get(0);
            arrowPool.remove(0);
            activeArrows.add(arrow);
            arrow.shoot(x,y,speed,dx,dy,true);

        }

    }

    public void shootMagic(int x, int y, float speed, float dx, float dy){

        if(magicPool.size()>0) {
            Projectile magic = magicPool.get(0);
            magicPool.remove(0);
            activeMagic.add(magic);
            magic.shoot(x,y,speed,dx,dy,false);

        }

    }

    public void update(float deltaTime){
        for (int i = 0; i < activeArrows.size(); i++) {
            activeArrows.get(i).update(deltaTime);
        }
        for (int i = 0; i < activeMagic.size(); i++) {
            activeMagic.get(i).update(deltaTime);
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
        for (int i = 0; i < activeMagic.size(); i++) {
            Projectile magic = activeMagic.get(i);
            if(magic.visible) {
                magic.physics(deltaTime);
            }
            if(magic.returnToPool){
                activeMagic.remove(magic);
                magicPool.add(magic);
                magic.returnToPool=false;
            }
        }
    }

    public void draw(Canvas canvas){
        for (int i = 0; i < activeArrows.size(); i++) {
            activeArrows.get(i).draw(canvas);
        }
        for (int i = 0; i < activeMagic.size(); i++) {
            activeMagic.get(i).draw(canvas);
        }
    }
}

