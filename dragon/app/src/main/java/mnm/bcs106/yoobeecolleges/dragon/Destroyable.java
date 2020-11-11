
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;

public class Destroyable extends GameObject{
    public int maxHealth = 100;
    protected float health = maxHealth; // Health reduced on damaged. Destroyed when health < 0

    boolean destroyed = false;
    int destroyedSound = -1, damagedSound = -1;


    public Destroyable(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);

    }

    @Override
    public void draw(Canvas canvas) {
        if(destroyed){
            destroyAnim(canvas);
        }
        super.draw(canvas);
    }


    public void onDamage(float damage){
        //On damage apply effect on health
        health -=damage;

        //Sound effect on being hit
        if(damagedSound>-1 && health >0){
            Game.instance.soundEffects.play(damagedSound,1f,1,1);
        }

        //Turn red when health is low
        int healthRatio = (int)Math.min(255*(health/maxHealth+0.5f),255);
        ColorFilter colorFilter = new LightingColorFilter(Color.rgb(255,healthRatio,healthRatio),0);
        paint.setColorFilter(colorFilter);

        //Destroy when health is depleted
        if(health<0){
            onDestroyed();
        }
    }



    //Apply states and sound when destroyed
    public void onDestroyed(){

        if(destroyedSound>-1 && destroyed == false){
            Game.instance.soundEffects.play(destroyedSound,0.5f,1,2);
        }
        destroyed = true;
        simulated = false;
    }

    protected void destroyAnim(Canvas canvas){
        //Shake when destroyed
        drawDisplacement = new Vector2((float)(Math.random()-0.5), (float)(Math.random()-0.5)).multiply(radius/100);
    }


    //Refined collision after bounding box collision
    @Override
    protected boolean collisionCheck(GameObject other) {
        //System.out.println(center + " " + position);
        return Vector2.distance(position,other.position)< radius+other.radius;
    }

    //Mutator methods

    public void setCircleCollider(float r){
        if(r>0) {
            radius = r;
        }
    }

    public void setDestroyedSound(int destroyedSound) {
        this.destroyedSound = destroyedSound;
    }

    public void setDamagedSound(int damagedSound) {
        this.damagedSound = damagedSound;
    }




}
