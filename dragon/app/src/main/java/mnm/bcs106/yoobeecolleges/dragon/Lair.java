package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

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

    public Lair() {
        width = (int) (Game.instance.screenWidth*2);
        height = width/2;
        lairBackSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_background);
        lairBackSprite = Bitmap.createScaledBitmap(lairBackSprite, width, height, false);
        lairFrontSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_foreground);
        lairFrontSprite = Bitmap.createScaledBitmap(lairFrontSprite, width, height, false);

        position = new Vector2(0, GameView.instance.getGroundLevel());
        player = GameView.instance.player;

    }

    public void sleep(View view){
        isSleeping = true;
        player.isSleeping = true;
    }
    public void wake(View view){
        isSleeping = false;
        player.isSleeping = false;
    }

    public void upgradeAttack(View view){
        if(upgradePoints > 0) {
            player.attack+=1f/2;
            upgradePoints--;
        }
    }
    public void upgradeMana(View view){
        if(upgradePoints > 0) {
            player.maxMana+=20;
            upgradePoints--;
        }
    }
    public void upgradeSpeed(View view){
        if(upgradePoints > 0) {
            player.maxMoveSpeed+=1f/8;
            upgradePoints--;
        }
    }
    public void upgradeHealth(View view){
        if(upgradePoints > 0) {
            player.maxHealth+=20;
            upgradePoints--;
        }
    }


    public void update(float deltaTime) {
        if(isSleeping){
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

        if (Math.abs(GameView.instance.player.position.x) < 300) {
            if(GameView.instance.player.goldHolding > 0){
                depositedGold+=GameView.instance.player.goldHolding;
                System.out.println(depositedGold);}

            //making images
            if (depositedGold > 1000) {
                int tempGold = depositedGold;
                tempGold = (int)(tempGold / 1000);
                for (int t = 0; t < tempGold; t++) {
                    //goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.house);
                }

                if (depositedGold > 100) {
                    int tempGold1 = depositedGold;
                    tempGold1 = (int)(tempGold1 / 100);
                    for (int t = 0; t < tempGold1; t++) {
                        //goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.houseruin);
                    }
                }

                if (depositedGold > 10) {
                    int tempGold2 = depositedGold;
                    System.out.println("before processing :" + tempGold2);
                    tempGold2 = (int)(tempGold2 / 10);
                    System.out.println("after processing :" + tempGold2);
                    for (int t = 0; t < tempGold2; t++) {
                        //goldPile = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.barn);
                    }
                }


                //GoldPool.instance.spawnGold(0, (int) (GameView.instance.groundLevel - 4), 1);
            }

            GameView.instance.player.goldHolding = 0;
        }
    }


    public void draw (Canvas canvas){
        canvas.drawBitmap(lairBackSprite, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
        canvas.drawBitmap(lairFrontSprite, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
        canvas.drawBitmap(goldPile, (int) (position.x - width / 2) + GameView.instance.cameraDisp.x, (int) (position.y - height), paint);
    }
}

