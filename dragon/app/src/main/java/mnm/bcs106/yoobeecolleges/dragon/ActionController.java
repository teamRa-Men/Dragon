

package mnm.bcs106.yoobeecolleges.dragon;
public class ActionController {
    public float chargeTime, performTime, coolDownTime;
    int state;
    public boolean ready = true, charging = false, performing = false, cooling = false;
    public static int READY = 0, CHARGING = 1, PERFORMING = 2, COOLING = 3;
    float time;
    public ActionController(float chargeTime, float performTime, float coolDownTime){
        this.coolDownTime = coolDownTime;
        this.performTime = performTime;
        this.chargeTime = chargeTime;
        state = READY;

    }

    public int update(float deltaTime){
        time += deltaTime;

        if(state == CHARGING){
            if(time > chargeTime){
                charging = false;
                performing = true;
                state = PERFORMING;
                time = 0;
            }
        }
        else if(state == PERFORMING){
            if(time > performTime){
                performing = false;
                cooling = true;
                state = COOLING;
                time = 0;
            }
        }
        else if(state == COOLING){
            if(time > coolDownTime){
                cooling = false;
                ready = true;
                state = READY;
                time = 0;
            }
        }
        return state;
    }

    public boolean triggerAction(){
        if(state == READY){
            ready = false;
            charging = true;
            state = CHARGING;
            time = 0;
            return true;
        }
        return false;
    }
}
