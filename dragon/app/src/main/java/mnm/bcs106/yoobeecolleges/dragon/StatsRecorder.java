package mnm.bcs106.yoobeecolleges.dragon;

public class StatsRecorder {
    int level,days,maxGold,houses,towers, kingdoms, farms, farmers, thieves, dragonSlayers, wizards, wooloo;
    long startTime, finishTime, playTime;
    boolean finalKingdom = false;
    public static StatsRecorder instance;

    public StatsRecorder(){
        instance = this;
    }
    public void init(){
        level=0;
        days =0;
        maxGold = 0;
        houses =0;
        towers=0;
        kingdoms=0;
        farms=0;
        farmers=0;
        thieves = 0;
        dragonSlayers = 0;
        wizards = 0;
        wooloo = 0;
        finishTime=0;
        playTime=0;
        setStartTime();
    }
    public void saveStats(){

    }
    public void getStats(){

    }
    public void setStartTime(){
        startTime = System.currentTimeMillis();
    }

    public void buildingDestroyed(int type){
        switch (type){
            case 1: kingdoms++; break;
            case 2: houses++; break;
            case 3: farms++; break;
            case 4: towers++; break;
        }
    }
    public void npcDestroyed(int type){
        switch (type){
            case 1: wooloo++; break;
            case 2: farmers++; break;
            case 3: dragonSlayers++; break;
            case 4: thieves++; break;
            case 5: wizards++; break;
        }
    }
    public void finalKingdomDestroyed(){
        finalKingdom = true;
        gameEnd();
    }


    public void gameEnd(){
        level = (int)GameView.instance.lair.level;
        days = Scene.instance.day;
        finishTime = System.currentTimeMillis();
        playTime = finishTime-startTime;
        saveStats();
    }

    public void setMaxGold(int gold){
        if(gold > maxGold){
            maxGold = gold;
        }
    }

}
