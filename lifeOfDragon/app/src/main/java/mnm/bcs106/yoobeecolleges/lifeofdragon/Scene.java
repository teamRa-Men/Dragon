package mnm.bcs106.yoobeecolleges.lifeofdragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Scene {
    Bitmap sky, ground,mountainBackground, hillsBackground, sea;
    int height, width;
    float mountainX, hillsX, groundX;
    int timeOfDay;
    float time;

    int  groundX2, groundX1, groundX0, mountainX2, mountainX1, mountainX0, hillsX2, hillsX1, hillsX0;
    Dragon player;

    Paint backPaint,frontPaint;

    GameView gameView;
    public Scene(){
        gameView = GameView.instance;
        player = gameView.player;
        width = (int)(gameView.screenWidth*1.5);
        height = gameView.cameraSize;
        sky = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.cloudy_sky);
        sky = Bitmap.createScaledBitmap(sky, width, (int) gameView.groundLevel,false);
        ground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.ground);
        ground = Bitmap.createScaledBitmap(ground, width,height/10,false);
        mountainBackground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mountain_background);
        mountainBackground = Bitmap.createScaledBitmap(mountainBackground, width,(int)(height/5),false);
        hillsBackground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.hills_background);
        hillsBackground = Bitmap.createScaledBitmap(hillsBackground, width,(int)(height/5),false);
        backPaint = new Paint();
        frontPaint = new Paint();
        frontPaint.setColor(Game.instance.getResources().getColor(R.color.colorDragon));
        backPaint.setColor(Color.rgb(timeOfDay,timeOfDay,timeOfDay));
    }

    public void drawBackground(Canvas canvas){
        canvas.drawRect(0, 0, gameView.screenWidth*1.2f,gameView.screenHeight, backPaint);


        canvas.drawBitmap(mountainBackground, mountainX+ mountainX0-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(mountainBackground, mountainX+ mountainX1-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(mountainBackground, mountainX+ mountainX2-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);

        canvas.drawBitmap(hillsBackground, hillsX+ hillsX0-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX1-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX2-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);

        canvas.drawBitmap(sky, gameView.cameraDisp.x + groundX0-width/4,0,null);
        canvas.drawBitmap(sky, gameView.cameraDisp.x+ groundX1-width/4,0,null);
        canvas.drawBitmap(sky, gameView.cameraDisp.x+ groundX2-width/4,0,null);

    }
    public void drawForeground(Canvas canvas){

        canvas.drawBitmap(ground, groundX+ groundX0-width/4,gameView.groundLevel-ground.getHeight()/12,null);
        canvas.drawBitmap(ground, groundX+ groundX1-width/4,gameView.groundLevel-ground.getHeight()/12,null);
        canvas.drawBitmap(ground, groundX+ groundX2-width/4,gameView.groundLevel-ground.getHeight()/12,null);
        canvas.drawRect(0, gameView.groundLevel+ground.getHeight()/12, gameView.screenWidth*1.2f,gameView.screenHeight*1.2f, frontPaint);
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

        backPaint.setColor(Color.rgb(timeOfDay,timeOfDay,timeOfDay));
        time += deltaTime;
        timeOfDay = (int)(250*Math.max(Math.min(Math.sin(time/1000/10)*2+0.5,1),0));
    }
}