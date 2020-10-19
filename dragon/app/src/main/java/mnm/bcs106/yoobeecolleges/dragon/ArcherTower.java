package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class ArcherTower extends Foundation {

    float attackRange = 1f/2;
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
        if (Math.abs(GameView.instance.player.position.x-x)<GameView.instance.cameraSize*attackRange){
            //System.out.println("in range");
            return true;}
        return false;
    }


    //shooting an arrow at target
    public void Attack(){
        float randomx = (float)(Math.random()-0.5)/2;
        float randomy = -(float)(Math.random())/4;
        Vector2 target = GameView.instance.player.aimFor();
        float dx = target.x-creationPoint.x;
        float dy = target.y-creationPoint.y;
        float l = (float) Math.sqrt(dx*dx +dy*dy);
        dx /=l;
        dy/=l;
        ProjectilePool.instance.shootArrow(creationPoint.x, creationPoint.y, 1f/2, dx+randomx, dy+randomy, 1);
    }


    //
    public void update(float fixedDeltaTime){
        if(inRange()) {
            if(Math.random() < 0.05){
            Attack();}
        }
    }

    @Override
    public int getTileNr() {
        return 1;
    }
}
