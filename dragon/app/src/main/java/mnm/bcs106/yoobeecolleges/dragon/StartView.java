
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

    void init(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        cameraSize = screenWidth;

        holder = getHolder();

        instance = this;
        SpriteManager spriteManager = new SpriteManager(getResources(),screenWidth);

        //Player gameobject
        Bitmap playerSprite = BitmapFactory.decodeResource(this.getResources(), R.drawable.empty);
        player = new StartDragon(playerSprite,0.5f,0.9f,screenHeight,screenHeight);

        back.setColor(Color.WHITE);

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
            canvas.drawRect(0, 0, screenWidth * 1.2f, screenHeight, back);

            player.draw(canvas);//80

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

            player.update(fixedDeltaTime);


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






