
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class AreaEffect extends GameObject{

    int effect; //Effect on the health on Destroyables inside area
    boolean effectApplied;
    int playSpeed = 20;

    int sound;

    //Physics
    float forceAdd, angularVelocityAdd;
    ArrayList<Destroyable> effected = new ArrayList<Destroyable>();



    public AreaEffect(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);
        visible = false;


    }

    public void init(float width, float height,float radius, int effect, float forceAdd, int playSpeed){
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.effect = effect;
        this.forceAdd = forceAdd;
        this.playSpeed = playSpeed;
    }

    //Show effect on screen and play sound effect when triggered
    public void onTrigger(Vector2 p){
        position = p;
        paint.setAlpha(255);
        visible = true;
        effectApplied = false;
        System.out.println("attack");
        Game.instance.soundEffects.play(sound,(radius/500)/2,SoundEffects.MAXSTREAMS, 300f/radius);
        effected.clear();
    }

    @Override
    public void draw(Canvas canvas) {


        //AExploding animation
        if(visible) {
            //Fade
            int alpha = paint.getAlpha() - (int)(playSpeed);
            if (alpha > 0) {
                paint.setAlpha(alpha);
            } else {
                visible = false;

            }
            //Shake
            drawDisplacement.y = height * ((float) Math.random() - 0.5f) / 50;
            drawDisplacement.x = width * ((float) Math.random() - 0.5f) / 50;


            //Expand
            //setWidth(width+radius/20);
            rotation+=90;
            super.draw(canvas);
        }
    }

    @Override
    protected void onCollision(GameObject other) {

        //Apply effect on collided Destroyables
        try {
            if(!effected.contains(other)) {
                ((Destroyable) other).onDamage(effect, 0, 0);
                effected.add((Destroyable) other);
                Vector2 dir = (other.position.sub(position)).getNormal();
                setDir(dir);

                //Apply spin
                if(Math.abs(angularVelocityAdd)>0 && other.simulated&&other.radius>0){

                    other.angularVelocity += (float)Math.signum(dir.x)*angularVelocityAdd/other.radius;
                }

                //Launch into the air
                if (Math.abs(forceAdd)>0 && other.simulated){
                    dir = other.position.sub(position).getNormal();
                    other.setVelocity(dir.multiply(forceAdd));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Physics



    }

    public boolean inEffectRange(Destroyable other){
        return Vector2.distance(other.position, position) < radius+other.radius;
    }

    public void setTriggerSound(int sound){
        this.sound = sound;
    }
}
