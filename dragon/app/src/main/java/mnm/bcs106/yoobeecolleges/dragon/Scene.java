package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

public class Scene {
    Bitmap ground, hillsBackground, lairFront,lairMiddle, lairBack;
    int height, width;
    float mountainX, hillsX, groundX;
    float timeOfDay;
    int dayLength;
    int day = 1;
    public static Scene instance;

    Fortress eastFort, westFort, finalFort;

    int  groundX2, groundX1, groundX0, mountainX2, mountainX1, mountainX0, hillsX2, hillsX1, hillsX0;


    Paint skyPaint, backPaint,frontPaint, black;

    float islandSize;

    GameView gameView;
    public Scene(){
        gameView = GameView.instance;
        instance = this;

        dayLength = 3*60*1000;

        width = (int)(gameView.screenWidth*1.2);
        height = gameView.screenHeight;
        islandSize =width*8;

        finalFort = new Fortress( -width*4, (int)gameView.groundLevel);
        for(int i = 0; i < 20; i++) {
            finalFort.currentGold = finalFort.maxGold;
            finalFort.grow();
            System.out.println("fort level " + finalFort.lv);
            if(finalFort.lv == 2){
                break;
            }
        }
        gameView.npc_pool.spawnDragonLayers(finalFort.x,finalFort.y,finalFort);
        gameView.npc_pool.spawnDragonLayers(finalFort.x,finalFort.y,finalFort);
        gameView.npc_pool.spawnDragonLayers(finalFort.x,finalFort.y,finalFort);

        gameView.npc_pool.spawnWizard(finalFort.x,finalFort.y,3,finalFort);

        eastFort = new Fortress( width*2, (int)gameView.groundLevel);
        gameView.npc_pool.spawnDragonLayers(eastFort.x,eastFort.y,eastFort);

        westFort = new Fortress( -width*1, (int)gameView.groundLevel);

        //finalFort.levelUp();
        //finalFort.levelUp();


        Bitmap sheet = SpriteManager.instance.environmentSheet;
        Rect r = SpriteManager.instance.getEnvironmentSpriteRect("Ground");
        ground = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        ground = Bitmap.createScaledBitmap(ground, width,width/r.width()*r.height(),false);//(int)((height-gameView.groundLevel)*1.3),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Mountains");
        hillsBackground = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        hillsBackground = Bitmap.createScaledBitmap(hillsBackground, width,(int)(height/10),false);



        backPaint = new Paint();
        frontPaint = new Paint();
        skyPaint = new Paint();
        black = new Paint();
        frontPaint.setColor(Game.instance.getResources().getColor(R.color.colorPrimaryDark));
        backPaint.setColor(Color.rgb(240,250,255));
        black.setColor(Color.BLACK);
        //skyPaint.setColor(Color.rgb(250,250,255));
    }

    public void drawBackground(Canvas canvas){
        canvas.drawRect(0,0,width,height,skyPaint);
        //canvas.drawRect(0,0,width,height,backPaint);
        //canvas.drawBitmap(sky, 0,0,backPaint);
        //canvas.drawBitmap(sky, mountainX+ mountainX0,0,backPaint);
        //canvas.drawBitmap(sky, mountainX+ mountainX1,0,backPaint);
        //canvas.drawBitmap(sky, mountainX+ mountainX2,0,backPaint);


        //canvas.drawBitmap(mountainBackground, mountainX+ mountainX0,0,backPaint);
        //canvas.drawBitmap(mountainBackground, mountainX+ mountainX1,0,backPaint);
        //canvas.drawBitmap(mountainBackground, mountainX+ mountainX2,0,backPaint);

        canvas.drawBitmap(hillsBackground, hillsX+ hillsX0,gameView.groundLevel-hillsBackground.getHeight()*0.9f,backPaint);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX1,gameView.groundLevel-hillsBackground.getHeight()*0.9f,backPaint);
        canvas.drawBitmap(hillsBackground, hillsX+ hillsX2,gameView.groundLevel-hillsBackground.getHeight()*0.9f,backPaint);
        if(Math.abs(eastFort.x - GameView.instance.player.position.x) < width*2) {
            eastFort.draw(canvas);
        }
        if(Math.abs(westFort.x - GameView.instance.player.position.x) < width*2) {
            westFort.draw(canvas);
        }
        if(Math.abs(finalFort.x - GameView.instance.player.position.x) < width*2) {
            finalFort.draw(canvas);
        }
    }
    public void drawForeground(Canvas canvas){
        canvas.drawRect(0,GameView.instance.groundLevel ,width,height,black);
        canvas.drawBitmap(ground, groundX + groundX0, GameView.instance.groundLevel * .985f, backPaint);
        canvas.drawBitmap(ground, groundX + groundX1, GameView.instance.groundLevel * .985f, backPaint);
        canvas.drawBitmap(ground, groundX + groundX2, GameView.instance.groundLevel * .985f, backPaint);

        //canvas.drawRect(0, gameView.groundLevel+ground.getHeight()/12, gameView.screenWidth*1.2f,gameView.screenHeight*1.2f, frontPaint);
    }

    public void physics(float deltaTime){
        try {
            if (Math.abs(eastFort.x - GameView.instance.player.position.x) < width * 2) {
                eastFort.physics(deltaTime);
            }
            if (Math.abs(westFort.x - GameView.instance.player.position.x) < width * 2) {
                westFort.physics(deltaTime);
            }
            if (Math.abs(finalFort.x - GameView.instance.player.position.x) < width * 2) {
                finalFort.physics(deltaTime);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void update(float deltaTime){
        eastFort.update(deltaTime);
        westFort.update(deltaTime);
        finalFort.update(deltaTime);

        mountainX = gameView.cameraDisp.x/4;
        hillsX = gameView.cameraDisp.x*3/4;
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
        int fc =  (int)(255*Math.max(Math.min(Math.sin(timeOfDay/dayLength*Math.PI*2)*4,1),0.8));
        //System.out.println(c);
        backPaint.setColorFilter(new LightingColorFilter(Color.rgb(bc,bc,bc),0));
        frontPaint.setColorFilter(new LightingColorFilter(Color.rgb(fc,fc,fc),0));

        skyPaint.setColor(Color.rgb((int)(bc*220f/255f),(int)(bc*240f/255f),(int)(bc*250f/255f)));
        //skyPaint.setAlpha(bc);

        timeOfDay +=deltaTime;
        if(timeOfDay > dayLength/2){
            timeOfDay +=deltaTime;
        }
        //System.out.println(timeOfDay/dayLength);
        if(timeOfDay > dayLength){
            timeOfDay = 0;
            day++;
            if(day%3==0){
                for(int i = 0; i < day/3; i++) {
                    DragonLayers d = GameView.instance.npc_pool.spawnDragonLayers(eastFort.x, eastFort.y, eastFort);
                    if(d!=null) {
                        d.lockTarget = true;
                    }
                }
            }
            Game.instance.showDay = true;
        }
    }
}
