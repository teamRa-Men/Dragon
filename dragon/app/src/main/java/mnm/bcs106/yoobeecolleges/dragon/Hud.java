package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Hud {
    public static Hud instance;
    Paint bar = new Paint(), mana = new Paint(),health= new Paint(),control= new Paint(),fire= new Paint(),gold= new Paint();

    Vector2 dragFrom = Game.instance.dragFrom;
    Vector2 dragTo = Game.instance.dragTo;

    Bitmap fireButtonSprite;
    Bitmap goldMeterBack;
    Bitmap goldMeterFront;
    Bitmap goldMeter;
    Bitmap goldMeterGold;
    Rect meterSrc;
    Rect meterDst;


    int barLeft, manaTop, manaMaxWidth, manaBottom, healthTop, healthMaxWidth, healthBottom;
    int manaWidth, healthWidth;

    int screenWidth, screenHeight;
    Dragon player;

    public Hud(){

            instance = this;

            screenWidth = GameView.instance.screenWidth;
            screenHeight = GameView.instance.screenHeight;
            player = GameView.instance.player;

            control.setColor(Color.WHITE);
            control.setAlpha(100);
            control.setStrokeWidth(20);
            control.setStrokeCap(Paint.Cap.ROUND);

            fireButtonSprite = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.fire_hud);
            fireButtonSprite = Bitmap.createScaledBitmap(fireButtonSprite, Game.instance.controlRadius*2,Game.instance.controlRadius*2, false);
            fire.setColorFilter(new LightingColorFilter(Game.instance.getResources().getColor(R.color.colorFire),0));

        goldMeter = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.goldmeter);
        goldMeter = Bitmap.createScaledBitmap(goldMeter, Game.instance.controlRadius*2,Game.instance.controlRadius*2, false);

        goldMeterBack = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.goldmeterback);
        goldMeterBack = Bitmap.createScaledBitmap(goldMeterBack, Game.instance.controlRadius*2,Game.instance.controlRadius*2, false);

        goldMeterFront = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.goldmeterfront);
        goldMeterFront = Bitmap.createScaledBitmap(goldMeterFront, Game.instance.controlRadius*2,Game.instance.controlRadius*2, false);

        goldMeterGold = BitmapFactory.decodeResource(Game.instance.getResources(),R.drawable.goldmetergold);
        goldMeterGold = Bitmap.createScaledBitmap(goldMeterGold, Game.instance.controlRadius*2,Game.instance.controlRadius*2, false);

        meterSrc = new Rect(0,0,Game.instance.controlRadius*2,Game.instance.controlRadius*2);
        int left = GameView.instance.screenWidth-Game.instance.controlRadius*2;
        int right = left+Game.instance.controlRadius*2;
        int top = screenHeight/30;
        int bottom = top +Game.instance.controlRadius*2;
        meterDst = new Rect(left,top,right,bottom);



/*
        gold.setTextSize(GameView.instance.screenWidth/30);
            gold.setFakeBoldText(true);
            gold.setColor(Color.WHITE);
            gold.setTextAlign(Paint.Align.RIGHT);
            gold.setShadowLayer(3,1,1,Color.DKGRAY);

            bar.setColor(Color.BLACK);
            bar.setShadowLayer(3,0,3,Color.DKGRAY);
            health.setColor(Game.instance.getResources().getColor(R.color.colorHealth));
            mana.setColor(Game.instance.getResources().getColor(R.color.colorMana));
            barLeft = screenWidth/8;
            manaMaxWidth = (int)(screenWidth/3*player.maxMana/100);
            healthMaxWidth = screenWidth/3*player.maxHealth/100;

            healthTop = screenHeight/15;
            healthBottom = healthTop+20;
            manaTop = healthBottom + 10;
            manaBottom = manaTop+20;
            */
    }
    public void update(float deltaTime){
        dragFrom = Game.instance.dragFrom;
        dragTo = Game.instance.dragTo;


        meterSrc.top = (int)( (0.85-(float)player.goldHolding/player.maxGoldHolding)*Game.instance.controlRadius*2+1);
        meterDst.top = (int)( screenHeight/30  +(0.85-(float)player.goldHolding/player.maxGoldHolding)*Game.instance.controlRadius*2+1);



        if(GameView.instance.player.breathingFire){
            fire.setColorFilter(new LightingColorFilter(Color.WHITE,Color.rgb(100,100,50)));

        }
        else {
            fire.setColorFilter(new LightingColorFilter(Color.WHITE,0));

        }
        /*
        healthWidth = Math.max((int)(healthMaxWidth*player.health/player.maxHealth),0);
        manaWidth = (int)(manaMaxWidth*player.mana/player.maxMana);

         */
    }
    public  void draw(Canvas canvas){
        if(dragFrom !=null && dragTo!=null) {

            canvas.drawLine(dragFrom.x, dragFrom.y, dragTo.x, dragTo.y, control);
        }


        canvas.drawBitmap(fireButtonSprite,Game.instance.fireButton.x-Game.instance.controlRadius, Game.instance.fireButton.y-Game.instance.controlRadius, fire);

        canvas.drawBitmap(goldMeterBack,GameView.instance.screenWidth-Game.instance.controlRadius*2, screenHeight/30, null);
        canvas.drawBitmap(goldMeter,GameView.instance.screenWidth-Game.instance.controlRadius*2, screenHeight/30, null);

        canvas.drawBitmap(goldMeterGold,meterSrc,meterDst, null);

        canvas.drawBitmap(goldMeterFront,GameView.instance.screenWidth-Game.instance.controlRadius*2, screenHeight/30, null);

        // canvas.drawText(GameView.instance.player.goldHolding+" G",GameView.instance.screenWidth, screenHeight/10,gold);
    /*
        canvas.drawRect(barLeft, manaTop, barLeft + manaMaxWidth, manaBottom, bar);
        canvas.drawRect(barLeft, healthTop, barLeft + healthMaxWidth, healthBottom, bar);
        canvas.drawRect(barLeft, manaTop,barLeft + manaWidth, manaBottom, mana);
        canvas.drawRect(barLeft, healthTop,barLeft+ healthWidth, healthBottom, health);*/

    }
}
