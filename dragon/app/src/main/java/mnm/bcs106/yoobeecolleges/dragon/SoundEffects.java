
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundEffects{
    SoundPool soundPool;
    public static SoundEffects instance;

    Context context;
    public static int
            SELECT = 0,
            MENU = 1,
            COIN = 2,
            FIRE = 3,
            BREATH = 4,
            DAMAGE = 5,
            SLEEP = 6,
            HURT = 7,
            BIGHURT = 8,
            ARROW=9,
            MAGIC=10,
            HIT=11,
            EXPLOSION=12,
            DEATH=13,
            LEVELUP = 14,
            BELLS = 15,
            SPEAR = 16,
            SHEEPFLEEING = 17,
            STEAL = 18,
    WALKING = 19, FLYING = 20;


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
        soundId = new int[21];
        soundId[HIT] = soundPool.load(context,R.raw.damage,1);
        soundId[EXPLOSION] = soundPool.load(context,R.raw.explosion,1);
        soundId[ARROW] = soundPool.load(context,R.raw.arrow,1);

        soundId[COIN] = soundPool.load(context,R.raw.coin,1);
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
        soundId[LEVELUP] = soundPool.load(context,R.raw.levelup,1);

        soundId[BELLS] = soundPool.load(context,R.raw.bells,1);
        soundId[SPEAR] = soundPool.load(context,R.raw.spear,1);
        soundId[STEAL] = soundPool.load(context,R.raw.steal,1);

        soundId[SHEEPFLEEING] = soundPool.load(context,R.raw.sheep_fleeing,1);
        soundId[FLYING] = soundPool.load(context,R.raw.flying,1);
        soundId[WALKING] = soundPool.load(context,R.raw.walking,1);

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

}
