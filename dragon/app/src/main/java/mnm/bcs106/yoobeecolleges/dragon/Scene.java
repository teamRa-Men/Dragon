package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Scene {
    Bitmap sky, ground,mountainBackground, hillsBackground, sea;
    int height, width;
    float mountainX, hillsX;

    int  groundX2, groundX1, groundX0, mountainX2, mountainX1, mountainX0, hillsX2, hillsX1, hillsX0;
    Dragon player;

    GameView gameView;
    public Scene(){
        gameView = GameView.instance;
        player = gameView.player;
        width = gameView.screenWidth*2;
        height = gameView.screenHeight;
        sky = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.cloudy_sky);
        sky = Bitmap.createScaledBitmap(sky, width,height,false);
        ground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.ground);
        ground = Bitmap.createScaledBitmap(ground, width,(int)((height - (int)gameView.groundLevel)*1.1f),false);
        mountainBackground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mountain_background);
        mountainBackground = Bitmap.createScaledBitmap(mountainBackground, width,(int)(height/5),false);
        hillsBackground = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.hills_background);
        hillsBackground = Bitmap.createScaledBitmap(hillsBackground, width,(int)(height/5),false);

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(sky, gameView.cameraDisp.x + groundX0-width/4,0,null);
        canvas.drawBitmap(sky, gameView.cameraDisp.x+ groundX1-width/4,0,null);
        canvas.drawBitmap(sky, gameView.cameraDisp.x+ groundX2-width/4,0,null);


        canvas.drawBitmap(mountainBackground, mountainX+ mountainX0-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(mountainBackground, mountainX+ mountainX1-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(mountainBackground, mountainX+ mountainX2-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);

        canvas.drawBitmap(hillsBackground, hillsX+ hillsX0-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX1-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX2-width/4,gameView.groundLevel-mountainBackground.getHeight(),null);

        canvas.drawBitmap(ground, gameView.cameraDisp.x+ groundX0-width/4,height-ground.getHeight(),null);
        canvas.drawBitmap(ground, gameView.cameraDisp.x+ groundX1-width/4,height-ground.getHeight(),null);
        canvas.drawBitmap(ground, gameView.cameraDisp.x+ groundX2-width/4,height-ground.getHeight(),null);
    }

    public void update(float deltaTime){
        float dv = -player.speed*Math.signum(player.direction.x);
        mountainX += dv*deltaTime/4;
        hillsX += dv*deltaTime/2;
        groundX0 = (int)(-gameView.cameraDisp.x/width)*width;
        groundX1 = (int)(-gameView.cameraDisp.x/width-1)*width;
        groundX2 = (int)(-gameView.cameraDisp.x/width +1)*width;
        hillsX0 = (int)(-hillsX/width)*width;
        hillsX1 = (int)(-hillsX/width-1)*width;
        hillsX2 = (int)(-hillsX/width+1)*width;
        mountainX0 = (int)(-mountainX/width)*width;
        mountainX1 = (int)(-mountainX/width-1)*width;
        mountainX2 = (int)(-mountainX/width+1)*width;
    }
}
