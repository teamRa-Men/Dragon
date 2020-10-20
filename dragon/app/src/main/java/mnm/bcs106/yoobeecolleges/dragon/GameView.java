
package mnm.bcs106.yoobeecolleges.dragon;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//-----------------------------------------------------------------------------------------------------------
//Game engine
//Handles logic, physics and graphics
//-----------------------------------------------------------------------------------------------------------

public class GameView extends SurfaceView implements Runnable {
    public static GameView instance;
    int screenWidth, screenHeight, cameraSize;
    Vector2 screenCenter;
    float zoom;


    //final float fixedDeltaTime = (int) (1000 / Game.instance.refreshRating); // in milliseconds
    final float fixedDeltaTime = (int) (1000 / 30); // in milliseconds

    float deltaTime = fixedDeltaTime;

    //Physics
    public float groundLevel, upperBound, gravity = 0.3f;
    int physicsIterations = 3;
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
    public Dragon player;
    GameObject ground;
    NPC_Pool npc_pool;
    GoldPool goldPool;
    ProjectilePool projectilePool;
    Lair lair;
    Fortress fortress;
    Hud hud;

    //Drawing
    SurfaceHolder holder;
    Paint back = new Paint();

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

            instance = this;

        //System.out.println(fixedDeltaTime);
        //Dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        cameraSize = (int) (screenWidth);
        screenCenter = new Vector2(screenWidth/2,screenHeight/2);
        back.setColor(Color.WHITE);

        holder = getHolder();

        groundLevel = screenHeight*7/10;

        //Player gameobject
        Bitmap playerSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.empty);
        player = new Dragon(playerSprite,0.5f,0.9f,screenHeight/20,screenHeight/20);

        npc_pool = new NPC_Pool();
        //npc_pool.spawnWizard(500,500,3);

        goldPool = new GoldPool();
        //GoldPool.instance.spawnGold(screenHeight/2, screenWidth/4,100);
        projectilePool = new ProjectilePool();

        player.setDamagedSound(SoundEffects.DAMAGE);
        player.setDestroyedSound(SoundEffects.DEATH);

        hud = new Hud();

        //Init scene
        scene = new Scene();
        lair = new Lair();


        fortress = new Fortress( screenWidth*2, (int)groundLevel, true, this);

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

        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) {
                    long started = System.currentTimeMillis();
                    draw();
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
        });
        //drawThread.start();
    }

    public void pause() {
        isRunning = false;
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();//execute completely and then stop
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

            draw();
            long drawTime = System.currentTimeMillis() - updateTime;
            //System.out.println( "draw main " + drawTime);
            totalFrame += drawTime;
            numberFrame++;
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
                //90
                //canvas.drawRect(0, 0, screenWidth * 1.2f, screenHeight, back);
                scene.drawBackground(canvas);//40
                //lair.draw(canvas);//80
                fortress.draw(canvas);//90
                projectilePool.draw(canvas);//80
                player.draw(canvas);//80
                npc_pool.draw(canvas);//90
                scene.drawForeground(canvas);//
                goldPool.draw(canvas);//

                hud.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
    }

    //-----------------------------------------------------------------------------------------------------------
    //Physics
    //-----------------------------------------------------------------------------------------------------------
    private void physics() {
        cameraDisp.x = -player.position.x+screenWidth/2;
        if(!Game.instance.gameOver) {

            npc_pool.physics(fixedDeltaTime);
            fortress.physics(deltaTime);
            //Enemy motion
            if (!player.destroyed) {
                goldPool.physics(fixedDeltaTime / physicsIterations);
                projectilePool.physics(fixedDeltaTime / physicsIterations);
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
            npc_pool.update(fixedDeltaTime);
            projectilePool.update(fixedDeltaTime);
            //System.out.println(fixedDeltaTime +" "+ deltaTime);
            goldPool.update(fixedDeltaTime);
            fortress.update(fixedDeltaTime);
            hud.update(deltaTime);
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

    public float getGroundLevel(){
        return  groundLevel;
    }
    public void breathFire(boolean breathingFire){

        player.breathingFire = breathingFire;

    }
}






