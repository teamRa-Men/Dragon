
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
import android.util.Log;

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
    public boolean breathingFire, flying, isSleeping;
    float distanceTravelled = 0, upperBound, groundLevel, walkSpeed = 1f/2;
    Segment[] colliders;



    int goldHolding = 0,maxGoldHolding = 50;
    float attack = 1, maxMana = 60;
    float mana = maxMana;
    float flyingManaCost = 5, fireManaCost = 4f, manaRegen=7;

    public Dragon(Bitmap sprite, float offsetX, float offsetY,int width, int height) {
        super(sprite, offsetX, offsetY);
        this.width = width;
        this.height = height;
        maxHealth = 60;
        health = maxHealth;
        maxMoveSpeed = 1f/2;



        friction = 0.99f;
        facing = true;
        upperBound = GameView.instance.screenHeight/10;

        direction = Vector2.right;

        groundLevel = GameView.instance.groundLevel;
        position = new Vector2(GameView.instance.screenWidth/2, groundLevel);


        initBody(35);


        setAttackController(0,100,100);

    }

    public void initBody(int size){
        segments.clear();

        this.size = size;

        colliders = new Segment[size/6];
        int cameraSize = GameView.instance.cameraSize;
        radius = (float)cameraSize*size/3000;

        bodyStart = size/7;
        bodyEnd = size/3;


        destroyed = false;
        centerPivot = true;
        simulated = true;
        visible = true;

        head = new Head(this, radius);


        int dragonColor = Game.instance.getResources().getColor(R.color.colorDragon);

        Bitmap dragonSheet = SpriteManager.instance.dragonSheet;

        Rect r = SpriteManager.instance.getDragonSpriteRect("Segment");
        Bitmap segmentSprite =Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        r = SpriteManager.instance.getDragonSpriteRect("SpikedSegment");
        Bitmap spikedSegmentSprite =Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());





        for (int i = 0; i < size; i++) {
            Bitmap sprite;
            if (i% 2 != 0){
                sprite = segmentSprite;
            }
            else{
                sprite = spikedSegmentSprite;
            }
            Segment s;
            if(i < bodyStart) {
                s = new Segment(this, i, (float)Math.pow((float)i / bodyStart*0.2f+0.55f,2) * radius,sprite);
                segments.add(s);
            }
            else if(i < (bodyEnd+bodyStart)/2) {
                s = new Segment(this, i, (segments.get(i-1).radius+radius*0.75f)/2,sprite);
                segments.add(s);
            }
            else{
                s = new Segment(this, i, (segments.get(i-1).radius+(float) Math.pow((float) (size - i) /(size - bodyEnd)*0.7f,1.5f) * radius)/2+1,sprite);
                segments.add(s);
            }


            if(i>0) {

                s.position = segments.get(i - 1).position.add(direction.multiply(-radius));
                s.update(fixedDeltaTime, segments.get(i - 1).position);
            }
            else{
                s.position = position.add(direction.multiply(-radius));
                s.update(fixedDeltaTime,position);
            }

        }

        frontLeg = new Leg(this, segments.get(bodyEnd+2), true);
        backLeg = new Leg(this, segments.get(bodyEnd+2), false);
        frontArm = new Arm(this, segments.get(bodyStart), true);
        backArm = new Arm(this, segments.get(bodyStart), false);
        frontWing = new Wing(this,segments.get(bodyStart),(int)(radius*3), true);
        backWing = new Wing(this,segments.get(bodyStart),(int)(radius*3), false);

        fireBreath = new FireBreath(this);
        for (int i = 0; i < colliders.length; i++) {
            Segment segment = segments.get(i*(bodyEnd+size)/2/colliders.length);
            colliders[i] = segment;
        }


    }



    @Override
    public void onDamage(float damage) {
        GameView.instance.lair.wake();
        if(!stunController.performing && !destroyed) {
            if(!destroyed && (health-damage)<0 && flying){
                setVelocity((Math.signum(direction.x)/2*size/35)*(1-(float)Math.pow(position.y/groundLevel/2,2))+getVelocity().x,getVelocity().y);
            }
            super.onDamage(damage);

            if(destroyed){
                SoundEffects.instance.play(SoundEffects.BIGHURT);

            }
            else{
                if(damage<maxHealth/8) {
                    SoundEffects.instance.play(SoundEffects.HURT);
                }
                else{
                    SoundEffects.instance.play(SoundEffects.BIGHURT);
                }
            }
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
        backLeg.update(deltaTime);
        frontLeg.update(deltaTime);
        distanceTravelled+=speed*deltaTime;
        if(isSleeping){

            position.y = (position.y+groundLevel+0.6f*radius)/2;


        }




    }



    public void moveBy(Vector2 moveBy){
        //System.out.println(isSleeping);
        if(!isSleeping && !destroyed) {

            if (moveBy == null) {
                //setDir(Math.signum(direction.x),direction.y/2);
                friction = 0.97f;
                if (!breathingFire) {
                    if (!flying) {
                        mana += manaRegen * 2 * fixedDeltaTime / 1000;
                        mana = Math.min(mana, maxMana);
                    } else {
                        mana -= flyingManaCost / 3 * fixedDeltaTime / 1000 * (GameView.instance.screenHeight - position.y) / GameView.instance.screenHeight;
                        mana = Math.max(0, mana);
                    }
                }
                return;
            }
            if( GameView.instance.lair != null&&!destroyed)
                groundLevel = GameView.instance.lair.getGroundLevel(position, radius);
            if (!stunController.performing) {
                float magnitude = moveBy.getLength();
                if (magnitude > 0.01) {
                    if (!flying) {

                        backWing.walking = true;
                        frontWing.walking = true;

                        if (backArm.segment.position.y >= groundLevel - radius/4) {
                            backArm.walking = true;
                            frontArm.walking = true;
                        }

                        if (backLeg.segment.position.y >= groundLevel - radius/4) {
                            backLeg.walking = true;
                            frontLeg.walking = true;
                        }

                        setDir(moveBy.add(direction.multiply(0.1f)));
                        if (Math.abs(direction.x) > 0.3f) {

                            speed = (speed + Math.min(magnitude, maxMoveSpeed * walkSpeed)) / 2 * Math.abs(direction.x);

                        } else {

                            speed = speed * 0.99f;
                        }

                        if (mana <= 0) {
                            direction.y = Math.max(direction.y, 0);
                        }
                        direction.y = Math.min(direction.y, 0);

                    } else {

                        mana -= flyingManaCost * fixedDeltaTime / 1000 * (GameView.instance.screenHeight - position.y) / GameView.instance.screenHeight;
                        mana = Math.max(0, mana);

                        if (breathingFire) {

                            setDir(moveBy.multiply(0.5f).add(direction));

                            if (mana <= 0) {
                                direction.y = 0.5f;
                            }

                                speed = (speed + Math.min(magnitude, maxMoveSpeed / 2)) / 2;


                        } else {
                            setDir(moveBy.add(direction.multiply(0.1f)));

                            if (mana <= 0) {
                                direction.y = 0.5f;

                            }
                                speed = (speed + Math.min(magnitude, maxMoveSpeed)) / 2;

                        }


                        backWing.walking = false;
                        frontWing.walking = false;
                        backArm.walking = false;
                        frontArm.walking = false;
                        backLeg.walking = false;
                        frontLeg.walking = false;
                    }
                /*
                if(position.y > groundLevel-GameView.instance.screenHeight/10) {
                    direction.y = Math.min(direction.y, 0.2f);
                    direction.x = Math.signum(direction.x) * Math.max(direction.x, 0.8f);
                }
                */

                    friction = 1;
                }

            }
        }
        direction.y =  Math.signum(direction.y)*Math.min(Math.abs(direction.y), 0.9f);
        direction.x = Math.signum(direction.x)*Math.max(Math.abs(direction.x), 0.1f);
    }
    public boolean inReach(Vector2 p){
        //Vector2 hand = new Vector2(frontArm.dst.centerX(),frontArm.dst.bottom);
        if(goldHolding<maxGoldHolding) {
            return Vector2.sqrDistance(p, aimFor()) < radius * radius * 4;
        }
        return  false;

    }


    @Override
    public boolean collision(GameObject other) {
        boolean collided = false;
        for (int i = 0; i < colliders.length; i++) {
            Segment segment = colliders[i];
            if(segment.collision(other)){
                collided = true;
            }
        }
        return collided;
    }
    public boolean projectileCollision(GameObject other) {
        boolean collided = false;

        for (int i = 0; i < colliders.length; i++) {
            Segment segment = colliders[i];
            if(segment.getBounds().contains(other.position.x,other.position.y)){
                if(Vector2.distance(other.position,segment.getCenter())<segment.radius*0.8) {
                    collided = true;
                    other.setParent(segment);
                }
            }
        }
        return collided;
    }
    //lead
    public Vector2 aimFor(){

        return segments.get(segments.size()/5).position;
    }

    @Override
    public void physics(float deltaTime) {

        if(!isSleeping) {
            if(position.x < -Scene.instance.islandSizeLeft+Scene.instance.width && velocity!=null) {
                //direction.x = (-0.1f + direction.x)/2;
                //direction.y = 0;
                //position.x =  -Scene.instance.islandSize+Scene.instance.width;
                position.x = Scene.instance.islandSizeRight;
                for(int i = 0; i < segments.size();i++){
                    segments.get(i).position.x += Scene.instance.islandSizeLeft+Scene.instance.islandSizeRight+Scene.instance.width;
                }
                System.out.println("wall right");
            }
            if(position.x  > Scene.instance.islandSizeRight && velocity!=null) {
                //direction.x = (0.1f + direction.x)/2f;
                //direction.y = 0;
                //position.x =  Scene.instance.islandSize;
                position.x = -Scene.instance.islandSizeLeft+Scene.instance.width;
                for(int i = 0; i < segments.size();i++){
                    segments.get(i).position.x -= Scene.instance.islandSizeLeft+Scene.instance.islandSizeRight+Scene.instance.width;
                }
                System.out.println("wall left");
            }

            if (position.y < upperBound) {
                direction.y = 0;
                position.y = upperBound;
            }
            if (position.y > groundLevel) {
                direction.y = 0;
                position.y = groundLevel;
            }

            flying = !(position.y >= groundLevel);


            if (breathingFire && mana > 0 && !destroyed) {

                //if(Math.random()<0.05)
                //ProjectilePool.instance.shootArrow((int)position.x,(int)position.y,1f/2+speed,direction.x+(float)Math.random()/4,direction.y+(float)Math.random()/4);

                fireBreath.breath(deltaTime);
                mana -= fireManaCost * deltaTime / 1000;
                mana = Math.max(0, mana);
                if(!breathSound) {
                    breathSoundID = SoundEffects.instance.play(SoundEffects.BREATH,-1,0);
                    breathSound = true;
                    breathSoundVol = 1;
                }

            }




        }
        else {

            breathingFire = false;
        }


        fireBreath.physics(deltaTime);


        super.physics(deltaTime);
        speed *= friction;
        if(!breathingFire || mana <= 0 || destroyed) {

            SoundEffects.instance.stop(breathSoundID);
            breathSound = false;

        }
    }
    float breathSoundVol = 1;
    int breathSoundID;
    boolean breathSound = false;



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(int i =  segments.size()-1; i >= 0; i--) {
            if(i==bodyEnd+segments.size()/10 && !isSleeping){
                backLeg.draw(canvas);
            }
            if(i==bodyEnd-segments.size()/15){
                frontLeg.draw(canvas);
            }
            if(i==bodyStart+segments.size()/10 && !isSleeping){
                backArm.draw(canvas);
                backWing.draw(canvas);
            }
            if(i==bodyStart-segments.size()/15){
                frontArm.draw(canvas);
                frontWing.draw(canvas);
            }
            if(i == 0 && !isSleeping){
                fireBreath.draw(canvas);
            }
            segments.get(i).draw(canvas);
        }


        head.draw(canvas);

        //Debug: show colliders
/*
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawRect(frontArm.dst,p);

        for (int i = 0; i < colliders.length; i++) {
            canvas.drawRect(colliders[i].dst,p);
        }*/

    }

    public void collectedGold(){
        goldHolding++;
        SoundEffects.instance.playCoin();
    }

    int animDuration = 500, animTime = 0, deathTime = 0;

    boolean down = false;
    @Override
    protected void destroyAnim(Canvas canvas) {
        if(breathSound) {
                breathingFire = false;

                SoundEffects.instance.stop(breathSoundID);
                breathSound = false;

        }

        GameView.instance.timeScale = 0.2f;

        groundLevel = GameView.instance.lair.getGroundLevel(position, radius*0.6f);
        if (animTime>animDuration || deathTime>2000) {

            visible = false;
            down = false;
            animTime = 0;
            deathTime = 0;
        }
        deathTime+=fixedDeltaTime;
        if(down) {
            animTime += fixedDeltaTime;
        }

        friction=0.998f;
        bounce = 0.35f;
        if (position.y < groundLevel) {
            setVelocity(getVelocity().x, getVelocity().y + fixedDeltaTime/200 );
        } else {
            float y = -speed*bounce;

            if(y*y<0.02){
                y = 0;
                if(Math.abs(getVelocity().x) < 0.1) {
                    GameView.instance.lair.lieDown();
                    down = true;
                    speed = 0.001f;
                }
            }
            else {
                position.y = groundLevel;
            }
            setVelocity(speed*direction.x*friction,y);

        }
        position = position.add(direction.multiply(fixedDeltaTime * speed));




    }


}


class Head{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    float open;
    public float radius, time;
    Dragon dragon;
    Bitmap dragonSheet;
    Bitmap head, jaw, sleeping;
    RectF src;

    Paint paint = new Paint();
    public float wave;

    public Head(Dragon dragon, float radius) {
        this.radius = radius;
        this.dragon = dragon;
        position = dragon.position;

        direction = new Vector2(dragon.direction.x,dragon.direction.y);

        dragonSheet = SpriteManager.instance.dragonSheet;
        src = new RectF(0,0,radius * 2, radius * 2);


        Rect r = SpriteManager.instance.getDragonSpriteRect("Head");
        head = Bitmap.createBitmap(dragonSheet, r.left, r.top, r.width(), r.height());
        head = Bitmap.createScaledBitmap(head, (int) src.width(), (int) src.height(),  true);


        r = SpriteManager.instance.getDragonSpriteRect("Jaw");
        jaw = Bitmap.createBitmap(dragonSheet,r.left, r.top, r.width(), r.height());
        jaw = Bitmap.createScaledBitmap(jaw,(int)src.width(),(int)src.height(),true);
        r = SpriteManager.instance.getDragonSpriteRect("HeadSleeping");
        sleeping = Bitmap.createBitmap(dragonSheet,r.left, r.top, r.width(), r.height());
        sleeping = Bitmap.createScaledBitmap(sleeping,(int)src.width(),(int)src.height(),true);


    }
    public void draw(Canvas canvas){

        float left = position.x- src.width()/2 + GameView.instance.cameraDisp.x+wave*direction.y;
        float top = position.y - src.height() *0.4f+wave*direction.x;
        float right = left + src.width();
        float bottom = top + src.height();



        Matrix matrix = new Matrix();
        //RectF dst = new RectF(10, 10, 20, 20);
        RectF dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(1,Math.signum(direction.x),  dst.centerX(),dst.centerY());
        matrix.postRotate((float) rotation + Math.signum(direction.x)*open, dst.centerX(),dst.centerY());

        if(!dragon.isSleeping){
            canvas.drawBitmap(jaw, matrix,paint);
        }

        matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(1,Math.signum(direction.x),  dst.centerX(),dst.centerY());
        matrix.postRotate((float) rotation - Math.signum(direction.x)*open, dst.centerX(),dst.centerY());

        if(!dragon.isSleeping) {
            canvas.drawBitmap(head, matrix, paint);
        }
        else{
            canvas.drawBitmap(sleeping, matrix, paint);
        }

        //canvas.drawBitmap(spriteEye, matrix,null);
        //canvas.drawBitmap(dragonSheet, matrix,paint);
    }
    public void update(float deltaTime){
        if(!dragon.isSleeping) {
            direction = dragon.direction;

        }
        rotation = Math.toDegrees(Math.atan2(direction.y,direction.x));
        position = dragon.position;
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;
        // wave = (float)Math.cos((-time/1000)*Math.PI)*dragon.radius*0.2f;

        wave = 0;
        if(dragon.breathingFire && dragon.mana > 0){
            wave+=(Math.random()-0.5f)*radius/4;
            open = (45+open)/2 + (float)Math.random()*10;
        }
        else{
            open = open/2;
        }
        if(dragon.destroyed || Game.instance.mourning){
            open = (20+open)/2;
        }
    }
}

class Segment extends GameObject{
    public Vector2 target;
    public float radius, time;
    Dragon dragon;
    Bitmap sprite,tailSprite;
    RectF src, dst,collider,tailSrc;
    Matrix matrix;
    Paint paint = new Paint();
    float index;
    public float wave;

    public Segment(Dragon dragon, int index, float radius, Bitmap sprite){
        super(null,0.5f,0.25f);
        this.radius = radius;
        this.dragon = dragon;
        this.index = index;
        //System.out.println(radius +" "+index);
        position = dragon.position.add(Vector2.left.multiply(1000));
        direction = dragon.direction;

        this.sprite = Bitmap.createScaledBitmap(sprite, (int) (radius * 2), (int) (radius * 2), true);
        src = new RectF(0, 0, radius * 2, radius * 2);

        dst = src;
        matrix = new Matrix();


        paint.setAntiAlias(true);
        centerPivot = true;
    }
    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(sprite, matrix,paint);
    }
    public void update(float deltaTime, Vector2 target){

        this.target = target;
        Vector2 disp = target.sub(position);
        direction = disp.getNormal();

        rotation = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));


        if (disp.getLength() > Math.min(radius / 2, dragon.radius / 4)) {
            position = target.sub(direction.multiply(Math.min(radius / 2, dragon.radius / 4)));
        }
        time += deltaTime * (dragon.speed / dragon.maxMoveSpeed * 4 + 1) * 0.75f;
        if(!dragon.isSleeping) {
            wave = (float) Math.cos((-time / 1000 + index / dragon.segments.size() * 2) * Math.PI) * dragon.radius * index / dragon.segments.size() * 0.2f;
        }
        else{
            wave = (float) Math.sin(time / 1300 )*(1-Math.abs(2*(dragon.bodyStart+dragon.bodyEnd)/2-index)/dragon.segments.size())/10*dragon.radius;
        }
        if(dragon.destroyed){
            wave = 0;
        }
        float left = position.x - src.width() / 2 + wave * direction.y;
        float top = position.y - src.height() / 4 + wave * direction.x;
        float right = left + src.width();
        float bottom = top + src.height();

        collider = new RectF(left, top, right, bottom);
        dst = new RectF(left + GameView.instance.cameraDisp.x, top + GameView.instance.cameraDisp.y, right + GameView.instance.cameraDisp.x, bottom + GameView.instance.cameraDisp.y);

        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

        matrix.postScale(1, Math.signum(direction.x), dst.centerX(), dst.centerY());


        matrix.postRotate(rotation, dst.centerX(), dst.centerY());

    }

    @Override
    public RectF getBounds() {
        return collider;
    }
}

class Leg{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    Dragon dragon;
    Bitmap sprite, spriteFlying;
    RectF src, dst;
    Paint paint = new Paint();
    Segment segment;
    boolean front,walking;
    Matrix matrix = new Matrix();

    public Leg(Dragon dragon, Segment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        this.front = front;
        position = dragon.position;
        Bitmap dragonSheet = SpriteManager.instance.dragonSheet;
        if(!front){
            Rect r = SpriteManager.instance.getDragonSpriteRect("BackLeg");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
            r = SpriteManager.instance.getDragonSpriteRect("BackLegFlying");
            spriteFlying = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        else{
            Rect r = SpriteManager.instance.getDragonSpriteRect("Leg");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
            r = SpriteManager.instance.getDragonSpriteRect("LegFlying");
            spriteFlying = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*3/2f  ), (int) (dragon.radius *3/2f-GameView.instance.screenWidth/200), true);
        spriteFlying = Bitmap.createScaledBitmap(spriteFlying, (int) (dragon.radius*3/2f  ), (int) (dragon.radius *3/2f -GameView.instance.screenWidth/200), true);
        src = new RectF(0, 0, dragon.radius*3/2f , dragon.radius *3/2f-GameView.instance.screenWidth/200);
        dst = src;
    }
    boolean soundPlayed = false;
    public void update(float deltaTime){
        float left = segment.position.x - src.width()/2 + GameView.instance.cameraDisp.x;// + segment.wave*segment.direction.y;
        float top = segment.position.y;// + segment.wave*segment.direction.x;

        if(walking){
            float phase = (float)Math.PI/4;
            if(!front){
                phase = (float)Math.PI+(float)Math.PI/4;
            }
            left+=Math.min(dragon.speed/dragon.maxMoveSpeed*3,1)*(Math.cos(2*dragon.maxMoveSpeed*Math.signum(segment.direction.x)*segment.time/900*Math.PI+phase))*dragon.radius/2;
            top+=dragon.speed/dragon.maxMoveSpeed*Math.min(Math.sin(2*dragon.maxMoveSpeed*Math.signum(segment.direction.x)*segment.time/900*Math.PI+phase)*dragon.radius/2,0.1f);

            if(Math.sin(2*dragon.maxMoveSpeed*Math.signum(segment.direction.x)*segment.time/900*Math.PI+phase)>0.9 && !soundPlayed && dragon.speed/dragon.maxMoveSpeed>0.1){
                soundPlayed = true;
                SoundEffects.instance.play(SoundEffects.WALKING);
            }
            else{
                soundPlayed = false;
            }
        }
        float right = left + src.width();
        float bottom = top + src.height();

        dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(Math.signum(segment.direction.x),1,  dst.centerX(),dst.centerY());
        if (!walking) {
            matrix.postRotate(Math.signum(segment.direction.x) * dragon.speed / dragon.maxMoveSpeed * 20, dst.centerX(), dst.top);
        }

    }
    public void draw(Canvas canvas){

        if (!walking) {
            canvas.drawBitmap(spriteFlying, matrix, paint);
        } else {
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
    RectF dst;
    public RectF collider;
    Paint paint=new Paint();
    Segment segment;
    public boolean walking;
    boolean front;

    public Arm(Dragon dragon, Segment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        this.front = front;
        position = dragon.position;

        Bitmap dragonSheet = SpriteManager.instance.dragonSheet;
        if(!front){
            Rect r = SpriteManager.instance.getDragonSpriteRect("BackArm");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        else{
            Rect r = SpriteManager.instance.getDragonSpriteRect("Arm");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*3/2f-GameView.instance.screenWidth/200  ), (int) (dragon.radius *3/2f-GameView.instance.screenWidth/200), true);
        src = new RectF(0, 0, dragon.radius*3/2f-GameView.instance.screenWidth/200 , dragon.radius *3/2f-GameView.instance.screenWidth/200);
        dst = src;

    }
    public void draw(Canvas canvas){

        float left = segment.position.x - src.width()/2;
        float top = segment.position.y;
        if(walking){
            float phase = 0;
            if(front){
                phase = (float)Math.PI;
            }
            left+=Math.min(dragon.speed/dragon.maxMoveSpeed*3,1)*(Math.cos(2*dragon.maxMoveSpeed*Math.signum(segment.direction.x)*segment.time/900*Math.PI+phase))*dragon.radius/2;
            top+=dragon.speed/dragon.maxMoveSpeed*Math.min(Math.sin(2*dragon.maxMoveSpeed*Math.signum(segment.direction.x)*segment.time/900*Math.PI+phase)*dragon.radius/2,0.1f);
        }
        float right = left + src.width();
        float bottom = top + src.height();

        Matrix matrix = new Matrix();
        collider = new RectF(left, top, right+src.width()/2, bottom+src.height()/2);
        dst =new RectF(left + GameView.instance.cameraDisp.x, top + GameView.instance.cameraDisp.y, right + GameView.instance.cameraDisp.x, bottom + GameView.instance.cameraDisp.y);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(Math.signum(segment.direction.x),1,  dst.centerX(),dst.centerY());
        if(!walking) {
            matrix.postRotate(Math.signum(segment.direction.x) * dragon.speed / dragon.maxMoveSpeed * 20, dst.centerX(), dst.top);
        }
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
    Paint paint=new Paint();
    Dragon dragon;
    public boolean walking;

    public Wing(Dragon dragon, Segment segment, int size, boolean front){
        this.segment = segment;
        this.front = front;
        this.dragon = dragon;
        Bitmap dragonSheet = SpriteManager.instance.dragonSheet;
        if(front){
            Rect r = SpriteManager.instance.getDragonSpriteRect("Wing");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        else {
            Rect r = SpriteManager.instance.getDragonSpriteRect("BackWing");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        sprite = Bitmap.createScaledBitmap(sprite, size/2, size,true);
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
        if(!((walking|| dragon.isSleeping || dragon.destroyed) && !front)) {
            canvas.drawBitmap(sprite, matrix, paint);
        }

    }
    boolean soundPlayed = false;
    public void update(float deltaTime){
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;
        position = new Vector2(segment.position.x,segment.position.y);
        //System.out.println(dragon.speed/dragon.maxMoveSpeed/2);
        if(walking || dragon.isSleeping || dragon.destroyed){
            flap = (Math.signum(segment.direction.x)*1f/4+flap)/2;

            rotation = segment.rotation+Math.signum(segment.direction.x)*10;
            scaleX=(2f+scaleX)/2;
            //position.x -= Math.signum(segment.direction.x)*sprite.getWidth()/3;
        }
        else {
            flap = ((float) Math.sin(time / 1000 * Math.PI)+flap)/2;
            double f = Math.cos(time / 1000 * Math.PI);
            if(front) {
                if (flap > 0.4 && flap <0.6 && dragon.speed/dragon.maxMoveSpeed>0.5) {
                    if(!soundPlayed) {
                        soundPlayed = true;
                        SoundEffects.instance.play(SoundEffects.FLYING);
                    }
                }
                else {
                    soundPlayed = false;
                }
            }
            rotation = segment.rotation;
            scaleX=(1+scaleX)/2;

        }

    }
}

class FireBreath{
    int breathSize = 20;
    ArrayList<Flame> flames = new ArrayList<Flame>();
    ArrayList<Flame> backFlames = new ArrayList<Flame>();


    int currentBreath = 0;
    Dragon dragon;
    float range,length;

    float shootTime = 30, timeSinceShoot;
    Vector2 direction, collisionPosition;
    Bitmap[] sprites = new Bitmap[6];
    Paint paint = new Paint();
    Paint backPaint = new Paint();

    int colliderIndex=0;

    public FireBreath(Dragon dragon){
        this.dragon = dragon;
        range = 8*dragon.radius;
        direction = dragon.direction;

        Bitmap fireSheet = SpriteManager.instance.fireSheet;
        Rect r = SpriteManager.instance.getFireSpriteRect("Fire0");
        sprites[0] =Bitmap.createBitmap(fireSheet,r.left,r.top,r.width(),r.height());
        r = SpriteManager.instance.getFireSpriteRect("Fire1");
        sprites[1] =Bitmap.createBitmap(fireSheet,r.left,r.top,r.width(),r.height());
        r = SpriteManager.instance.getFireSpriteRect("Fire2");
        sprites[2] =Bitmap.createBitmap(fireSheet,r.left,r.top,r.width(),r.height());
        r = SpriteManager.instance.getFireSpriteRect("Fire3");
        sprites[3] =Bitmap.createBitmap(fireSheet,r.left,r.top,r.width(),r.height());
        r = SpriteManager.instance.getFireSpriteRect("Fire4");
        sprites[4] =Bitmap.createBitmap(fireSheet,r.left,r.top,r.width(),r.height());
        r = SpriteManager.instance.getFireSpriteRect("Fire5");
        sprites[5] =Bitmap.createBitmap(fireSheet,r.left,r.top,r.width(),r.height());

        paint.setColorFilter(new LightingColorFilter(Game.instance.getResources().getColor(R.color.colorFire),0));
        backPaint.setColorFilter(new LightingColorFilter(Game.instance.getResources().getColor(R.color.colorFireCold),0));
        for(float i = 0; i < breathSize;i++){
            backFlames.add(new Flame(this,  range,Game.instance.getResources().getColor(R.color.colorFireCold), 2*dragon.radius*(1+(float)Math.random())));
        }
        for(float i = 0; i < breathSize;i++){
            flames.add(new Flame(this,  range,Game.instance.getResources().getColor(R.color.colorFire), 1.3f*dragon.radius*(1+(float)Math.random())));
        }


    }

    public void breath(float deltaTime){



        if(timeSinceShoot > shootTime) {
            currentBreath++;
            if(currentBreath >= breathSize){
                currentBreath = 0;
            }
            Flame f = flames.get(currentBreath);
            f.shoot(dragon.position, direction, dragon.speed + dragon.radius / 90);
            f = backFlames.get(currentBreath);
            f.shoot(dragon.position, direction, dragon.speed + dragon.radius / 90);
            timeSinceShoot = 0;


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


        Flame c = backFlames.get(colliderIndex);
        if(!c.active){
            colliderIndex++;
            if(colliderIndex >= breathSize){
                colliderIndex = 0;
            }
        }
    }

    public boolean projectileCollision(GameObject other) {
        boolean collided = false;
        Flame f = backFlames.get(colliderIndex);
        if(f.active) {
            collided = f.collider.contains(other.position.x,other.position.y);
        }
        return collided;
    }

    public boolean collision(Rect r) {

        RectF other = new RectF(r.left,r.top,r.right,r.bottom);
        return collision(other);
    }
    public boolean collision(RectF other) {
        boolean collided = false;
        Flame f = backFlames.get(colliderIndex);
        if(f.active) {
            collided = other.intersect(f.collider);
        }
        return collided;
    }
    public boolean collisionStop(RectF other) {
        boolean collided = collision(other);
        Flame f = backFlames.get(colliderIndex);
        if(collided) {
            if (collided) {
                collisionPosition = new Vector2(f.position.x, f.position.y);

                f.active = false;
                flames.get(colliderIndex).active = false;
            }
        }
        return collided;
    }
public void setColor(float strength){
    for (int i = backFlames.size() - 1; i >= 0; i--) {
        Flame f = backFlames.get(i);
       f.setColor(strength);
    }
}

    public void draw(Canvas canvas){
        //Paint paint = new Paint();
        for (int i = backFlames.size() - 1; i >= 0; i--) {
            Flame f = backFlames.get(i);
            f.draw(canvas);
        }
        for (int i = flames.size() - 1; i >= 0; i--) {
            Flame f = flames.get(i);
            f.draw(canvas);
        }

    }
}
class Flame {
    RectF dst, collider;
    RectF src;
    Vector2 direction;
    Vector2 position, shootFrom;
    float speed;
    float size, maxSize;
    boolean active = false;

    int flameType;
    FireBreath breath;
    Paint paint = new Paint();
    float distanceTravelled;
    public Flame(FireBreath breath, float range, int color, float maxSize){
        dst = new RectF(0,0,size,size);
        this.maxSize = maxSize;
        direction = Vector2.zero;
        position = Vector2.zero;
        this.breath = breath;

        src = new RectF(0,0,breath.sprites[0].getWidth(), breath.sprites[0].getHeight());
        dst = new RectF(0,0,0,0);
        collider = new RectF(0,0,0,0);


        paint.setColorFilter(new LightingColorFilter(color,0));
    }
    public void setColor(float strength){
        paint.setColorFilter(new LightingColorFilter(Color.rgb(255,(int)(150+100*strength),85),0));
    }
    public void physics(float deltaTime){

        if(active) {
            distanceTravelled = Vector2.distance(shootFrom,position);
            if (distanceTravelled < breath.range) {
                position = position.add(direction.multiply(speed * deltaTime));
                size = Math.min(distanceTravelled/(7*breath.dragon.radius)*0.8f+0.2f,1)*maxSize;
                //paint.setAlpha((int)(Math.min(distanceTravelled/range*2+0.1f,1)*255));


            } else {
                active = false;

            }
            if(Math.random()<0.2f) {
                flameType = (int) (Math.random() * 6);
            }
        }
        else{
            size=0;
            position=breath.dragon.position;
        }
        float width = 1;//(float)Math.cos(4*(distanceTravelled/range)*Math.PI*2)/8+1;
        float left = position.x - size/2*width;
        float right = left + size*width;//*(((float)Math.sin(distanceTravelled/range*Math.PI*4+maxSize*Math.PI)+7)/8);//+Math.abs(direction.y)/2);;
        float bottom = position.y+size/2*3/2 + breath.dragon.radius/10;
        float top = bottom-size*3/2+ breath.dragon.radius/10;

        collider = new RectF(left,top,right,bottom);
        dst = new RectF(left + GameView.instance.cameraDisp.x,top+GameView.instance.cameraDisp.y, right + GameView.instance.cameraDisp.x, bottom + GameView.instance.cameraDisp.y
        );
    }
    public void draw(Canvas canvas){
        if(active) {
            Matrix matrix = new Matrix();
            matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

            matrix.postRotate( direction.toDegrees()-90, dst.centerX(),dst.centerY());
            canvas.drawBitmap(breath.sprites[flameType], matrix,paint);
        }
    }
    public void shoot(Vector2 position, Vector2 direction, float speed){
        this.speed = speed;
        this.position = position;
        shootFrom = position;
        this.direction = direction;//.add(Vector2.down.multiply(0.1f*Math.abs(direction.x)));
        active = true;
        distanceTravelled = 0;
        size = 0;
    }
}

