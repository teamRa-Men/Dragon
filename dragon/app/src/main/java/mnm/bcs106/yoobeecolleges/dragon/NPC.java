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
    public  Boolean alive = true;
    public  Point movement;
    public Point target = new Point();
    public NPC (Bitmap bitmap, int x, int y, float speed, int maxHP, int width,int height) {
        npcBitmap = bitmap;
        npcX = x;
        npcY = y;
        npcMaxHP = maxHP;
        npcHp = maxHP;
        npcSpeed = speed;
        npcWidth = width;
        npcHeight = height;
        npcRect = new Rect(npcX,npcY,width+npcX,height+npcY);
    }
    public void spawn (){
        alive = true;
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
    public void  move(int moveToX){
        if(!there){
            npcX+=npcSpeed;
            if (npcX >= moveToX) there = true;
        }else if (there){

        }
        npcRect.offsetTo(npcX,npcY);
    }
    public void moveToTarget(float deltaTime){
        if (Math.abs(target.x-npcX) > 0.1){
            npcX+=Math.signum(target.x-npcX)*npcSpeed*deltaTime;
        }
    }
    public void update(float deltaTime){
        moveToTarget(deltaTime);
        npcY = (int) GameView.instance.groundLevel-npcRect.height();
        npcRect.offsetTo((int) (npcX+GameView.instance.cameraDisp.x),npcY);
    }
}