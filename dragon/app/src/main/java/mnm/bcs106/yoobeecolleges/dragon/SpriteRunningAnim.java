
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class SpriteRunningAnim {
    SpriteSheet sheet;
    SpriteAnimation anim;
    int spriteWidth, spriteHeight;
    float animPeriod;
    Context context;
    boolean flipX, flipY;

    public SpriteRunningAnim(Context context, int spriteWidth, int spriteHeight, float animPeriod, SpriteSheet sheet){
        this.sheet = sheet;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.animPeriod = animPeriod;
        this.context = context;

        anim = new SpriteAnimation(context, sheet, spriteWidth, spriteHeight, animPeriod);

    }

    public Rect draw(Canvas canvas, RectF dst, float animPlay, float deltaTime, Paint p){

        Rect frame = anim.getFrame(deltaTime, animPlay);
        canvas.drawBitmap(anim.spriteSheet,frame,dst,p);
        return frame;
    }

}
