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
    public Lair() {
        width = (int) (Game.instance.screenWidth*2);
        height = width/2;
        lairBackSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_background);
        lairBackSprite = Bitmap.createScaledBitmap(lairBackSprite, width, height, false);
        lairFrontSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.lair_foreground);
        lairFrontSprite = Bitmap.createScaledBitmap(lairFrontSprite, width, height, false);

        position = new Vector2(0, GameView.instance.getGroundLevel());

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(lairBackSprite,(int)(position.x-width/2)+GameView.instance.cameraDisp.x,(int)(position.y-height), paint);
        canvas.drawBitmap(lairFrontSprite,(int)(position.x-width/2)+GameView.instance.cameraDisp.x,(int)(position.y-height), paint);
    }
}
