
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

public class Projectile extends GameObject {
    int damage = 5;
    float coolDown = 5000; //This determines when the projectile returns to the pool after being shot
    float timeSinceShot; //When this is larger than the coolDown the projectile returns to pool
    public boolean returnToPool = false;
    public int type;//0=arrow, 1 = magic, 2 = spear
    public static int MAGIC=1, ARROW=0, SPEAR=2;
    float explosionTime = -1;

    public Projectile(Bitmap sprite, float offsetX, float offsetY,int type) {
        super(sprite, offsetX, offsetY);
        bounce = 0;
        this.type = type;
        init();
        paint.setColor(Color.WHITE);

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
        radius = GameView.instance.screenWidth/30;
        if(type==MAGIC && explosionTime > 0){
            canvas.drawCircle((int)(position.x+GameView.instance.cameraDisp.x+radius/10*Math.random()),(int)(position.y+radius/10*Math.random()),radius,paint);
        }
        else{
            super.draw(canvas);
        }

    }

    @Override
    public void physics(float deltaTime) {
        if(simulated) {
            super.physics(deltaTime);
            timeSinceShot += deltaTime;
            if (parent == null) {
                if(type != SPEAR){
                     if(GameView.instance.player.fireBreath.projectileCollision(this)){
                        timeSinceShot = Math.max(0.9f*coolDown,timeSinceShot);
                    }
                }
                if(timeSinceShot < coolDown * 0.75f){
                    if (GameView.instance.player.projectileCollision(this)) {
                        GameView.instance.player.onDamage(damage);
                        timeSinceShot = coolDown * 0.75f;
                        if(type == MAGIC){
                            explosionTime = 1000;
                            parent = null;
                            speed = 0;
                        }
                    }
                    else{
                        if (type == MAGIC) {
                            setDir(direction.add(GameView.instance.player.aimFor().add(position.multiply(-1f)).multiply(0.2f)));
                        }
                    }
                }


            }
        }
    }


    public void shoot(int x, int y, float speed, float dx, float dy, float gravity){

        position = new Vector2(x,y);
        this.speed = speed;
        timeSinceShot = 0;

        if(Scene.instance.timeOfDay/Scene.instance.dayLength > 0.5){
            if(type == ARROW) {
                dx += (Math.random() - 0.5f);
                dy += (Math.random() - 0.5f);
            }
            else if (type == MAGIC){
                this.speed*=2f;
            }
        }
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
        speed = 0;

        switch (type){
            case 0: coolDown = 5000;
                width = GameView.instance.screenWidth/30;
                height = GameView.instance.screenWidth/120;
                break;
            case 1: coolDown = 5000;
                width = GameView.instance.screenWidth/15;
                height = GameView.instance.screenWidth/15;
                break;
            case 2: coolDown = 5000;
                width = GameView.instance.screenWidth/10;
                height = GameView.instance.screenWidth/120;
                break;
        }
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
