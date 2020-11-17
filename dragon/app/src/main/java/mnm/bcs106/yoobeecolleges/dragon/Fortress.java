package mnm.bcs106.yoobeecolleges.dragon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Fortress extends Foundation {


    //All the stuff the fortress needs and can do;

    int lv;



    int currentGold;
    int maxGold;
    public int goldRate = 15;

    int currentTilesLeft;
    int currentTilesRight;
    int maxTiles = 16;

    public Point creationPoint = new Point();
    float attackRange = (1f/2);

    float countdown = 0;
    int attack = 0;

    ArrayList<Foundation> currentBuildingsRight = new ArrayList<Foundation>();
    ArrayList<Foundation> currentBuildingsLeft = new ArrayList<Foundation>();
    int maxBuildings;

    //number of buildings in the village/town

    public int currentTownInhabitants;
    public int maxTownInhabitants; // defined by the houses/farms

    //public Foundation[] buildings = new Foundation[5];

    int towerLeft, towerRight;
    boolean hasTaxed = false;
    boolean hasTribute = false;
    boolean hasFarm = false;
    // attack function
    public static int tileNr = 4;

    float townFear;

    ArrayList<String> BD = new ArrayList<>();
    boolean muchFarms = true;
    boolean muchFear = false;
    int tower = 0;

    boolean summonedWizard = false;
    boolean surrender = false;
    float surrenderFear;

    Flag flag;
    int flagy;
    Bitmap flagpole;

    Bitmap background;
    //Fortress constructor, used when calling Fortress();

    //this specific Fortress
    public Fortress(int x, int y) {
        super(x, y, tileNr, null);
        buildingType = 1;
        buildingImage = SpriteManager.instance.getBuildingSprite("Fortress1");
        background = SpriteManager.instance.getBuildingSprite("Background");
        height = width * buildingImage.getHeight() / buildingImage.getWidth();
        collider = new Rect(x,y-height,x+width,y);
        flag = new Flag();
        flagpole = BitmapFactory.decodeResource(GameView.instance.getResources(), R.drawable.flagpole);
        flagpole = Bitmap.createScaledBitmap(flagpole, tilesize, tilesize*2, false);

        BD.add("House");
        BD.add("House");
        BD.add("House");
        BD.add("Farm");
        BD.add("Farm");
        BD.add("Farm");
        BD.add("Farm");


        Random r = new Random();
        x = r.nextInt();
        buildingType = 1;
        currentGold = 150;

        maxBuildings = 5;
        maxGold = 400;
        lv = 0;
        maxHealth = 1000;
        surrenderFear = 100;

        health = maxHealth;

        Farmers newFarmer = GameView.instance.npc_pool.spawnFarmers(x, (int) GameView.instance.groundLevel, this);
    }

    //new test with arraylists works pretty much, tiles and buildings still individual from each other

    public void update(float deltaTime) {
        if (creationPoint.y != (int)(GameView.instance.groundLevel - height*3/4) || creationPoint.x != x+width/2){
            creationPoint.x = x+width/2-width/4;
            creationPoint.y = (int)(GameView.instance.groundLevel - height/2)+height/8;
        }


        if(isStanding) {

        /*System.out.println(creationPoint.x);
        System.out.println(GameView.instance.player.position.x);
        System.out.println(GameView.instance.player.position.x-creationPoint.x);
        System.out.println(GameView.instance.cameraSize*attackRange);*/

          tax();


            //=======================================================================================//

            //Buildings

            //=======================================================================================//


            grow();
/*
            if ((((currentBuildingsRight.size() + currentBuildingsLeft.size()) >= maxBuildings) || (currentTilesLeft + currentTilesRight >= 8))
                    && (currentGold >= (maxGold / 10 * 9))
                    && lv == 0) {

                lv++;
                maxGold = maxGold * 4 + 300;
                maxBuildings = 12;

                this.buildingImage = SpriteManager.instance.getBuildingSprite("Fortress2");


                currentBuildingsRight.add(new ArcherTower(x + (tilesize * tileNr) + (currentTilesRight) * tilesize, y, true, this));
                currentTilesRight += 1;

                currentBuildingsLeft.add(new ArcherTower(x - (currentTilesLeft) * tilesize - ArcherTower.tileNr * tilesize, y, true, this));
                currentTilesLeft += 1;
            }

            if ((((currentBuildingsRight.size() + currentBuildingsLeft.size()) >= maxBuildings) || (currentTilesLeft + currentTilesRight >= 8))
                    && (currentGold >= (maxGold / 10 * 9))
                    && lv == 1) {

                lv++;
                maxGold = maxGold * 4 + 600;
                maxBuildings = 18;

                this.buildingImage = SpriteManager.instance.getBuildingSprite("Fortress3");


                currentBuildingsRight.add(new ArcherTower(x + (tilesize * tileNr) + (currentTilesRight) * tilesize, y, true, this));
                currentTilesRight += 1;

                currentBuildingsLeft.add(new ArcherTower(x - (currentTilesLeft) * tilesize - ArcherTower.tileNr * tilesize, y, true, this));
                currentTilesLeft += 1;
            }*/

            //    =   ========  ==   ==
            //   = =     ==     ==  ==
            //  =====    ==     ====
            // =     =   ==     ==  ===

            if (inRange() && !surrender) {
                countdown+=GameView.instance.fixedDeltaTime;
                //System.out.println(countdown);
                float shootSpeed=4-lv;
                if (countdown > 1000*shootSpeed) {

                    if (countdown > 1200*shootSpeed && attack == 0) {
                        Attack();

                        attack += 1;
                    }

                    if (countdown > 1400*shootSpeed && attack == 1) {
                        Attack();

                        attack += 1;
                    }

                    if (countdown > 1600*shootSpeed && attack == 2) {
                        Attack();

                        attack += 1;
                    }

                    if (countdown >= 1800*shootSpeed) {
                        countdown = 0;
                        attack = 0;
                    }
                }
            }

            //spawning thief
            if((townFear > 20 && lv != 0 && (currentGold < maxGold/2)) || (goldRate < 200 && lv != 0)&&Scene.instance.day>2 && Scene.instance.timeOfDay/ Scene.instance.dayLength>0.5
            &&Scene.instance.timeOfDay/ Scene.instance.dayLength<0.6){
                GameView.instance.npc_pool.spawnThiefs(x, (int) GameView.instance.groundLevel,1, this);
            }

            //spawning dragonslayer
            if(townFear > 30 && lv != 0){
                GameView.instance.npc_pool.spawnDragonLayers(x, (int) GameView.instance.groundLevel, this);
            }

            //spawning wizard
            if(townFear > 35 && lv ==2 && !summonedWizard){
                GameView.instance.npc_pool.spawnFarmers(x, (int) GameView.instance.groundLevel, this);
                summonedWizard = true;
            }

            if(!surrender) {
                if (townFear > surrenderFear) {
                    surrender = true;
                    flag.setSurrender(surrender);
                }
            }
            else {
                if(townFear < surrenderFear/2) {
                    surrender = false;
                    flag.setSurrender(surrender);

                }
            }



            Flagposition(deltaTime);
        }
        else {
            buildingImage = SpriteManager.instance.getBuildingSprite("FortressRuin");

            if(beenEmptied == false){
                GoldPool.instance.spawnGold(collider.centerX(), collider.centerY(),Math.min(currentGold,100*(lv+1)) );
                beenEmptied = true;
            }
            townFear = 0;
        }

        //====    =====  =====     =     ==  ====    ============================
        //=   =   ==     =    =   = =    ==  =   =   ============================
        //====    ==     =====   =====   ==  ====    ============================
        //=   ==  =====  =      =     =  ==  =   ==  ============================
        repair(deltaTime);

        for(int i = 0; i < currentBuildingsLeft.size(); i++){
            currentBuildingsLeft.get(i).update(deltaTime);
        }

        for(int i = 0; i < currentBuildingsRight.size(); i++){
            currentBuildingsRight.get(i).update(deltaTime);
        }
        super.update(deltaTime);

    }


    public void spawnRandomBuilding(){

        double lr = (Math.random() - 0.5f);
        int offset = 0;//tilesize/2;

        ArrayList<Foundation> direction;
        int directionTiles;

        if(lr < 0){
            direction = currentBuildingsLeft;
        }
        else{
            direction = currentBuildingsRight;
        }

        int position;
        if(direction ==  currentBuildingsLeft){
            position = x-(currentTilesLeft*tilesize+offset);
        }
        else position = x + (tilesize*Fortress.tileNr) +currentTilesRight*tilesize + offset;

        //////////////////////////////////////////////////////////////////
        //deciding Building
        //////////////////////////////////////////////////////////////////


        Foundation building;
        int frm = 0;

        for(int i = 0; i < currentBuildingsRight.size(); i++){
            if(currentBuildingsRight.get(i).buildingType == 3)
                frm++;
        }

        for(int i = 0; i < currentBuildingsLeft.size(); i++){
            if(currentBuildingsLeft.get(i).buildingType == 3)
                frm++;
        }

        if(goldRate < 100 && !muchFarms && frm < 5){BD.add("Farm");BD.add("Farm");BD.add("Farm"); muchFarms = true;}
        else if(goldRate >= 100 && muchFarms){BD.remove("Farm");BD.remove("Farm"); BD.remove("Farm"); muchFarms = false;}

        if(townFear >= 12 && !muchFear && lv>0){BD.add("Tower");BD.add("Tower");BD.add("Tower");BD.add("Tower"); muchFear = true;}
        else if(townFear < 12 && muchFear){BD.remove("Tower");BD.remove("Tower");BD.remove("Tower");BD.remove("Tower"); muchFear = false;}

        if(currentBuildingsRight.size() == 0 && currentBuildingsLeft.size() == 0){
            if(direction == currentBuildingsLeft) {
                position -= Farm.tileNr*tilesize;
            }
            building = new Farm(position,y,this);
            currentGold-=Farm.cost*((lv*1.50)+1);
        }

        else{
            int random = (int)(-0.5+(Math.random()*BD.size()));

            if(BD.get(random) == "House" && currentGold > House.cost*(int)((lv*1.75)+1)){
                if(direction == currentBuildingsLeft){
                    position-=House.tileNr*tilesize;
                }
                building = new House(position,y,this);currentGold-=(int)(House.cost*((lv*1.75)+1));}

            else if(BD.get(random) == "Farm" && currentGold > Farm.cost*(int)((lv*1.75)+1)){
                if(direction == currentBuildingsLeft){
                    position-=Farm.tileNr*tilesize;
                }
                building = new Farm(position,y,this);currentGold-=(int)(Farm.cost*((lv*1.75)+1));}

            else if(BD.get(random) == "Tower" && currentGold > ArcherTower.cost*(int)((lv*1.75)+1)){
                if(direction == currentBuildingsLeft){
                    position-=ArcherTower.tileNr*tilesize;
                }
                building = new ArcherTower(position,y,true, this); currentGold-=(int)(ArcherTower.cost*((lv*1.75)+1));}
            else return;
        }

        ///_______________________________________________///
        //giving feedback to the Tiles right and Tiles left
        ///===============================================///
        if(direction == currentBuildingsLeft) {
            currentTilesLeft += building.tileNr;
        }
        else {
            currentTilesRight += building.tileNr;
        }
        direction.add(building);
    }

    public void levelUp() {

        if (lv == 0) {
            lv++;
            maxGold = maxGold * 4 + 300;
            maxBuildings = 12;

            this.buildingImage = SpriteManager.instance.getBuildingSprite("Fortress2");


            currentBuildingsRight.add(new ArcherTower(x + (tilesize * tileNr) + (currentTilesRight) * tilesize, y, true, this));
            currentTilesRight += 1;
            towerRight = x + (tilesize * tileNr) + (currentTilesRight) * tilesize;

            currentBuildingsLeft.add(new ArcherTower(x - (currentTilesLeft) * tilesize - ArcherTower.tileNr * tilesize, y, true, this));
            currentTilesLeft += 1;
            towerLeft = x - (currentTilesLeft) * tilesize - ArcherTower.tileNr * tilesize;

        } else {

            lv++;
            maxGold = maxGold * 4 + 600;
            maxBuildings = 18;

            this.buildingImage = SpriteManager.instance.getBuildingSprite("Fortress3");


            currentBuildingsRight.add(new ArcherTower(x + (tilesize * tileNr) + (currentTilesRight) * tilesize, y, true, this));
            currentTilesRight += 1;
            towerRight = x + (tilesize * tileNr) + (currentTilesRight) * tilesize;

            currentBuildingsLeft.add(new ArcherTower(x - (currentTilesLeft) * tilesize - ArcherTower.tileNr * tilesize, y, true, this));
            currentTilesLeft += 1;
            towerLeft = x - (currentTilesLeft) * tilesize - ArcherTower.tileNr * tilesize;
        }

        maxHealth*=1.25f;
        for(int i = 0; i < currentBuildingsRight.size(); i++){
            currentBuildingsRight.get(i).maxHealth*=1.25f;
        }

        for(int i = 0; i < currentBuildingsLeft.size(); i++){
            currentBuildingsLeft.get(i).maxHealth*=1.25f;
        }

    }

    public void grow(){
//System.out.println((int)(House.cost*((lv*1.75)+1)));

        if (((currentBuildingsRight.size() + currentBuildingsLeft.size()) < maxBuildings)) {
            spawnRandomBuilding();
        }

        tower = 0;
        for (int i = 0; i < currentBuildingsRight.size(); i++) {
            if (currentBuildingsRight.get(i).buildingType == 4)
                tower++;
        }

        for (int i = 0; i < currentBuildingsLeft.size(); i++) {
            if (currentBuildingsLeft.get(i).buildingType == 4)
                tower++;
        }

        //  ==       =====   ====        ==           =====
        //  ==        ==      =          ==           =============
        //  ==         ==    =      =============         ==================
        //  ==          ==  =            ==           ===============
        //  ========     ===             ==           ===
        if ((((currentBuildingsRight.size() + currentBuildingsLeft.size()) >= maxBuildings) || (currentTilesLeft + currentTilesRight >= 8))
                && (currentGold >= (maxGold / 10 * 9))){
            levelUp();
        }
    }
    public void tax(){
        int currentGold1 = currentGold;
        if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) < 0.2) {
            if (currentGold < maxGold && (!hasTaxed)) {

                // MONEY INCOME
                goldRate = 15;

                for (int i = 0; i < currentBuildingsRight.size(); i++) {
                    goldRate = currentBuildingsRight.get(i).goldRate + goldRate;
                }

                for (int i = 0; i < currentBuildingsLeft.size(); i++) {
                    goldRate = currentBuildingsLeft.get(i).goldRate + goldRate;
                }


                currentGold = currentGold + (int) (goldRate * 1.2 * GameView.instance.timeScale);

                if (currentGold > maxGold) {
                    currentGold = maxGold;
                }

                for (int i = 0; i < BD.size(); i++) {
                    //System.out.print(BD.get(i) + ", ");
                }
                System.out.println("Town's Fear :" + townFear);
                hasTaxed = true;
                //TODO tribute
            }
            if (currentGold != currentGold1) {
               //System.out.println("Goldrate : " + goldRate);
                System.out.println("Gold : " + currentGold);
            }
            if (surrender && !hasTribute) {
                //System.out.println("TRIBUTE");
                GameView.instance.npc_pool.spawnTribute(x, y, Math.min(currentGold,100*(lv+1)) / 4, this);
                hasTribute = true;
            }

        }
        if ((Scene.instance.timeOfDay) / (Scene.instance.dayLength) > 0.7) {
            hasTaxed = false;
            hasTribute = false;
        }
    }
    public void repair(float deltaTime){
        float tempfear;
        int standingBuildings = 1;
        if(health < maxHealth){
            repair((currentTownInhabitants/5) + 1, deltaTime);
            //System.out.println("repair fort" + health);
        }
        if (!isStanding) {
            repair(currentTownInhabitants + 1, deltaTime);
            //System.out.println("repair fort is sta" + health);
            if(health > maxHealth/2){
                isStanding = true;
                if(lv == 0) {
                    buildingImage = SpriteManager.instance.getBuildingSprite("Fortress1");
                }else if(lv == 1){
                    buildingImage = SpriteManager.instance.getBuildingSprite("Fortress2");
                }
                else{
                    buildingImage = SpriteManager.instance.getBuildingSprite("Fortress3");
                }
            }
        }

        else {
            boolean repairingRight = false, repairingLeft = false;
            tempfear = 0;

            //RIGHT SIDE
            for (int i = 0; i < currentBuildingsRight.size(); i++) {
                currentBuildingsRight.get(i).update(deltaTime);
                if(currentBuildingsRight.get(i).isStanding){
                    standingBuildings++;
                }
                //repairing
                if (!repairingRight && !currentBuildingsRight.get(i).isStanding) {

                    if (currentBuildingsRight.get(i).buildingType == 2) {

                        if ((townFear - 4) / 10 > tower / 2 && lv > 0) {
                            System.out.println("converting house -> tower R");
                            int bx = currentBuildingsRight.get(i).x;
                            currentBuildingsRight.set(i, new ArcherTower(bx, y, true,this));
                            currentBuildingsRight.set(i, new ArcherTower(bx, y, false, this));
                            currentBuildingsRight.get(i).health = 0;
                        }
                    }

                    else if(currentBuildingsRight.get(i).buildingType == 4){

                        if (townFear/10 < 2*lv && tower > 6) {
                            System.out.println("converting tower -> house R");
                            int bx = currentBuildingsRight.get(i).x;
                            currentBuildingsRight.set(i, new ArcherTower(bx,y,true,this));
                        }
                    }

                    currentBuildingsRight.get(i).repair((currentTownInhabitants / 5) + 1, deltaTime);
                    repairingRight = true;

                }

                //gathering fear
                tempfear += currentBuildingsRight.get(i).fear;
            }

            //LEFT SIDE
            for (int i = 0; i < currentBuildingsLeft.size(); i++) {
                currentBuildingsLeft.get(i).update(deltaTime);
                if(currentBuildingsLeft.get(i).isStanding){
                    standingBuildings++;
                }
                //repairing
                if (!repairingLeft && !currentBuildingsLeft.get(i).isStanding) {


                    if (currentBuildingsLeft.get(i).buildingType == 2) {
                        System.out.println("this is a house L");
                        if ((townFear - 4) / 10 > tower / 2 && lv > 0) {
                            System.out.println("converting house -> tower L");
                            int bx = currentBuildingsLeft.get(i).x;
                            currentBuildingsLeft.set(i, new ArcherTower(bx, y, false, this));
                            currentBuildingsLeft.get(i).health = 0;
                        }
                    }

                    else if(currentBuildingsLeft.get(i).buildingType == 4){

                        if (townFear/10 < 2*lv && tower > 6) {
                            System.out.println("converting tower -> house L");
                            int bx = currentBuildingsLeft.get(i).x;
                            currentBuildingsLeft.set(i, new ArcherTower(bx,y,true, this));
                        }
                    }
                }
                currentBuildingsLeft.get(i).repair((currentTownInhabitants / 5) + 1, deltaTime);
                repairingLeft = true;

                tempfear += currentBuildingsLeft.get(i).fear;
            }
            tempfear += fear;
            //gathering fear
            townFear = (tempfear / standingBuildings);// (currentBuildingsLeft.size() + currentBuildingsRight.size()));
        }
    }

    public void Flagposition(float deltaTime){

        countdown+=deltaTime;
        int flagf = 0;

        flagy = (int)(GameView.instance.groundLevel-tilesize*2*(Math.min((townFear /surrenderFear+2)/3,1)));

        flag.y = flagy;

    }

    @Override
    public void draw(Canvas c) {

        for(int i = 0; i < (towerRight-towerLeft)/tilesize-2;i++){
            c.drawBitmap(background, towerLeft+tilesize*1.5f+i*tilesize +GameView.instance.cameraDisp.x,(int)(GameView.instance.groundLevel-background.getHeight()),null);
        }


        for (int i = 0; i < currentBuildingsLeft.size(); i++) {

            if (currentBuildingsLeft.get(i) != null) {

                currentBuildingsLeft.get(i).draw(c);

            }
        }

        for (int i = 0; i < currentBuildingsRight.size(); i++) {

            if (currentBuildingsRight.get(i) != null) {

                currentBuildingsRight.get(i).draw(c);

            }
        }


        c.drawBitmap(flagpole, x+GameView.instance.cameraDisp.x+tilesize*3,(int)(GameView.instance.groundLevel-flagpole.getHeight()),null);
        flag.x = x+tilesize*3;
        flag.draw(c);

        super.draw(c);
    }



    /*public void CombList(){

        ArrayList <Foundation> townassets = new ArrayList<>();

        for(int i = 0; i < currentBuildingsRight.size(); i++){
            townassets.add(currentBuildingsRight.get(i));
        }

        for(int i = 0; i < currentBuildingsLeft.size(); i++){
            townassets.add(currentBuildingsLeft.get(i));
        }
      }*/

    public void physics(float deltaTime){

        super.physics(deltaTime);

        for(int i = 0; i < currentBuildingsLeft.size(); i++){
            currentBuildingsLeft.get(i).physics(deltaTime);
        }

        for(int i = 0; i < currentBuildingsRight.size(); i++){
            currentBuildingsRight.get(i).physics(deltaTime);
        }
    }

    // calculates if the dragon is in range
    public boolean inRange(){
        //System.out.println("inRange");
        return Math.abs(GameView.instance.player.position.x - creationPoint.x) < GameView.instance.cameraSize * attackRange;
    }

    //shooting an arrow at target
    public void Attack(){
        float randomx = (float)(Math.random()-0.5)*attackRange*GameView.instance.cameraSize/10;
        float randomy = (float)(Math.random()-0.5)*attackRange*GameView.instance.cameraSize/5;
        Vector2 target = GameView.instance.player.aimFor();
        float dx = target.x-creationPoint.x;
        float dy =target.y-creationPoint.y;
        float l= (float)Math.sqrt(dx*dx+dy*dy);
        dx = dx/l-((float)Math.random()-0.5f)/2;
        dy = dy/l-(float)(Math.random())/10;
        ProjectilePool.instance.shootArrow(creationPoint.x, creationPoint.y, 1, dx, dy, 5);
    }
}





//TODO:
//      Conditional Building output :
//      Fortress size : 4 tiles: done
//      Fortress Attacking :

