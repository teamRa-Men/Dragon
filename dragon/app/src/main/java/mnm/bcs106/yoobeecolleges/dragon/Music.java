
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
        themeMusicPlayer = MediaPlayer.create(context, R.raw.theme);
        themeMusicPlayer.setVolume(themeVolume * volume, themeVolume * volume);

        deathMusicPlayer = MediaPlayer.create(context, R.raw.death);
        deathMusicPlayer.setLooping(false);
        deathMusicPlayer.setVolume(deathVolume * volume, deathVolume * volume);

    }

    public void setVolume(float volume){
        this.volume = volume;
        deathMusicPlayer.setVolume(deathVolume * this.volume, deathVolume * this.volume);
        themeMusicPlayer.setVolume(themeVolume * this.volume, themeVolume * this.volume);
    }

    public void playThemeMusic(boolean loop){

        if(!themeMusicPlayer.isPlaying()) {
            System.out.println("theme");
            stopDeathMusic();
            themeVolume = 1;

            themeMusicPlayer.setLooping(loop);
            themeMusicPlayer.setVolume(themeVolume * volume, themeVolume * volume);

            themeMusicPlayer.start();
        }
    }

    public void stopThemeMusic(){
        if(themeMusicPlayer.isPlaying()) {
            themeMusicPlayer.pause();
            themeMusicPlayer.seekTo(0);
        }
    }

    public void playDeathMusic(){
        if(!deathMusicPlayer.isPlaying()) {
            stopThemeMusic();
            deathVolume = 1;
            deathMusicPlayer.setLooping(false);
            deathMusicPlayer.setVolume(deathVolume * volume, deathVolume * volume);
            deathMusicPlayer.start();
        }
    }



    public void stopDeathMusic(){
        if(deathMusicPlayer.isPlaying()) {
            deathMusicPlayer.pause();
            deathMusicPlayer.seekTo(0);
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
            if(deathMusicPlayer.isPlaying()){
                deathVolume = fadeTime/fadeLength;
                deathMusicPlayer.setVolume(deathVolume*volume,deathVolume*volume);
                if(fadeTime<0){
                    stopDeathMusic();
                    fadeOut = false;
                    fadeTime = 0;
                }
            }
            if(themeMusicPlayer.isPlaying()){
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
