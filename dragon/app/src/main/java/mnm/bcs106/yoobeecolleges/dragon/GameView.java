
package mnm.bcs106.yoobeecolleges.dragon;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;

//-----------------------------------------------------------------------------------------------------------
//Game engine
//Handles logic, physics and graphics
//-----------------------------------------------------------------------------------------------------------

public class GameView extends SurfaceView implements Runnable {
    public static GameView instance;
    int screenWidth, screenHeight;
    Vector2 screenCenter;

    final static int FPS = 30;
    final float fixedDeltaTime = (int) (1000 / FPS); // in milliseconds
    float deltaTime = fixedDeltaTime;

    //Physics
    public float groundLevel, gravity = 0.3f;
    int physicsIterations = 3;
    Vector2 cameraDisp = Vector2.zero;

    //Projectile
    int projectileIndex = 0;//Next projectile in array to spawn
    float shootSpeedVariance=0.1f, shootDirectionVariance = 10f;//Error in shooting speed and direction

    //Logic
    boolean isRunning = false;
    Thread gameThread;
    //WaveController waveController;//Controls when enemies spawn
    int enemyIndex = 0;//Next enemy in array to spawn

    //Game objects
    int maxEnemyCount = 3;
    public Dragon player;
    GameObject ground;


    //Drawing
    Bitmap wall;
    SurfaceHolder holder;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //-----------------------------------------------------------------------------------------------------------
    //Initialization
    //-----------------------------------------------------------------------------------------------------------

    void init(){
        //Singleton
        if(instance == null) {
            instance = this;
        }

        //Dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        screenCenter = new Vector2(screenWidth/2,screenHeight/2);

        holder = getHolder();

        //Ground gameobject
        Bitmap groundSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.ground);
        groundSprite = Bitmap.createScaledBitmap(groundSprite,screenWidth,screenHeight,false);
        ground =  new GameObject(groundSprite,0.5f,0.5f);
        ground.setPos(screenCenter);

        //Player gameobject
        Bitmap swordSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.sword_swipe);
        AreaEffect attack = new AreaEffect(swordSprite,0.5f,0.5f);
        attack.init(screenWidth/8,screenWidth/8,screenHeight/16,20,1,20);

        Bitmap playerSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.empty);
        player = new Dragon(playerSprite,0.5f,0.9f,screenHeight/20,screenHeight/20, attack);
        player.init(screenWidth/2, screenHeight-100,screenWidth/10, screenWidth/40,1f/2, 100);
        player.setDetection(screenHeight/10,90);

        player.setDamagedSound(SoundEffects.DAMAGE);
        player.setDestroyedSound(SoundEffects.DEATH);

        Game.instance.gameOver = false;
    }

    Vector2 randomPosition(){
        Vector2 random = Vector2.getRandom();
        random.x *= ground.width*0.3f;
        random.y *= ground.height*0.3f;
        return ground.position.add(random);
    }

    //-----------------------------------------------------------------------------------------------------------
    //Game loop
    //-----------------------------------------------------------------------------------------------------------

    public void resume() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        isRunning = false;
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (Exception e) {
                gameThread.stop();
            }
        }
    }

    public void run() {
        while (isRunning) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            long started = System.currentTimeMillis();

            //Apply physics calculations per frame
            for (int i = 0; i < physicsIterations; i++) {
                physics();
            }

            //Apply ame logic to game objects
            update();

            //Draw graphics to surface view
            draw();

            //If the time between frames does not match the target FPS, delay or skip to match
            deltaTime = (System.currentTimeMillis() - started);
            int lag = (int) (fixedDeltaTime - deltaTime);
            if (lag > 0) {
                try {
                    gameThread.sleep(lag);
                } catch (Exception e) {
                }
            }
            while (lag < 0) {
                lag += fixedDeltaTime;
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    //Graphics
    //-----------------------------------------------------------------------------------------------------------

    private void draw() {

        Canvas canvas = holder.lockCanvas(null);

        if (canvas != null) {
            Paint background = new Paint();
            background.setColor(Color.WHITE);
            canvas.drawRect(0,0,screenWidth,screenHeight, background);
            //Draw ground
            ground.draw(canvas);


            holder.unlockCanvasAndPost(canvas);
        }

    }

    //-----------------------------------------------------------------------------------------------------------
    //Physics
    //-----------------------------------------------------------------------------------------------------------
    private void physics() {
        cameraDisp.x = -player.position.x+screenWidth/2;
        if(!Game.instance.gameOver) {



            //Enemy motion
            if (!player.destroyed) {

                player.physics(deltaTime / physicsIterations);

            }
        }
    }

    public boolean outOfBounds(GameObject g){
        float r = g.radius;
        RectF bounds = g.getBounds();
        Vector2 v = g.getVelocity();

        boolean out = false;
        /*
        if(bounds.left < ground.getLeft()){
            g.setVelocity(g.bounce*Math.abs(v.x), g.friction*v.y);
            g.position.x = ground.getLeft()+ g.offset.x*g.width + 5;
            out = true;
        }
        else if(bounds.right > ground.getRight()){
            g.setVelocity(-g.bounce*Math.abs(v.x), g.friction*v.y);
            g.position.x = ground.getRight()-(1-g.offset.x)*g.width - 5;
            out = true;
        }*/
        if(bounds.top < ground.getTop()){
            g.setVelocity(g.friction*v.x, g.bounce*Math.abs(v.y));
            g.position.y = ground.getTop()+ g.offset.y*g.height + 5;
            out = true;
        }
        else if(bounds.bottom > ground.getBottom()){
            g.setVelocity(g.friction*v.x, -g.bounce*Math.abs(v.y));
            g.position.y = ground.getBottom() - (1-g.offset.y)*g.height - 5;
            out = true;
        }
        return out;

    }


    //If gameobject is above ground level, apply gravity
    public void gravity(GameObject g) {
        if (g.position.y  < groundLevel) {
            g.setVelocity(g.getVelocity().x, g.getVelocity().y + gravity * fixedDeltaTime / 1000 / physicsIterations);
        } else {
            g.onGrounded(groundLevel);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    //Game logic
    //-----------------------------------------------------------------------------------------------------------
    private void update() {
        if(player.visible){

            player.update(deltaTime);


            //If the tower is destroyed, tell the game controller to show game over alert dialog

        }
        else{
            if(!Game.instance.gameOver) {
                Game.instance.showGameOver = true;
            }
        }
    }
    //Move to position
    public void setPlayerMovement(Vector2 moveTo){
        player.move(moveTo);
    }
    public void movePlayerBy(Vector2 moveBy){
        player.moveBy(moveBy);
    }
}






