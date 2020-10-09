
package mnm.bcs106.yoobeecolleges.dragon;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
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
    int screenWidth, screenHeight, cameraSize;
    Vector2 screenCenter;
    float zoom;

    final static int FPS = 30;
    final float fixedDeltaTime = (int) (1000 / FPS); // in milliseconds
    float deltaTime = fixedDeltaTime;

    //Physics
    public float groundLevel, upperBound, gravity = 0.3f;
    int physicsIterations = 5;
    Vector2 cameraDisp = Vector2.zero;

    //Projectile
    int projectileIndex = 0;//Next projectile in array to spawn
    float shootSpeedVariance=0.1f, shootDirectionVariance = 10f;//Error in shooting speed and direction

    //Logic
    boolean isRunning = false;
    Thread gameThread, drawThread;
    //WaveController waveController;//Controls when enemies spawn
    int enemyIndex = 0;//Next enemy in array to spawn
    Vector2 moveBy;

    //Scene
    Scene scene;

    //Game objects
    int maxEnemyCount = 3;
    public Dragon player;
    GameObject ground;
    Wooloo npc;


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
        cameraSize = screenWidth;
        screenCenter = new Vector2(screenWidth/2,screenHeight/2);

        holder = getHolder();

        groundLevel = screenHeight*9/10;

        //Player gameobject
        Bitmap playerSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.circle);
        player = new Dragon(playerSprite,0.5f,0.9f,screenHeight/20,screenHeight/20);

        npc = new Wooloo(playerSprite,0+playerSprite.getWidth(),100,0.1f,100,playerSprite.getWidth()/3,playerSprite.getHeight()/3, 500);

        player.setDamagedSound(SoundEffects.DAMAGE);
        player.setDestroyedSound(SoundEffects.DEATH);

        //Init scene
        scene = new Scene();

        Game.instance.gameOver = false;

        resume();

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
    float totalFrame, numberFrame;
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

            long started = System.currentTimeMillis();
            //Apply physics calculations per frame
            for (int i = 0; i < physicsIterations; i++) {
                physics();
            }
            long physicsTime = System.currentTimeMillis();
            //System.out.println( "physics " + (physicsTime - started));

            //Apply ame logic to game objects

            update();
            long updateTime = System.currentTimeMillis();
            //System.out.println( "update " + (updateTime-physicsTime));


            draw();
            //long drawTime = System.currentTimeMillis() - updateTime;
            //System.out.println( "draw main " + drawTime);
            //totalFrame += drawTime;
            //numberFrame++;
            //System.out.println("average main " + totalFrame/numberFrame);

            //If the time between frames does not match the target FPS, delay or skip to match

            deltaTime = (System.currentTimeMillis() - started);
            int lag = (int) (fixedDeltaTime - deltaTime);

            //System.out.println(deltaTime + " " + fixedDeltaTime + " " + lag);
            if (lag > 0) {
                try {
                    gameThread.sleep(lag);
                } catch (Exception e) {
                }
            }
            while (lag < 0) {
                lag += fixedDeltaTime;
                //Apply physics calculations per frame
                for (int i = 0; i < physicsIterations; i++) {
                    physics();
                }
                //Apply game logic to game objects
                update();
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
            background.setColor(Color.BLACK);
            canvas.drawRect(0,0,screenWidth*1.2f,screenHeight, background);
            //Draw ground
            //ground.draw(canvas);
            scene.draw(canvas);

            player.draw(canvas);
            npc.draw(canvas);
            //Draw Controls
            Vector2 dragFrom = Game.instance.dragFrom;
            Vector2 dragTo = Game.instance.dragTo;
            Paint p = new Paint();

            p.setColor(Color.WHITE);
            p.setAlpha(100);
            if(dragFrom !=null && dragTo!=null) {
                p.setStrokeWidth(20);
                p.setStrokeCap(Paint.Cap.ROUND);
                canvas.drawLine(dragFrom.x, dragFrom.y, dragTo.x, dragTo.y, p);
            }

            canvas.drawCircle(Game.instance.fireButton.x,Game.instance.fireButton.y,Game.instance.controlRadius,p);
            Bitmap fireButtonSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.flame5_minimalism);
            fireButtonSprite = Bitmap.createScaledBitmap(fireButtonSprite, Game.instance.controlRadius*2,Game.instance.controlRadius*2, false);
            p.setColorFilter(new LightingColorFilter(Game.instance.getResources().getColor(R.color.colorFire),0));
            if(player.breathingFire){
                p.setAlpha(255);
            }
            canvas.drawBitmap(fireButtonSprite,Game.instance.fireButton.x-Game.instance.controlRadius, Game.instance.fireButton.y-Game.instance.controlRadius, p);

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

                player.physics(fixedDeltaTime / physicsIterations);
            }
        }
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
            player.update(fixedDeltaTime);
            scene.update(fixedDeltaTime);
            npc.update(fixedDeltaTime);
        }
        else{
            if(!Game.instance.gameOver) {
                Game.instance.showGameOver = true;
            }
        }
    }
    public void movePlayerBy(Vector2 dv){

        if(dv!=null){
            this.moveBy = new Vector2(dv.x,dv.y);
            player.moveBy(moveBy.multiply(player.maxMoveSpeed));

        }
        else {
            player.moveBy(dv);
        }
    }
    public void breathFire(boolean breathingFire){
        player.breathingFire = breathingFire;
    }
}






