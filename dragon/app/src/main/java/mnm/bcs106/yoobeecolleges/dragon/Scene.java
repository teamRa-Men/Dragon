package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class Scene {
    Bitmap sky, ground,mountainBackground, hillsBackground, sea;
    int height, width;
    float mountainX, hillsX, groundX;
    float timeOfDay;
    int dayLength;
    int day;
    public static Scene instance;

    int  groundX2, groundX1, groundX0, mountainX2, mountainX1, mountainX0, hillsX2, hillsX1, hillsX0;
    Dragon player;

    Paint skyPaint, backPaint,frontPaint;

    GameView gameView;
    public Scene(){
        gameView = GameView.instance;
        instance = this;

        dayLength = 1*60*1000;

        player = gameView.player;
        width = (int)(gameView.screenWidth*1.2);
        height = gameView.screenHeight;
        sky = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.cloudy_sky);
        sky = Bitmap.createScaledBitmap(sky, width, sky.getHeight()*width/sky.getWidth(),false);
        ground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.ground);
        ground = Bitmap.createScaledBitmap(ground, width,(int)((height-gameView.groundLevel)*1.02),false);
        mountainBackground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mountain_background);
        mountainBackground = Bitmap.createScaledBitmap(mountainBackground, width,height,false);
        hillsBackground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.hills_background);
        hillsBackground = Bitmap.createScaledBitmap(hillsBackground, width,(int)(height/10),false);
        backPaint = new Paint();
        frontPaint = new Paint();
        skyPaint = new Paint();
;        frontPaint.setColor(Game.instance.getResources().getColor(R.color.colorPrimaryDark));
        backPaint.setColor(Color.rgb(250,250,255));
        skyPaint.setColor(Color.rgb(250,250,255));
    }

    public void drawBackground(Canvas canvas){
        canvas.drawRect(0, 0, gameView.screenWidth*1.2f,gameView.screenHeight*1.2f, skyPaint);
        //canvas.drawBitmap(sky, gameView.cameraDisp.x + groundX0-width/4,0,backPaint);
        //canvas.drawBitmap(sky, gameView.cameraDisp.x+ groundX1-width/4,0,backPaint);
        //canvas.drawBitmap(sky, gameView.cameraDisp.x+ groundX2-width/4,0,backPaint);


        //canvas.drawBitmap(mountainBackground, mountainX+ mountainX0,0,backPaint);
        //canvas.drawBitmap(mountainBackground, mountainX+ mountainX1,0,backPaint);
        //canvas.drawBitmap(mountainBackground, mountainX+ mountainX2,0,backPaint);

        canvas.drawBitmap(hillsBackground, hillsX+ hillsX0,gameView.groundLevel-hillsBackground.getHeight()*0.9f,backPaint);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX1,gameView.groundLevel-hillsBackground.getHeight()*0.9f,backPaint);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX2,gameView.groundLevel-hillsBackground.getHeight()*0.9f,backPaint);


    }
    public void drawForeground(Canvas canvas){

        canvas.drawBitmap(ground, groundX+ groundX0,gameView.screenHeight-ground.getHeight(),backPaint);
        canvas.drawBitmap(ground, groundX+ groundX1,gameView.screenHeight-ground.getHeight(),backPaint);
        canvas.drawBitmap(ground, groundX+ groundX2,gameView.screenHeight-ground.getHeight(),backPaint);
        //canvas.drawRect(0, gameView.groundLevel+ground.getHeight()/12, gameView.screenWidth*1.2f,gameView.screenHeight*1.2f, frontPaint);
    }


    public void update(float deltaTime){
        mountainX = gameView.cameraDisp.x/4;
        hillsX = gameView.cameraDisp.x/2;
        groundX = gameView.cameraDisp.x;

        groundX0 = (int)(-groundX/width)*width;
        groundX1 = groundX0 - width;
        groundX2 = groundX0 + width;
        hillsX0 = (int)(-hillsX/width)*width;
        hillsX1 = (int)(-hillsX/width-1)*width;
        hillsX2 = (int)(-hillsX/width+1)*width;
        mountainX0 = (int)(-mountainX/width)*width;
        mountainX1 = (int)(-mountainX/width-1)*width;
        mountainX2 = (int)(-mountainX/width+1)*width;

        //backPaint.setColor(Color.rgb(timeOfDay-10,timeOfDay-10,timeOfDay));
        int bc =  (int)(255*Math.max(Math.min(Math.sin(timeOfDay/dayLength*Math.PI*2)*4,1),0.2));
        int fc =  (int)(255*Math.max(Math.min(Math.sin(timeOfDay/dayLength*Math.PI*2)*4,1),0.4));
        //System.out.println(c);
        backPaint.setColorFilter(new LightingColorFilter(Color.rgb(bc,bc,bc),0));
        frontPaint.setColorFilter(new LightingColorFilter(Color.rgb(fc,fc,fc),0));

        skyPaint.setColor(Color.rgb((int)(bc*0.95f),(int)(bc*0.95f),bc));

        timeOfDay +=deltaTime;
        //System.out.println(timeOfDay/dayLength);
        if(timeOfDay > dayLength){
            timeOfDay = 0;
            day++;
        }
    }
}
