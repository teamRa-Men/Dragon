
package mnm.bcs106.yoobeecolleges.lifeofdragon;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

//-----------------------------------------------------------------------------------------------------------
//Game Controller
//Initialized game, handles user input, UI, sounds
//-----------------------------------------------------------------------------------------------------------

public class Game extends AppCompatActivity {
    //UI
    TextView scoreText;
    Button musicButton;
    ProgressBar healthBar;
    //float progress;

    //state variables
    boolean paused = false, showGameOver = false, gameOver = false, waveStart = false, waveEnd = false;

    int screenHeight, screenWidth;
    public int score = 0, highScore;

    //misc
    MediaPlayer pointsPlayer;
    SoundEffects soundEffects;
    AlertDialog.Builder dialogBuilder;
    SharedPreferences.Editor highScoreEdit;

    //Threads
    Handler handler;
    Runnable runnable;

    //Game engine
    GameView gameView;

    //Singleton
    public static Game instance;

    //Player control
    boolean dragging = false;
    boolean breathFire = false;
    Vector2 dragTo, dragFrom;
    int controlRadius = 30;
    Vector2 fireButton;

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Singleton
        if(instance == null) {
            instance = this;
            context = this;

            //Get screen dimensions
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screenHeight = displayMetrics.heightPixels;
            screenWidth = displayMetrics.widthPixels;
            //Hide navigation
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);





            //initialize
            initUI();
            initSound(this);

            waveStart();
            handler = new Handler();


            //start game loop
            gameView = new GameView(this);
            ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
            gameLayout.addView(gameView);
            updateUI();



        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }


    }


    //*********************************************************************************************************************************************************//
    // Initialization Methods

    void initUI(){

        controlRadius = screenWidth/20;
        fireButton = new Vector2(screenWidth*9/10,screenHeight*8.5f/10);

        //Load high score
        SharedPreferences pref = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        highScoreEdit = pref.edit();


        //Game over dialog box
        dialogBuilder = new AlertDialog.Builder(this);
    }


    void initSound(Context context){

        soundEffects = new SoundEffects(context);
    }

    //*********************************************************************************************************************************************************//
    // Game loop and game state methods

    public void updateUI(){
        runnable = new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        };
        //30 frames per second
        handler.postDelayed(runnable, 1000/30);

        if(!gameOver) {
            //gameView.setPlayerMovement(dragTo);
            if(dragFrom !=null && dragTo!=null) {
                gameView.movePlayerBy(dragTo.sub(dragFrom).multiply(1f / controlRadius/2));
            }
            else {
                gameView.movePlayerBy(null);
            }
            gameView.breathFire(breathFire);
            pointsAndLevels();

        }

        //Show game over pop up if told by game engine
        if(showGameOver) {
            gameOver();
        }
    }

    //Calculate points scored and level
    void pointsAndLevels(){


    }



    //On game over show dialog box with results and give the player the options of quiting to main menu or trying again
    void gameOver(){
        //Apply only once
        showGameOver = false;
        gameOver = true;

        //Custom alert dialog
        ViewGroup showGameOver = (ViewGroup) getLayoutInflater().inflate(R.layout.game_over,null,false);

        //Handle messages for dialog box
        ((TextView)showGameOver.findViewById(R.id.gameOverText)).setText("GAME OVER");
        String plural = "S";
        if(score == 1){
            plural = "";
        }
        if(score > highScore){
            highScore = score;

            //Save high score
            highScoreEdit.putInt("HighScore", highScore);
            highScoreEdit.commit();

            ((TextView)showGameOver.findViewById(R.id.newHighScore)).setText("NEW RECORD \n"+highScore +" MONSTER"+plural+" DEFEATED");
        }
        else{
            ((TextView)showGameOver.findViewById(R.id.newHighScore)).setText("YOU DEFEATED \n "+score + " MONSTER"+plural);
        }

        dialogBuilder.setView(showGameOver);
        final AlertDialog dialog = dialogBuilder.create();

        //Dialog box negative button, return to main menu
        showGameOver.findViewById(R.id.mainMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Dialog box positive button, start new game
        showGameOver.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset states and restart game loop
                score = 0;
                paused = false;
                gameView.init();

                //Close dialog box
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        gameOverAnim(score==highScore && score>0);
    }

    //Show score and play rooster sound effect at beginning of wave
    private void waveStart(){

        //soundEffects.play(SoundEffects.ROOSTER);
        waveStart = false;
    }

    //Move on to the next score
    private void waveEnd(){
        score++;
        waveEnd = false;
    }

    //*********************************************************************************************************************************************************//
    // On click methods




    //*********************************************************************************************************************************************************//
    // Animations


    //Show points scored, rise up and fade away
    void pointsScoredAnim(int pointsScored, int x, int y){
    }

    void deathAnim(){

    }


    //Shape animation on game over
    void gameOverAnim(boolean hiScore){


    }

    //*********************************************************************************************************************************************************//
    // Accessor / Mutator methods


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Vector2 p = new Vector2(event.getX(),event.getY());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragFrom = p;
            dragging = true;
            if(Vector2.distance(p,fireButton) < controlRadius){
                breathFire = true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() ==  MotionEvent.ACTION_OUTSIDE) {
            dragging = false;
            dragTo = null;
            breathFire = false;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (dragging) {
                Vector2 disp = p.sub(dragFrom);

                if(disp.getLength()>controlRadius*2){

                    p = disp.getNormal().multiply(Math.min(disp.getLength(),controlRadius*2)).add(dragFrom);
                }
                dragTo = p;
            }
        }

        return super.onTouchEvent(event);

    }

    public void onGrow(View view){
        gameView.pause();
        int size = gameView.player.size+5;
        if(size <70)
            gameView.player.initBody(size);
        gameView.resume();
    }

    public void onShrink(View view){
        gameView.pause();
        int size = gameView.player.size-5;
        if(size > 25)
            gameView.player.initBody(size);
        gameView.resume();
    }
}