package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class SharedAnim {

        public static Vector2 shake(float width, float height, float maxDispX, float maxDispY){
            float dispY = height * ((float) Math.random() - 0.5f)*maxDispY;
            float dispX = width * ((float) Math.random() - 0.5f)*maxDispX;
            return  new Vector2(dispX, dispY);
        }

        public static Paint flashingRed(Paint p){
            p.setColorFilter(new LightingColorFilter(Color.rgb((int)(Math.random()*255),255,255),0));
            return p;
        }

        public static boolean fade(Paint paint){
            int alpha = paint.getAlpha() - 10;
            if (alpha > 0) {
                paint.setAlpha(alpha);
                return  true;
            } else {
                return false;
            }
        }

}
