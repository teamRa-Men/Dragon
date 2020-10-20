
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Projectile extends GameObject {
    int damage = 5;
    float pushX, pushY;
    float coolDown = 5000; //This determines when the projectile returns to the pool after being shot
    float timeSinceShot; //When this is larger than the coolDown the projectile returns to pool
    public boolean returnToPool = false;

    public Projectile(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);
        bounce = 0;
        init();

    }

    @Override
    public void draw(Canvas canvas) {


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
        super.draw(canvas);
    }

    @Override
    public void physics(float deltaTime) {

        super.physics(deltaTime);
        timeSinceShot+=deltaTime;
        if(parent == null){
            if(GameView.instance.player.projectileCollision(this)){
                GameView.instance.player.onDamage(damage);
                timeSinceShot = coolDown*0.75f;
            }
        }
    }


    public void shoot(int x, int y, float speed, float dx, float dy, float gravity){

        position = new Vector2(x,y);
        this.speed = speed;
        timeSinceShot = 0;
        setDir(dx,dy);
        rotation = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        simulated = true;
        visible = true;
        parent = null;
        this.gravity = gravity;
        //Game.instance.soundEffects.play(Game.instance.soundEffects.PEW);
    }


    void init(){
        visible=false;
        position = new Vector2(GameView.instance.screenWidth*2,GameView.instance.screenHeight*2);
        centerPivot = false;
        facing = true;
        simulated=false;
        matrix = new Matrix();
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
