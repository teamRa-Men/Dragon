
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;


public class Dragon extends Character {
    SpriteRunningAnim[] runningAnims;
    SpriteIdleAnim idleAnim, stunAnim;
    float deltaTime;

    RectF dodgeStart;
    Vector2 dodgeDir;
    int dodgeDistance = 150;
    ActionController dodgeController;

    Rect dodgeFrame;

    FootStep[] footSteps = new FootStep[10];
    float distanceTravelled = 0;
    int stepFoot = 1;



    public Dragon(Bitmap sprite, float offsetX, float offsetY,int width, int height, AreaEffect effect) {
        super(sprite, offsetX, offsetY, effect);
        this.width = width;
        this.height = height;
        friction = 0.9f;
        facing = true;
        dodgeController = new ActionController(0,100,500);
        setAttackController(0,100,100);
        setRunningAnim();
        effect.setPos(position);
        effect.setParent(this);
    }


    public void attack(Character enemy){
        if(speed>0) {
            if (attackController.ready) {
                target = null;
                if (detection.inDetectRange(enemy)) {
                    effect.onTrigger(getCenter().add(direction.multiply(radius)));
                }
            }
        }
    }

    @Override
    public void onDamage(float damage, float dx, float dy) {
        if(!stunController.performing) {
            super.onDamage(damage, dx, dy);
        }
        stunController.triggerAction();
    }

    public void setRunningAnim(){

        SpriteSheet runningEast = new SpriteSheet(R.drawable.running_spritesheet,12);
        SpriteSheet runningWest = new SpriteSheet(R.drawable.running_spritesheet_flipped,12);
        SpriteSheet runningSouth = new SpriteSheet(R.drawable.running_spritesheet_front,10);
        SpriteSheet runningNorth = new SpriteSheet(R.drawable.running_spritesheet_back,10);
        SpriteSheet idle = new SpriteSheet(R.drawable.sprite_standing,4);
        SpriteSheet stunned = new SpriteSheet(R.drawable.sprite_stunned,4);

        runningAnims = new SpriteRunningAnim[4];
        runningAnims[0] = new SpriteRunningAnim(Game.instance.context, (int)width, (int)height,500,runningEast);
        runningAnims[1] = new SpriteRunningAnim(Game.instance.context, (int)width, (int)height,500,runningSouth);
        runningAnims[2] = new SpriteRunningAnim(Game.instance.context, (int)width, (int)height,500,runningWest);
        runningAnims[3] = new SpriteRunningAnim(Game.instance.context, (int)width, (int)height,500,runningNorth);

        idleAnim = new SpriteIdleAnim(Game.instance.context, (int)width, (int)height,1000,idle);
        stunAnim = new SpriteIdleAnim(Game.instance.context, (int)width, (int)height,1000,stunned);
    }


    @Override
    public void update(float deltaTime) {

        this.deltaTime = deltaTime;

        if(!stunController.performing)
        dodgeController.update(deltaTime);

        stunController.update(deltaTime);

    }
    public void moveBy(Vector2 moveBy){
        if(moveBy == null){
            speed = 0;
            return;
        }
        if(!stunController.performing) {
            float magnitude = moveBy.getLength();
            if(magnitude > 0.1){
                speed = Math.min(magnitude,maxMoveSpeed);
                setDir(moveBy);
            }
        }

    }

    public void move(Vector2 moveTo){
        if(moveTo == null){
            speed = 0;
            return;
        }
        if(!stunController.performing) {
            moveTo = moveTo.sub(position).sub(new Vector2(0,50));
            float dist = moveTo.getLength();
            //System.out.println(dist);
            if (dist > 10) {
                setDir(moveTo);
            }
            if (!dodgeController.performing) {
                simulated = true;
                if (dist < 10) {
                    speed = 0;
                } else {
                    dist = Math.min(dist/GameView.instance.screenHeight*4, 1);
                    speed = dist*maxMoveSpeed;
                }
            } else {
                simulated = false;
            }
        }
    }
    public void dodge(Vector2 move){
        dodgeFrame = null;
        dodgeStart = getBounds();
        Vector2 p = position;
        position = position.add(move.getNormal().multiply(dodgeDistance));
        dodgeDir = position.sub(p).getNormal();
        speed = 0;
    }

    @Override
    public void physics(float deltaTime) {
        if(!destroyed)
        super.physics(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        Paint stepPaint = new Paint();
        stepPaint.setColor(Color.BLACK);

        distanceTravelled+=speed*deltaTime;

        if(distanceTravelled > width/2){
            distanceTravelled = 0;

            for(int i = 0; i < footSteps.length;i++){
                if(i == footSteps.length-1){

                    Vector2 stepDisp = new Vector2(0,0);
                    stepDisp = stepDisp.add(new Vector2(-direction.y,direction.x).multiply(stepFoot*3*(direction.y*direction.y+1f)));
                    Vector2 setPos = new Vector2(0,(1-offset.y)*height).add(position.sub(new Vector2(0,(1-offset.y)*height)));
                    footSteps[footSteps.length-1]=new FootStep(setPos.add(stepDisp),255);
                    stepFoot = -stepFoot;

                }
                else {
                    footSteps[i] = footSteps[i + 1];


                }

            }
        }
        for(int i = 0; i < footSteps.length;i++) {
            if(footSteps[i]!=null) {
                //stepPaint.setAlpha(footSteps[i].alpha);

                //footSteps[i].alpha-=5;
                canvas.drawCircle(footSteps[i].x+GameView.instance.cameraDisp.x, footSteps[i].y, 5, stepPaint);
            }
        }



        //stepPaint.setAlpha((int)(40f/255*paint.getAlpha()));


        //canvas.drawOval(position.x-width/4, position.y-width/12, position.x+width/4,position.y+width/6,stepPaint);

        float bearing = rotation+45;
        while(bearing >360){
            bearing -= 360;
        }
        int animIndex = (int)Math.min(bearing/90,3);
        if(visible) {
            Paint p = new Paint();
            int c = (int) Math.max(health / maxHealth * 100 + 50,0);
            p.setColorFilter(new LightingColorFilter(Color.rgb( 155, c, c), 0));
            p.setAlpha(paint.getAlpha());

            RectF drawTo = getBounds();
            drawTo.offset(GameView.instance.cameraDisp.x,0);
            if(!destroyed) {

                if (!stunController.performing) {
                    if (speed > 0.05f || dodgeController.performing) {
                        Rect frame;

                        if (animIndex == 2) {
                            frame = runningAnims[animIndex].draw(canvas, drawTo, -speed / maxMoveSpeed, deltaTime, p);
                        } else {
                            frame = runningAnims[animIndex].draw(canvas, drawTo, speed / maxMoveSpeed, deltaTime, p);
                        }
                        if (dodgeController.performing) {
                            if (dodgeFrame == null) {
                                dodgeFrame = frame;
                            }
                            for (float i = 1; i < 5; i++) {
                                Vector2 dv = dodgeDir.multiply(dodgeDistance * i / 5);
                                float top = dodgeStart.top + dv.y;
                                float left = dodgeStart.left + dv.x;
                                p.setAlpha((int) (i * i / 25 * 255));
                                RectF dst = new RectF(left, top, left + width, top + height);
                                canvas.drawBitmap(runningAnims[animIndex].anim.spriteSheet, dodgeFrame, dst, p);
                            }
                        }
                    } else {
                        idleAnim.draw(canvas, drawTo, animIndex, deltaTime, p);
                    }
                } else {
                    stunAnim.draw(canvas, drawTo, animIndex, deltaTime, p);
                    drawDisplacement.y = height * ((float) Math.random() - 0.5f) / 50;
                    drawDisplacement.x = width * ((float) Math.random() - 0.5f) / 20;
                }
            }
            else{
                stunAnim.draw(canvas, drawTo, animIndex, deltaTime, p);
                drawDisplacement.y += height * ((float) Math.random() - 0.5f) / 20;
                drawDisplacement.x += width * ((float) Math.random() - 0.5f) / 10;
            }
        }
    }
    @Override
    protected void destroyAnim(Canvas canvas) {
        if(visible) {
            //Fading animation
            int alpha = paint.getAlpha() - 5;
            if (alpha > 0) {
                paint.setAlpha(alpha);
            } else {
                visible = false;
            }

            //Shaking animation
            drawDisplacement.y = height * ((float) Math.random() - 0.5f) / 50;
            drawDisplacement.x = width * ((float) Math.random() - 0.5f) / 20;

        }
    }


}

class BodySegment extends GameObject{
    GameObject follow;
    public BodySegment(Bitmap sprite, float offsetX, float offsetY, GameObject follow) {
        super(sprite, offsetX, offsetY);
        facing = true;
        this.follow = follow;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 disp = follow.position.sub(position);
        setDir(disp);
        speed = disp.getLength();
    }
}

class FootStep{
    public int x, y;
    public int alpha;
    public int foot;
    public FootStep(Vector2 position, int alpha){
        x = (int)position.x;
        y = (int)position.y;
        this.alpha = alpha;

    }
}


