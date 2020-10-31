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
    int depositedGold = 0;
    Bitmap goldPile;
    Dragon player;
    int experience, upgradePoints, level;
    boolean isSleeping = false;
    Button sleepButton;

    public Lair() {
        width = (int) (Game.instance.screenWidth*2);
        height = width/2;
        lairBackSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_background);
        lairBackSprite = Bitmap.createScaledBitmap(lairBackSprite, width, height, false);
        lairFrontSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_foreground);
        lairFrontSprite = Bitmap.createScaledBitmap(lairFrontSprite, width, height, false);

        position = new Vector2(GameView.instance.screenWidth/2, GameView.instance.getGroundLevel());
        player = GameView.instance.player;
        sleepButton = Game.instance.sleepButton;

    }

    public void sleep(View view){
        isSleeping = true;
        player.isSleeping = true;
    }
    public void wake(View view){
        isSleeping = false;
        player.isSleeping = false;
    }

    public boolean upgradeAttack(){
        if(upgradePoints > 0) {
            player.attack+=1f/2;
            upgradePoints--;
            return true;
        }
        return false;
    }
    public boolean upgradeMana(){
        if(upgradePoints > 0) {
            player.maxMana+=20;
            upgradePoints--;
            return true;
        }
        return false;
    }
    public boolean upgradeSpeed(){
        if(upgradePoints > 0) {
            player.maxMoveSpeed+=1f/8;
            upgradePoints--;
            return true;
        }
        return false;
    }
    public boolean upgradeHealth(){
        if(upgradePoints > 0) {
            player.maxHealth+=20;
            upgradePoints--;
            return true;
        }
        return false;
    }


    public void update(float deltaTime) {


        if(isSleeping){
            Game.instance.showSleepButton = false;

            Game.instance.showWakeButton = true;
            Game.instance.showUpgradeButton = true;

            experience+=deltaTime*depositedGold/1000;
            if(experience > level*1000){
                experience = 0;
                level++;
                upgradePoints+=3;

                //Grow
                GameView.instance.pause();
                int size = player.size+3;
                if(size <70) {
                    player.initBody(size);
                }
                GameView.instance.resume();
            }
            if(player.mana<player.maxMana) {
                player.mana += player.manaRegen * 3 * deltaTime / 1000;
                player.mana = Math.min(player.mana, player.maxMana);
            }
            if(player.health<player.maxHealth) {
                player.health += player.manaRegen * 3 * deltaTime / 1000;
                player.health = Math.min(player.health, player.maxHealth);
            }
        }
        else{
            Game.instance.showWakeButton = false;
            Game.instance.showUpgradeButton = false;

            if(Math.abs(player.position.x - position.x)<GameView.instance.cameraSize/20 && !player.flying){
                Game.instance.showSleepButton = true;
                System.out.println("sleep button on");
            }
            else{
                Game.instance.showSleepButton = false;
                System.out.println("sleep button off");
            }
        }

        if (Math.abs(GameView.instance.player.position.x) < 300) {
            if(GameView.instance.player.goldHolding > 0){
                depositedGold+=GameView.instance.player.goldHolding;
                System.out.println(depositedGold);}

            //making images
            if (depositedGold > 1000) {
                int tempGold = depositedGold;
                tempGold = (int) (tempGold / 1000);
                for (int t = 0; t < tempGold; t++) {
                    //goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.house);
                }
            }
            else if (depositedGold > 100) {
                int tempGold1 = depositedGold;
                tempGold1 = (int)(tempGold1 / 100);
                for (int t = 0; t < tempGold1; t++) {
                    //goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.houseruin);
                }
            }
            else if (depositedGold > 10) {
                int tempGold2 = depositedGold;
                tempGold2 = (int)(tempGold2 / 10);
                for (int t = 0; t < tempGold2; t++) {
                    //goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.barn);
                }
            }


            //GoldPool.instance.spawnGold(0, (int) (GameView.instance.groundLevel - 4), 1);


            GameView.instance.player.goldHolding = 0;
        }
    }


    public void draw (Canvas canvas){
        canvas.drawBitmap(lairBackSprite, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
        canvas.drawBitmap(lairFrontSprite, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
        //canvas.drawBitmap(goldPile, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
    }
}

