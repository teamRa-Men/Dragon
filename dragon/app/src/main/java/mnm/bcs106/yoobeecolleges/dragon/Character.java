
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class Character extends Destroyable {

        float maxMoveSpeed;
        ActionController attackController;
        ActionController stunController;


        float friction = 0f;

        float UIalpha = 0;
        String UIString = "";


        GameObject target = null; //Target to move towards
        Vector2 targetPosition = Vector2.zero;

        public Character(Bitmap sprite, float offsetX, float offsetY) {
            super(sprite, offsetX, offsetY);
            stunController = new ActionController(0,200,0);
        }

        public void setAttackController(int chargeTime, int performTime, int coolDownTime){
            attackController = new ActionController(chargeTime,performTime,coolDownTime);
        }

        public void setStunController(int chargeTime, int performTime, int coolDownTime){
            stunController = new ActionController(chargeTime,performTime,coolDownTime);
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
                visible = SharedAnim.fade(paint);
                drawDisplacement = SharedAnim.shake(width,height,1/50,1/20);
                //Shaking animation

            }
        }

    @Override
    public void onDamage(float damage) {
        super.onDamage(damage);
        stunController.triggerAction();
    }

    protected void onCollision(GameObject other){
        Vector2 disp = position.sub(other.position);
        position = (disp.getNormal().multiply(other.radius + radius+10)).add(other.position);
        //setVelocity(disp.multiply(1f/Game.instance.screenWidth));
    }
}




