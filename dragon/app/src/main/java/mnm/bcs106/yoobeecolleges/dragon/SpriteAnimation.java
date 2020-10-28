
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class SpriteAnimation {
    Rect [] sprites;
    float progress,  animTime;
    int frameCount, currentFrame = 0;
    Rect srcBounds;

    public SpriteAnimation(Rect[] sprites, float animTime){
        this.sprites = sprites;
        this.animTime = animTime;
        this.frameCount = sprites.length;
    }
    public Rect getFrame(float deltaTime, float playSpeed){
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

    public Rect getFrame(int index){
        currentFrame = index;
        return sprites[index];
    }


}
