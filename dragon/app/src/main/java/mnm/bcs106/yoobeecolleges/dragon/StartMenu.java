package mnm.bcs106.yoobeecolleges.dragon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class StartMenu extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable runnable;
    StartView startView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Music music = new Music(this);
        music.playThemeMusic(true);
        new SoundEffects(this);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        startView = new StartView(this);
        ConstraintLayout startLayout = findViewById(R.id.startSurfaceView);
        startLayout.addView(startView);
        updateUI();
    }

    public void updateUI(){
        runnable = new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        };
        //30 frames per second
        handler.postDelayed(runnable, 1000/15);
        startView.movePlayerBy(null);
        startView.update(1000/15);
        startView.physics(1000/15);
        startView.draw();

    }


    public void startGame(View view){
        SoundEffects.instance.play(SoundEffects.SELECT);
        Intent i = new Intent(this, Game.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }
}