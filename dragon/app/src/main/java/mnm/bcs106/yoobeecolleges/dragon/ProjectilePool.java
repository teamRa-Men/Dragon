package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

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

    int maxSpear= 5;
    ArrayList<Projectile> spearPool = new ArrayList<Projectile>();
    ArrayList<Projectile> activeSpear = new ArrayList<Projectile>();
    Bitmap spearSprite;

    public static ProjectilePool instance;

    public ProjectilePool(){

        instance = this;
        Bitmap sheet = SpriteManager.instance.NPCSheet;

        Rect r = SpriteManager.instance.getNPCSpriteRect("Arrow");
        arrowSprite =Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        for (int i = 0; i < maxArrows; i++) {
            Projectile newArrow = new Projectile(arrowSprite,0.6f,0.5f,Projectile.ARROW);
            arrowPool.add(newArrow);
        }

        r = SpriteManager.instance.getNPCSpriteRect("Magic");
        magicSprite =Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        for (int i = 0; i < maxMagic; i++) {
            Projectile newMagic = new Projectile(magicSprite,0.5f,0.5f, Projectile.MAGIC);
            magicPool.add(newMagic);
        }

        r = SpriteManager.instance.getNPCSpriteRect("Spear");
        spearSprite =Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        for (int i = 0; i < maxSpear; i++) {
            Projectile newSpear = new Projectile(spearSprite,0.7f,0.5f, Projectile.SPEAR);
            spearPool.add(newSpear);
        }
    }
    public void shootArrow(int x, int y, float speed, float dx, float dy, int damage){
        SoundEffects.instance.play(SoundEffects.ARROW);

        if(arrowPool.size()>0) {
            Projectile arrow = arrowPool.get(0);
            arrow.shoot(x,y,speed,dx,dy,1f/4);
            arrowPool.remove(0);

            arrow.damage = damage;
            activeArrows.add(arrow);
        }

    }

    public void shootMagic(int x, int y, float speed, float dx, float dy, int damage){
        SoundEffects.instance.play(SoundEffects.MAGIC);

        if(magicPool.size()>0) {
            Projectile magic = magicPool.get(0);
            magic.shoot(x,y,speed,dx,dy,0);
            magicPool.remove(0);

            magic.damage = damage;
            activeMagic.add(magic);
        }

    }

    public void shootSpear(int x, int y, float speed, float dx, float dy, int damage){
        SoundEffects.instance.play(SoundEffects.SPEAR);
        if(spearPool.size()>0) {
            Projectile spear = spearPool.get(0);
            spear.shoot(x,y,speed,dx,dy,1f/4);
            spearPool.remove(0);

            spear.damage = damage;
            activeSpear.add(spear);
        }

    }

    public void update(float deltaTime){
        for (int i = 0; i < activeArrows.size(); i++) {
            activeArrows.get(i).update(deltaTime);
        }
        for (int i = 0; i < activeMagic.size(); i++) {
            activeMagic.get(i).update(deltaTime);
        }
        for (int i = 0; i < activeSpear.size(); i++) {
            activeSpear.get(i).update(deltaTime);
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

        for (int i = 0; i < activeSpear.size(); i++) {
            Projectile spear = activeSpear.get(i);
            if(spear.returnToPool){
                activeSpear.remove(spear);
                spearPool.add(spear);
                spear.returnToPool=false;
                spear.init();
            }
            else {
                spear.physics(deltaTime);
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
        for (int i = 0; i < activeSpear.size(); i++) {
            activeSpear.get(i).draw(canvas);
        }
    }
}

