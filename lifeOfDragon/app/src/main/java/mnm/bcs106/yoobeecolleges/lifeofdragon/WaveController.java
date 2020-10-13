
package mnm.bcs106.yoobeecolleges.lifeofdragon;
import android.graphics.Bitmap;

public class WaveController extends GameObject {

    float time; //Time elapsed since change
    float timeOfBreak = 0;

    float waveLength =80000; //Length of time of wavea of enemies spawning
    float breakLength = 30000; //Time between waves
    float waveIntensity = 0.015f;

    boolean isBreak;

    public WaveController(Bitmap sprite, float offsetX, float offsetY) {
        super(sprite, offsetX, offsetY);
        time = 0;
        isBreak = false;
        paint.setAlpha(0);
        Game.instance.waveStart=true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!Game.instance.gameOver){
            time+=deltaTime;

            if(isBreak) {
                //Regenerate health during the break and update the health bar accordingly

                //Change the background image at the start of break and back again at the end of break
                double phaseTime = time / breakLength * Math.PI;
                timeOfBreak = (float) Math.sin(phaseTime);
                paint.setAlpha((int) Math.max(Math.min(3*timeOfBreak * 255, 255), 0));

                //If break is over start wave
                if(time > breakLength){
                    isBreak = false;
                    time = 0;
                    Game.instance.waveStart=true;
                }
            }
            else{
                //Spawn enemies if the wave is active
                //GameView.instance.spawn(waveIntensity);

                //End the wave and start the break when time is up
                if(time > waveLength){
                    time = 0;
                    isBreak = true;
                    Game.instance.waveEnd=true;

                    //Increase the probability of spawning enemies
                    waveIntensity+=0.005f;
                }
            }
        }
    }
}
