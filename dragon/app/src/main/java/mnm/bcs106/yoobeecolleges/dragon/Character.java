
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class Character extends Destroyable {
        AreaEffect effect; // Area affect on trigger

        float maxMoveSpeed;

        Detect detection;
        Chase chasing;

        ActionController attackController;
        ActionController stunController;

        public int spawningSoundEffect = -1;
        float friction = 0f;

        float UIalpha = 0;
        String UIString = "";


        GameObject target = null; //Target to move towards
        Vector2 targetPosition = Vector2.zero;

        public Character(Bitmap sprite, float offsetX, float offsetY, AreaEffect effect) {
            super(sprite, offsetX, offsetY);
            this.effect = effect;
            stunController = new ActionController(0,200,0);
        }

        public void setAttackController(int chargeTime, int performTime, int coolDownTime){
            attackController = new ActionController(chargeTime,performTime,coolDownTime);
        }

        public void setStunController(int chargeTime, int performTime, int coolDownTime){
            stunController = new ActionController(chargeTime,performTime,coolDownTime);
        }


        public void setDetection(int detectRange, int detectAngle) {
            detection = new Detect(this, detectRange, detectAngle);
        }

        public void setChasing(float distFromTarget) {
            chasing = new Chase(distFromTarget);
        }


        public void setTarget(GameObject target){
            this.target = target;
        }

        //Intitialize states and attributes on spawning
        public void init(float x, float y, float size, float radius, float maxMoveSpeed, int maxHealth) {
            setPos(x,y);
            setWidth(size);
            setCircleCollider(radius);

            this.maxMoveSpeed = maxMoveSpeed;
            this.maxHealth = maxHealth;

            health = maxHealth;
            speed = 0;
            rotation = 0;
            direction = new Vector2(0,0);

            //Visuals
            paint.setAlpha(255);
            ColorFilter colorFilter = new LightingColorFilter(Color.WHITE,0);
            paint.setColorFilter(colorFilter);

            //States
            destroyed = false;
            centerPivot = true;
            simulated = true;
            visible = true;

            if(spawningSoundEffect > -1) {
                SoundEffects.instance.play(spawningSoundEffect);
            }
        }

    public void setSpawningSoundEffect(int spawningSoundEffect) {
        this.spawningSoundEffect = spawningSoundEffect;
    }

    @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);

            if(visible) {
                Paint p = new Paint();
                if(stunController.performing){
                    drawDisplacement.y = height * ((float) Math.random() - 0.5f) / 50;
                    drawDisplacement.x = width * ((float) Math.random() - 0.5f) / 20;
                }

                p.setColor(Color.DKGRAY);
                p.setTextSize(100);
                p.setFakeBoldText(true);
                p.setTextScaleX(1.5f);
                p.setTextAlign(Paint.Align.CENTER);

                //UI fade
                //    UIalpha-=0.1f;
                //    UIalpha = Math.max(UIalpha,0);

                p.setAlpha((int)(UIalpha*255));
                canvas.drawText(UIString, position.x, position.y - height, p);
            }
            effect.draw(canvas);
        }

        public void setUIString(String s){
            UIString = s;
        }


        @Override
        public void update(float deltaTime){

            if( !stunController.performing) {

            }
            stunController.update(deltaTime);
        }

        @Override
        public void physics(float deltaTime) {
            if(stunController.performing) {
                speed *= friction;
            }

            super.physics(deltaTime);
        }

        boolean grounded(){
            return Math.abs(position.y - GameView.instance.groundLevel) < 1;
        }

        @Override
        protected void destroyAnim(Canvas canvas) {
            if(visible) {
                //Fading animation
                visible = FadingAnim.fade(paint);
                drawDisplacement = ShakingAnim.shake(width,height,1/50,1/20);
                //Shaking animation

            }
        }

    @Override
    public void onDamage(float damage, float dx, float dy) {
        super.onDamage(damage, dx, dy);
        stunController.triggerAction();
    }

    protected void onCollision(GameObject other){
        Vector2 disp = position.sub(other.position);
        position = (disp.getNormal().multiply(other.radius + radius+10)).add(other.position);
        //setVelocity(disp.multiply(1f/Game.instance.screenWidth));
    }
}


class ShakingAnim{
    public static Vector2 shake(float width, float height, float maxDispX, float maxDispY){
        float dispY = height * ((float) Math.random() - 0.5f)*maxDispY;
        float dispX = width * ((float) Math.random() - 0.5f)*maxDispX;
        return  new Vector2(dispX, dispY);
    }
}

class FadingAnim{
    public static boolean fade(Paint paint){
        int alpha = paint.getAlpha() - 10;
        if (alpha > 0) {
            paint.setAlpha(alpha);
            return  true;
        } else {
            return false;
        }
    }
}

class Detect{
    int detectRange, detectAngle;
    Character character;

    public Detect (Character character, int detectRange, int detectAngle){
        this.detectAngle = detectAngle;
        this.detectRange = detectRange;
        this.character = character;
    }
    public boolean inDetectRange(Destroyable other){
        Vector2 position = character.getPos();
        Vector2 direction = character.getDir();
        GameObject target =  character.target;

        Vector2 dispOther = other.position.sub(position);
        float distOther = dispOther.getLength();

        if(distOther < detectRange){
            if(target==null) {
                double angle =  Math.acos(dispOther.dot(direction)/(distOther*1));
                angle = Math.abs(Math.toDegrees(angle));
                if (angle < detectAngle) {
                    character.setTarget(other);
                    return true;
                }
            }
            else {
                return true;
            }
        }
        character.setTarget(null);
        return false;
    }
}

class Chase {
    float distFromTarget;
    public Chase(float distFromTarget){
        this.distFromTarget = distFromTarget;
    }

    public boolean chasing(Character character) {
        Vector2 position = character.position;
        GameObject target = character.target;
        float radius = character.radius;
        float maxMoveSpeed = character.maxMoveSpeed;

        Vector2 posToTarget = target.position.sub(position);
        float distance = posToTarget.getLength();
        character.setDir(posToTarget);

        if (distance > radius + distFromTarget) {
            character.setDir(posToTarget);
            character.setSpeed(Math.min(distance / 50, 1) * maxMoveSpeed * posToTarget.getLength());
        } else {
            character.setSpeed(0);
        }
        return distance > radius + distFromTarget;
    }


}

