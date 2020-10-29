package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Lair {
    Bitmap lairBackSprite,lairFrontSprite;
    int width,height;
    Vector2 position;
    Paint paint = new Paint();
    int depositedGold = 0;
    Bitmap goldPile;

    public Lair() {
        width = (int) (Game.instance.screenWidth*2);
        height = width/2;
        lairBackSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_background);
        lairBackSprite = Bitmap.createScaledBitmap(lairBackSprite, width, height, false);
        lairFrontSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_foreground);
        lairFrontSprite = Bitmap.createScaledBitmap(lairFrontSprite, width, height, false);

        position = new Vector2(0, GameView.instance.getGroundLevel());

    }


    public void update(float deltaTime) {

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


                GoldPool.instance.spawnGold(0, (int) (GameView.instance.groundLevel - 4), 1);
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

