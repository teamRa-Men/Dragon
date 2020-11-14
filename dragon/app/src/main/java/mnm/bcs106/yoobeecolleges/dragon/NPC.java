package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public class NPC {
    public Bitmap npcBitmap;
    public float npcX,npcY,npcMaxHP,npcHp,npcWidth,npcHeight;
    public Rect npcRect, npcCollider;
    RectF destTempRect;
    Matrix matrix = new Matrix();
    public float npcSpeed;
    public Boolean alive = false;
    public Boolean active = false;
    public Point movement;
    public Point target = new Point();
    public float npcFleeSpeed;
    public boolean flee = false;
    public ActionController damagePeriod;
    public int direction;
    public int countdown;
    public int afterLife;
    double random;
    public Point tempCreationPoint = new Point();
    public Point CreationPoint = new Point();
    public Paint NpcPain = new Paint();
    ColorFilter colorFilter = new LightingColorFilter(Color.parseColor("#40000000"),0);
    Fortress fortress;

    public NPC (float speed, int maxHP, int width,int height) {

        npcX = 0;
        npcY = 0;
        npcMaxHP = maxHP;
        npcHp = maxHP;
        npcSpeed = speed*(float)(Math.random()+4)/5;
        npcFleeSpeed = (float) ((Math.random()*npcSpeed)+(npcSpeed*3));
        npcWidth = width;
        npcHeight = height;
        npcRect = new Rect((int)npcX,(int)npcY,(int)(width+npcX),(int)(height+npcY));
        npcCollider = new Rect((int)npcX,(int)npcY,(int)(width+npcX),(int)(height+npcY));
        damagePeriod = new ActionController(0,5000,5000);
        random = Math.random();
    }

    public void spawn (int spawnX, int spawnY, Fortress fortress){

        npcHp = npcMaxHP;
        npcX = spawnX;
        npcY = (int) GameView.instance.groundLevel-npcRect.height();
        tempCreationPoint.x = (int)npcX;
        tempCreationPoint.y = (int)npcY;
        CreationPoint.x = spawnX;
        CreationPoint.y = spawnY;
        target.x = (int)npcX;
        alive = true;
        active = true;
        flee = false;
        this.fortress = fortress;
    }

    /*
    Down bellow i made method "OnDamage to make NPCs killable and damageable"
     */

    public  void OnDamage () {
        if(Math.random() < 0.1 && alive) {
            FirePool.instance.spawnFire(npcX + ((float)Math.random()-0.5f)*npcWidth/2, npcY+npcHeight);
        }
        damagePeriod.triggerAction();
        npcHp-=1*GameView.instance.player.attack;
        if (npcHp<=0){
            death();
        }
    }

    /*
    Down bellow i made method that eases up way of making sure that NPC died.
     */

    public void death(){
        alive = false;
    }
    float distTravel = 0;

    /*
    Down bellow I made method which makes all sprites and bitmaps of NPCs get drawn on the screen of the phone
    and also makes them rotate left or right depending on direction and giving them animation while they walk
    making it feel like it's jumps or like it's controlled like a puppet.
     */

    public void draw(Canvas canvas){
        if(npcBitmap!=null) {
            if (alive) {
                canvas.drawBitmap(npcBitmap, matrix, null);
            } else {
                canvas.drawBitmap(npcBitmap, matrix, NpcPain);
            }
        }

    }

    /*
    Down bellow is method that helps me to trace when NPC walks somewhere
    or when it's reached it's target so it can stop and start doing next action.
     */

    public  boolean there = false;
    public void moveToTarget(float deltaTime){
        if (Math.abs(target.x-npcX) > 5){
            if (!flee){
                npcX+=direction*npcSpeed*deltaTime;
                distTravel+=npcSpeed*deltaTime;
            } else {
                npcX+=direction*npcFleeSpeed*deltaTime;
                distTravel+=npcFleeSpeed*deltaTime;
            }
        }
        else {
            distTravel = 0;
        }
    }

    /*
    In method down bellow is main core of all NPCs that makes all other methods in action
    using moveToTarget to move NPCs to targets they need or making sure to show the player that NPC died
    making it's sprite black and face down the earth.
     */

    public void update(float deltaTime){



        if (!alive){
            afterLife+=deltaTime;
            if (afterLife >= 10000){
                active = false;
                npcY = Game.instance.screenHeight*2;
                afterLife = 0;
            }else {
                if (Math.abs(GameView.instance.player.position.x-npcX) < 150 && Math.abs(GameView.instance.player.position.y-npcY) < 150) {
                    if (GameView.instance.player.health < GameView.instance.player.maxHealth || (GameView.instance.player.mana < GameView.instance.player.maxMana)){

                        GameView.instance.player.health+=20;
                        GameView.instance.player.mana+=20;
                        active = false;
                        afterLife = 0;
                        if (GameView.instance.player.health > GameView.instance.player.maxHealth){
                            GameView.instance.player.health = GameView.instance.player.maxHealth;
                        }
                        if (GameView.instance.player.mana > GameView.instance.player.maxMana){
                            GameView.instance.player.mana = GameView.instance.player.maxMana;
                        }
                    }
                }
            }
            npcY = (int) GameView.instance.groundLevel-npcRect.height()+npcRect.height()/3;
            NpcPain.setColorFilter(colorFilter);
        }else {
            npcY = (int) GameView.instance.groundLevel-npcRect.height();
            NpcPain.setColorFilter(null);
            countdown+=deltaTime;
            if (Math.abs(target.x-npcX)>1){
                direction = (int) Math.signum(target.x-npcX);
            }
            moveToTarget(deltaTime);
        }
        npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),(int)npcY);
    }

    /*
    In physics I realized the way of damaging NPCs by fire breath of dragon making them taking damage.
     */

    public  void  physics(float deltaTime) {
        npcCollider = new Rect((int)npcX, (int)npcY, (int)(npcX + npcRect.width()), (int)(npcY + npcRect.height()));
        //System.out.println("npcphysics");
        try {
            if (GameView.instance.player.fireBreath.collision(npcCollider) && !damagePeriod.performing) {
                //System.out.println(npcHp);
                OnDamage();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        int top = (int) (npcRect.top + Math.sin(distTravel / 4 + random * Math.PI * 2) * 3);
        int left = npcRect.left;
        int right = left + npcRect.width();
        int bottom = top + npcRect.height();
        RectF tempRect = new RectF(0, 0, npcBitmap.getWidth(), npcBitmap.getHeight());
        destTempRect = new RectF(left, top, right, bottom);
        matrix.setRectToRect(tempRect, destTempRect, Matrix.ScaleToFit.FILL);
        if (alive) {
            matrix.postScale(direction, 1, destTempRect.centerX(), destTempRect.centerY());
        }
        else {
            matrix.postRotate(90, destTempRect.centerX(), destTempRect.centerY());
        }
    }

    /*
    Down bellow I realized method in which i made Idle state of all NPCs as in some situations they'd need it
    like Wooloos Idling on hte fields or NPC's Idling when they run far enough.
     */

    public void idle(int boundry,boolean lessTen){
        if (countdown >= Math.random()*5000){
            if (lessTen) {
                flee = false;
                double targetDistance = (Math.random() - 0.5) * boundry;
                target.x = (int) (tempCreationPoint.x + targetDistance);
                countdown = 0;
            }
        }
    }
}