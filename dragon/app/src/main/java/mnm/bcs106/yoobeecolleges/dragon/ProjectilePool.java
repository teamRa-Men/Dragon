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
            Projectile newArrow = new Projectile(arrowSprite,0.9f,0.5f);

            arrowPool.add(newArrow);
        }

        magicSprite =  BitmapFactory.decodeResource(GameView.instance.getResources(),R.drawable.projectile);

        for (int i = 0; i < maxMagic; i++) {
            Projectile newMagic = new Projectile(magicSprite,0.9f,0.5f);
            magicPool.add(newMagic);
        }
    }
    public void shootArrow(int x, int y, float speed, float dx, float dy, int damage){

        if(arrowPool.size()>0) {
            Projectile arrow = arrowPool.get(0);
            arrow.shoot(x,y,speed,dx,dy,true);
            arrowPool.remove(0);
            activeArrows.add(arrow);
            arrow.damage = damage;
        }

    }

    public void shootMagic(int x, int y, float speed, float dx, float dy, int damage){

        if(magicPool.size()>0) {
            Projectile magic = magicPool.get(0);
            magic.shoot(x,y,speed,dx,dy,false);
            magicPool.remove(0);
            activeMagic.add(magic);
            magic.damage = damage;


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
                arrow.init();
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
                magic.init();
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

