
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


public class StartDragon extends Character {
    int cameraSize;
    int size;
    ArrayList<StartSegment> segments = new ArrayList<StartSegment>();
    StartWing frontWing, backWing;
    StartLeg frontLeg, backLeg;
    StartArm frontArm, backArm;
    StartHead head;

    int bodyStart;
    int bodyEnd;



    public StartDragon(Bitmap sprite, float offsetX, float offsetY,int width, int height) {
        super(sprite, offsetX, offsetY);

        cameraSize = width;
        maxHealth = 60;
        health = maxHealth;
        maxMoveSpeed = 1f/2;



        friction = 0.99f;
        facing = true;

        direction = Vector2.right;


        position = new Vector2(width*0.65f, height*0.55f);


        initBody(55);


        setAttackController(0,100,100);

    }

    public void initBody(int size){
        segments.clear();

        this.size = size;



        radius = (float)cameraSize*size/1500;

        bodyStart = size/7;
        bodyEnd = size/3;


        destroyed = false;
        centerPivot = true;
        simulated = true;
        visible = true;

        head = new StartHead(this, radius);


        Bitmap dragonSheet = SpriteManager.instance.startDragonSheet;

        Rect r = SpriteManager.instance.getStartDragonSpriteRect("Segment");
        Bitmap segmentSprite =Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        r = SpriteManager.instance.getStartDragonSpriteRect("SpikedSegment");
        Bitmap spikedSegmentSprite =Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());





        for (int i = 0; i < size; i++) {
            Bitmap sprite;
            if (i% 2 != 0){
                sprite = segmentSprite;
            }
            else{
                sprite = spikedSegmentSprite;
            }
            StartSegment s;
            if(i < bodyStart) {
                s = new StartSegment(this, i, (float)Math.pow((float)i / bodyStart*0.2f+0.55f,2) * radius,sprite);
                segments.add(s);
            }
            else if(i < (bodyEnd+bodyStart)/2) {
                s = new StartSegment(this, i, (segments.get(i-1).radius+radius*0.75f)/2,sprite);
                segments.add(s);
            }
            else{
                s = new StartSegment(this, i, (segments.get(i-1).radius+(float) Math.pow((float) (size - i) /(size - bodyEnd)*0.7f,1.5f) * radius)/2+1,sprite);
                segments.add(s);
            }
            float c = Math.min((float)(size-i)/size/5,0.15f)+0.85f;

            /*s.paint.setColorFilter(new LightingColorFilter(Color.rgb((int)(Color.red(dragonColor)*c),
                    (int)(Color.green(dragonColor)*c),
                    (int)(Color.blue(dragonColor)*c)),0));*/

            if(i>0) {

                s.position = segments.get(i - 1).position.add(direction.multiply(-radius));
                s.update(fixedDeltaTime, segments.get(i - 1).position);
            }
            else{
                s.position = position.add(direction.multiply(-radius));
                s.update(fixedDeltaTime,position);
            }

        }

        frontLeg = new StartLeg(this, segments.get(bodyEnd+2), true);
        backLeg = new StartLeg(this, segments.get(bodyEnd+2), false);
        frontArm = new StartArm(this, segments.get(bodyStart), true);
        backArm = new StartArm(this, segments.get(bodyStart), false);
        frontWing = new StartWing(this,segments.get(bodyStart),(int)(radius*3), true);
        backWing = new StartWing(this,segments.get(bodyStart),(int)(radius*3), false);


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

    }



    public void moveBy(Vector2 moveBy){
        //System.out.println(isSleeping);
        if (moveBy == null) {
            //setDir(Math.signum(direction.x),direction.y/2);
            friction = 0.97f;
            return;
        }
        if (!stunController.performing) {
            float magnitude = moveBy.getLength();
            if (magnitude > 0.01) {


                setDir(moveBy.add(direction.multiply(0.1f)));


                speed = (speed + Math.min(magnitude, maxMoveSpeed)) / 2;

                friction = 1;
            }


        }
        direction.y =  Math.signum(direction.y)*Math.min(Math.abs(direction.y), 0.9f);
        direction.x = Math.signum(direction.x)*Math.max(Math.abs(direction.x), 0.1f);
    }



    @Override
    public void physics(float deltaTime) {


        super.physics(deltaTime);
        speed *= friction;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(int i =  segments.size()-1; i >= 0; i--) {
            if(i==bodyEnd+segments.size()/10){
                backLeg.draw(canvas);
            }
            if(i==bodyEnd-segments.size()/15){
                frontLeg.draw(canvas);
            }
            if(i==bodyStart+segments.size()/10){
                backArm.draw(canvas);
                backWing.draw(canvas);
            }
            if(i==bodyStart-segments.size()/15){
                frontArm.draw(canvas);
                frontWing.draw(canvas);
            }

            segments.get(i).draw(canvas);
        }


        head.draw(canvas);


    }


}


class StartHead{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    float open;
    public float radius, time;
    StartDragon dragon;
    Bitmap dragonSheet;
    Bitmap head, jaw;
    RectF src;

    Paint paint = new Paint();
    public float wave;

    public StartHead(StartDragon dragon, float radius) {
        this.radius = radius;
        this.dragon = dragon;
        position = dragon.position;

        direction = new Vector2(dragon.direction.x,dragon.direction.y);

        dragonSheet = SpriteManager.instance.startDragonSheet;
        src = new RectF(0,0,radius * 2, radius * 2);


        Rect r = SpriteManager.instance.getStartDragonSpriteRect("Head");
        head = Bitmap.createBitmap(dragonSheet, r.left, r.top, r.width(), r.height());
        head = Bitmap.createScaledBitmap(head, (int) src.width(), (int) src.height(),  true);


        r = SpriteManager.instance.getStartDragonSpriteRect("Jaw");
        jaw = Bitmap.createBitmap(dragonSheet,r.left, r.top, r.width(), r.height());
        jaw = Bitmap.createScaledBitmap(jaw,(int)src.width(),(int)src.height(),true);
        r = SpriteManager.instance.getStartDragonSpriteRect("HeadSleeping");
    }
    public void draw(Canvas canvas){

        float left = position.x- src.width()/2;
        float top = position.y - src.height() *0.4f+wave*direction.x;
        float right = left + src.width();
        float bottom = top + src.height();



        Matrix matrix = new Matrix();
        //RectF dst = new RectF(10, 10, 20, 20);
        RectF dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(1,Math.signum(direction.x),  dst.centerX(),dst.centerY());
        matrix.postRotate((float) rotation + Math.signum(direction.x)*open, dst.centerX(),dst.centerY());
        matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(1,Math.signum(direction.x),  dst.centerX(),dst.centerY());
        matrix.postRotate((float) rotation - Math.signum(direction.x)*open, dst.centerX(),dst.centerY());

        canvas.drawBitmap(jaw, matrix, paint);
        canvas.drawBitmap(head, matrix, paint);

        //canvas.drawBitmap(spriteEye, matrix,null);
        //canvas.drawBitmap(dragonSheet, matrix,paint);
    }
    public void update(float deltaTime){

        direction = dragon.direction;


        rotation = Math.toDegrees(Math.atan2(direction.y,direction.x));
        position = dragon.position;
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;
        // wave = (float)Math.cos((-time/1000)*Math.PI)*dragon.radius*0.2f;

        wave = 0;
    }
}

class StartSegment extends GameObject{
    public Vector2 target;
    public float radius, time;
    StartDragon dragon;
    Bitmap sprite;
    RectF src, dst;
    Matrix matrix;
    Paint paint = new Paint();
    float index;
    public float wave;

    public StartSegment(StartDragon dragon, int index, float radius, Bitmap sprite){
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

        //.setAntiAlias(true);
        centerPivot = true;
    }
    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(sprite, matrix,null);
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

        wave = (float) Math.cos((-time / 1000 + index / dragon.segments.size() * 2) * Math.PI) * dragon.radius * index / dragon.segments.size() * 0.2f;


        float left = position.x - src.width() / 2 + wave * direction.y;
        float top = position.y - src.height() / 4 + wave * direction.x;
        float right = left + src.width();
        float bottom = top + src.height();


        dst = new RectF(left,top, right, bottom);

        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

        matrix.postScale(1, Math.signum(direction.x), dst.centerX(), dst.centerY());


        matrix.postRotate(rotation, dst.centerX(), dst.centerY());

    }

}

class StartLeg{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    StartDragon dragon;
    Bitmap spriteFlying;
    RectF src, dst;
    Paint paint = new Paint();
    StartSegment segment;
    boolean front;
    Matrix matrix = new Matrix();

    public StartLeg(StartDragon dragon, StartSegment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        this.front = front;
        position = dragon.position;
        Bitmap dragonSheet = SpriteManager.instance.startDragonSheet;
        if(!front){
            Rect r = SpriteManager.instance.getStartDragonSpriteRect("BackLegFlying");
            spriteFlying = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        else{
            Rect r = SpriteManager.instance.getStartDragonSpriteRect("LegFlying");
            spriteFlying = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }

        spriteFlying = Bitmap.createScaledBitmap(spriteFlying, (int) (dragon.radius*3/2f  ), (int) (dragon.radius *3/2f -StartView.instance.screenWidth/200), true);
        src = new RectF(0, 0, dragon.radius*3/2f , dragon.radius *3/2f-StartView.instance.screenWidth/200);
        dst = src;
    }
    boolean soundPlayed = false;
    public void update(float deltaTime){
        float left = segment.position.x - src.width()/2;// + segment.wave*segment.direction.y;
        float top = segment.position.y;// + segment.wave*segment.direction.x;


        float right = left + src.width();
        float bottom = top + src.height();

        dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(Math.signum(segment.direction.x),1,  dst.centerX(),dst.centerY());

        matrix.postRotate(Math.signum(segment.direction.x) * dragon.speed / dragon.maxMoveSpeed * 20, dst.centerX(), dst.top);


    }
    public void draw(Canvas canvas){

        canvas.drawBitmap(spriteFlying, matrix, paint);

    }


}

class StartArm{
    public Vector2 position;
    public Vector2 direction;

    StartDragon dragon;
    Bitmap sprite;
    RectF src;
    RectF dst;

    Paint paint=new Paint();
    StartSegment segment;

    boolean front;

    public StartArm(StartDragon dragon, StartSegment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        this.front = front;
        position = dragon.position;

        Bitmap dragonSheet = SpriteManager.instance.startDragonSheet;
        if(!front){
            Rect r = SpriteManager.instance.getStartDragonSpriteRect("BackArm");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        else{
            Rect r = SpriteManager.instance.getStartDragonSpriteRect("Arm");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*3/2f-StartView.instance.screenWidth/200  ), (int) (dragon.radius *3/2f-StartView.instance.screenWidth/200), true);
        src = new RectF(0, 0, dragon.radius*3/2f-StartView.instance.screenWidth/200 , dragon.radius *3/2f-StartView.instance.screenWidth/200);
        dst = src;

    }
    public void draw(Canvas canvas){

        float left = segment.position.x - src.width()/2;
        float top = segment.position.y;

        float right = left + src.width();
        float bottom = top + src.height();

        Matrix matrix = new Matrix();

        dst =new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(Math.signum(segment.direction.x),1,  dst.centerX(),dst.centerY());

        matrix.postRotate(Math.signum(segment.direction.x) * dragon.speed / dragon.maxMoveSpeed * 20, dst.centerX(), dst.top);

        canvas.drawBitmap(sprite, matrix,paint);

    }
}

class StartWing{
    public StartSegment segment;
    Vector2 position;
    double rotation;
    Bitmap sprite;
    RectF src;
    RectF dst;
    float time, flap;
    boolean front;
    Paint paint=new Paint();
    StartDragon dragon;


    public StartWing(StartDragon dragon, StartSegment segment, int size, boolean front){
        this.segment = segment;
        this.front = front;
        this.dragon = dragon;
        Bitmap dragonSheet = SpriteManager.instance.startDragonSheet;
        if(front){
            Rect r = SpriteManager.instance.getStartDragonSpriteRect("Wing");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        else {
            Rect r = SpriteManager.instance.getStartDragonSpriteRect("BackWing");
            sprite = Bitmap.createBitmap(dragonSheet,r.left,r.top,r.width(),r.height());
        }
        sprite = Bitmap.createScaledBitmap(sprite, size/2, size,true);
        src = new RectF(0, 0, sprite.getWidth(),sprite.getHeight());
        position = segment.position;
        rotation = segment.rotation;

    }
    public void draw(Canvas canvas){

        float left = position.x - sprite.getWidth()/2+segment.radius*0.3f*segment.direction.y*Math.signum(segment.direction.x)+segment.wave*segment.direction.y;
        float right = left+sprite.getWidth();
        float top = position.y-sprite.getHeight()+segment.wave*segment.direction.x;
        float bottom = position.y+segment.radius/8+segment.wave*segment.direction.x;

        dst = new RectF(left, top, right, bottom);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

        matrix.postScale(1,flap,left,bottom);
        matrix.postRotate((float) rotation, dst.centerX(),dst.bottom);


        System.out.println("drawWing" +dst.left + " " + dst.right  );

        canvas.drawBitmap(sprite, matrix, paint);


    }
    boolean soundPlayed = false;
    public void update(float deltaTime){
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;
        position = new Vector2(segment.position.x,segment.position.y);
        //System.out.println(dragon.speed/dragon.maxMoveSpeed/2);

        flap = ((float) Math.sin(time / 1000 * Math.PI*2)+flap)/2;
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
    }
}

