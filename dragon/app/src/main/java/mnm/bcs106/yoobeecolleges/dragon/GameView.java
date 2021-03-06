
package mnm.bcs106.yoobeecolleges.dragon;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

//-----------------------------------------------------------------------------------------------------------
//Game engine
//Handles logic, physics and graphics
//-----------------------------------------------------------------------------------------------------------

public class GameView extends SurfaceView implements Runnable {
    public static GameView instance;
    int screenWidth, screenHeight, cameraSize;
    Vector2 screenCenter;


    final float fixedDeltaTime = (int) (1000 / Game.instance.refreshRating); // in milliseconds
    // //final float fixedDeltaTime = (int) (1000 / 60); // in milliseconds
    public float timeScale = 1;
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
    //Game objects
    public Dragon player;
    GameObject ground;
    NPC_Pool npc_pool;
    GoldPool goldPool;
    ProjectilePool projectilePool;
    FirePool firePool;
    Lair lair;

    Hud hud;

    //Drawing
    public boolean drawHUD = true;
    SurfaceHolder holder;
    Paint back = new Paint();
    SpriteManager spriteManager;
    boolean isDrawing = true;

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
        instance = this;
        isRunning = false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        cameraSize = screenWidth;
        screenCenter = new Vector2(screenWidth/2,screenHeight/2);
        back.setColor(Color.BLACK);
        holder = getHolder();

        groundLevel = screenHeight*7/10;

        spriteManager = new SpriteManager(getResources(),cameraSize);
        //Init scene

        npc_pool = new NPC_Pool();
        goldPool = new GoldPool();
        projectilePool = new ProjectilePool();
        firePool = new FirePool();
        scene = new Scene();

        //Player gameobject
        Bitmap playerSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.empty);
        player = new Dragon(playerSprite,0.5f,0.9f,screenHeight/20,screenHeight/20);
        player.setDamagedSound(SoundEffects.DAMAGE);
        player.setDestroyedSound(SoundEffects.DEATH);
        lair = new Lair();

        drawHUD = true;

        hud = new Hud();





        //npc_pool.spawnThiefs((int)fortress.x ,(int)groundLevel,1);
        //npc_pool.spawnDragonLayers((int)fortress.x ,(int)groundLevel,1);
        //npc_pool.spawnWizard((int)fortress.x ,(int)groundLevel,1);
        //npc_pool.spawnTribute((int)screenWidth*2,(int)groundLevel,150, scene.finalFort);
        Game.instance.gameOver = false;
        timeScale = 1;
        Music.instance.startFadeOut(6000);
        Game.instance.stopButton.setVisibility(VISIBLE);
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
        SoundEffects.instance.soundPool.autoResume();
        gameThread.start();
    }

    public void pause() {
        isRunning = false;
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();//execute completely and then stop
                SoundEffects.instance.soundPool.autoPause();
                Music.instance.stopDeathMusic();
                Music.instance.stopThemeMusic();
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

            //Apply game logic to game objects

            update();


            long updateTime = System.currentTimeMillis();
            //System.out.println( "update " + (updateTime-physicsTime));
            if(isDrawing) {
                draw();
            }
            long drawTime = System.currentTimeMillis() - updateTime;
            //System.out.println( "draw main " + drawTime);
            totalFrame += drawTime;
            numberFrame++;
            //System.out.println("average draw " + totalFrame/numberFrame);

            //If the time between frames does not match the target FPS, delay or skip to match

            deltaTime = (System.currentTimeMillis() - started);
            int lag = (int) (fixedDeltaTime/timeScale - deltaTime);

            //System.out.println(deltaTime + " " + fixedDeltaTime + " " + lag);
            if (lag > 0) {
                try {
                    Thread.sleep(lag);
                } catch (Exception e) {
                }
            }
            while (lag < 0) {
                lag += fixedDeltaTime/timeScale;
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

        Canvas canvas = new Canvas();
        try {
            canvas = holder.lockCanvas(null);
        }
        catch (Exception e){

        }
        if (canvas != null) {
            //90
            //canvas.drawRect(0, 0, screenWidth * 1.2f, screenHeight, back);
            scene.drawBackground(canvas);//40
            lair.drawBackground(canvas);//80

            projectilePool.draw(canvas);//80
            npc_pool.draw(canvas);//90

            goldPool.draw(canvas);//
            player.draw(canvas);//80
            lair.drawForeground(canvas);

            firePool.draw(canvas);
            scene.drawForeground(canvas);//

            if(drawHUD) {
                hud.draw(canvas);
            }


            holder.unlockCanvasAndPost(canvas);
        }
    }


    //-----------------------------------------------------------------------------------------------------------
    //Physics
    //-----------------------------------------------------------------------------------------------------------
    private void physics() {
        cameraDisp.x = -player.position.x+screenWidth/2;


        npc_pool.physics(fixedDeltaTime);

        //Enemy motion

        goldPool.physics(fixedDeltaTime / physicsIterations);
        projectilePool.physics(fixedDeltaTime / physicsIterations);

        player.physics(fixedDeltaTime / physicsIterations);
        lair.physics(fixedDeltaTime / physicsIterations);
        scene.physics(fixedDeltaTime/physicsIterations);


    }

    //-----------------------------------------------------------------------------------------------------------
    //Game logic
    //-----------------------------------------------------------------------------------------------------------

    private void update() {

        Music.instance.update(fixedDeltaTime);
        if(player.health<0 && player.visible ){
            drawHUD = false;
            Music.instance.playDeathMusic();

        }
        if(!player.visible) {
            if (!Game.instance.gameOver) {
                timeScale = 0f;
                Game.instance.showGameOver = true;
                SoundEffects.instance.pauseAll();


            }
        }
        else{
            player.update(fixedDeltaTime);
            scene.update(fixedDeltaTime);
            npc_pool.update(fixedDeltaTime);
            projectilePool.update(fixedDeltaTime);
            //System.out.println(fixedDeltaTime +" "+ deltaTime);
            goldPool.update(fixedDeltaTime);
            hud.update(fixedDeltaTime);
            lair.update(fixedDeltaTime);
            firePool.update(fixedDeltaTime);
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

    public float getGroundLevel(){
        return  groundLevel;
    }
    public void breathFire(boolean breathingFire){

        player.breathingFire = breathingFire;

    }
}






