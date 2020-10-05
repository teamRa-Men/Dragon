
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;


public class Dragon extends Character {
    Segment[] segments = new Segment[40];
    Wing frontWing, backWing;
    Leg frontLeg, backLeg;
    Arm frontArm, backArm;
    Head head;
    FireBreath fireBreath;
    int bodyStart = segments.length/7;
    int bodyEnd = segments.length/3-1;
    public boolean breathingFire;
    float distanceTravelled = 0;

    public Dragon(Bitmap sprite, float offsetX, float offsetY,int width, int height) {
        super(sprite, offsetX, offsetY);
        this.width = width;
        this.height = height;
        friction = 0.99f;
        facing = true;
        setAttackController(0,100,100);

        int cameraSize = GameView.instance.cameraSize;
        init(GameView.instance.screenWidth/2, GameView.instance.screenHeight-100,cameraSize /30, cameraSize /60,3f/4, 100);


        int dragonColor = Game.instance.getResources().getColor(R.color.colorDragon);
        for (int i = 0; i < segments.length; i++) {
            if(i < bodyStart) {
                segments[i] = new Segment(this, i, (float)Math.pow((float)i / bodyStart*0.3f+0.4f,1) * radius);
            }
            else if(i < (bodyEnd+bodyStart)/2) {
                segments[i] = new Segment(this, i, (segments[i-1].radius+radius)/2);
            }
            else{
                segments[i] = new Segment(this, i, (segments[i-1].radius+(float) Math.pow((float) (segments.length - i) /(segments.length - bodyEnd)*0.6f+0.05f,1) * radius)/2);
            }
            float c = Math.min((float)(segments.length-i)/segments.length,0.4f)+0.6f;

            segments[i].paint.setColorFilter(new LightingColorFilter(Color.rgb((int)(Color.red(dragonColor)*c),
                                                                               (int)(Color.green(dragonColor)*c),
                                                                               (int)(Color.blue(dragonColor)*c)),0));
        }

        frontLeg = new Leg(this, segments[bodyEnd+1], true);
        backLeg = new Leg(this, segments[bodyEnd+1], false);
        frontArm = new Arm(this, segments[bodyStart], true);
        backArm = new Arm(this, segments[bodyStart], false);
        frontWing = new Wing(this,segments[bodyStart+2],(int)(radius*6), true);
        backWing = new Wing(this,segments[bodyStart+2],(int)(radius*6), false);
        head = new Head(this, radius*1.1f);
        fireBreath = new FireBreath(this);

        backWing.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        frontWing.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        backArm.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        frontArm.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        backLeg.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        frontLeg.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
        head.paint.setColorFilter(new LightingColorFilter(dragonColor,0));
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
        segments[0].update(deltaTime, position);
        for(int i = 1; i < segments.length;i++) {
            segments[i].update(deltaTime, segments[i-1].position);

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
            return;
        }
        if(!stunController.performing) {
            float magnitude = moveBy.getLength();
            if(magnitude > 0.01){

                if(!breathingFire) {
                    setDir(moveBy.add(direction.multiply(0.3f)));
                    speed = (speed+Math.min(magnitude,maxMoveSpeed))/2;
                }
                else {
                    float dirDff= Vector2.distance(direction,moveBy.getNormal())*4+1;
                    setDir(moveBy.multiply(1f/10).add(direction));
                    speed = (speed+Math.min(magnitude,maxMoveSpeed))/dirDff/3;
                }
                friction = 1;
            }
        }

    }


    @Override
    public void physics(float deltaTime) {
        if(!destroyed)
            super.physics(deltaTime);
        speed*=friction;
        if(breathingFire){
            fireBreath.breath(deltaTime);
        }
        fireBreath.physics(deltaTime);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(int i =  segments.length-1; i >= 0; i--) {
            if(i == bodyStart+segments.length/10){
                backWing.draw(canvas);
            }
            if(i==bodyEnd+segments.length/10){
                backLeg.draw(canvas);
            }
            if(i==bodyEnd-segments.length/10){
                frontLeg.draw(canvas);
            }
            if(i==bodyStart+segments.length/10){
                backArm.draw(canvas);
            }
            if(i==bodyStart-segments.length/10){
                frontArm.draw(canvas);
                frontWing.draw(canvas);
            }
            if(i == 2){
                fireBreath.draw(canvas);
            }
            segments[i].draw(canvas);
        }

        head.draw(canvas);


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
    Bitmap spriteTop, spriteLower;
    RectF src;
    Paint paint = new Paint();
    public float wave;

    public Head(Dragon dragon, float radius) {
        this.radius = radius;
        this.dragon = dragon;
        position = dragon.position;
        spriteTop = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.head_top);
        spriteTop = Bitmap.createScaledBitmap(spriteTop, (int) (radius * 2), (int) (radius * 2), false);
        spriteLower = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.head_lower);
        spriteLower = Bitmap.createScaledBitmap(spriteLower, (int) (radius * 2), (int) (radius * 2), false);
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
    Bitmap sprite;
    RectF src;
    Paint paint = new Paint();
    float index;
    public float wave;

    public Segment(Dragon dragon, int index, float radius){
        this.radius = radius;
        this.dragon = dragon;
        this.index = index;
        position = dragon.position;
        if(index < dragon.segments.length-1) {
            if (index % 2 == 0){
               sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.segment);
            }
            else{
                sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.segment_spiked);
            }
            sprite = Bitmap.createScaledBitmap(sprite, (int) (radius * 2), (int) (radius * 2), false);
            src = new RectF(0, 0, radius * 2, radius * 2);




        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.tail);
            sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius), (int) (dragon.radius), false);
            src = new RectF(0, 0, dragon.radius, dragon.radius);

        }


    }
    public void draw(Canvas canvas){

        float left = position.x- src.width()/2 + GameView.instance.cameraDisp.x+wave*direction.y;
        float top = position.y - src.height()/4+wave*direction.x;
        float right = left + src.width();
        float bottom = top + src.height();

        Matrix matrix = new Matrix();
        RectF dst = new RectF(left, top, right, bottom);
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

        if(disp.getLength() > Math.min(radius,dragon.radius/4)){
            position = target.sub(direction.multiply(Math.min(radius,dragon.radius/4)));

        }
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;

        wave = (float)Math.sin((-time/1000+index/dragon.segments.length)*Math.PI)*dragon.radius*(float)Math.sin((index-dragon.bodyEnd)/dragon.bodyEnd*Math.PI)*2*0.05f;
        if(dragon.breathingFire){
            wave+=(Math.random()-0.5f)*radius/8;
        }
    }
}

class Leg{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    Dragon dragon;
    Bitmap sprite;
    RectF src;
    Paint paint = new Paint();
    Segment segment;

    public Leg(Dragon dragon, Segment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        position = dragon.position;

        if(!front){
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_leg);
        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.leg);
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*2.3f  ), (int) (dragon.radius * 2.3f), false);
        src = new RectF(0, 0, dragon.radius*2.3f , dragon.radius * 2.3f);
    }
    public void draw(Canvas canvas){
        float left = segment.position.x - src.width()/2 + GameView.instance.cameraDisp.x+segment.wave*segment.direction.y;
        float top = segment.position.y+segment.wave*segment.direction.x;
        float right = left + src.width();
        float bottom = top + src.height();

        Matrix matrix = new Matrix();
        RectF dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        matrix.postScale(Math.signum(segment.direction.x),1,  dst.centerX(),dst.centerY());
        matrix.postRotate( Math.signum(segment.direction.x)*dragon.speed/dragon.maxMoveSpeed*40, dst.centerX(),dst.top);
        canvas.drawBitmap(sprite, matrix,paint);

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

    public Arm(Dragon dragon, Segment segment, boolean front){
        this.dragon = dragon;
        this.segment = segment;
        position = dragon.position;


        if(!front){
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_arm);
        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.arm);
        }
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*2.5f  ), (int) (dragon.radius * 2.5f), false);
        src = new RectF(0, 0, dragon.radius*2.5f , dragon.radius * 2.5f);
    }
    public void draw(Canvas canvas){
        float left = segment.position.x - src.width()/2 + GameView.instance.cameraDisp.x+segment.wave*segment.direction.y;
        float top = segment.position.y+segment.wave*segment.direction.x;
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
    float time, flap;
    boolean front;
    Paint paint = new Paint();
    Dragon dragon;

    public Wing(Dragon dragon, Segment segment, int size, boolean front){
        this.segment = segment;
        this.front = front;
        this.dragon = dragon;
        if(front) {
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.wing);
        }
        else {
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_wing);
        }
        sprite = Bitmap.createScaledBitmap(sprite, size/2, size,false);
        src = new RectF(0, 0, sprite.getWidth(),sprite.getHeight());
        position = segment.position;
        rotation = segment.rotation;
    }
    public void draw(Canvas canvas){

        float left = GameView.instance.cameraDisp.x+position.x - +sprite.getWidth()/2+segment.radius*0.3f*segment.direction.y*Math.signum(segment.direction.x)+segment.wave*segment.direction.y;
        float right = left+sprite.getWidth();
        float top = position.y-sprite.getHeight()+segment.wave*segment.direction.x;
        float bottom = position.y+segment.radius/8+segment.wave*segment.direction.x;

        dst = new RectF(left, top, right, bottom);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

        matrix.postScale(1,flap,left,bottom);
        matrix.postRotate((float) rotation, dst.centerX(),dst.bottom);
        canvas.drawBitmap(sprite, matrix,paint);

    }
    public void update(float deltaTime){
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1)*0.75f;
        position = segment.position;
        //System.out.println(dragon.speed/dragon.maxMoveSpeed/2);
        flap = (float)Math.sin(time/1000*Math.PI);
        rotation = segment.rotation;
    }
}

class FireBreath{
    int breathSize = 10;
    ArrayList<Flame> flames = new ArrayList<Flame>();
    int currentBreath = 0;
    Dragon dragon;
    float range = 200;
    float shootTime = 30, timeSinceShoot;

    public FireBreath(Dragon dragon){
        this.dragon = dragon;
        for(float i = 0; i < breathSize;i++){
            flames.add(new Flame(dragon,  range,i/(float)breathSize));
        }
    }
    public void breath(float deltaTime){
        if(currentBreath >= breathSize){
            currentBreath = 0;
        }
        if(timeSinceShoot > shootTime) {
            Flame f = flames.get(currentBreath);
            f.shoot(dragon.position, dragon.direction, dragon.speed + range / 350);
            timeSinceShoot = 0;
            currentBreath++;
        }
        timeSinceShoot+=deltaTime;
    }
    public void physics(float deltaTime){
        for (int i = 0; i < flames.size(); i++) {
            Flame f =  flames.get(i);
            f.physics(deltaTime);
        }
    }
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        for (int i = flames.size()-1; i >=0; i--) {
            Flame f =  flames.get(i);
            f.draw(canvas);
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
float distanceTravelled;
    public Flame(Dragon dragon, float range, float t){
        dst = new RectF(0,0,size,size);
        this.range = range;
        this.dragon = dragon;
        maxSize = dragon.radius*(1.25f+(float)Math.random())*2;
        direction = Vector2.zero;
        position = Vector2.zero;

        sprites[0] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame);
        sprites[1] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame1);
        sprites[2] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame2);
        sprites[3] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame3);
        sprites[4] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame4);
        sprites[5] = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flame5);

        src = new RectF(0,0,sprites[0].getWidth(), sprites[0].getHeight());
    }
    public void physics(float deltaTime){
        if(active) {
            distanceTravelled = Vector2.distance(dragon.position, position);
            if (distanceTravelled < range) {
                position = position.add(direction.multiply(speed * deltaTime));
                size = (float)Math.min(distanceTravelled/range*3f/4+1f/4,1)*maxSize;

            } else {
                active = false;
            }
            if(Math.random()<0.05f) {
                flameType = (int) (Math.random() * 6);
            }
        }
    }
    public void draw(Canvas canvas){
        if(active) {
            Paint paint = new Paint();
            //paint.setAlpha(150+(int)(Math.random()*100));
            //if(distanceTravelled > 5*range/6) {
                //paint.setAlpha((int) ((range - distanceTravelled) / (range/6) * 255));
            //}
            float width = (float)Math.cos(4*(distanceTravelled/range)*Math.PI*2)/8+1;
            float left = position.x - size/2*width + GameView.instance.cameraDisp.x;
            float right = left + size*width;//*(((float)Math.sin(distanceTravelled/range*Math.PI*4+maxSize*Math.PI)+7)/8);//+Math.abs(direction.y)/2);;
            float bottom = position.y+size/2*3/2 + dragon.radius/4;
            float top = bottom-size*3/2+ dragon.radius/4;


            dst = new RectF(left,top, right, bottom);
            Matrix matrix = new Matrix();
            matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

            matrix.postRotate( direction.toDegrees()-90, dst.centerX(),dst.centerY());
            canvas.drawBitmap(sprites[flameType], matrix,paint);

        }
    }
    public void shoot(Vector2 position, Vector2 direction, float speed){
        this.speed = speed;
        this.position = position;
        this.direction = direction.add(Vector2.down.multiply(0.25f*Math.abs(direction.x)));
        active = true;
        distanceTravelled = 0;
        size = 0;
    }
}

