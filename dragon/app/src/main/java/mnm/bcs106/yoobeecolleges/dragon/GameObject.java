
package mnm.bcs106.yoobeecolleges.dragon;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

public class GameObject implements Comparable{
    public float width, height, scaleX = 1, scaleY = 1, z;


    //Physics
    protected Vector2 position, direction, velocity, offset;
    public float speed, angularVelocity, rotation, localRotation,  bounce = 0.3f, friction = 1f, mass =1;
    RectF srcRect;
    float radius = 0;
    float fixedDeltaTime = 0;
    public boolean visible = true, simulated = false, facing = false, centerPivot = true;
    public float gravity;
    //The parent game object this is attached to if any
    GameObject parent = null;
    Vector2 localPosition;

    //Visuals
    private Bitmap sprite;
    public Paint paint;
    Vector2 drawDisplacement;
    Matrix matrix = new Matrix();

    public GameObject(Bitmap sprite, float offsetX, float offsetY){
        //Init visuals
        offset = new Vector2(offsetX,offsetY);//Bitmap offset from position
        if(sprite != null) {


            width = sprite.getWidth();
            height = sprite.getHeight();
            setSprite(sprite);
        }

        paint = new Paint();
        drawDisplacement = new Vector2(0,0);

        //Init physics
        position = new Vector2(0,0);
        direction = new Vector2(0,0);
        velocity = new Vector2(0,0);
        speed = 0;
        if(GameView.instance != null){
            fixedDeltaTime = GameView.instance.fixedDeltaTime;
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    //Visuals
    //-----------------------------------------------------------------------------------------------------------
    public void draw(Canvas canvas){

        if(visible && sprite!=null) {

            //Rotate sprite around pivot
            float r = rotation;
            Vector2 pivot = position;
            if(centerPivot) {
                pivot = getCenter();
            }
            //Handle transformations if attached to a parent gameobject
            if(parent!= null){
                //Rotate sprite around pivot relative to parent
                r = localRotation+parent.rotation;
                Vector2 p = localPosition;
                //Displace from pivot relative to parent
                if(parent.centerPivot){
                    position = parent.getCenter().add(p);
                }
                else {
                    position = parent.getPos().add(p);
                }
            }

            //Drawing transformation matices
            RectF dst = getBounds();
            if(GameView.instance!=null) {
                dst.offset(GameView.instance.cameraDisp.x, GameView.instance.cameraDisp.y);
                matrix.setRectToRect(srcRect, dst, Matrix.ScaleToFit.FILL);
                matrix.postScale(scaleX, scaleY, pivot.x + GameView.instance.cameraDisp.x, pivot.y);
                matrix.postRotate(r, pivot.x + GameView.instance.cameraDisp.x, pivot.y + GameView.instance.cameraDisp.y);

                canvas.drawBitmap(sprite, matrix, paint);
            }
        }
    }

    //Sprite flipping
    public void flipX(){
        scaleX = -Math.abs(scaleX);
    }
    public void flipY(){
        scaleY = -Math.abs(scaleY);
    }

    public RectF getDrawTo(){
        return getBounds();
    }
    //-----------------------------------------------------------------------------------------------------------
    //Physics
    //-----------------------------------------------------------------------------------------------------------

    //Apply physics if simulated
    public void physics(float deltaTime){


        //If not attached to parent apply movement due to physics
        if(parent==null) {
            //Turn towards the direcion of movement
            if(facing) {
                rotation = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
                //Keep rotations in range of 0-360 degrees
                while (rotation>360){
                    rotation-=360;
                }
                while(rotation<0){
                    rotation+=360;
                }
            }
            if(simulated) {
                if(gravity>0){
                    gravity(deltaTime);
                }
                position = position.add(direction.multiply(speed * deltaTime));
                rotation += angularVelocity*deltaTime;
            }
        }
    }

    public boolean collision(GameObject other){

        boolean collided = false;
        if(other.getBounds().intersect(getBounds())) {
            collided = other.collisionCheck(this);
            if (collided) {
                onCollision(other);
            }
        }
        return collided;
    }



    protected boolean collisionCheck(GameObject other){
        return true;
    }

    protected void onCollision(GameObject other){

    }

    public void gravity(float deltaTime){
        if(GameView.instance != null) {
            if (position.y + height / 2 < GameView.instance.getGroundLevel()-height/4) {
                setVelocity(getVelocity().x, getVelocity().y + gravity * deltaTime/1000 );
            } else {
                onGrounded(GameView.instance.getGroundLevel()-height/4);
            }
        }
        else{
            setVelocity(getVelocity().x, getVelocity().y + gravity * deltaTime/1000 );
        }
    }


    protected  void onGrounded(float level){
        //if(speed > 0.1){

        //Game.instance.soundEffects.play(SoundEffects.HIT);
        //}
        setVelocity(speed*direction.x*friction,-speed*direction.y*bounce);
        position.y = level-height/2;
    }
    //-----------------------------------------------------------------------------------------------------------
    //Game logic
    //-----------------------------------------------------------------------------------------------------------

    //Non physics movement
    public void move(float deltaTime, float speedX, float speedY){
        position = position.add(new Vector2(speedX*deltaTime,speedY*deltaTime));
    }
    public void update(float deltaTime){

    }

    //-----------------------------------------------------------------------------------------------------------
    //Assessor mutator methods
    //-----------------------------------------------------------------------------------------------------------

    public void setWidth(float w){
        width= w;
        if(sprite!=null)
        height = w*(float)sprite.getHeight()/sprite.getWidth();
    }

    public void setHeight(float h){
        height = h;
        if(sprite!=null)
        width = h*(float)sprite.getWidth()/sprite.getHeight();
    }

    public void setScaleX(float sx){
        scaleX = sx;
    }
    public void setScaleY(float sy){
        scaleY = sy;
    }
    public void setScale(float sx, float sy){
        scaleX = sx;
        scaleY = sy;
    }

    public void setPos(float x, float y){
        position.x = x;
        position.y = y;
    }
    public void setPos(Vector2 p) {
        position = p;
    }
    public Vector2 getPos(){
        return position;
    }

    public void setDir(float x, float y){
        direction.x = x;
        direction.y = y;
        direction = direction.getNormal();
    }
    public void setDir(Vector2 d){
        direction = d.getNormal();
    }
    public Vector2 getDir(){
        return direction;
    }

    public RectF getBounds(){
        float top = position.y -offset.y*height*Math.abs(scaleY)+drawDisplacement.y;
        float left =position.x -offset.x*width*Math.abs(scaleX)+drawDisplacement.x;
        float bottom = (top+height*Math.abs(scaleY));
        float right = (left + width*Math.abs(scaleX));
        return new RectF(left,top,right,bottom);
    }
    public float getBottom(){
        return (getTop()+height*Math.abs(scaleY));
    }
    public float getTop(){
        return (position.y -offset.y*height*Math.abs(scaleY));
    }
    public float getLeft(){
        return (position.x -offset.x*width*Math.abs(scaleX));
    }
    public float getRight(){
        return (getLeft() + width*Math.abs(scaleX));
    }
    public Vector2 getCenter(){
        RectF b = getBounds();
        return new Vector2(b.centerX(), b.centerY());
    }

    public void setSpeed(float s){
        speed = s;
    }
    public float getSpeed(){
        return speed;
    }

    public void setVelocity(float vx, float vy){
        speed = (float)Math.sqrt(vx*vx + vy*vy);
        direction = new Vector2(vx,vy).getNormal();
        velocity = direction.multiply(speed);
    }
    public void setVelocity(Vector2 v){
        setVelocity(v.x,v.y);
    }
    public Vector2 getVelocity(){
        return direction.multiply(speed);
    }

    public void setParent(GameObject p){
        parent = p;


        if(p.centerPivot){
            localPosition = position.sub(p.getCenter());
        }
        else{
            localPosition = position.sub(p.getPos());
        }

        localRotation = rotation-p.rotation;
    }
    public void removeParent(){
        parent = null;
        localPosition = new Vector2(0,0);
        localRotation = 0;
    }

    public void setSprite(Bitmap sprite){
        srcRect = new RectF(0,0,sprite.getWidth(),sprite.getHeight());
        this.sprite = sprite;
    }

    public Bitmap getSprite(){
        return sprite;
    }

    @Override
    public int compareTo(Object o) {
        try{
            GameObject other = (GameObject)o;
            if(other != null){
                if(other.z < z){
                    return 1;
                }
                else if(other.z > z){
                    return -1;
                }
            }

        }
        catch(Exception e){

        }
        return 0;
    }
}

