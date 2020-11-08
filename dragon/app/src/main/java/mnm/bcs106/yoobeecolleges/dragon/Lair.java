package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

public class Lair {
    Bitmap lairBackSprite,lairFrontSprite;
    int width,height;
    Vector2 position;
    Paint paint = new Paint();
    int depositedGold = 100;//500;
    Bitmap goldPile;
    float goldPileHeight;
    float sleepTimeSpeed = 8;

    Dragon player;
    float maximumMana = 200;
    float maximumHealth = 200;
    float maximumSpeed = 3f/2;
    float maximumAttack= 3;

    float minimumMana;
    float minimumHealth;
    float minimumSpeed;
    float minimumAttack;

    float experience, upgradePoints, level = 1;
    boolean isSleeping = false;
    Button sleepButton;


    public Lair() {
        width = (int) (Game.instance.screenWidth);
        height =(int)GameView.instance.groundLevel/2;
        lairBackSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair);
        lairBackSprite = Bitmap.createScaledBitmap(lairBackSprite, width, height, false);
        //lairFrontSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_front);
        //lairFrontSprite = Bitmap.createScaledBitmap(lairFrontSprite, width, height, false);

        position = new Vector2(GameView.instance.screenWidth/2, GameView.instance.getGroundLevel());
        player = GameView.instance.player;
        sleepButton = Game.instance.sleepButton;
        goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.gold_pile);
        goldPile = Bitmap.createScaledBitmap(goldPile,Game.instance.screenWidth/4, Game.instance.screenWidth/4,false);
        goldPileHeight = getGoldPileHeight();
        lieDown();

        minimumAttack = player.attack;
        minimumMana = player.maxMana;
        minimumHealth = player.maxHealth;
        minimumSpeed = player.maxMoveSpeed;

    }

    public void sleep(){
        isSleeping = true;
        player.isSleeping = true;
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

        player.position.y = getGroundLevel(player.position, player.radius/2);
        player.head.position.y =getGroundLevel( player.head.position,  player.head.radius/2);
        for(int i = 0; i < player.segments.size();i++) {

            player.segments.get(i).position.y = getGroundLevel(player.segments.get(i).position, player.segments.get(i).radius);

        }
        player.physics(player.fixedDeltaTime);
        player.update(player.fixedDeltaTime);
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
            Hud.instance.manaMaxWidth = (int)(GameView.instance.screenWidth/4*player.maxMana/100);

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
            Hud.instance.healthMaxWidth = (int)(GameView.instance.screenWidth/4*player.maxHealth/100);
            return true;
        }
        return false;
    }
 public float getGroundLevel(Vector2 p, float r){
        float groundLevel = GameView.instance.groundLevel -r*1.2f;
        if(Math.abs(p.x - position.x) < goldPile.getWidth()/2) {
            float goldSurface = goldPileHeight + goldPile.getHeight() / 2 - (float) Math.sqrt(Math.pow(goldPile.getHeight() / 2, 2) - Math.pow(p.x - position.x, 2));
            groundLevel = Math.min(GameView.instance.groundLevel - r * 1.2f, goldSurface - r);
        }
        return  groundLevel;
 }

    public void update(float deltaTime) {


        if (isSleeping) {

            Game.instance.showSleepButton = false;

            Game.instance.showWakeButton = true;
            Game.instance.showUpgradeButton = true;

            experience += deltaTime * depositedGold / 1000;
            //System.out.println(experience);
            if (experience > level * 1000) {
                experience = 0;
                level++;
                upgradePoints += 3;

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


            if (Math.abs(player.position.x - position.x) < goldPile.getWidth() / 2 && player.position.y > goldPileHeight - player.radius * 2) {
                Game.instance.showSleepButton = true;
                //System.out.println("sleep button on");


                player.groundLevel = getGroundLevel(player.position, player.radius);

            } else {
                player.groundLevel = GameView.instance.groundLevel - player.radius * 1.2f;
                Game.instance.showSleepButton = false;
                //System.out.println("sleep button off");
            }

        }

        if (GameView.instance.player.goldHolding > 0) {
            if (Math.abs(player.aimFor().x - position.x) < goldPile.getWidth() / 4 ) {
                //depositedGold += 1;
                //System.out.println(depositedGold);}
                //goldPileHeight = getGoldPileHeight();
                //GameView.instance.player.goldHolding -= 1;]
                Vector2 p = GameView.instance.player.aimFor();

                GoldPool.instance.spawnGold((int)p.x, (int)p.y, 1,true);
                GameView.instance.player.goldHolding--;
                depositedGold++;
            }
        }
    }
    public float getGoldPileHeight(){
        return GameView.instance.groundLevel-goldPile.getHeight()/3*(Math.min((float)depositedGold/500,1));
    }

    public void draw (Canvas canvas){
        //canvas.drawBitmap(lairBackSprite, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
       // canvas.drawBitmap(lairFrontSprite, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
        canvas.drawBitmap(goldPile, (int) (position.x - goldPile.getWidth() / 2) + GameView.instance.cameraDisp.x, goldPileHeight, paint);
        //canvas.drawCircle(position.x,goldPileHeight+goldPile.getHeight()/2,goldPile.getHeight()/2,paint);
    }

    public void attractGold(Gold g){
        if(Math.abs(g.position.x - position.x) < goldPile.getWidth() / 2){
            Vector2 disp = position.add(g.position.multiply(-1));
            if(g.position.y < getGroundLevel(g.position,g.width/2)){
                if(g.velocity.y>0){
                    g.setDir(disp.getNormal().multiply(0.1f).add(g.direction));
                }
            }
            else{
                GoldPool.instance.collectedGold(g);

                goldPileHeight = getGoldPileHeight();
            }
        }
    }
}

