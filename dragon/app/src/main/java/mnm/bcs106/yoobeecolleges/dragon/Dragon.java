
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Dragon extends Character {

    int size;
    ArrayList<Segment> segments = new ArrayList<Segment>();
    Wing frontWing, backWing;
    Leg frontLeg, backLeg;
    Arm frontArm, backArm;
    Head head;
    FireBreath fireBreath;
    int bodyStart;
    int bodyEnd;
    public boolean breathingFire, flying;
    float distanceTravelled = 0, upperBound, groundLevel, walkSpeed = 1f/2;
    Segment[] colliders = new Segment[4];


    int goldHolding = 0;
    int attack, maxMana = 100;
    float mana = maxMana;
    float flyingManaCost = 5, fireManaCost = 10, manaRegen=5;
    int attackLevel, healthLevel, manaLevel, speedLevel;

    public Dragon(Bitmap sprite, float offsetX, float offsetY,int width, int height) {
        super(sprite, offsetX, offsetY);
        this.width = width;
        this.height = height;



        friction = 0.99f;
        facing = true;
        upperBound = GameView.instance.screenHeight/10;


        initBody(65);


        setAttackController(0,100,100);


    }

    public void initBody(int size){
        segments.clear();

        this.size = size;
        int cameraSize = GameView.instance.cameraSize;
        radius = (float)cameraSize*size/3000;

        bodyStart = size/9;
        bodyEnd = size/4;
        groundLevel = GameView.instance.groundLevel-1.7f*radius;
        position = new Vector2(GameView.instance.screenWidth/2, groundLevel);

        init(position.x, position.y,radius*2, radius,3f/4, 100);


        int dragonColor = Game.instance.getResources().getColor(R.color.colorDragon);
        for (int i = 0; i < size; i++) {
            if(i < bodyStart) {
                segments.add(new Segment(this, i, (float)Math.pow((float)i / bodyStart*0.5f+0.3f,1) * radius));
            }
            else if(i < (bodyEnd+bodyStart)/2) {
                segments.add(new Segment(this, i, (segments.get(i-1).radius+radius)/2));
            }
            else{
                segments.add(new Segment(this, i, (segments.get(i-1).radius+(float) Math.pow((float) (size - i) /(size - bodyEnd)*0.8f,1.5f) * radius)/2+1));
            }
            float c = Math.min((float)(size-i)/size*10,0.2f)+0.8f;

            segments.get(i).paint.setColorFilter(new LightingColorFilter(Color.rgb((int)(Color.red(dragonColor)*c),
                    (int)(Color.green(dragonColor)*c),
                    (int)(Color.blue(dragonColor)*c)),0));
        }

        frontLeg = new Leg(this, segments.get(bodyEnd+2), true);
        backLeg = new Leg(this, segments.get(bodyEnd+2), false);
        frontArm = new Arm(this, segments.get(bodyStart), true);
        backArm = new Arm(this, segments.get(bodyStart), false);
        frontWing = new Wing(this,segments.get(bodyStart+2),(int)(radius*4), true);
        backWing = new Wing(this,segments.get(bodyStart+2),(int)(radius*4), false);
        head = new Head(this, radius*1.1f);
        fireBreath = new FireBreath(this);

        backWing.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        frontWing.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        backArm.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        frontArm.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        backLeg.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        frontLeg.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        head.paint.setColorFilter(new LightingColorFilter(dragonColor,0));

        for (int i = 0; i < colliders.length; i++) {
            Segment segment = segments.get(i*(bodyEnd+size)/2/colliders.length);
            colliders[i] = segment;
        }

        moveBy(Vector2.right);
    }


    public void attack(Character enemy){

    }

    @Override
    public void onDamage(float damage, float dx, float dy) {
        if(!stunController.performing) {
            super.onDamage(damage, dx, dy);
        }
        stunController.triggerAction();
    }

    @Override
    public void update(float deltaTime) {
        stunController.update(deltaTime);
        segments.get(0).update(deltaTime, position);
        for(int i = 1; i < segments.size();i++) {
            segments.get(i).update(deltaTime, segments.get(i-1).position);

        }
        frontWing.update(deltaTime);
        backWing.update(deltaTime);
        head.update(deltaTime);
        distanceTravelled+=speed*deltaTime;


    }
    public void moveBy(Vector2 moveBy){
        if(moveBy == null){
            setDir(Math.signum(direction.x),direction.y/2);
            friction = 0.97f;
            if(!breathingFire) {
                mana += manaRegen * fixedDeltaTime / 1000;
                mana = Math.min(mana, maxMana);
                if (!flying ){
                    mana += manaRegen * 2 * fixedDeltaTime / 1000;
                    mana = Math.min(mana, maxMana);
                }
            }
            return;
        }
        if(!stunController.performing) {
            float magnitude = moveBy.getLength();
            if(magnitude > 0.01){
                if(!flying) {

                    backWing.walking = true;
                    frontWing.walking = true;
                    backArm.walking = true;
                    frontArm.walking = true;

                    if(backLeg.segment.position.y >= groundLevel-radius/10) {
                        backLeg.walking = true;
                        frontLeg.walking = true;
                    }

                    setDir(moveBy.add(direction.multiply(0.3f)));
                    if(Math.abs(direction.x) > 0.3f) {
                        setDir(moveBy.multiply(1f / 15).add(direction));
                        speed = (speed + Math.min(magnitude, maxMoveSpeed * walkSpeed)) / 2 * Math.abs(direction.x);

                    }
                    else {
                        speed = speed*0.99f;
                    }
                    direction.y = Math.min(direction.y,0);
                    if(!breathingFire) {
                        mana += manaRegen * fixedDeltaTime / 1000;
                        mana = Math.min(mana, maxMana);
                    }
                }
                else {
                    mana -= flyingManaCost*fixedDeltaTime/1000*(GameView.instance.screenHeight - position.y)/ GameView.instance.screenHeight;
                    mana = Math.max(0,mana);

                    if(breathingFire) {

                        setDir(moveBy.multiply(1f / 15).add(direction));

                        if(mana <= 0){
                            direction.y = 0.5f;
                            speed = (speed + Math.min(magnitude, maxMoveSpeed/2))/2 ;
                        }
                        else {
                            speed = (speed + Math.min(magnitude, maxMoveSpeed/2))/2;
                        }

                    }
                    else {
                        setDir(moveBy.add(direction.multiply(0.3f)));

                        if(mana <= 0){
                            direction.y = 0.5f;
                            speed = (speed + Math.min(magnitude, maxMoveSpeed/2))/2 ;
                        }
                        else {
                            speed = (speed + Math.min(magnitude, maxMoveSpeed))/2 ;
                        }
                    }


                    backWing.walking=false;
                    frontWing.walking=false;
                    backArm.walking=false;
                    frontArm.walking=false;
                    backLeg.walking=false;
                    frontLeg.walking=false;
                }
                if(position.y > groundLevel-GameView.instance.screenHeight/10) {
                    direction.y = Math.min(direction.y, 0.2f);
                    direction.x = Math.signum(direction.x) * Math.max(direction.x, 0.8f);
                }


                friction = 1;
            }

        }

    }

    @Override
    public boolean collision(GameObject other) {
        boolean collided = false;
        for (int i = 0; i < colliders.length; i++) {
            Segment segment = colliders[i];
            RectF bound = segment.dst;
            if(other.getBounds().intersect(bound.left-radius,bound.top-radius,bound.right+radius,bound.bottom+radius)){
                collided = true;
            }
        }
        return collided;
    }
    public boolean collisionStick(GameObject other) {
        boolean collided = false;
        for (int i = 0; i < colliders.length; i++) {
            Segment segment = colliders[i];
            RectF bound = segment.dst;
            if(other.getBounds().intersect(bound.left-radius,bound.top-radius,bound.right+radius,bound.bottom+radius)){
                collided = true;
                //make child
            }
        }
        return collided;
    }

    @Override
    public void physics(float deltaTime) {
        if(position.y < upperBound){
            direction.y = 0;
            position.y = upperBound;
        }
        if(position.y > groundLevel){
            direction.y = 0;
            position.y = groundLevel;
        }

        if(position.y >= groundLevel){
            flying = false;
        }
        else{
            flying = true;
        }

        if(!destroyed)
            super.physics(deltaTime);
        speed*=friction;
        if(breathingFire && mana > 0){

            if(Math.random()<0.05)
            ProjectilePool.instance.shootArrow((int)position.x,(int)position.y,1f/2,direction.x+(float)Math.random()/4,direction.y+(float)Math.random()/4);


            /*
            fireBreath.breath(deltaTime);
            mana -= fireManaCost*deltaTime/1000;
            mana = Math.max(0,mana);

             */
        }
        fireBreath.physics(deltaTime);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);



        for(int i =  segments.size()-1; i >= 0; i--) {

            if(i==bodyEnd+segments.size()/5){
                backLeg.draw(canvas);
            }
            if(i==bodyEnd-segments.size()/5){
                frontLeg.draw(canvas);
            }
            if(i==bodyStart+segments.size()/10){
                backArm.draw(canvas);
                backWing.draw(canvas);
            }
            if(i==bodyStart-segments.size()/10){
                frontArm.draw(canvas);
                frontWing.draw(canvas);
            }
            if(i == 0){
                fireBreath.draw(canvas);
            }
            segments.get(i).draw(canvas);
        }

        head.draw(canvas);
/*Debug: show colliders
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        for (int i = 0; i < colliders.length; i++) {
            canvas.drawRect(colliders[i].dst,p);
        }
*/
    }

    public void collectedGold(){
        goldHolding++;
        SoundEffects.instance.play(SoundEffects.PEW);
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
class Head{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    float open;
    public float radius, time;
    Dragon dragon;
    Bitmap spriteTop, spriteLower, spriteEye;
    RectF src;
    Paint paint = new Paint();
    public float wave;

    public Head(Dragon dragon, float radius) {
        this.radius = radius;
        this.dragon = dragon;
        position = dragon.position;
        spriteTop = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.head_top_minimalism);
        spriteTop = Bitmap.createScaledBitmap(spriteTop, (int) (radius * 2), (int) (radius * 2), false);
        spriteLower = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.head_lower_minimalism);
        spriteLower = Bitmap.createScaledBitmap(spriteLower, (int) (radius * 2), (int) (radius * 2), false);
        spriteEye = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.dragon_eye);
        spriteEye = Bitmap.createScaledBitmap(spriteEye, (int) (radius * 2), (int) (radius * 2), false);

        src = new RectF(0, 0, radius * 2, radius * 2);

    }
    public void draw(Canvas canvas){

        float left = position.x- src.width()/2 + GameView.instance.cameraDisp.x+wave*direction.y;;
        float top = position.y - src.height()*0.4f+wave*direction.x;;
        float right = left + src.width();
        float bottom = top + src.height();



        Matrix matrix = new Matrix();
        RectF dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(1,Math.signum(direction.x),  dst.centerX(),dst.centerY());
        matrix.postRotate((float) rotation + Math.signum(direction.x)*open, dst.centerX(),dst.centerY());
        canvas.drawBitmap(spriteLower, matrix,paint);

        matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(1,Math.signum(direction.x),  dst.centerX(),dst.centerY());
        matrix.postRotate((float) rotation - Math.signum(direction.x)*open, dst.centerX(),dst.centerY());
        canvas.drawBitmap(spriteTop, matrix,paint);
        canvas.drawBitmap(spriteEye, matrix,null);

    }
    public void update(float deltaTime){
        direction = dragon.direction;
        rotation = Math.toDegrees(Math.atan2(direction.y,direction.x));
        position = dragon.position;
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;
        // wave = (float)Math.cos((-time/1000)*Math.PI)*dragon.radius*0.2f;

        wave = 0;
        if(dragon.breathingFire){
            wave+=(Math.random()-0.5f)*radius/4;
        }
        if(dragon.breathingFire){
            open = (45+open)/2 + (float)Math.random()*10;
        }
        else{
            open = open/2;
        }
    }
}

class Segment{
    public Vector2 position;
    public Vector2 target;
    public Vector2 direction;
    double rotation;
    public float radius, time;
    Dragon dragon;
    Bitmap sprite,tailSprite;
    RectF src, dst,tailSrc;
    Paint paint = new Paint();
    float index;
    public float wave;

    public Segment(Dragon dragon, int index, float radius){
        this.radius = radius;
        this.dragon = dragon;
        this.index = index;
        //System.out.println(radius +" "+index);
        position = dragon.position.add(Vector2.left.multiply(1000));

        if (index % 2 == 0){
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.segment_minimalism);
        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.segment_spiked_minimalism);
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (radius * 2), (int) (radius * 2), false);
        src = new RectF(0, 0, radius * 2, radius * 2);

        dst = src;
        paint.setAntiAlias(true);

    }
    public void draw(Canvas canvas){

        float left = position.x- src.width()/2 + GameView.instance.cameraDisp.x+wave*direction.y;
        float top = position.y - src.height()/4+wave*direction.x;
        float right = left + src.width();
        float bottom = top + src.height();

        Matrix matrix = new Matrix();
        dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(1,Math.signum(direction.x),  dst.centerX(),dst.centerY());
        matrix.postRotate((float) rotation, dst.centerX(),dst.centerY());
        canvas.drawBitmap(sprite, matrix,paint);


    }
    public void update(float deltaTime, Vector2 target){
        this.target = target;
        Vector2 disp = target.sub(position);
        direction = disp.getNormal();



        rotation = Math.toDegrees(Math.atan2(direction.y,direction.x));

        if(disp.getLength() > Math.min(radius/2,dragon.radius/4)){
            position = target.sub(direction.multiply(Math.min(radius/2,dragon.radius/4)));
        }
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;

        wave = (float)Math.cos((-time/1000+index/dragon.segments.size()*2)*Math.PI)*dragon.radius*index/dragon.segments.size()*0.2f;
        if(dragon.breathingFire){
            //wave+=(Math.random()-0.5f)*radius/6;
        }
    }
}

class Leg{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    Dragon dragon;
    Bitmap sprite, spriteFlying;
    RectF src;
    Paint paint = new Paint();
    Segment segment;
    boolean front,walking;

    public Leg(Dragon dragon, Segment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        this.front = front;
        position = dragon.position;

        if(!front){
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_leg_minimalism);
            spriteFlying = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_leg_flying_minimalism);
        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.leg_minimalism);
            spriteFlying = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.leg_flying_minimalism);
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*2f  ), (int) (dragon.radius * 2f-GameView.instance.screenWidth/200), false);
        spriteFlying = Bitmap.createScaledBitmap(spriteFlying, (int) (dragon.radius*2f  ), (int) (dragon.radius * 2f -GameView.instance.screenWidth/200), false);
        src = new RectF(0, 0, dragon.radius*2f , dragon.radius * 2f-GameView.instance.screenWidth/200);
    }
    public void draw(Canvas canvas){
        float left = segment.position.x - src.width()/2 + GameView.instance.cameraDisp.x;
        float top = segment.position.y;

        if(walking){
            float phase = (float)Math.PI/4;
            if(!front){
                phase = (float)Math.PI+(float)Math.PI/4;
            }
            left+=dragon.speed/dragon.maxMoveSpeed*Math.cos(Math.signum(segment.direction.x)*segment.time/1000*Math.PI+phase)*dragon.radius*3f/2;
            top+=dragon.speed/dragon.maxMoveSpeed*Math.min(Math.sin(Math.signum(segment.direction.x)*segment.time/1000*Math.PI+phase)*dragon.radius,0.15f);
        }
        float right = left + src.width();
        float bottom = top + src.height();
        Matrix matrix = new Matrix();
        RectF dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(Math.signum(segment.direction.x),1,  dst.centerX(),dst.centerY());
        matrix.postRotate( Math.signum(segment.direction.x)*dragon.speed/dragon.maxMoveSpeed*40, dst.centerX(),dst.top);
        if(!walking) {
            canvas.drawBitmap(spriteFlying, matrix, paint);
        }
        else{
            canvas.drawBitmap(sprite, matrix, paint);
        }

    }
}

class Arm{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    Dragon dragon;
    Bitmap sprite;
    RectF src;
    Paint paint = new Paint();
    Segment segment;
    public boolean walking;
    boolean front;

    public Arm(Dragon dragon, Segment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        this.front = front;
        position = dragon.position;


        if(!front){
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_arm_minimalism);
        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.arm_minimalism);
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*2f-GameView.instance.screenWidth/200  ), (int) (dragon.radius * 2f-GameView.instance.screenWidth/200), false);
        src = new RectF(0, 0, dragon.radius*2f-GameView.instance.screenWidth/200 , dragon.radius * 2f-GameView.instance.screenWidth/200);
    }
    public void draw(Canvas canvas){

        float left = segment.position.x - src.width()/2 + GameView.instance.cameraDisp.x;
        float top = segment.position.y;
        if(walking){
            float phase = 0;
            if(front){
                phase = (float)Math.PI;
            }
            left+=dragon.speed/dragon.maxMoveSpeed*Math.cos(Math.signum(segment.direction.x)*segment.time/1000*Math.PI+phase)*dragon.radius*3f/2;
            top+=dragon.speed/dragon.maxMoveSpeed*Math.min(Math.sin(Math.signum(segment.direction.x)*segment.time/1000*Math.PI+phase)*dragon.radius,0.15f);
        }
        float right = left + src.width();
        float bottom = top + src.height();

        Matrix matrix = new Matrix();
        RectF dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(Math.signum(segment.direction.x),1,  dst.centerX(),dst.centerY());
        matrix.postRotate( Math.signum(segment.direction.x)*dragon.speed/dragon.maxMoveSpeed*30, dst.centerX(),dst.top);
        canvas.drawBitmap(sprite, matrix,paint);

    }
}

class Wing{
    public Segment segment;
    Vector2 position;
    double rotation;
    Bitmap sprite;
    RectF src;
    RectF dst;
    float time, flap,scaleX;
    boolean front;
    Paint paint = new Paint();
    Dragon dragon;
    public boolean walking;

    public Wing(Dragon dragon, Segment segment, int size, boolean front){
        this.segment = segment;
        this.front = front;
        this.dragon = dragon;
        if(front) {
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.wing_minimalism);
        }
        else {
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_wing_minimalism);
        }
        sprite = Bitmap.createScaledBitmap(sprite, size/2, size,false);
        src = new RectF(0, 0, sprite.getWidth(),sprite.getHeight());
        position = segment.position;
        rotation = segment.rotation;
    }
    public void draw(Canvas canvas){

        float left = GameView.instance.cameraDisp.x+position.x - sprite.getWidth()*(scaleX/2)+segment.radius*0.3f*segment.direction.y*Math.signum(segment.direction.x)+segment.wave*segment.direction.y;
        float right = left+sprite.getWidth()*scaleX;
        float top = position.y-sprite.getHeight()+segment.wave*segment.direction.x;
        float bottom = position.y+segment.radius/8+segment.wave*segment.direction.x;

        dst = new RectF(left, top, right, bottom);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

        matrix.postScale(1,flap,left,bottom);
        matrix.postRotate((float) rotation, dst.centerX(),dst.bottom);
        if(!(walking && !front)) {
            canvas.drawBitmap(sprite, matrix, paint);
        }

    }
    public void update(float deltaTime){
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;
        position = new Vector2(segment.position.x,segment.position.y);
        //System.out.println(dragon.speed/dragon.maxMoveSpeed/2);
        if(walking){
            flap = (Math.signum(segment.direction.x)*1f/4+flap)/2;
            rotation = segment.rotation+Math.signum(segment.direction.x)*10;
            scaleX=(2.5f+scaleX)/2;
            position.x -= Math.signum(segment.direction.x)*sprite.getWidth()/3;
        }
        else {
            flap = ((float) Math.sin(time / 1000 * Math.PI)+flap)/2;
            rotation = segment.rotation;
            scaleX=(1+scaleX)/2;

        }

    }
}

class FireBreath{
    int breathSize = 10;
    ArrayList<Flame> flames = new ArrayList<Flame>();
    ArrayList<Flame> backFlames = new ArrayList<Flame>();
    int currentBreath = 0;
    Dragon dragon;
    float range;
    float shootTime = 30, timeSinceShoot;
    Vector2 direction;
    Bitmap flameShadow;


    public FireBreath(Dragon dragon){
        this.dragon = dragon;
        range = 6*dragon.radius;
        direction = dragon.direction;

        for(float i = 0; i < breathSize;i++){
            backFlames.add(new Flame(dragon,  range,Game.instance.getResources().getColor(R.color.colorFire), 2*dragon.radius*(1.25f+(float)Math.random())));
        }
        for(float i = 0; i < breathSize;i++){
            flames.add(new Flame(dragon,  range,Color.WHITE, dragon.radius*(1.25f+(float)Math.random())));
        }
        flameShadow = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame_shadow);

    }
    public void breath(float deltaTime){

        if(timeSinceShoot > shootTime) {
            Flame f = flames.get(currentBreath);
            f.shoot(dragon.position, direction, dragon.speed + range / 350);
            f = backFlames.get(currentBreath);
            f.shoot(dragon.position, direction, dragon.speed + range / 350);
            timeSinceShoot = 0;
            currentBreath++;
            if(currentBreath >= breathSize){
                currentBreath = 0;
            }
        }
        timeSinceShoot+=deltaTime;
    }
    public void physics(float deltaTime){

            for (int i = 0; i < flames.size(); i++) {
                Flame f = flames.get(i);
                f.physics(deltaTime);
                f = backFlames.get(i);
                f.physics(deltaTime);
            }
            direction = dragon.direction.add(direction).multiply(0.5f);


    }

    public boolean collision(GameObject other) {
        return collision(other.getBounds());
    }

    public boolean collision(Rect r) {

        RectF other = new RectF(r.left,r.top,r.right,r.bottom);
        return collision(other);
    }
    public boolean collision(RectF r) {

        RectF other = new RectF(r.left,r.top,r.right,r.bottom);
        return flames.get(currentBreath).dst.intersect(other) && dragon.breathingFire ;
    }
    public boolean collision(Vector2 center, float radius) {
        return Vector2.distance(flames.get(currentBreath).position, center) < radius && dragon.breathingFire;
    }

    public void draw(Canvas canvas){
        if(dragon.breathingFire){
            //Paint paint = new Paint();
            for (int i = backFlames.size()-1; i >=0; i--) {
                Flame f =  backFlames.get(i);
                f.draw(canvas);
            }
            for (int i = flames.size()-1; i >=0; i--) {
                Flame f =  flames.get(i);
                f.draw(canvas);
            }
        }
    }
}
class Flame {
    RectF dst;
    RectF src;
    Vector2 direction;
    Vector2 position;
    float speed;
    float range;
    float size, maxSize;
    boolean active = false;
    Bitmap sprites[] = new Bitmap[6];
    int flameType;
    Dragon dragon;
    Paint paint = new Paint();
    float distanceTravelled;
    public Flame(Dragon dragon, float range, int color, float maxSize){
        dst = new RectF(0,0,size,size);
        this.range = range;
        this.dragon = dragon;
        this.maxSize = maxSize;
        direction = Vector2.zero;
        position = Vector2.zero;

        sprites[0] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame_minimalism);
        sprites[1] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame1_minimalism);
        sprites[2] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame2_minimalism);
        sprites[3] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame3_minimalism);
        sprites[4] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame4_minimalism);
        sprites[5] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame5_minimalism);

        src = new RectF(0,0,sprites[0].getWidth(), sprites[0].getHeight());
        dst = new RectF(0,0,0,0);



        paint.setColorFilter(new LightingColorFilter(color,0));
    }
    public void physics(float deltaTime){
        if(active) {
            distanceTravelled = Vector2.distance(dragon.position, position);
            if (distanceTravelled < range) {
                position = position.add(direction.multiply(speed * deltaTime));
                size = Math.min(distanceTravelled/range*0.9f+(float)(Math.cos(distanceTravelled/range*Math.PI*8)+1)*0.05f+0.1f,1)*maxSize;

            } else {
                active = false;
            }
            if(Math.random()<0.05f) {
                flameType = (int) (Math.random() * 6);
            }
        }
        else{
            size=0;
        }
        float width = 1;//(float)Math.cos(4*(distanceTravelled/range)*Math.PI*2)/8+1;
        float left = position.x - size/2*width + GameView.instance.cameraDisp.x;
        float right = left + size*width;//*(((float)Math.sin(distanceTravelled/range*Math.PI*4+maxSize*Math.PI)+7)/8);//+Math.abs(direction.y)/2);;
        float bottom = position.y+size/2*3/2 + dragon.radius/8;
        float top = bottom-size*3/2+ dragon.radius/8;


        dst = new RectF(left,top, right, bottom);
    }
    public void draw(Canvas canvas){
        if(dragon.breathingFire && active) {

            //paint.setAlpha(150+(int)(Math.random()*100));
            //if(distanceTravelled > 5*range/6) {
            //paint.setAlpha((int) ((range - distanceTravelled) / (range/6) * 255));
            //}



            Matrix matrix = new Matrix();
            matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

            matrix.postRotate( direction.toDegrees()-90, dst.centerX(),dst.centerY());
            canvas.drawBitmap(sprites[flameType], matrix,paint);


        }
    }
    public void shoot(Vector2 position, Vector2 direction, float speed){
        this.speed = speed;
        this.position = position;
        this.direction = direction.add(Vector2.down.multiply(0.1f*Math.abs(direction.x)));
        active = true;
        distanceTravelled = 0;
        size = 0;
    }
}

