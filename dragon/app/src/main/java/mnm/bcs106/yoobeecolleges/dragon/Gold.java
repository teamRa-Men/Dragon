package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;

public class Gold extends GameObject{
    float spawnSpeed = 1f/2;
    public Gold(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);
        simulated = true;
        bounce = 0.6f;
        friction = 0.8f;
        width = GameView.instance.screenWidth/20;
        height = width;
    }

    public  void spawn(Vector2 p){
        position = p;
        direction = new Vector2((float) (Math.random()-0.5f), (float)Math.random());
        speed = spawnSpeed;
    }

    @Override
    public void physics(float deltaTime) {
        if(Vector2.distance(GameView.instance.player.position,position)< width/2){
            GameView.instance.goldController.collectedGold(this);
            GameView.instance.player.collectedGold();
            System.out.println("collected");
        }
        super.physics(deltaTime);
       //System.out.println("phys");
        if(position.y + width/2< GameView.instance.getGroundLevel()){
            setVelocity(getVelocity().x, getVelocity().y + 1f/2* deltaTime/1000);

        }
        else{
            if(speed == 0){
                //simulated = false;
            }
            else {
                onGrounded(GameView.instance.getGroundLevel());
            }

        }


    }
}
