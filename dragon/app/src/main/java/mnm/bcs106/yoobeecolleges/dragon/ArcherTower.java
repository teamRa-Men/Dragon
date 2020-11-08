package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class ArcherTower extends Foundation {
    static int cost;
    float attackRange = (1f/2);

    boolean hasAttacked = false;
    public Point creationPoint = new Point();

    float countdown = 0;
    int attack = 0;

    public static int tileNr = 1;

    public ArcherTower( int x, int y, boolean isStanding, GameView activity){
        super( x, y, tileNr, isStanding, activity );

        cost = 160;
        buildingType = 4;
        maxHealth = 400;
        health = maxHealth;

        if(Math.random()<0.5f) {
            this.buildingImage = SpriteManager.instance.getBuildingSprite("Tower1");
        }
        else{
            this.buildingImage = SpriteManager.instance.getBuildingSprite("Tower1");
        }
        height = width*buildingImage.height()/buildingImage.width();
        creationPoint.x = x+(width/2);
        creationPoint.y = (int)((y - height*3/4));
        System.out.println("Tower spawned");
    }


    //adding physics to the arrows
    public void Physics(float deltaTime){

    }

    // calculates if the dragon is in range
    public boolean inRange(){
        if (Math.abs(GameView.instance.player.position.x-creationPoint.x)<GameView.instance.cameraSize*attackRange){
            return true;}
        return false;
    }

    //shooting an arrow at target
    public void Attack(){
        float randomx = (float)(Math.random()-0.5)*attackRange*GameView.instance.cameraSize/10;
        float randomy = (float)(Math.random()-0.5)*attackRange*GameView.instance.cameraSize/5;
        Vector2 target = GameView.instance.player.aimFor();
        float dx = target.x-creationPoint.x;
        float dy =target.y-creationPoint.y;
        ProjectilePool.instance.shootArrow(creationPoint.x, creationPoint.y, 1, dx+randomx, dy+randomy, 2);
    }

    public void update(float fixedDeltaTime){
        super.update(fixedDeltaTime);
        creationPoint.y = (int)((y - height*3/4));

        /*System.out.println(health);
        System.out.println(isStanding);*/

        if(isStanding) {
            countdown+=fixedDeltaTime;
            //System.out.println(countdown);
            if (inRange()) {

                if (countdown > 1000) {

                    if(countdown > 1200 && attack == 0) {
                        Attack();

                        attack+=1;
                    }

                    if(countdown > 1400 && attack == 1) {
                        Attack();

                        attack+=1;
                    }

                    if(countdown > 1600 && attack == 2) {
                        Attack();

                        attack+=1;
                    }

                    if(countdown >= 1800){
                        countdown = 0;
                        attack= 0;
                    }
                }
            }
        }

        else{
            buildingImage = SpriteManager.instance.getBuildingSprite("TowerRuin");

            if(beenEmptied == false){
                GoldPool.instance.spawnGold(x, y-3,goldRate/5);
                beenEmptied = true;}

            goldRate = 0;
        }
    }

    @Override
    public void OnDamage() {
        super.OnDamage();

        if(health == 0 && isStanding){
            isStanding = false;
        }
    }

    @Override
    public int getTileNr() {
        return 1;
    }
}
