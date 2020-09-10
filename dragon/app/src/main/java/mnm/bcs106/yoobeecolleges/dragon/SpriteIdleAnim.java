
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class SpriteIdleAnim {
    SpriteSheet sheet;
    SpriteAnimation anim;
    int spriteWidth, spriteHeight;
    float animPeriod;
    Context context;
    float time;

    public SpriteIdleAnim(Context context, int spriteWidth, int spriteHeight, float animPeriod, SpriteSheet sheet){
        this.sheet = sheet;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.animPeriod = animPeriod;
        this.context = context;

        anim = new SpriteAnimation(context, sheet, spriteWidth, spriteHeight, animPeriod);

    }

    public Rect draw(Canvas canvas, RectF dst, int facing , float deltaTime, Paint p){

        Rect frame = anim.getFrame(facing);
        dst.top=(int)(dst.top+(Math.sin(time/animPeriod*Math.PI*2)+1)*3);
        canvas.drawBitmap(anim.spriteSheet,frame,dst,p);
        time+=deltaTime;
        return frame;
    }

}
