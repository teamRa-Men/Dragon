
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

//-----------------------------------------------------------------------------------------------------------
//Game Controller
//Initialized game, handles user input, UI, sounds
//-----------------------------------------------------------------------------------------------------------

public class Game extends AppCompatActivity {
    //UI
    Vector2 fireButton;

    Button mournButton;
    Button sleepButton;
    Button wakeButton;
    View upgradeButton;
    ProgressBar xpBarLair;
    TextView xpTextLair;
    ProgressBar healthBar;
    TextView healthText;
    ProgressBar manaBar;
    TextView manaText;
    TextView goldDeposited;
    TextView levelLair;

    Button stopButton;
    Button pauseContinue;
    Button pauseRestart;
    Button pauseCredits;
    Button pauseExit;
    SeekBar soundVolume;
    SeekBar musicVolume;

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
    TextView upgradePoints;
    TextView level;

    TextView showDayText;

    boolean visibleCredits;
    CardView creditCard;

    //state variables
    boolean breathCoolDown, showGameOver = false, gameOver = false, showSleepButton = false, showWakeButton = false, showDay = true;
    boolean showMournButton = false;
    boolean mourning = false;
    int breathCoolDownLength = 150, coolDownTime = 0;
    int screenHeight, screenWidth;
    public StatsRecorder statsRecorder;
    float refreshRating;

    //misc
    MediaPlayer pointsPlayer;

    AlertDialog.Builder gameOverDialogBuilder;
    AlertDialog.Builder pauseDialogBuilder;
    AlertDialog.Builder upgradeDialogBuilder;
    ConstraintLayout loadScreen;

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
    int runTime = 0, loadingTime = 3000;


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

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);




        //initialize

        initUI();

        handler = new Handler();

        statsRecorder = new StatsRecorder();

        //start game loop
        gameView = new GameView(this);
        statsRecorder.init();

        SoundEffects.instance.release();
        SoundEffects soundEffects = new SoundEffects(Game.instance);

        ConstraintLayout gameLayout = findViewById(R.id.surfaceView);
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
        loadScreen = findViewById(R.id.loadScreen);

        gameOverDialogBuilder =  new AlertDialog.Builder(this,R.style.MyAlertDialog);

        pauseDialogBuilder = new AlertDialog.Builder(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        final ViewGroup pauseMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_pause_menu,null,false);
        pauseDialogBuilder.setView(pauseMenu);
        final AlertDialog dialog = pauseDialogBuilder.create();

//******************************************************************************************************************
        //PAUSE MENU

        stopButton = findViewById(R.id.buttonOfStop);
        soundVolume = pauseMenu.findViewById(R.id.soundVolume);
        musicVolume = pauseMenu.findViewById(R.id.musicVolume);
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
                SoundEffects.instance.play(SoundEffects.MENU);
                dialog.show();
            }
        });
        pauseContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView.instance.resume();
                SoundEffects.instance.play(SoundEffects.MENU);
                dialog.dismiss();
            }
        });
        pauseCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.instance.play(SoundEffects.MENU);
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
                SoundEffects.instance.play(SoundEffects.SELECT);
                gameView.pause();
                statsRecorder.gameEnd();

                runTime = 0;
                loadScreen.setAlpha(1);
                Game.instance.showDay = true;
                gameView.init();

                statsRecorder.init();
                dialog.dismiss();
            }
        });
        pauseExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.instance.play(SoundEffects.SELECT);
                StatsRecorder.instance.gameEnd();
                finish();
            }
        });
        musicVolume.setProgress( (int)(Music.instance.volume*100));
        musicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Music.instance.setVolume((float)progress/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Music.instance.playThemeMusic(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Music.instance.stopThemeMusic();
                SoundEffects.instance.play(SoundEffects.MENU);
            }
        });

        soundVolume.setProgress( (int)(SoundEffects.instance.volumeMul*100));
        soundVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                SoundEffects.instance.volumeMul = (float)progress/100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SoundEffects.instance.playCoin();
                SoundEffects.instance.play(SoundEffects.MENU);
            }
        });

//******************************************************************************************************************
        //UPGRADE MENU


        upgradeDialogBuilder = new AlertDialog.Builder(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        final ViewGroup upgradeMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_upgrade,null,false);
        upgradeDialogBuilder.setView(upgradeMenu);
        final AlertDialog upgradeDialog = upgradeDialogBuilder.create();

        upgradeAttackButton = upgradeMenu.findViewById(R.id.upgradeAttack);
        upgradeSpeedButton = upgradeMenu.findViewById(R.id.upgradeSpeed);
        upgradeManaButton = upgradeMenu.findViewById(R.id.upgradeMana);
        upgradeHealthButton = upgradeMenu.findViewById(R.id.upgradeHealth);

        level = upgradeMenu.findViewById(R.id.levelLair);
        upgradePoints = upgradeMenu.findViewById(R.id.upgradePoints);
        xpText = upgradeMenu.findViewById(R.id.xpTextAmount);
        xpBar = upgradeMenu.findViewById(R.id.xpBar);
        progressHealth = upgradeMenu.findViewById(R.id.progressHealth);
        progressAttack = upgradeMenu.findViewById(R.id.progressAttack);
        progressMana = upgradeMenu.findViewById(R.id.progressMana);
        progressSpeed = upgradeMenu.findViewById(R.id.progressSpeed);


        upgradeAttackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeAttack()){
                    //play upgrade sound, show graphic
                    SoundEffects.instance.play(SoundEffects.SELECT);
                    updateUpgradeMenu();
                }
                else{
                    SoundEffects.instance.play(SoundEffects.MENU);
                }
            }
        });
        upgradeSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeSpeed()){
                    SoundEffects.instance.play(SoundEffects.SELECT);
                    updateUpgradeMenu();
                }
                else{
                    SoundEffects.instance.play(SoundEffects.MENU);
                }
            }
        });
        upgradeManaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeMana()){
                    SoundEffects.instance.play(SoundEffects.SELECT);
                    updateUpgradeMenu();
                }
                else{
                    SoundEffects.instance.play(SoundEffects.MENU);
                }
            }
        });
        upgradeHealthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameView.instance.lair.upgradeHealth()){
                    SoundEffects.instance.play(SoundEffects.SELECT);
                    updateUpgradeMenu();
                }
                else{
                    SoundEffects.instance.play(SoundEffects.MENU);
                }
            }
        });
        backButton = upgradeMenu.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.instance.play(SoundEffects.MENU);
                upgradeDialog.dismiss();
                GameView.instance.resume();
            }
        });

        showDayText = findViewById(R.id.showDayText);
        mournButton = findViewById(R.id.mourn);
        sleepButton = findViewById(R.id.sleepButton);
        wakeButton = findViewById(R.id.wakeButton);
        upgradeButton = findViewById(R.id.statsHUD);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.instance.play(SoundEffects.MENU);
                GameView.instance.pause();
                updateUpgradeMenu();
                upgradeDialog.show();
            }
        });


        //******************************************************************************************************************
        //HUD

        xpBarLair= findViewById(R.id.xpBarLair);
        xpTextLair = findViewById(R.id.xpTextLair);

        healthBar= findViewById(R.id.health);
        healthText = findViewById(R.id.healthText);

        manaBar= findViewById(R.id.mana);
        manaText = findViewById(R.id.manaText);

        goldDeposited = findViewById(R.id.goldDeposited);
        levelLair = findViewById(R.id.levelLair);


    }

    public void updateUpgradeMenu(){
        Lair lair = GameView.instance.lair;
        Dragon player = GameView.instance.player;
        level.setText("LV "+ (int)lair.level);
        upgradePoints.setText((int)lair.upgradePoints + "AP");
        xpBar.setProgress((int)(lair.experience/(lair.nextLevel)*100));
        xpText.setText((int)lair.experience + " XP");
        progressAttack.setProgress((int)((player.attack-lair.minimumAttack)/(lair.maximumAttack-lair.minimumAttack)*100));
        progressMana.setProgress((int)((player.maxMana-lair.minimumMana)/(lair.maximumMana-lair.minimumMana)*100));
        progressHealth.setProgress((int)((player.maxHealth-lair.minimumHealth)/(lair.maximumHealth-lair.minimumHealth)*100));
        progressSpeed.setProgress((int)((player.maxMoveSpeed-lair.minimumSpeed)/(lair.maximumSpeed-lair.minimumSpeed)*100));
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
        handler.postDelayed(runnable, 1000/60);

        if(!gameOver && gameView.isRunning) {
            if(runTime > loadingTime) {
                float alpha = loadScreen.getAlpha();
                if (alpha > 0) {
                    loadScreen.setAlpha(loadScreen.getAlpha() - 0.01f / alpha / alpha);
                }

                if(!mourning) {
                    //gameView.setPlayerMovement(dragTo);
                    if (dragFrom != null && dragTo != null) {
                        gameView.movePlayerBy(dragTo.sub(dragFrom).multiply(1f / controlRadius / 2));
                    } else {
                        gameView.movePlayerBy(null);
                    }
                    if (!breathCoolDown) {
                        gameView.breathFire(breathFire);
                    } else {
                        coolDownTime += 1000 / 60;
                        if (coolDownTime > breathCoolDownLength) {
                            breathCoolDown = false;
                            coolDownTime = 0;

                        }
                    }
                }
                else{

                    coolDownTime += 1000 / 60;
                    if(coolDownTime>4000){

                        coolDownTime = 0;

                        gameView.drawHUD = true;
                        //gameView.player.direction = new Vector2(gameView.player.direction.x,0);
                        float groundLevel = GameView.instance.groundLevel-gameView.player.radius*0.6f;
                        gameView.player.groundLevel = groundLevel;
                        gameView.player.position.x += Math.signum(gameView.player.direction.x)*gameView.player.radius/8;
                        gameView.player.position.y = groundLevel;
                        gameView.player.direction.y = 0;
                        gameView.lair.lieDown();
                        groundLevel = GameView.instance.groundLevel-gameView.player.radius*0.6f;
                        gameView.player.groundLevel = groundLevel;
                        gameView.player.position.x += Math.signum(gameView.player.direction.x)*gameView.player.radius/8;
                        gameView.player.position.y = groundLevel;
                        gameView.player.direction.y = 0;
                        mourning = false;
                    }
                }



                if(showDay){
                    showDayText.setText("DAY "+ gameView.scene.day);
                    showDayText.setAlpha(1);
                    showDay = false;
                }
                else  if (showDayText.getAlpha()> 0){
                    showDayText.setAlpha(showDayText.getAlpha() - 0.005f /showDayText.getAlpha() /showDayText.getAlpha());
                }
            }


            runTime+=1000/15;




            Lair lair = GameView.instance.lair;
            Dragon player = GameView.instance.player;

            fadeView(showSleepButton&&!mourning&&player.health>=0, sleepButton);
            fadeView(showWakeButton&&!mourning&&player.health>=0, wakeButton);
            fadeView(showMournButton&&!mourning&&player.health>=0,mournButton);
            fadeView(!mourning&&player.health>=0,upgradeButton);
            fadeView(!mourning&&player.health>=0,stopButton);

            //UPDATE HUD
            goldDeposited.setText(""+lair.depositedGold);
            levelLair.setText("LV "+ (int)lair.level);
            xpBarLair.setProgress((int) (lair.experience / (lair.nextLevel) * 100));
            xpTextLair.setText((int)lair.experience +""+"/"+""+ (int)lair.nextLevel);
            healthBar.setProgress((int) ( player.health/ (player.maxHealth) * 100));
            healthText.setText((int)player.health +""+"/"+""+ (int)player.maxHealth);
            manaBar.setProgress((int) (player.mana / (player.maxMana) * 100));
            manaText.setText((int)player.mana +""+"/"+""+ (int)player.maxMana);
        }

        //Show game over pop up if told by game engine
        if(showGameOver) {
            gameOver();
        }
    }

    void fadeView(boolean condition, View v){
        if(condition){
            v.setVisibility(View.VISIBLE);
            if(v.getAlpha() <1) {
                v.setAlpha(v.getAlpha() + 0.1f);
            }
            else{
                v.setAlpha(1);
            }
        }
        else {
            if(v.getAlpha() >0) {
                v.setAlpha(v.getAlpha() - 0.1f);
            }
            else{
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    //On game over show dialog box with results and give the player the options of quiting to main menu or trying again
    void gameOver(){
        //Apply only once
        showGameOver = false;
        gameOver = true;
        statsRecorder.gameEnd();


        //Custom alert dialog
        ViewGroup showGameOver = (ViewGroup) getLayoutInflater().inflate(R.layout.game_over,null,false);

        gameOverDialogBuilder.setView(showGameOver);
        final AlertDialog dialog = gameOverDialogBuilder.create();
        TextView records = showGameOver.findViewById(R.id.newHighScore);
        if(statsRecorder.days-1 == 1) {
            records.setText("You Survived " + (statsRecorder.days-1) + " Day");
        }
        else{
            records.setText("You Survived " + statsRecorder.days + " Days");
        }

        //Dialog box positive button, start new game
        showGameOver.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset states and restart game loop
                SoundEffects.instance.play(SoundEffects.SELECT);
                Music.instance.startFadeOut(1000);
                gameView.pause();

                runTime = 0;
                loadScreen.setAlpha(1);
                Game.instance.showDay = true;
                gameView.init();
                statsRecorder.init();
                //Close dialog box

                SoundEffects.instance.release();

                SoundEffects soundEffects = new SoundEffects(Game.instance);
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


        for (int i = 0; i < event.getPointerCount(); i++) {

            Vector2 p = new Vector2(event.getX(i), event.getY(i));

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
                //System.out.println("dow");
                if (Vector2.distance(fireButton, p) > controlRadius) {
                    if (!dragging) {
                        dragFrom = p;
                        dragging = true;
                    }


                } else {
                    breathFire = true;
                }


            }

            if (action == MotionEvent.ACTION_MOVE) {
                //System.out.println("move");
                if (Vector2.distance(fireButton, p) > controlRadius) {
                    dragging = true;
                    Vector2 disp = p.sub(dragFrom);

                    if (disp.getLength() > controlRadius * 2) {

                        p = disp.getNormal().multiply(Math.min(disp.getLength(), controlRadius * 2)).add(dragFrom);
                    }
                    dragTo = p;

                } else {
                    breathFire = true;
                }
            }

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_OUTSIDE || action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_POINTER_2_UP || action == MotionEvent.ACTION_POINTER_3_UP) {
                //System.out.println("up");
                if (Vector2.distance(fireButton, p) > controlRadius) {
                    dragging = false;
                    dragTo = null;
                } else {
                    breathFire = false;
                    breathCoolDown = true;

                }
                if (!dragging) {
                    breathFire = false;
                    breathCoolDown = true;
                }
                if (!breathFire) {
                    dragging = false;
                }

            }


            System.out.println();

        }


        return super.onTouchEvent(event);

    }


    public void onSleep(View view){
        GameView.instance.lair.sleep();
        SoundEffects.instance.play(SoundEffects.MENU);
    }

    public void onWake(View view){
        GameView.instance.lair.wake();
        SoundEffects.instance.play(SoundEffects.MENU);
        //System.out.println("wake");
    }

    public void onMourn(View view) {

        SoundEffects.instance.play(SoundEffects.MOURN);
        mourning = true;
        gameView.drawHUD = false;
        coolDownTime = 0;
        float dx = gameView.player.direction.x;

        gameView.lair.lieDown();
        //gameView.player.head.position.y =GameView.instance.groundLevel;

        gameView.player.direction = new Vector2(dx,-Math.abs(4*dx));
        //gameView.player.moveBy(new Vector2(dx,-Math.abs(4*dx)));

    }

    @Override
    protected void onPause() {
        super.onPause();
        GameView.instance.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView.instance.resume();
    }
}
