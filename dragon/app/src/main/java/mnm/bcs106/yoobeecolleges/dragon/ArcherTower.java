package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class ArcherTower extends Foundation {

    float attackRange = (1/3);
    public Projectile[] Arrows = new Projectile[15];

    public boolean lockTarget = false;
    public Point target = new Point();
    public Point creationPoint = new Point();

    public ArcherTower( int x, int y, boolean isStanding, GameView activity){
        super( x, y, 1, isStanding, activity );

        this.buildingImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.house);
        this.buildingImage = Bitmap.createScaledBitmap(this.buildingImage,100,100,false);
        creationPoint.x = x+(width/2);
        creationPoint.y = (int)GameView.instance.groundLevel - buildingImage.getHeight();

        System.out.println("Tower spawned");
    }


    //adding physics to the arrows
    public void Physics(float deltaTime){

    }

    // calculates if the dragon is in range
    public boolean inRange(){
        System.out.println("something went off");
        if (Math.abs(GameView.instance.player.position.y-y)<500){
            System.out.println("in range");
            return true;}
        return false;
    }


    //shooting an arrow at target
    public void Attack(){
        float dx = GameView.instance.player.position.x-x;
        float dy =GameView.instance.player.position.y-y;
        ProjectilePool.instance.shootArrow(creationPoint.x, creationPoint.y, 1, dx, dy, 2);
    }


    //
    public void update(float fixedDeltaTime){
        if(inRange()) {
            if(Math.random() < 0.01){
            Attack();}
        }
    }

    @Override
    public int getTileNr() {
        return 1;
    }
}
