
package mnm.bcs106.yoobeecolleges.lifeofdragon;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class SpriteAnimation {
    public Bitmap spriteSheet;
    float progress, frameTime, animPeriod;
    int frameCount, currentFrame = 0, spriteWidth, spriteHeight;
    Rect srcBounds;

    public SpriteAnimation(Context context,SpriteSheet sheet, int spriteWidth, int spriteHeight, float animPeriod){
        this.frameCount = sheet.frameCount;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.frameTime = frameTime;
        this.animPeriod = animPeriod;
        srcBounds = new Rect(0,0,spriteWidth, spriteHeight);


        spriteSheet = BitmapFactory.decodeResource(context.getResources(), sheet.spriteID);
        spriteSheet = Bitmap.createScaledBitmap(spriteSheet,frameCount*spriteWidth,spriteHeight,false);

    }
    public Rect getFrame(float deltaTime, float playSpeed){
        progress += playSpeed*deltaTime/animPeriod;
        if(progress >= 1){
            progress = 0;
        }
        if(progress < 0){
            progress = 0.9f;
        }
        currentFrame = (int)(progress*frameCount);
        srcBounds.left = currentFrame*spriteWidth;
        srcBounds.right = this.srcBounds.left + spriteWidth;

        return srcBounds;
    }

    public Rect getFrame(int index){
        currentFrame = index;
        srcBounds.left = currentFrame*spriteWidth;
        srcBounds.right = this.srcBounds.left + spriteWidth;

        return srcBounds;
    }


}
