package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;

public class Lair {
    Bitmap lairBack,lairFront,lairMiddle;
    int width,height;
    Vector2 position;
    Paint paint = new Paint();
    Paint frontPaint = new Paint();
    int depositedGold = 2;//100;//500;
    Bitmap goldPile;
    float goldPileHeight;
    float sleepTimeSpeed = 8;

    Dragon player;
    float maximumMana = 200;
    float maximumHealth = 200;
    float maximumSpeed = 1.2f;
    float maximumAttack= 3;

    float minimumMana;
    float minimumHealth;
    float minimumSpeed;
    float minimumAttack;

    float experience, upgradePoints, level = 1;
    public float nextLevel;
    boolean isSleeping = false;
    Button sleepButton;

    Rect colliderLeft,colliderCenter,colliderRight;


    public Lair() {
        width = Game.instance.screenWidth*3/4;
        height = GameView.instance.screenWidth*3/4/4;

        Bitmap sheet = SpriteManager.instance.environmentSheet;

        Rect r = SpriteManager.instance.getEnvironmentSpriteRect("LairFront");
        lairFront = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        lairFront = Bitmap.createScaledBitmap(lairFront, width,height,false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("LairMiddle");
        lairMiddle = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        lairMiddle = Bitmap.createScaledBitmap(lairMiddle, width,height,false);

        r = SpriteManager.instance.getEnvironmentSpriteRect("LairBack");
        lairBack = Bitmap.createBitmap(sheet,r.left,r.top,r.width(),r.height());
        lairBack = Bitmap.createScaledBitmap(lairBack, width,height,false);

        position = new Vector2(GameView.instance.screenWidth/2, GameView.instance.getGroundLevel());
        player = GameView.instance.player;
        sleepButton = Game.instance.sleepButton;
        goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.gold_pile);
        goldPile = Bitmap.createScaledBitmap(goldPile,Game.instance.screenWidth/5, Game.instance.screenWidth/5,false);
        goldPileHeight = getGoldPileHeight();
        lieDown();

        minimumAttack = player.attack;
        minimumMana = player.maxMana;
        minimumHealth = player.maxHealth;
        minimumSpeed = player.maxMoveSpeed;

        int left =  (int) (position.x - width / 4);
        int right = left + width/2;
        int top = (int) (position.y - height*5/6);
        int bottom = top + height/3;
        colliderCenter= new Rect(left,top,right,bottom);

        left =  (int) (position.x + width / 4);
        right = left + width/5;
        top = (int) (position.y - height*5/6);
        bottom = top + height/2;
        colliderRight = new Rect(left,top,right,bottom);

        left =  (int) (position.x - width /4-width/5);
        right = left + width/5;
        top = (int) (position.y - height*3/5);
        bottom = top + height/3;
        colliderLeft = new Rect(left,top,right,bottom);

    }

    public void sleep(){
        isSleeping = true;
        player.isSleeping = true;
        player.speed = 0;
        lieDown();
        GameView.instance.timeScale = sleepTimeSpeed;
    }

    public  void stealGold(int steal){
        depositedGold -= steal;
        goldPileHeight = getGoldPileHeight();
        if(isSleeping) {
            lieDown();
            //wake();
        }
    }

    public void lieDown(){

        if(level%2 == 0) {
            player.head.direction = new Vector2(-player.direction.x, player.direction.y);
        }

        player.position.y = getGroundLevel(player.position, player.radius*0.6f);
        player.head.position.y =getGroundLevel( player.head.position,  player.head.radius*0.6f);
        for(int i = 0; i < player.segments.size();i++) {

            player.segments.get(i).position.y = getGroundLevel(player.segments.get(i).position, player.segments.get(i).radius);

        }
        player.physics(player.fixedDeltaTime);
        player.update(player.fixedDeltaTime);
    }

    public void physics(float deltaTime){
        int x= (int)player.position.x;
        int y= (int)player.position.y;
        if(player.position.y > colliderLeft.top) {
            if (colliderRight.contains(x, y)) {
                player.direction.y = -player.direction.y/2;
                player.position.y = colliderRight.bottom+1;
            }
            if (colliderLeft.contains(x, y)) {
                player.direction.y = -player.direction.y/2;
                player.position.y = colliderLeft.bottom+1;
            }
            if (colliderCenter.contains(x, y)) {
                player.direction.y = -player.direction.y/2;
                player.position.y = colliderCenter.bottom+1;
            }
        }
        else{

            if (colliderRight.contains(x, y)) {
                player.direction.y = -player.direction.y/2;
                player.position.y = colliderRight.top;
            }
            y += (int)player.radius;
            if (colliderLeft.contains(x, y)) {
                player.direction.y = 0;
                player.position.y = colliderLeft.top-player.radius;
            }
            if (colliderCenter.contains(x, y)) {
                player.direction.y = 0;
                player.position.y = colliderCenter.top-player.radius;
            }
        }
    }

    public void wake(){
        isSleeping = false;
        player.isSleeping = false;
        GameView.instance.timeScale = 1;
    }

    public boolean upgradeAttack(){
        if(upgradePoints > 0 && player.attack < maximumAttack) {
            player.attack+=maximumAttack/10;
            upgradePoints--;
            return true;
        }
        return false;
    }
    public boolean upgradeMana(){
        if(upgradePoints > 0 && player.maxMana < maximumMana) {
            player.maxMana+=20;
            upgradePoints--;
            Hud.instance.manaMaxWidth = (int)(GameView.instance.screenWidth/3*player.maxMana/100);

            return true;
        }
        return false;
    }
    public boolean upgradeSpeed(){
        if(upgradePoints > 0 && player.maxMoveSpeed < maximumSpeed) {
            player.maxMoveSpeed+=maximumSpeed/10;
            upgradePoints--;
            return true;
        }
        return false;
    }
    public boolean upgradeHealth(){
        if(upgradePoints > 0 && player.maxHealth< maximumHealth) {
            player.maxHealth+=maximumHealth/10;
            upgradePoints--;
            Hud.instance.healthMaxWidth = GameView.instance.screenWidth/3*player.maxHealth/100;
            return true;
        }
        return false;
    }
    public float getGroundLevel(Vector2 p, float r){
        float groundLevel = GameView.instance.groundLevel -r*1.3f;
        if(Math.abs(p.x - position.x) < goldPile.getWidth()/2) {
            float goldSurface = goldPileHeight + goldPile.getHeight() / 2 - (float) Math.sqrt(Math.pow(goldPile.getHeight() / 2, 2) - Math.pow(p.x - position.x, 2));
            groundLevel = Math.min(GameView.instance.groundLevel - r * 1.4f, goldSurface - r);
        }
        return  groundLevel;
    }

    public void update(float deltaTime) {


        if (isSleeping) {

            Game.instance.showSleepButton = false;

            Game.instance.showWakeButton = true;
            Game.instance.showUpgradeButton = true;

            experience += deltaTime * depositedGold / 1500;
            //System.out.println(experience);
            nextLevel =  level*level * 500;
            if (experience > nextLevel) {
                experience = 0;
                level++;
                upgradePoints += 3;
                player.maxGoldHolding = (int)level*50;

                //Grow
                GameView.instance.isDrawing = false;
                int size = player.size + 3;
                if (size < 70) {
                    player.initBody(size);
                    lieDown();
                }
                GameView.instance.isDrawing = true;

            }
            if (player.mana < player.maxMana) {
                player.mana += player.manaRegen * 3 * deltaTime / 1000;
                player.mana = Math.min(player.mana, player.maxMana);
            }
            if (player.health < player.maxHealth) {
                player.health += player.manaRegen * 3 * deltaTime / 1000;
                player.health = Math.min(player.health, player.maxHealth);
            }
        } else {
            Game.instance.showWakeButton = false;
            Game.instance.showUpgradeButton = false;


            //System.out.println("sleep button on");
            //System.out.println("sleep button off");
            Game.instance.showSleepButton = Math.abs(player.position.x - position.x) < goldPile.getWidth() / 2 && player.position.y > goldPileHeight - player.radius * 2;

        }
        if (Math.abs(player.aimFor().x - position.x) < goldPile.getWidth() / 4 ) {
            Game.instance.showGold = true;
        if (GameView.instance.player.goldHolding > 0) {

                //depositedGold += 1;
                //System.out.println(depositedGold);}
                //goldPileHeight = getGoldPileHeight();
                //GameView.instance.player.goldHolding -= 1;]
                Vector2 p = GameView.instance.player.aimFor();

                GoldPool.instance.spawnGold((int)p.x, (int)(p.y+ GameView.instance.player.radius), 1,true);
                GameView.instance.player.goldHolding--;
                depositedGold++;

            }
        }
        else{
            Game.instance.showGold = false;
        }

        float dx=  Math.abs(player.position.x-position.x);
        if(!(player.position.y > colliderLeft.top && dx < width/2)) {
            frontPaint.setAlpha((frontPaint.getAlpha()*9+255)/10);
        }
        else{
            frontPaint.setAlpha((int)(frontPaint.getAlpha()*0.9f));
        }
    }
    public float getGoldPileHeight(){
        return GameView.instance.groundLevel-goldPile.getHeight()/3*(float)Math.pow((float)depositedGold/1000,1f/3);
    }

    public void drawBackground (Canvas canvas){
        canvas.drawBitmap(lairBack, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
        canvas.drawBitmap(goldPile, (int) (position.x - goldPile.getWidth() / 2) + GameView.instance.cameraDisp.x, goldPileHeight, paint);

        //canvas.drawCircle(position.x,goldPileHeight+goldPile.getHeight()/2,goldPile.getHeight()/2,paint);
    }

    public void drawForeground(Canvas canvas){

        canvas.drawBitmap(lairMiddle, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);


        if(frontPaint.getAlpha()>1) {
            canvas.drawBitmap(lairFront, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), frontPaint);
        }


    }

    public void attractGold(Gold g){
            Vector2 goldPilePosition = new Vector2(position.x, goldPileHeight+goldPile.getHeight()/2);
            Vector2 disp = goldPilePosition.add(g.position.multiply(-1));

            if( g.fromDragon) {

                if (disp.sqrMagnitude() > (goldPile.getWidth() / 2) * (goldPile.getWidth() / 2)) {
                        g.speed = (g.speed+0.2f)/2;
                        g.setDir(position.add(g.position.multiply(-1)).getNormal().multiply(0.03f).add(g.direction));

                } else {
                    GoldPool.instance.collectedGold(g);
                    goldPileHeight = getGoldPileHeight();

                }
            }
            else {
                if (disp.sqrMagnitude() < (goldPile.getWidth() / 2) * (goldPile.getWidth() / 2)){
                    GoldPool.instance.collectedGold(g);
                    goldPileHeight = getGoldPileHeight();
                }
            }
    }
}

