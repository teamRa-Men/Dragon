package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;

public class Flag {
    float direction = 1;
    Bitmap flag;
    Rect src, dst;
    int time = 0;
    int x,y;
    boolean surrender = false;
    Paint p = new Paint();
    public Flag() {
        flag = BitmapFactory.decodeResource(Game.instance.getResources(), R.drawable.flag);
        flag = Bitmap.createScaledBitmap(flag, GameView.instance.screenWidth / 8, GameView.instance.screenWidth / 32, false);
        src = new  Rect(flag.getWidth()/2,0,flag.getWidth(),flag.getHeight());
        x = GameView.instance.screenWidth/2;
        y = GameView.instance.screenHeight/2;
        setSurrender(false);

    }

    public void setSurrender(boolean surrender){
        this.surrender = surrender;
        if(surrender){
            p.setColorFilter(new LightingColorFilter(Color.WHITE,0));
        }
        else {
            p.setColorFilter(new LightingColorFilter(Color.rgb(50,50,50),0));
        }
    }
    public void draw(Canvas canvas){
        time+=2;
        if(time > flag.getWidth()/2){
            time = 0;
        }


        int left = flag.getWidth()/2-time;
        int right = left+flag.getWidth()/2;
        int top = 0;
        int bottom = flag.getHeight();
        src = new  Rect(left,top,right,bottom);

        left = (int)(x+GameView.instance.cameraDisp.x - flag.getWidth()/2*(1-direction)/2);
        right = left+flag.getWidth()/2;
        top = y;
        bottom = top+flag.getHeight();
        dst = new  Rect(left, top,right,bottom);

        canvas.drawBitmap(flag,src,dst,p);



    }
}
