package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Scene {
    Bitmap ground, hillsBackground,mountainBackground, forestLeft, forestRight, forest, bush, trunk;
    Bitmap[] treeTypes = new Bitmap[8];
    Bitmap[] tinies = new Bitmap[9];
    Bitmap[] clouds = new Bitmap[8];
    int height, width;
    float mountainX, hillsX, groundX;
    float timeOfDay;
    int dayLength;
    int day = 1;
    public static Scene instance;
    int treeCount = 30;
    int tiniesCount = 30;
    int bushCount = 20;
    int cloudCount = 10;

    Wooloo[] forestWooloo = new Wooloo[5];

    int[] treePositionsLair, treePositionsEast, treePositionsWest;
    Point[] tiniesPositionsLair, tiniesPositionsEast, tiniesPositions, cloudPositions;
    int[] cloudTypes;
    int[] bushPositionsWest, bushPositionsLair;

    Bitmap[] trees;

    Fortress eastFort, westFort, finalFort;

    int  groundX2, groundX1, groundX0, mountainX2, mountainX1, mountainX0, hillsX2, hillsX1, hillsX0;


    Paint skyPaint, backPaint,frontPaint, bottomPaint;

    float islandSizeLeft, islandSizeRight;

    GameView gameView;
    public Scene(){
        gameView = GameView.instance;
        instance = this;

        dayLength = 3*60*1000;

        width = (int)(gameView.screenWidth*1.2);
        height = gameView.screenHeight;
        islandSizeLeft =width*10;
        islandSizeRight =width*7;

        finalFort = new Fortress( -width*6, (int)gameView.groundLevel);

        for(int i = 0; i < 20; i++) {
            finalFort.currentGold = finalFort.maxGold;
            finalFort.grow();
            if(finalFort.lv == 2){
                break;
            }
        }
        gameView.npc_pool.spawnDragonLayers(finalFort.x,finalFort.y,finalFort);
        gameView.npc_pool.spawnDragonLayers(finalFort.x,finalFort.y,finalFort);
        gameView.npc_pool.spawnDragonLayers(finalFort.x,finalFort.y,finalFort);

        gameView.npc_pool.spawnWizard(finalFort.x,finalFort.y,3,finalFort);



        eastFort = new Fortress( width*3, (int)gameView.groundLevel);
        gameView.npc_pool.spawnDragonLayers(eastFort.x,eastFort.y,eastFort);

        westFort = new Fortress( -width*2, (int)gameView.groundLevel);




        Bitmap sheet = SpriteManager.instance.environmentSheet;
        Rect r = SpriteManager.instance.getEnvironmentSpriteRect("Ground");
        ground = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        ground = Bitmap.createScaledBitmap(ground, width,width/r.width()*r.height(),false);//(int)((height-gameView.groundLevel)*1.3),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Hills");
        hillsBackground = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        hillsBackground = Bitmap.createScaledBitmap(hillsBackground, width, width/r.width()*r.height(),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Mountains");
        mountainBackground = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        mountainBackground = Bitmap.createScaledBitmap(mountainBackground, width, width/r.width()*r.height(),false);


        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree1");
        treeTypes[0] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree2");
        treeTypes[1] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree3");
        treeTypes[2] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree4");
        treeTypes[3] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree5");
        treeTypes[4] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree6");
        treeTypes[5] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree7");
        treeTypes[6] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("Tree8");
        treeTypes[7] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());

        r = SpriteManager.instance.getEnvironmentSpriteRect("ForestLeft");
        forestLeft = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        forestLeft = Bitmap.createScaledBitmap(forestLeft, width/8, width/16,false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Forest");
        forest = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        forest = Bitmap.createScaledBitmap(forest, width/7, width/16,false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("ForestRight");
        forestRight = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        forestRight = Bitmap.createScaledBitmap(forestRight, width/8, width/16,false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Bush");
        bush = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        bush = Bitmap.createScaledBitmap(bush, width/20, width/40,false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Trunk");
        trunk = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        trunk = Bitmap.createScaledBitmap(trunk, width/20, width/40,false);

        for(int i = 0; i < 9; i++) {
            r = SpriteManager.instance.getEnvironmentSpriteRect("Tiny"+(i+1));
            tinies[i] = Bitmap.createBitmap(sheet, r.left, r.top, r.width(), r.height());
            tinies[i] = Bitmap.createScaledBitmap(tinies[i], width / 40, width / 40, false);
        }

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud1");
        clouds[0] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[0] = Bitmap.createScaledBitmap(clouds[0], width/5, (int)((float)clouds[0].getHeight()/clouds[0].getWidth()*width/5),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud2");
        clouds[1] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[1] = Bitmap.createScaledBitmap(clouds[1], width/5, (int)((float)clouds[1].getHeight()/clouds[1].getWidth()*width/5),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud3");
        clouds[2] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[2] = Bitmap.createScaledBitmap(clouds[2], width/5, (int)((float)clouds[2].getHeight()/clouds[2].getWidth()*width/6),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud1");
        clouds[3] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[3] = Bitmap.createScaledBitmap(clouds[3], width/7, (int)((float)clouds[3].getHeight()/clouds[3].getWidth()*width/7),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud2");
        clouds[4] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[4] = Bitmap.createScaledBitmap(clouds[4], width/7, (int)((float)clouds[4].getHeight()/clouds[4].getWidth()*width/7),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud3");
        clouds[5] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[5] = Bitmap.createScaledBitmap(clouds[5], width/7, (int)((float)clouds[5].getHeight()/clouds[5].getWidth()*width/8),false);
        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud1");
        clouds[6] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[6] = Bitmap.createScaledBitmap(clouds[6], width/8, (int)((float)clouds[6].getHeight()/clouds[6].getWidth()*width/9),false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("Cloud3");
        clouds[7] = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        clouds[7] = Bitmap.createScaledBitmap(clouds[7], width/4, (int)((float)clouds[7].getHeight()/clouds[7].getWidth()*width/5),false);






        treePositionsLair = new int[treeCount];
        treePositionsWest = new int[treeCount/2];
        bushPositionsLair = new int[bushCount];
        treePositionsEast = new int[treeCount];
        bushPositionsWest = new int[bushCount];
        tiniesPositionsLair = new Point[tiniesCount/2];
        tiniesPositionsEast= new Point[tiniesCount];
        tiniesPositions= new Point[tiniesCount];
        cloudPositions= new Point[cloudCount];
        cloudTypes= new int[cloudCount];

        trees = new Bitmap[treeCount];
        int j = 0;

        for(int i = 0; i < treeCount; i++){
            treePositionsLair[i] = (int)(Math.random()*1.2*width+width*0.5);
            trees[i] = treeTypes[(int)(Math.random()*7.5)];
            int size =  (int)((float)width/30+(float)width/15*Math.random());//*(float)(1.8*width-treePositionsLair[i])/width);
            trees[i] = Bitmap.createScaledBitmap(trees[i], size, size,false);
            if(j<bushCount) {
                bushPositionsLair[j] = treePositionsLair[i] + (int) ((Math.random() - 0.5) * width / 10);
                j++;
            }
            if(j<bushCount) {
                bushPositionsLair[j] = treePositionsLair[i] + (int) ((Math.random() - 0.5) * width / 10);
                j++;
            }
            if(j<bushCount) {
                bushPositionsLair[j] = treePositionsLair[i] + (int) ((Math.random() - 0.5) * width / 10);
                j++;
            }

        }


        for(int i = 0; i < tiniesCount/8; i++) {
            tiniesPositionsLair[i] = new Point();
            tiniesPositionsLair[i].x = (int)(Math.random()*width-width*0.8);
            tiniesPositionsLair[i].y = (int)(Math.random()*5.9+3);
        }
        for(int i = tiniesCount/8; i < tiniesCount/2; i++){
            tiniesPositionsLair[i] = new Point();
            tiniesPositionsLair[i].x = (int)(Math.random()*width+width*0.8);
            tiniesPositionsLair[i].y = (int)(Math.random()*8.9);
        }

        for(int i = 0; i < tiniesCount; i++){
            tiniesPositions[i] = new Point();
            tiniesPositions[i].x = (int)(Math.random()*width*14-width*8);
            tiniesPositions[i].y = (int)(Math.random()*5.9+3);
        }

        for(int i = 0; i < cloudCount; i++){
            cloudPositions[i] = new Point();
            cloudPositions[i].x = (int)(Math.random()*width*6-width*3);
            cloudPositions[i].y = (int)(Math.random()*GameView.instance.screenHeight/2);
            cloudTypes[i] = (int)(Math.random()*7.9);
        }

        j = 0;
        for(int i = 0; i < treeCount/4; i++) {
            treePositionsWest[i] = (int)(Math.random()*width-width*4);

            if(j<bushCount) {
                bushPositionsWest[j] = treePositionsWest[i] + (int) ((Math.random() - 0.5) * width / 10);
                j++;
            }
            if(j<bushCount) {
                bushPositionsWest[j] = treePositionsWest[i] + (int) ((Math.random() - 0.5) * width / 10);
                j++;
            }
            if(j<bushCount) {
                bushPositionsWest[j] = treePositionsWest[i] + (int) ((Math.random() - 0.5) * width / 10);
                j++;
            }


        }


        for(int i = 0; i < treeCount; i++) {
            treePositionsEast[i] = (int)(Math.random()*width*2+width*4);
        }
        for(int i = 0; i < tiniesCount; i++) {
            tiniesPositionsEast[i] = new Point();
            tiniesPositionsEast[i].x = (int)(Math.random()*width*1.6+width*4.1);
            tiniesPositionsEast[i].y = (int)(Math.random()*8.5);
        }
        for (int i = 0; i < 5; i++) {
            forestWooloo[i] = GameView.instance.npc_pool.spawnWooloo((int)(width*4 + Math.random()*width*2),(int)GameView.instance.groundLevel,eastFort);
        }


        backPaint = new Paint();
        frontPaint = new Paint();
        skyPaint = new Paint();
        bottomPaint = new Paint();
        frontPaint.setColor(Game.instance.getResources().getColor(R.color.colorPrimaryDark));
        backPaint.setColor(Color.rgb(240,250,255));
        bottomPaint.setColor(Color.rgb(30,30,20));
        //skyPaint.setColor(Color.rgb(250,250,255));
    }

    void drawLairForest(Canvas canvas){
        for (int i = 0; i < 9; i++) {
            if (i == 8) {
                canvas.drawBitmap(forestRight, groundX +width/2 + i * width / 8, gameView.groundLevel - forest.getHeight(), null);
            } else {
                canvas.drawBitmap(forest, groundX +width/2 + i * width / 8, gameView.groundLevel - forest.getHeight(), null);
            }
        }


        for (int i = 0; i < treeCount; i++) {
            canvas.drawBitmap(trees[i], groundX + treePositionsLair[i], gameView.groundLevel - trees[i].getHeight(), null);
        }
        for (int i = 0; i < bushPositionsLair.length; i++) {
            canvas.drawBitmap(bush, groundX + bushPositionsLair[i], gameView.groundLevel - bush.getHeight(), null);
        }

        for (int i = 0; i < tiniesPositionsLair.length; i++) {
            canvas.drawBitmap(tinies[tiniesPositionsLair[i].y], groundX + tiniesPositionsLair[i].x, gameView.groundLevel - tinies[0].getHeight(), null);
        }
    }
    void drawWoolooForest(Canvas canvas){
        for (int i = 0; i < 17; i++) {
            if (i == 0) {
                canvas.drawBitmap(forestLeft, groundX +4*width + (i) * width / 8 , gameView.groundLevel - forest.getHeight(), null);
            } else if (i == 16) {
                canvas.drawBitmap(forestRight, groundX +4*width + (i) * width / 8, gameView.groundLevel - forest.getHeight(), null);
            } else {
                canvas.drawBitmap(forest, groundX +4*width + (i) * width / 8, gameView.groundLevel - forest.getHeight(), null);
            }
        }
        for(int i = 0; i < treePositionsEast.length; i++) {
            canvas.drawBitmap(trees[i], groundX + treePositionsEast[i], gameView.groundLevel - trees[i].getHeight(), null);
        }
        for (int i = 0; i < tiniesPositionsEast.length; i++) {
            canvas.drawBitmap(tinies[tiniesPositionsEast[i].y], groundX + tiniesPositionsEast[i].x, gameView.groundLevel - tinies[0].getHeight(), null);
        }
    }

    public void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, skyPaint);
        for (int i = 0; i < cloudPositions.length; i++) {
            canvas.drawBitmap(clouds[cloudTypes[i]], mountainX + cloudPositions[i].x, cloudPositions[i].y, backPaint);
        }

        canvas.drawBitmap(mountainBackground, mountainX+ mountainX0,gameView.groundLevel - mountainBackground.getHeight() * 0.9f,backPaint);
        canvas.drawBitmap(mountainBackground, mountainX+ mountainX1,gameView.groundLevel - mountainBackground.getHeight() * 0.9f,backPaint);
        canvas.drawBitmap(mountainBackground, mountainX+ mountainX2,gameView.groundLevel - mountainBackground.getHeight() * 0.9f,backPaint);

        canvas.drawBitmap(hillsBackground, hillsX + hillsX0, gameView.groundLevel - hillsBackground.getHeight() * 0.9f, backPaint);
        canvas.drawBitmap(hillsBackground, hillsX + hillsX1, gameView.groundLevel - hillsBackground.getHeight() * 0.9f, backPaint);
        canvas.drawBitmap(hillsBackground, hillsX + hillsX2, gameView.groundLevel - hillsBackground.getHeight() * 0.9f, backPaint);



        if (Math.abs(GameView.instance.lair.position.x - GameView.instance.player.position.x) < width * 2.5){
            drawLairForest(canvas);

        }
        else if( GameView.instance.player.position.x < -0){
            for(int i = 0; i < treePositionsWest.length; i++) {
                canvas.drawBitmap(trees[i], groundX + treePositionsWest[i], gameView.groundLevel - trees[i].getHeight(), null);
            }
            for (int i = 0; i < bushPositionsWest.length; i++) {
                canvas.drawBitmap(bush, groundX + bushPositionsWest[i], gameView.groundLevel - bush.getHeight(), null);
            }
        }
        else{

            drawWoolooForest(canvas);
        }
        for (int i = 0; i < tiniesPositions.length; i++) {
            canvas.drawBitmap(tinies[tiniesPositions[i].y], groundX + tiniesPositions[i].x, gameView.groundLevel - tinies[0].getHeight(), null);
        }
        eastFort.draw(canvas);
        if(finalFort.isStanding) {
            finalFort.buildingImage = SpriteManager.instance.getBuildingSprite("Fortress3");
        }
        westFort.draw(canvas);
        finalFort.draw(canvas);
    }
    public void drawForeground(Canvas canvas){
        canvas.drawRect(0,GameView.instance.groundLevel ,width,height*2,bottomPaint);
        canvas.drawBitmap(ground, groundX + groundX0, GameView.instance.groundLevel * .985f, null);
        canvas.drawBitmap(ground, groundX + groundX1, GameView.instance.groundLevel * .985f, null);
        canvas.drawBitmap(ground, groundX + groundX2, GameView.instance.groundLevel * .985f, null);

        //canvas.drawRect(0, gameView.groundLevel+ground.getHeight()/12, gameView.screenWidth*1.2f,gameView.screenHeight*1.2f, frontPaint);
    }

    public void physics(float deltaTime){
        try {
            if (Math.abs(eastFort.x - GameView.instance.player.position.x) < width * 3) {
                eastFort.physics(deltaTime);
            }
            if (Math.abs(westFort.x - GameView.instance.player.position.x) < width * 3) {
                westFort.physics(deltaTime);
            }
            if (Math.abs(finalFort.x - GameView.instance.player.position.x) < width * 3) {
                finalFort.physics(deltaTime);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void update(float deltaTime){
        if(Math.abs(finalFort.x - GameView.instance.player.position.x) < 2*width && Math.random()<0.5) {
            if (!(GameView.instance.player.health <= 0)) {
                Music.instance.playThemeMusic(false);
            }

        }



        eastFort.update(deltaTime);
        westFort.update(deltaTime);
        finalFort.update(deltaTime);
        if(!finalFort.isStanding && StatsRecorder.instance.finalKingdom==-1){
            //StatsRecorder.instance.finalKingdomDestroyed();
            //Game.instance.showGameWon = true;

        }

        mountainX = gameView.cameraDisp.x/2;
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
        if(bc == 255) {
            backPaint = null;
        }else{
            backPaint = new mnm.bcs106.yoobeecolleges.dragon.Paint();
            backPaint.setColorFilter(new LightingColorFilter(Color.rgb(bc, bc, bc), 0));
            //frontPaint.setColorFilter(new LightingColorFilter(Color.rgb(fc, fc, fc), 0));
        }

        skyPaint.setColor(Color.rgb((int)(bc*220f/255f),(int)(bc*240f/255f),(int)(bc*250f/255f)));
        //skyPaint.setAlpha(bc);

        timeOfDay +=deltaTime;
        if(timeOfDay > dayLength/2){
            timeOfDay +=deltaTime;
            Music.instance.startFadeOut(6000);
        }
        //System.out.println(timeOfDay/dayLength);
        if(timeOfDay > dayLength){
            finalFort.buildingImage = SpriteManager.instance.getBuildingSprite("Fortress3");
            timeOfDay = 0;
            SoundEffects.instance.play(SoundEffects.BELLS);
            day++;
            if(day%3==0){
                for(int i = 0; i < (int)Math.min(GameView.instance.lair.level/3,5)+1; i++) {
                    DragonLayers d = GameView.instance.npc_pool.spawnDragonLayers(eastFort.x, eastFort.y, eastFort);
                    if(d!=null) {
                        d.lockTarget = true;
                    }
                }
            }
            for (int i = 0; i < forestWooloo.length; i++) {
                if(forestWooloo[i] != null) {
                    if (!forestWooloo[i].active) {
                        forestWooloo[i] = GameView.instance.npc_pool.spawnWooloo((int) (width * 4 + Math.random() * width), (int) GameView.instance.groundLevel, eastFort);
                    }
                }
            }
            Game.instance.showDay = true;
        }
    }
}
