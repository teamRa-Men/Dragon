package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Gold extends GameObject{

    public Gold(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);
    }

    @Override
    public void physics(float deltaTime) {
        super.physics(deltaTime);
        if(position.y+width < GameView.instance.getGroundLevel()){
            setVelocity(velocity.add(new Vector2(3,0)));
        }
        else{
            setVelocity(Vector2.zero);
        }
    }
}
