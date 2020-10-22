package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class ProjectilePool {

    int maxArrows = 20;
    ArrayList<Projectile> arrowPool = new ArrayList<Projectile>();
    ArrayList<Projectile> activeArrows = new ArrayList<Projectile>();
    Bitmap arrowSprite;

    int maxMagic = 5;
    ArrayList<Projectile> magicPool = new ArrayList<Projectile>();
    ArrayList<Projectile> activeMagic = new ArrayList<Projectile>();
    Bitmap magicSprite;

    public static ProjectilePool instance;

    public ProjectilePool(){

            instance = this;

        arrowSprite =  BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.projectile);

        for (int i = 0; i < maxArrows; i++) {
            Projectile newArrow = new Projectile(arrowSprite,0.7f,0.5f);

            arrowPool.add(newArrow);
        }

        magicSprite =  BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.projectile);

        for (int i = 0; i < maxMagic; i++) {
            Projectile newMagic = new Projectile(magicSprite,0.7f,0.5f);
            newMagic.itIsmagic(true);
            magicPool.add(newMagic);
        }
    }
    public void shootArrow(int x, int y, float speed, float dx, float dy, int damage){

        if(arrowPool.size()>0) {
            Projectile arrow = arrowPool.get(0);
            arrow.shoot(x,y,speed,dx,dy,1f/8);
            arrowPool.remove(0);

            arrow.damage = damage;
            activeArrows.add(arrow);
        }

    }

    public void shootMagic(int x, int y, float speed, float dx, float dy, int damage){

        if(magicPool.size()>0) {
            Projectile magic = magicPool.get(0);
            magic.shoot(x,y,speed,dx,dy,0);
            magicPool.remove(0);

            magic.damage = damage;
            activeMagic.add(magic);
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
            if(arrow.returnToPool){
                activeArrows.remove(arrow);
                arrowPool.add(arrow);
                arrow.returnToPool=false;
                arrow.init();
            }
            else {
                arrow.physics(deltaTime);
            }
        }
        for (int i = 0; i < activeMagic.size(); i++) {
            Projectile magic = activeMagic.get(i);
            if(magic.returnToPool){
                activeMagic.remove(magic);
                magicPool.add(magic);
                magic.returnToPool=false;
                magic.init();
            }
            else {
                magic.physics(deltaTime);
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

