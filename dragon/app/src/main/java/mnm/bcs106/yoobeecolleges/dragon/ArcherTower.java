package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class ArcherTower extends Foundation {

    float attackRange = 1f/3;
    public Projectile[] Arrows = new Projectile[15];


    public Point creationPoint = new Point();

    public ArcherTower( int x, int y, boolean isStanding, GameView activity){
        super( x, y, 1, isStanding, activity );
        height = 2*width;
        this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.house);
        this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,width,height,false);
        creationPoint.x = x+(width/2);
        creationPoint.y = (int)GameView.instance.groundLevel - height;

        System.out.println("Tower spawned");
    }


    //adding physics to the arrows
    public void Physics(float deltaTime){

    }

    // calculates if the dragon is in range
    public boolean inRange(){
        //System.out.println("something went off");
        if (Math.abs(GameView.instance.player.position.y-y)<GameView.instance.cameraSize*attackRange){
            //System.out.println("in range");
            return true;}
        return false;
    }


    //shooting an arrow at target
    public void Attack(){
        float randomx = 1+(float)(Math.random()-0.5);
        float randomy = 1+(float)(Math.random()-0.5);
        Vector2 target = GameView.instance.player.aimFor();
        float dx = target.x-creationPoint.x;
        float dy = target.y-creationPoint.y;

        ProjectilePool.instance.shootArrow(creationPoint.x, creationPoint.y, 1, dx*randomx, dy*randomy, 0);
    }


    //
    public void update(float fixedDeltaTime){
        if(inRange()) {
            if(Math.random() < 0.02){
            Attack();}
        }
    }

    @Override
    public int getTileNr() {
        return 1;
    }
}
