
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundEffects{
    SoundPool soundPool;
    public static SoundEffects instance;
    public boolean coinPlaying = false;
    public MediaPlayer[] coinPlayer = new MediaPlayer[4];

    Context context;
    public static int
            SELECT = 0,
            MENU = 1,
            TRIBUTE = 2,
            FIRE = 3,
            BREATH = 4,
            DAMAGE = 5,
            SLEEP = 6,
            HURT = 7,
            BIGHURT = 8,
            ARROW=9,
            MAGIC=10,
            HIT=11,
            MOURN=12,
            DEATH=13,
            LEVELUP = 14,
            BELLS = 15,
            SPEAR = 16,
            SHEEPFLEEING = 17,
            STEAL = 18,
            WALKING = 19,
            FLYING = 20,
            FARMER_IDLING = 21,
            GOLD_ARRIVED = 22,
            DRAGONLAYER_IDLING = 23,
            SCARED_WILLAGERS = 24,
            THROW_DA_HO = 25,
            WELL_ANYWAY = 26,
            WHAT_DID_YA_SAY = 27,
            WIZARD_CHARGE = 28;


    int[] soundId;
    public static int MAXSTREAMS = 10;
    public float volumeMul = 0.5f;



    public SoundEffects(Context context){
        //Singleton

        instance = this;
        this.context = context;

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attrBuilder.setFlags(AudioAttributes.FLAG_LOW_LATENCY);

        SoundPool.Builder poolBuilder = new SoundPool.Builder();
        poolBuilder.setMaxStreams(MAXSTREAMS);
        poolBuilder.setAudioAttributes(attrBuilder.build());

        soundPool = poolBuilder.build();

        //List of available sound effects
        soundId = new int[29];
        soundId[HIT] = soundPool.load(context,R.raw.damage,1);
        soundId[MOURN] = soundPool.load(context,R.raw.mourn,1);
        soundId[ARROW] = soundPool.load(context,R.raw.arrow,1);

        soundId[TRIBUTE] = soundPool.load(context,R.raw.tribute,1);
        soundId[SLEEP] = soundPool.load(context,R.raw.snoring,1);
        soundId[DAMAGE] = soundPool.load(context,R.raw.damage,1);

        soundId[DEATH] = soundPool.load(context,R.raw.death_sound_effect,1);
        soundId[FIRE] = soundPool.load(context,R.raw.fire,1);
        soundId[BREATH] = soundPool.load(context,R.raw.breath,1);

        soundId[MAGIC] = soundPool.load(context,R.raw.magic,1);
        soundId[HURT] = soundPool.load(context,R.raw.hurt,1);
        soundId[BIGHURT] = soundPool.load(context,R.raw.bighurt,1);

        soundId[SELECT] = soundPool.load(context,R.raw.select,1);
        soundId[MENU] = soundPool.load(context,R.raw.menu,1);
        soundId[LEVELUP] = soundPool.load(context,R.raw.level,1);

        soundId[BELLS] = soundPool.load(context,R.raw.bells,1);
        soundId[SPEAR] = soundPool.load(context,R.raw.spear,1);
        soundId[STEAL] = soundPool.load(context,R.raw.steal,1);

        soundId[SHEEPFLEEING] = soundPool.load(context,R.raw.sheep_fleeing,1);
        soundId[FLYING] = soundPool.load(context,R.raw.flying,1);
        soundId[WALKING] = soundPool.load(context,R.raw.walking,1);

        soundId[GOLD_ARRIVED] = soundPool.load(context,R.raw.goldarrived,1);
        soundId[DRAGONLAYER_IDLING] = soundPool.load(context,R.raw.idledragonlayer,1);
        soundId[FARMER_IDLING] = soundPool.load(context,R.raw.idlefarmers,1);

        soundId[SCARED_WILLAGERS] = soundPool.load(context,R.raw.scaredwillagers,1);
        soundId[THROW_DA_HO] = soundPool.load(context,R.raw.throwdaho,1);
        soundId[WELL_ANYWAY] = soundPool.load(context,R.raw.wellanyway,1);

        soundId[WHAT_DID_YA_SAY] = soundPool.load(context,R.raw.whatdidyasay,1);
        soundId[WIZARD_CHARGE] = soundPool.load(context,R.raw.wizardcharge,1);
        soundId[WELL_ANYWAY] = soundPool.load(context,R.raw.wellanyway,1);

//        soundId[] = soundPool.load(context,R.raw.,1);

        for (int i = 0; i < coinPlayer.length; i++) {
            coinPlayer[i] = MediaPlayer.create(context, R.raw.coin);
            coinPlayer[i].setVolume(volumeMul / 3, volumeMul / 3);
        }
    }

    //Play with default attributes
    public int play(int effect){
        if(volumeMul > 0){
            float rate = 1;
            if(GameView.instance!= null && effect!=SELECT){

                rate *=Math.min(GameView.instance.timeScale,1);
            }
            return soundPool.play(soundId[effect],volumeMul/3,volumeMul/3,1,0,rate);
        }
        return -1;
    }

    //Play with custom attributes
    public int play(int effect,int loop, float rate){
        if(volumeMul > 0){
            if(GameView.instance!= null && effect!=SELECT){
                rate *=Math.min(GameView.instance.timeScale,1);
            }
            return soundPool.play(soundId[effect],volumeMul/3,volumeMul/3,1,loop,rate);
        }
        return -1;
    }
    public void stop(int id){
        soundPool.stop(id);
    }

    public void pauseAll(){
        soundPool.autoPause();
    }

    public void release(){
        soundPool.release();
    }

    public void setVolume(int id,float volume){
        soundPool.setVolume(id,volume*volumeMul,volume*volumeMul);
    }

    public void playCoin(){
        for(int i = 0; i < coinPlayer.length;i++) {
            if(!coinPlayer[i].isPlaying()){
                coinPlayer[i].setVolume(volumeMul / 3, volumeMul / 3);
                coinPlayer[i].start();
                return;
            }
        }
    }

}
