
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Projectile extends GameObject {
    int damage = 5;
    float pushX, pushY;
    float coolDown = 5000; //This determines when the projectile returns to the pool after being shot
    float timeSinceShot; //When this is larger than the coolDown the projectile returns to pool
    public boolean returnToPool = false,gravity = false;

    public Projectile(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);
        bounce = 0;
        init();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //If attached parent is destroyed, remove from parent
        if(parent!=null) {
            if (!parent.visible) {
                parent = null;
                visible = false;
            }
        }

        //If shot, count the time since shot. Start fading away just before cool down ends and return to pool
        if(visible) {
            if (timeSinceShot > coolDown*0.75) {


                int alpha = (int) ((coolDown-timeSinceShot)/(coolDown/4)*255);
                paint.setAlpha(alpha);
                if (timeSinceShot > coolDown) {
                    init();
                   returnToPool = true;
                }
            }

        }

    }

    @Override
    public void physics(float deltaTime) {
        super.physics(deltaTime);
        timeSinceShot+=deltaTime;
        //gravity

        if(GameView.instance.player.collision(this)){
            GameView.instance.player.onDamage(damage);
        }
        if(gravity) {
            if (position.y < GameView.instance.getGroundLevel()) {
                setVelocity(getVelocity().x, getVelocity().y + 1f / 2 * deltaTime / 1000);
            } else {
                onGrounded(GameView.instance.getGroundLevel());
            }
        }
    }


    public void shoot(int x, int y, float speed, float dx, float dy, boolean gravity){

        position = new Vector2(x,y);
        this.speed = speed;
        timeSinceShot = 0;
        setDir(dx,dy);
        rotation = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        simulated = true;
        visible = true;
        this.gravity = gravity;
        //Game.instance.soundEffects.play(Game.instance.soundEffects.PEW);
    }


    void init(){
        centerPivot = false;
        facing = true;
        simulated=false;
        visible=false;
        paint.setAlpha(255);
        parent = null;

    }


    //Stop if the projectile hits the ground
    @Override
    protected void onGrounded(float level) {
        //super.onGrounded(level);
        if(speed > 0.1){
            //Game.instance.soundEffects.play(SoundEffects.HIT);
        }
        speed = 0;
    }


}
