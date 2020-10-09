package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.sql.Time;
import java.util.Timer;
import java.util.Vector;

public class NPC {
    Bitmap npcBitmap;
    int npcX,npcY,npcMaxHP,npcHp,npcWidth,npcHeight;
    Rect npcRect;
    int npcSpeed;
    Boolean alive = true;
    Point movement;
    public NPC (Bitmap bitmap, int x, int y, int speed, int maxHP, int width,int height) {
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
    boolean there = false;
    public void  move(int moveToX){
        if(!there){
            npcX+=npcSpeed;
            if (npcX >= moveToX) there = true;
        }else if (there){

        }
        npcRect.offsetTo(npcX,npcY);
    }
    public void update(float deltaTime){

    }
}