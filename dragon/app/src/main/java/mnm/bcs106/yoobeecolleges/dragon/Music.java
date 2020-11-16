
package mnm.bcs106.yoobeecolleges.dragon;
import android.content.Context;
import android.media.MediaPlayer;

//Handles the music in the shapes game
public class Music{
    public MediaPlayer themeMusicPlayer, deathMusicPlayer;
    public boolean playingTheme = false, playingDeath = false;
    public float themeVolume=1, deathVolume=1, volume = 0.5f;
    public boolean fadeOut = false;
    float fadeTime = 0, fadeLength = 3000;
    public static Music instance;
    Context context;

    public Music(Context context){
        //Set up media player if there is no instance of it
        instance = this;
        this.context = context;


    }

    public void setVolume(float volume){
        volume = Math.min(Math.max(volume,0),1);
        System.out.println(volume);
        if(deathMusicPlayer!=null) {
            deathMusicPlayer.setVolume(deathVolume * volume, deathVolume * volume);
        }
        if(themeMusicPlayer!=null) {
            themeMusicPlayer.setVolume(themeVolume * volume, themeVolume * volume);
        }
    }

    public void playThemeMusic(){
        if(!playingTheme) {
            stopDeathMusic();
            System.out.println("THEMEMUSIC");

            try {

                themeVolume = 1;
                themeMusicPlayer = MediaPlayer.create(context, R.raw.theme);
                themeMusicPlayer.setLooping(true);
                themeMusicPlayer.setVolume(themeVolume * volume, themeVolume * volume);
                playingTheme = true;
                themeMusicPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void stopThemeMusic(){
        playingTheme = false;
        if(themeMusicPlayer != null) {

            themeMusicPlayer.release();

        }
    }

    public void playDeathMusic(){
       if(!playingDeath) {
           System.out.println("DEATHMUSIC");

           stopThemeMusic();

           deathVolume = 1;


           try {
               deathMusicPlayer = MediaPlayer.create(context, R.raw.death);
               deathMusicPlayer.setLooping(false);
               deathMusicPlayer.setVolume(deathVolume * volume, deathVolume * volume);
               playingDeath = true;
               deathMusicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                   @Override
                   public void onCompletion(MediaPlayer mp) {
                       stopDeathMusic();
                   }
               });
               deathMusicPlayer.start();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }


    }



    public void stopDeathMusic(){
        playingDeath = false;
        if(deathMusicPlayer != null) {

            deathMusicPlayer.release();

        }
    }
    public void startFadeOut(float fadeLength){
        this.fadeLength = fadeLength;
        fadeTime = fadeLength;
        fadeOut = true;

    }

    public void update(float deltaTime){
        if(fadeOut){
            fadeTime-=deltaTime;
            if(playingDeath && deathMusicPlayer!=null){
                deathVolume = fadeTime/fadeLength;
                deathMusicPlayer.setVolume(deathVolume*volume,deathVolume*volume);
                if(fadeTime<0){
                    stopDeathMusic();
                    fadeOut = false;
                    fadeTime = 0;
                }
            }
            if(playingTheme && themeMusicPlayer!=null){
                themeVolume =fadeTime/fadeLength;
                themeMusicPlayer.setVolume(themeVolume*volume,themeVolume*volume);
                if(fadeTime<0){
                    stopThemeMusic();
                    fadeOut = false;
                    fadeTime = 0;
                }
            }
        }
    }
}
