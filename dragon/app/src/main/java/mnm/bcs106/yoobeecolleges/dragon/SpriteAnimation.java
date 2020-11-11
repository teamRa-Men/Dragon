
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class SpriteAnimation {
    Bitmap [] sprites;
    float progress,  animTime;
    int frameCount, currentFrame = 0;
    Rect srcBounds;

    public SpriteAnimation(Bitmap[] sprites, float animTime){
        this.sprites = sprites;
        this.animTime = animTime;
        this.frameCount = sprites.length;
    }
    public Bitmap getFrame(float deltaTime, float playSpeed){
        progress += playSpeed*deltaTime/animTime;
        if(progress >= 1){
            progress = 0;
        }
        if(progress < 0){
            progress = 0.9f;
        }
        currentFrame = (int)(progress*frameCount);


        return sprites[currentFrame];
    }

    public Bitmap getFrame(int index){
        currentFrame = index;
        return sprites[index];
    }


}
