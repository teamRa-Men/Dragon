package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

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
    public NPC (Bitmap bitmap, int x, int y, float speed, int maxHP, int width,int height) {
        npcBitmap = bitmap;
        npcX = x;
        npcY = y;
        npcMaxHP = maxHP;
        npcHp = maxHP;
        npcSpeed = speed;
        npcFleeSpeed = npcSpeed*4;
        npcWidth = width;
        npcHeight = height;
        npcRect = new Rect(npcX,npcY,width+npcX,height+npcY);
        damagePeriod = new ActionController(0,500,500);
    }
    public void spawn (){
        alive = true;
    }
    public  void OnDamage () {
        damagePeriod.triggerAction();
        npcHp-=10;
        if (npcHp<=0){
            death();
        }
    }
    public void death(){
        alive = false;
    }
    public void draw(Canvas canvas){
        if (alive){
            canvas.drawBitmap(npcBitmap,null,npcRect,null);
        }
    }
    public  boolean there = false;
    public void moveToTarget(float deltaTime){
        if (Math.abs(target.x-npcX) > 0.1){
            if (!flee){
                npcX+=Math.signum(target.x-npcX)*npcSpeed*deltaTime;
            } else npcX+=Math.signum(target.x-npcX)*npcFleeSpeed*deltaTime;
        }
    }
    public void update(float deltaTime){
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