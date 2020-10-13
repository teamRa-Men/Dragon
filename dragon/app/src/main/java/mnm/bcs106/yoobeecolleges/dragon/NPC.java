package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.BoringLayout;

import java.sql.Time;
import java.util.Timer;
import java.util.Vector;

public class NPC {
    public Bitmap npcBitmap;
    public int npcX,npcY,npcMaxHP,npcHp,npcWidth,npcHeight;
    public Rect npcRect;
    public float npcSpeed;
    public Boolean alive = false;
    public Point movement;
    public Point target = new Point();
    public float npcFleeSpeed;
    public boolean flee = false;
    public ActionController damagePeriod;
    public int direction;
    public NPC (Bitmap bitmap, int x, int y, float speed, int maxHP, int width,int height) {
        npcBitmap = bitmap;
        npcX = x;
        npcY = y;
        npcMaxHP = maxHP;
        npcHp = maxHP;
        npcSpeed = speed;
        npcFleeSpeed = (float) ((Math.random()*npcSpeed)+(npcSpeed*3));
        npcWidth = width;
        npcHeight = height;
        npcRect = new Rect(npcX,npcY,width+npcX,height+npcY);
        damagePeriod = new ActionController(0,5000,5000);
    }
    public void spawn (){
        alive = true;
    }
    public  void OnDamage () {
        damagePeriod.triggerAction();
        npcHp-=1;
        if (npcHp<=0){
            death();
        }
    }
    public void death(){
        alive = false;
        npcX = 0;
        npcY = 0;
    }
    float distTravel = 0;
    public void draw(Canvas canvas){
        if (alive){
            RectF tempRect = new RectF(0,0,npcBitmap.getWidth(),npcBitmap.getHeight());
            int top  = (int) (npcRect.top+Math.cos(distTravel/10)*10);
            int left  = npcRect.left;
            int right  = left+npcRect.width();
            int bottom  = top+npcRect.height();
            RectF destTempRect = new RectF(left,top,right,bottom);
            Matrix matrix = new Matrix();
            matrix.setRectToRect(tempRect,destTempRect, Matrix.ScaleToFit.FILL);
            matrix.postScale(direction,1,destTempRect.centerX(),destTempRect.centerY());
            canvas.drawBitmap(npcBitmap,matrix,null);
        }
    }
    public  boolean there = false;
    public void moveToTarget(float deltaTime){
        if (Math.abs(target.x-npcX) > 0.1){
            if (!flee){
                npcX+=direction*npcSpeed*deltaTime;
                distTravel+=npcSpeed*deltaTime;
            } else {
                npcX+=direction*npcFleeSpeed*deltaTime;
                distTravel+=npcFleeSpeed*deltaTime;
            }
        }
    }
    public void update(float deltaTime){
        if (Math.abs(target.x-npcX)>1){
            direction = (int) Math.signum(target.x-npcX);
        }
        moveToTarget(deltaTime);
        npcY = (int) GameView.instance.groundLevel-npcRect.height();
        npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),npcY);
    }
    public  void  physics(float deltaTime){
        if (GameView.instance.player.fireBreath.collision(npcRect)&&!damagePeriod.performing){
            OnDamage();
        }
    }
}