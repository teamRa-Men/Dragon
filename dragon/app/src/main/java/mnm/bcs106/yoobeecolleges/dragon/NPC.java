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
    public void spawn (int spawnX, int spawnY){
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
    }
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
    public void death(){
        alive = false;
    }
    float distTravel = 0;

    public void draw(Canvas canvas){
        if(npcBitmap!=null) {
            Matrix matrix = new Matrix();
            int top = (int) (npcRect.top + Math.sin(distTravel / 4 + random * Math.PI * 2) * 3);
            int left = npcRect.left;
            int right = left + npcRect.width();
            int bottom = top + npcRect.height();
            RectF tempRect = new RectF(0, 0, npcBitmap.getWidth(), npcBitmap.getHeight());
            RectF destTempRect;
            destTempRect = new RectF(left, top, right, bottom);
            matrix.setRectToRect(tempRect, destTempRect, Matrix.ScaleToFit.FILL);
            if (alive) {
                matrix.postScale(direction, 1, destTempRect.centerX(), destTempRect.centerY());
                canvas.drawBitmap(npcBitmap, matrix, Scene.instance.frontPaint);
            } else {
                matrix.postRotate(90, destTempRect.centerX(), destTempRect.centerY());
                canvas.drawBitmap(npcBitmap, matrix, NpcPain);
            }
        }

    }

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
    public void update(float deltaTime){
        if (!alive){
            afterLife+=deltaTime;
            if (afterLife >= 10000){
                active = false;
                afterLife = 0;
            }else {
                if (Math.abs(GameView.instance.player.position.x-npcX) < 100 && Math.abs(GameView.instance.player.position.y-npcY) < 100) {
                    if (GameView.instance.player.health < GameView.instance.player.maxHealth){
                        GameView.instance.player.health+=npcMaxHP/20;
                        active = false;
                        afterLife = 0;
                        if (GameView.instance.player.health > GameView.instance.player.maxHealth){
                            GameView.instance.player.health = GameView.instance.player.maxHealth;
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
    }

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