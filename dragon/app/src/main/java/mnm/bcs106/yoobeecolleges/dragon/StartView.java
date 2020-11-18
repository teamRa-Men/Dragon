
package mnm.bcs106.yoobeecolleges.dragon;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

//-----------------------------------------------------------------------------------------------------------
//Game engine
//Handles logic, physics and graphics
//-----------------------------------------------------------------------------------------------------------

public class StartView extends SurfaceView {

    int screenWidth, screenHeight, cameraSize;
    Vector2 screenCenter;
    Vector2 moveBy;
    public StartDragon player;
    SurfaceHolder holder;
    public static StartView instance;
    Paint back = new Paint();
    Bitmap ground, hillsBackground, mountainBackground;

    public StartView(Context context) {
        super(context);
        init();
    }

    public StartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public StartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //-----------------------------------------------------------------------------------------------------------
    //Initialization
    //-----------------------------------------------------------------------------------------------------------

    Bitmap[] clouds = new Bitmap[8];
    int width, height;
    float mountainX, hillsX, groundX;
    int groundX2, groundX1, groundX0, mountainX2, mountainX1, mountainX0, hillsX2, hillsX1, hillsX0;
    Paint skyPaint, backPaint;
    int move = 0;

    void init() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        cameraSize = screenWidth;

        holder = getHolder();

        instance = this;
        SpriteManager spriteManager = new SpriteManager(getResources(), screenWidth);

        //Player gameobject
        Bitmap playerSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.empty);
        player = new StartDragon(playerSprite, 0.5f, 0.9f, screenWidth, screenHeight);

        back.setColor(Color.WHITE);

        width = (int) (screenWidth * 1.2f);
        height = screenHeight;

        Bitmap sheet = SpriteManager.instance.environmentSheet;
        Rect r = SpriteManager.instance.getEnvironmentSpriteRect("Ground");
        ground = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        ground = Bitmap.createScaledBitmap(ground, width, width / r.width() * r.height(), false);//(int)((height-gameView.groundLevel)*1.3),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Hills");
        hillsBackground = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        hillsBackground = Bitmap.createScaledBitmap(hillsBackground, width, width / r.width() * r.height(), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Mountains");
        mountainBackground = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        mountainBackground = Bitmap.createScaledBitmap(mountainBackground, width, width / r.width() * r.height(), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud1");
        clouds[0] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[0] = Bitmap.createScaledBitmap(clouds[0], width / 5, (int) ((float) clouds[0].getHeight() / clouds[0].getWidth() * width / 5), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud2");
        clouds[1] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[1] = Bitmap.createScaledBitmap(clouds[1], width / 5, (int) ((float) clouds[1].getHeight() / clouds[1].getWidth() * width / 5), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud3");
        clouds[2] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[2] = Bitmap.createScaledBitmap(clouds[2], width / 5, (int) ((float) clouds[2].getHeight() / clouds[2].getWidth() * width / 6), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud1");
        clouds[3] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[3] = Bitmap.createScaledBitmap(clouds[3], width / 7, (int) ((float) clouds[3].getHeight() / clouds[3].getWidth() * width / 7), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud2");
        clouds[4] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[4] = Bitmap.createScaledBitmap(clouds[4], width / 7, (int) ((float) clouds[4].getHeight() / clouds[4].getWidth() * width / 7), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud3");
        clouds[5] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[5] = Bitmap.createScaledBitmap(clouds[5], width / 7, (int) ((float) clouds[5].getHeight() / clouds[5].getWidth() * width / 8), false);
        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud1");
        clouds[6] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[6] = Bitmap.createScaledBitmap(clouds[6], width / 8, (int) ((float) clouds[6].getHeight() / clouds[6].getWidth() * width / 9), false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud3");
        clouds[7] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
        clouds[7] = Bitmap.createScaledBitmap(clouds[7], width / 4, (int) ((float) clouds[7].getHeight() / clouds[7].getWidth() * width / 5), false);



        skyPaint = new Paint();
        skyPaint.setColor(Color.rgb(240, 250, 255));


        for(int i = 0; i < clouds.length; i++){
            cloudPositions[i] = new Vector2(width*(float)Math.random(), height*(float)Math.random());

        }
    }

    Vector2[] cloudPositions = new Vector2[8];
    public void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, skyPaint);
        for (int i = 0; i < clouds.length; i++) {
            canvas.drawBitmap(clouds[i], mountainX+cloudPositions[i].x, cloudPositions[i].y, backPaint);
            if(cloudPositions[i].x < clouds[i].getWidth()){
                cloudPositions[i].x = width + width*(float)Math.random();
            }
        }

        canvas.drawBitmap(mountainBackground, mountainX + mountainX0, height - mountainBackground.getHeight() * 0.9f, backPaint);
        canvas.drawBitmap(mountainBackground, mountainX + mountainX1, height - mountainBackground.getHeight() * 0.9f, backPaint);
        canvas.drawBitmap(mountainBackground, mountainX + mountainX2, height - mountainBackground.getHeight() * 0.9f, backPaint);

        canvas.drawBitmap(hillsBackground, hillsX + hillsX0, height - hillsBackground.getHeight() * 0.9f, backPaint);
        canvas.drawBitmap(hillsBackground, hillsX + hillsX1, height - hillsBackground.getHeight() * 0.9f, backPaint);
        canvas.drawBitmap(hillsBackground, hillsX + hillsX2, height - hillsBackground.getHeight() * 0.9f, backPaint);



    }

    Paint black = new Paint();
    public void drawForeground(Canvas canvas) {
        black.setColor(Color.BLACK);
        canvas.drawBitmap(ground, groundX + groundX0, height - ground.getHeight(), null);
        canvas.drawBitmap(ground, groundX + groundX1, height - ground.getHeight(), null);
        canvas.drawBitmap(ground, groundX + groundX2, height - ground.getHeight(), null);
        canvas.drawRect(0, height, width, height + height/2, black);
    }






    //-----------------------------------------------------------------------------------------------------------
    //Game loop
    //-----------------------------------------------------------------------------------------------------------


    public void draw() {
        Canvas canvas = new Canvas();
        try {
            canvas = holder.lockCanvas(null);
        }
        catch (Exception e){

        }
        if (canvas != null) {
            //90
            //canvas.drawRect(0, 0, screenWidth * 1.2f, screenHeight, back);

            drawBackground(canvas);
            player.draw(canvas);//80
            drawForeground(canvas);

            holder.unlockCanvasAndPost(canvas);
        }
    }


    //-----------------------------------------------------------------------------------------------------------
    //Physics
    //-----------------------------------------------------------------------------------------------------------
    public void physics(float fixedDeltaTime) {

        player.physics(fixedDeltaTime);
    }

    //-----------------------------------------------------------------------------------------------------------
    //Game logic
    //-----------------------------------------------------------------------------------------------------------
    public void update(float fixedDeltaTime) {
        move -= fixedDeltaTime/4;

        player.update(fixedDeltaTime);
        mountainX = move/2;
        hillsX = move*3/4;
        groundX = move;

        groundX0 = (int)(-groundX/width)*width;
        groundX1 = groundX0 - width;
        groundX2 = groundX0 + width;
        hillsX0 = (int)(-hillsX/width)*width;
        hillsX1 = (int)(-hillsX/width-1)*width;
        hillsX2 = (int)(-hillsX/width+1)*width;
        mountainX0 = (int)(-mountainX/width)*width;
        mountainX1 = (int)(-mountainX/width-1)*width;
        mountainX2 = (int)(-mountainX/width+1)*width;

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



}






