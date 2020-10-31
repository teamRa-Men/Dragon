
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;

//-----------------------------------------------------------------------------------------------------------
//Game Controller
//Initialized game, handles user input, UI, sounds
//-----------------------------------------------------------------------------------------------------------

public class Game extends AppCompatActivity {
    //UI
    Vector2 fireButton;

    Button sleepButton;
    Button wakeButton;
    Button upgradeButton;
    Button stopButton;
    Button pauseContinue;
    Button pauseRestart;
    Button pauseCredits;
    Button pauseExit;

    Button backButton;
    Button upgradeAttackButton;
    Button upgradeHealthButton;
    Button upgradeManaButton;
    Button upgradeSpeedButton;

    TextView xpText;
    ProgressBar xpBar;
    ProgressBar progressHealth;
    ProgressBar progressAttack;
    ProgressBar progressMana;
    ProgressBar progressSpeed;

    boolean visibleCredits;
    CardView creditCard;

    //state variables
    boolean showGameOver = false, gameOver = false, showSleepButton = false, showUpgradeButton = false, showWakeButton = false;

    int screenHeight, screenWidth;
    public int score = 0, highScore;
    float refreshRating;

    //misc
    MediaPlayer pointsPlayer;
    SoundEffects soundEffects;
    AlertDialog.Builder gameOverDialogBuilder;
    AlertDialog.Builder pauseDialogBuilder;
    AlertDialog.Builder upgradeDialogBuilder;
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


    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Screen refresh rate detection
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        refreshRating = display.getRefreshRate();

        //Singleton

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

        handler = new Handler();


        //start game loop
        gameView = new GameView(this);
        ConstraintLayout gameLayout = findViewById(R.id.game);
        gameLayout.addView(gameView);
        updateUI();


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
        fireButton = new Vector2(screenWidth*0.95f,screenHeight*0.85f);

        //Load high score
        SharedPreferences pref = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        highScoreEdit = pref.edit();

        gameOverDialogBuilder = new AlertDialog.Builder(this);

        pauseDialogBuilder = new AlertDialog.Builder(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        final ViewGroup pauseMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_pause_menu,null,false);
        pauseDialogBuilder.setView(pauseMenu);
        final AlertDialog dialog = pauseDialogBuilder.create();



        stopButton = findViewById(R.id.buttonOfStop);
        pauseContinue = pauseMenu.findViewById(R.id.pauseContinue);
        pauseRestart = pauseMenu.findViewById(R.id.pauseRestart);
        pauseCredits = pauseMenu.findViewById(R.id.pauseCredits);
        pauseExit = pauseMenu.findViewById(R.id.pauseExit);
        creditCard = pauseMenu.findViewById(R.id.creditsCard);
        visibleCredits = false;
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView.instance.pause();
                dialog.show();
            }
        });
        pauseContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView.instance.resume();
                dialog.dismiss();
            }
        });
        pauseCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visibleCredits){
                    creditCard.setVisibility(View.VISIBLE);
                    visibleCredits = true;
                } else {
                    creditCard.setVisibility(View.INVISIBLE);
                    visibleCredits = false;
                }
            }
        });
        pauseRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView.instance.init();
                dialog.dismiss();
            }
        });
        pauseExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        upgradeDialogBuilder = new AlertDialog.Builder(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        final ViewGroup upgradeMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_upgrade,null,false);
        upgradeDialogBuilder.setView(upgradeMenu);
        final AlertDialog upgradeDialog = upgradeDialogBuilder.create();

        upgradeAttackButton = upgradeMenu.findViewById(R.id.upgradeAttack);
        upgradeSpeedButton = upgradeMenu.findViewById(R.id.upgradeSpeed);
        upgradeManaButton = upgradeMenu.findViewById(R.id.upgradeMana);
        upgradeHealthButton = upgradeMenu.findViewById(R.id.upgradeHealth);



        upgradeAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeAttack()){
                    //play upgrade sound, show graphic
                }
            }
        });
        upgradeSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeSpeed()){

                }
            }
        });
        upgradeManaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeMana()){

                }
            }
        });
        upgradeHealthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeHealth()){

                }
            }
        });
        backButton = upgradeMenu.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upgradeDialog.dismiss();
                GameView.instance.resume();
            }
        });


        sleepButton = findViewById(R.id.sleepButton);
        wakeButton = findViewById(R.id.wakeButton);
        upgradeButton = findViewById(R.id.upgradeButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView.instance.pause();
                upgradeDialog.show();
            }
        });


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

            fadeButton(showSleepButton, sleepButton);
            fadeButton(showWakeButton, wakeButton);
            fadeButton(showUpgradeButton, upgradeButton);
        }

        //Show game over pop up if told by game engine
        if(showGameOver) {
            gameOver();
        }
    }

    void fadeButton(boolean condition, Button button){
        if(condition){
            button.setVisibility(View.VISIBLE);
            if(button.getAlpha() <1) {
                button.setAlpha(button.getAlpha() + 0.1f);
            }
            else{
                button.setAlpha(1);
            }
        }
        else {
            if(button.getAlpha() >0) {
                button.setAlpha(button.getAlpha() - 0.1f);
            }
            else{
                button.setVisibility(View.INVISIBLE);
            }
        }
    }

    //On game over show dialog box with results and give the player the options of quiting to main menu or trying again
    void gameOver(){
        //Apply only once
        showGameOver = false;
        gameOver = true;
        gameView.pause();

        //Custom alert dialog
        ViewGroup showGameOver = (ViewGroup) getLayoutInflater().inflate(R.layout.game_over,null,false);

        gameOverDialogBuilder.setView(showGameOver);
        final AlertDialog dialog = gameOverDialogBuilder.create();


        //Dialog box positive button, start new game
        showGameOver.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset states and restart game loop
                score = 0;
                gameView.init();

                //Close dialog box
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    //*********************************************************************************************************************************************************//
    // Accessor / Mutator methods


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        for (int i = 0; i < event.getPointerCount(); i++){

            Vector2 p = new Vector2(event.getX(i), event.getY(i));

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN ) {
                //System.out.println("dow");
                if(Vector2.distance(fireButton, p)> controlRadius) {
                    if(!dragging) {
                        dragFrom = p;
                        dragging = true;
                    }


                }
                else {
                    breathFire = true;
                }


            }

            if (action == MotionEvent.ACTION_MOVE ) {
                //System.out.println("move");
                if (Vector2.distance(fireButton, p)> controlRadius) {
                    dragging = true;
                    Vector2 disp = p.sub(dragFrom);

                    if (disp.getLength() > controlRadius * 2) {

                        p = disp.getNormal().multiply(Math.min(disp.getLength(), controlRadius * 2)).add(dragFrom);
                    }
                    dragTo = p;

                }
                else {
                    breathFire = true;
                }
            }

            if (action== MotionEvent.ACTION_UP || action == MotionEvent.ACTION_OUTSIDE|| action == MotionEvent.ACTION_POINTER_UP|| action == MotionEvent.ACTION_POINTER_2_UP|| action == MotionEvent.ACTION_POINTER_3_UP) {
                //System.out.println("up");
                if(Vector2.distance(fireButton, p)> controlRadius) {
                    dragging = false;
                    dragTo = null;
                }
                else {
                    breathFire = false;
                }
                if(!dragging){
                    breathFire = false;
                }
                if(!breathFire){
                    dragging = false;
                }

            }


            System.out.println();



        }


        return super.onTouchEvent(event);

    }

    public void onSleep(View view){
        GameView.instance.lair.sleep(view);
    }

    public void onWake(View view){
        GameView.instance.lair.wake(view);
    }



    public void onGrow(View view){
        gameView.pause();
        int size = gameView.player.size+3;
        if(size <70)
            gameView.player.initBody(size);
        gameView.resume();
    }

    public void onShrink(View view){
        gameView.pause();
        int size = gameView.player.size-5;
        if(size > 35)
            gameView.player.initBody(size);
        gameView.resume();
    }
}
