
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


public class Dragon extends Character {
    float deltaTime;
    Segment[] segments = new Segment[30];
    Wing frontWing, backWing;
    Leg frontLeg, backLeg;
    Arm frontArm, backArm;
    Head head;
    int bodyStart = segments.length/7;
    int bodyEnd = segments.length/4;
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
        init(GameView.instance.screenWidth/2, GameView.instance.screenHeight-100,cameraSize /50, cameraSize /100,1f/2, 100);


        for (int i = 0; i < segments.length; i++) {
            if(i < bodyStart) {
                segments[i] = new Segment(this, i, (float)Math.pow((float) Math.pow((float)i / bodyStart*0.4f, 1)+0.6f,2) * radius);
            }
            else if(i < bodyEnd) {
                segments[i] = new Segment(this, i, radius);
            }
            else{
                segments[i] = new Segment(this, i, (float) Math.pow((float) (segments.length - i) /(segments.length - bodyEnd)*0.7f+0.3f,1) * radius);
            }
        }

        frontLeg = new Leg(this, bodyEnd+1, true);
        backLeg = new Leg(this, bodyEnd+1, false);
        frontArm = new Arm(this, bodyStart, true);
        backArm = new Arm(this, bodyStart, false);
        frontWing = new Wing(segments[bodyStart],(int)(radius*6), true);
        backWing = new Wing(segments[bodyStart],(int)(radius*6), false);
        head = new Head(this, radius*0.4f);
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
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        Paint stepPaint = new Paint();
        stepPaint.setColor(Color.BLACK);



        for(int i =  segments.length-1; i >= 0; i--) {
            if(i == bodyStart){
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
            }
            segments[i].draw(canvas);
        }
        head.draw(canvas);
        frontWing.draw(canvas);
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
    Paint paint;
    public float wave;

    public Head(Dragon dragon, float radius) {
        this.radius = radius;
        this.dragon = dragon;
        position = dragon.position;
        spriteTop = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.head_top);
        spriteTop = Bitmap.createScaledBitmap(spriteTop, (int) (dragon.radius * 3), (int) (dragon.radius * 2), false);
        spriteLower = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.head_lower);
        spriteLower = Bitmap.createScaledBitmap(spriteLower, (int) (dragon.radius * 3), (int) (dragon.radius * 2), false);
        src = new RectF(0, 0, dragon.radius * 3, dragon.radius * 2);
    }
    public void draw(Canvas canvas){
        wave = (float)Math.cos((-time/1000)*Math.PI)*radius/8;
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
        matrix.postRotate((float) rotation - Math.signum(direction.x)*open/2, dst.centerX(),dst.centerY());
        canvas.drawBitmap(spriteTop, matrix,paint);

    }
    public void update(float deltaTime){
        direction = dragon.direction;
        rotation = Math.toDegrees(Math.atan2(direction.y,direction.x));
        position = dragon.position;
        if(dragon.breathingFire){
            open = (45+open)/2 + (float)Math.random()*10;
        }
        else{
            open = open/2;
        }
        time+=deltaTime;
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
    Paint paint;
    float index;
    public float wave;

    public Segment(Dragon dragon, int index, float radius){
        this.radius = radius;
        this.dragon = dragon;
        this.index = index;
        position = dragon.position;
        paint = new Paint();
        if(index < dragon.segments.length-1) {
            if (index % 2 != 0){
                sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.segment);
            }
            else{
                sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.segment_spiked);
            }
            sprite = Bitmap.createScaledBitmap(sprite, (int) (radius * 2), (int) (radius * 2), false);
            src = new RectF(0, 0, radius * 2, radius * 2);
            int c = (int)(155*((float)(dragon.segments.length-index)/dragon.segments.length)+100);
            paint.setColorFilter(new LightingColorFilter(Color.rgb(c,c,c),0));

        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.tail);
            sprite = Bitmap.createScaledBitmap(sprite, (int) (dragon.radius), (int) (dragon.radius), false);
            src = new RectF(0, 0, dragon.radius, dragon.radius);
            int c = 100;
            paint.setColorFilter(new LightingColorFilter(Color.rgb(c,c,c),0));
        }


    }
    public void draw(Canvas canvas){
        wave = (float)Math.cos((-time/1000+index/dragon.segments.length*5)*Math.PI)*radius/8;
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

        if(disp.getLength() > Math.min(radius,dragon.radius/2)){
            position = target.sub(direction.multiply(Math.min(radius,dragon.radius/2)));
        }
        time+=deltaTime;
    }
}

class Leg{
    public Vector2 position;
    public Vector2 direction;
    double rotation;
    Dragon dragon;
    Bitmap sprite;
    RectF src;
    Paint paint;
    int index;
    Segment segment;

    public Leg(Dragon dragon, int index, boolean front){
        this.dragon = dragon;
        this.index = index;
        this.segment = dragon.segments[index];
        position = dragon.position;
        paint = new Paint();

        if(!front){
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.back_leg);
        }
        else{
            sprite = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.leg);
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
        matrix.postScale(Math.signum(dragon.segments[index].direction.x),1,  dst.centerX(),dst.centerY());
        matrix.postRotate( Math.signum(dragon.segments[index].direction.x)*dragon.speed/dragon.maxMoveSpeed*40, dst.centerX(),dst.top);
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
    Paint paint;
    int index;
    Segment segment;

    public Arm(Dragon dragon, int index, boolean front){
        this.dragon = dragon;
        this.index = index;
        this.segment = dragon.segments[index];
        position = dragon.position;
        paint = new Paint();

        if(!front){
            paint.setColorFilter(new LightingColorFilter(Color.rgb(220,220,220),0));
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
        matrix.postScale(Math.signum(dragon.segments[index].direction.x),1,  dst.centerX(),dst.centerY());
        matrix.postRotate( Math.signum(dragon.segments[index].direction.x)*dragon.speed/dragon.maxMoveSpeed*40, dst.centerX(),dst.top);
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
    float time;
    boolean front;
    Paint paint;

    public Wing(Segment segment, int size, boolean front){
        this.segment = segment;
        this.front = front;
        paint = new Paint();
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
        float left = GameView.instance.cameraDisp.x+position.x - sprite.getWidth()/2+segment.wave*segment.direction.y;
        float right = left+sprite.getWidth();
        float top = position.y-sprite.getHeight();
        float bottom = position.y+segment.radius/8+segment.wave*segment.direction.x;

        dst = new RectF(left, top, right, bottom);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
        float flap = (float)Math.sin(time/750*Math.PI);
        matrix.postScale(1,flap,left,bottom);
        matrix.postRotate((float) rotation, dst.centerX(),dst.bottom);
        canvas.drawBitmap(sprite, matrix,paint);

    }
    public void update(float deltaTime){
        time += deltaTime;
        position = segment.position;
        rotation = segment.rotation;
    }
}


