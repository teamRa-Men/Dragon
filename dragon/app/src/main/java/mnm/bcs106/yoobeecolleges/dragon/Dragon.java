
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
    Segment[] segments = new Segment[30];
    Wing frontWing, backWing;
    Leg frontLeg, backLeg;
    Arm frontArm, backArm;
    Head head;
    FireBreath fireBreath;
    int bodyStart = segments.length/7;
    int bodyEnd = segments.length/3;
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
        init(GameView.instance.screenWidth/2, GameView.instance.screenHeight-100,cameraSize /30, cameraSize /60,1f, 100);


        int dragonColor = Game.instance.getResources().getColor(R.color.colorDragon);
        for (int i = 0; i < segments.length; i++) {
            if(i < bodyStart) {
                segments[i] = new Segment(this, i, (float)Math.pow((float)i / bodyStart*0.4f+0.4f,1) * radius);
            }
            else if(i < bodyEnd) {
                segments[i] = new Segment(this, i, (segments[i-1].radius+radius)/2);
            }
            else{
                segments[i] = new Segment(this, i, (float) Math.pow((float) (segments.length - i) /(segments.length - bodyEnd)*0.85f+0.05f,1) * radius);
            }
            float c = Math.min((float)(segments.length-i)/segments.length,0.4f)+0.6f;

            segments[i].paint.setColorFilter(new LightingColorFilter(Color.rgb((int)(Color.red(dragonColor)*c),
                                                                               (int)(Color.green(dragonColor)*c),
                                                                               (int)(Color.blue(dragonColor)*c)),0));
        }

        frontLeg = new Leg(this, segments[bodyEnd], true);
        backLeg = new Leg(this, segments[bodyEnd], false);
        frontArm = new Arm(this, segments[bodyStart-1], true);
        backArm = new Arm(this, segments[bodyStart-1], false);
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

        if(breathingFire){
            fireBreath.breath(deltaTime);
        }
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
                speed = (speed+Math.min(magnitude,maxMoveSpeed))/2;
                setDir(moveBy.add(direction.multiply(0.15f)));
                friction = 1;
            }
        }

    }


    @Override
    public void physics(float deltaTime) {
        if(!destroyed)
            super.physics(deltaTime);
        speed*=friction;
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
            segments[i].draw(canvas);
        }
        fireBreath.draw(canvas);
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
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1);
        wave = (float)Math.sin((-time/1000)*Math.PI)*radius/20;
        if(dragon.breathingFire){
            wave+=(Math.random()-0.5f)*dragon.radius/8;
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

        if(disp.getLength() > Math.min(radius,dragon.radius/3)){
            position = target.sub(direction.multiply(Math.min(radius,dragon.radius/3)));
        }
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1);

        wave = (float)Math.sin((-time/1000+index/dragon.segments.length*5)*Math.PI)*radius/8;
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
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*2.2f  ), (int) (dragon.radius * 2.2f), false);
        src = new RectF(0, 0, dragon.radius*2.2f , dragon.radius * 2.2f);
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
        sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius*2f  ), (int) (dragon.radius * 2f), false);
        src = new RectF(0, 0, dragon.radius*2f , dragon.radius * 2f);
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
        Matrix matrix = new Matrix();
        float left = GameView.instance.cameraDisp.x+position.x - +sprite.getWidth()/2+segment.radius*0.3f*segment.direction.y*Math.signum(segment.direction.x)+segment.wave*segment.direction.y;
        float right = left+sprite.getWidth();
        float top = position.y-sprite.getHeight()+segment.wave*segment.direction.x;
        float bottom = position.y+segment.radius/8+segment.wave*segment.direction.x;

        dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);

        matrix.postScale(1,flap,left,bottom);
        matrix.postRotate((float) rotation, dst.centerX(),dst.bottom);
        canvas.drawBitmap(sprite, matrix,paint);

    }
    public void update(float deltaTime){
        time += deltaTime*(dragon.speed/dragon.maxMoveSpeed*4+1);
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

    public FireBreath(Dragon dragon){
        this.dragon = dragon;
        for(int i = 0; i < breathSize;i++){
            flames.add(new Flame(dragon,  range));
        }
    }
    public void breath(float deltaTime){
        if(currentBreath >= breathSize){
            currentBreath = 0;
        }

        Flame f =  flames.get(currentBreath);
        f.shoot(dragon.segments[0].position, dragon.direction, dragon.speed+range/300);

        currentBreath++;

    }
    public void physics(float deltaTime){
        for (int i = 0; i < flames.size(); i++) {
            Flame f =  flames.get(i);
            f.physics(deltaTime);
        }
    }
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        for (int i = 0; i < flames.size(); i++) {
            Flame f =  flames.get(i);
            f.draw(canvas);
        }
    }
}
class Flame {
    RectF rect;
    Rect srcRect;
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
    public Flame(Dragon dragon, float range){
        rect = new RectF(0,0,size,size);
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

        srcRect = new Rect(0,0,sprites[0].getWidth(), sprites[0].getHeight());
    }
    public void physics(float deltaTime){
        if(active) {
            distanceTravelled = Vector2.distance(dragon.position, position);
            if (distanceTravelled < range) {
                position = position.add(direction.multiply(speed * deltaTime));
                size = (float)Math.min(distanceTravelled/range*3f/2+0.25f,1)*maxSize;

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
            float left = position.x - size/2 + GameView.instance.cameraDisp.x;
            float right = left + size;
            float bottom = position.y+size/2 + dragon.radius/4;
            float top = bottom-size*((float)Math.sin(distanceTravelled/range*Math.PI*6)+9)/10;


            rect = new RectF(left,top, right, bottom);
            canvas.drawBitmap(sprites[flameType],srcRect,rect,paint);
        }
    }
    public void shoot(Vector2 position, Vector2 direction, float speed){
        this.speed = speed;
        this.position = position;
        this.direction = direction;
        active = true;
        distanceTravelled = 0;
        size = 0;
    }
}

