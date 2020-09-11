package com.example.buildings_prototype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class GameView extends View {

    long Time;
    Fortress fort;
    public MainActivity activity;

    public GameView(Context context) {
        super(context);

        Time = System.currentTimeMillis();
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.house);
        b = Bitmap.createScaledBitmap(b,100,100,false);
        fort = new Fortress(b, 400,400, 4, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        fort.update(1);
        fort.draw(canvas);
        postInvalidate();


    }
}
