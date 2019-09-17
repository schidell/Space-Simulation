import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.List;

/**
 * World that the battle will take place in. In the SpaceWorld, aliens and humans fight to conquer 5 target planets. 
 * There are 2 types of fighters, attackers and invaders. The attackers aim to eliminate members of the opposing team,
 * while the invaders aim to conquer the planets. 
 * <p> There are also 2 healing planets that revive the attackers if their health is low. 
 * The health planets also lose health when reviving the attackers, so when the healing planets are recharging, 
 * attackers cannot heal there. 
 * <p>The Space World also randomly spawns multiple black holes and comets at once. When all the planets get conquered, a 
 * barrage of comets will appear, followed by a barrage of black holes to show the futility of winning.
 * <p>The user can choose to spawn comets and black holes. 
 * There are also buttons that change the frequency the weapons are spawned at and the spawn rates of the fighters.
 * <p>
 * <h3>CREDITS
 * <div>
 *  CODE <br>
 *      Explosion class:    Jordan Cohen <br>
 *      TextButton class:   Jordan Cohen
 *      
 * <p>IMAGES <br>
 *      comet.png           https://www.themindmuseum.org/support-science/virtual-exhibits/catch-a-comet <br>
 *      planet sprites      http://freegameassets.blogspot.ca/2013/09/asteroids-and-planets-if-you-needed-to.html <br>
 *      attacker            http://www.freepngimg.com/png/24752-spaceship <br>
 *      invader             https://ru.pngtree.com/freepng/space-ship_2303742.html <br>
 *      laser.png           https://upload.wikimedia.org/wikipedia/commons/e/eb/Green_laser.png
 *      
 *  <p>SOUND
 *  <br>
 *      battleofheroes.mp3  https://www.youtube.com/watch?v=P1k5zo0w6N8 <br>
 *      explosion2.mp3      https://www.youtube.com/watch?v=mJAX16YVQ3U 
 *  
 * @author Maggie Lin
 * @version April 6 2018
 */
public class SpaceWorld extends World
{
    //spawn rates
    private int alienSpawn = 50;
    private int humanSpawn = 50;

    private int cometSpawn = 4000;  //frequency of comets appearing
    private int blackHoleSpawn = 4000;  //frequency of black holes appearing

    //variables for music
    private TextButton musicButton;
    GreenfootSound backgroundMusic = new GreenfootSound("battleofheroes.mp3");  //sets background music
    private boolean playing;

    //instance objects in class regarding planets
    private TargetPlanet[] targetPlanets = new TargetPlanet[5];
    private HealPlanet[] healPlanets = new HealPlanet[2];
    private Text[] planetLabels = new Text[5];

    private Scorebar displayBar;    //score bar

    //variables and objects for buttons and controls
    private TextButton cometButton;
    private TextButton holeButton;

    //0 is aliens; 1 is humans
    private Text[] teamLabels = new Text[2];
    private Text[] weaponLabels = new Text[2];
    private Text[] spawnLabels = new Text[2];

    private TextButton[] upWeapons = new TextButton[2];
    private TextButton[] downWeapons = new TextButton[2];

    private TextButton[] upSpawnRate = new TextButton[2];
    private TextButton[] downSpawnRate = new TextButton[2];

    //variable used to delay spawning when all planets are conquered by the same species
    private int conquerDelay = 50;
    private boolean allConquered = false;

    /**
     * Constructor for objects of class SpaceWorld. This adds buttons, text, the score bar and planets to the world.
     */
    public SpaceWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(960, 640, 1, false); 

        // add planets ----------------------------------------------------------------------------------------------------------------------------------

        //add target planets to world
        for(int i = 0; i < targetPlanets.length; i++)
        {
            targetPlanets[i] = new TargetPlanet(-10 * Math.abs(i-2) + 80, 'g');   //neutral planet
            //addObject(targetPlanets[i], getWidth()/2, (i+1)*100+20+30);     //set location
            addObject(targetPlanets[i], Greenfoot.getRandomNumber(100)+430, (i+1)*100+50);

            //numbers each planet
            planetLabels[i] = new Text(""+(i+1),40);        
            addObject(planetLabels[i], targetPlanets[i].getX(), targetPlanets[i].getY()); //add text to world
        }

        //add healing planets to the world
        healPlanets[0] = new HealPlanet(60);
        healPlanets[1] = new HealPlanet(60);

        for(int i = 0; i < healPlanets.length; i++) //add the two healing planets
        {
            addObject(healPlanets[i], i*560+200, Greenfoot.getRandomNumber(240)+200);
        }

        // add buttons that directly influence world ----------------------------------------------------------------------------------------------------------------------------------

        //add blackhole button
        holeButton = new TextButton("  BLACK HOLE  ", 24);
        addObject(holeButton, 720, 100);
        //add comet button
        cometButton = new TextButton("     COMET     ", 24);
        addObject(cometButton, 870,100);

        //add display bar
        displayBar = new Scorebar();
        addObject(displayBar, 480, 30);

        //ensures scorebar and button are always at top
        setPaintOrder(Scorebar.class, TextButton.class, Text.class);

        //adds music button to world
        musicButton = new TextButton(" PLAY/PAUSE MUSIC ", 24);
        addObject(musicButton, 120, 100);

        backgroundMusic.setVolume(50);  //set volume
        playing = false;    //at complile time, music shouldn't play

        /** add buttons that control variables --------------------------------------------------------------------------------------------------------------*/
        teamLabels[0] = new Text("ALIENS", 24, new Color(234,123,231), new Color(0,0,0,0));
        teamLabels[1] = new Text("HUMANS", 24, new Color(123,234,231), new Color(0,0,0,0));

        //adds labels to world
        for(int i = 0; i < teamLabels.length; i++)
        {
            addObject(teamLabels[i], i*850+50, 550);    //add HUMAN and ALIEN labels

            weaponLabels[i] = new Text("WEAPON FREQUENCY", 20); //add weapon frequency labels
            addObject(weaponLabels[i], 690*i+100,580);  

            spawnLabels[i] = new Text("SPAWN RATE", 20);    //add spawn rate labels
            addObject(spawnLabels[i], 690*i+132,610);
        }

        //adds buttons to world
        for(int i = 0; i < teamLabels.length; i++)
        {
            upWeapons[i] = new TextButton(" ↑ ",20);    //up arrows for weapons
            upSpawnRate[i] = new TextButton(" ↑ ",20);  //up arrow for spawn rates

            downWeapons[i] = new TextButton(" ↓ ",20);  //down arrows for weapons
            downSpawnRate[i] = new TextButton(" ↓ ",20);    //down arrow for spawn rates
            //add buttons
            addObject(upWeapons[i], 690*i+235, 580);
            addObject(downWeapons[i], 690*i+205, 580);

            addObject(upSpawnRate[i], 690*i+235, 610);
            addObject(downSpawnRate[i], 690*i+205, 610);
        }
    }

    /**
     * Plays and pauses music.
     */
    private void changeMusicState()
    {
        if(playing) //if playing, play music
            backgroundMusic.playLoop();
        else        //else, stop music
            backgroundMusic.pause();
    }

    /**
     * Act - do whatever the TargetPlanet wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if(!allConquered)       //if not all conquered
            spawnFighters();    //spawn fighters
        spawnComets();          //spawn comets in barrages
        spawnBlackHoles();      //spawn black holes in barrages

        if(Greenfoot.mouseClicked(cometButton)) //if comet button is clicked, spawn comet
            cometAppears();
        if(Greenfoot.mouseClicked(holeButton))  //if black hole button is clicked, spawn black hole
            blackHoleAppears();
        if(Greenfoot.mouseClicked(musicButton)) //toggle between playing and pausing music
        {
            playing = !playing;
            changeMusicState();
        }

        checkWeaponFrequency(); //checks if a button has been pressed to change weapon frequency
        checkSpawnRate();   //checks if a spawn rate button has been pressed to change spawn rate
        checkConquered();  //checks if all the planets are conquered

        //decreases the delay to spawn objects when all planets are conquered by the same species
        if(allConquered)
            conquerDelay--;

    }

    /**
     * Changes spawn rate on either team.
     * 
     * @param change    difference in spawn rate
     * @param isHuman   true if human, otherwise false; determines which team's frequency will change
     */
    private void changeSpawnRate(int change, boolean isHuman)
    {
        if(isHuman)
        {
            if(humanSpawn+change >= 10)
                humanSpawn += change;
        }
        else
        {
            if(alienSpawn+change >= 10)
                alienSpawn += change;
        }
    }

    /**
     * Checks if a button has been clicked to change spawn rate.
     */
    private void checkSpawnRate()
    {
        if(Greenfoot.mouseClicked(upSpawnRate[0]))  //up aliens
            changeSpawnRate(-3,false);
        if(Greenfoot.mouseClicked(upSpawnRate[1]))  //up humans
            changeSpawnRate(-3,true);
        if(Greenfoot.mouseClicked(downSpawnRate[0]))    //down aliens
            changeSpawnRate(3,false);
        if(Greenfoot.mouseClicked(downSpawnRate[1]))    //down humans
            changeSpawnRate(3,true);
    }

    /**
     * Checks if a button has been clicked to change the weapon frequency.
     */
    private void checkWeaponFrequency()
    {
        if(Greenfoot.mouseClicked(upWeapons[0]))        //up aliens
            Attacker.changeWeaponFrequency(-3,false);
        if(Greenfoot.mouseClicked(upWeapons[1]))        //up humans
            Attacker.changeWeaponFrequency(-3,true);
        if(Greenfoot.mouseClicked(downWeapons[0]))      //down aliens
            Attacker.changeWeaponFrequency(3,false);
        if(Greenfoot.mouseClicked(downWeapons[1]))      //down humans
            Attacker.changeWeaponFrequency(3,true);
    }

    /**
     * Checks if all the planets are conquered by one team. If all the planets are conquered, 15 to 20 comets and 25 to 30 blackholes will appear in world.
     */
    private void checkConquered()
    {
        boolean isConquered = true;     //starts as conquered
        if(targetPlanets[0].getState() == 'g')
        {
            isConquered = false; //if the state of the first planet is neutral, planets are not conquered
        }
        else
        {
            for(int i = 1; i < targetPlanets.length; i++)   //for every planet except the first one
            {
                if(targetPlanets[0].getState() != targetPlanets[i].getState())
                {
                    isConquered = false;    //if the state of the planet is not equal the first planet, planet is not conquered
                    break;
                }
            }
        }

        if(isConquered) //if all planets are conquered
        {
            allConquered = true;
            for(int i = 0; i < Greenfoot.getRandomNumber(3)+3; i++)    //3 to 5 comets will appear
            {
                cometAppears();
            }
        }

        if(conquerDelay <= 0)   //onces the conquer delay reaches zero, spawn blackholes
        {
            for(int i = 0; i < Greenfoot.getRandomNumber(3)+3; i++)    //3 to 5 blackholes will appear
            {
                blackHoleAppears();
            }
            allConquered = false;
            conquerDelay = 50;
        }
    }

    /**
     * Spawn black holes in world.
     */
    private void spawnBlackHoles()
    {
        int randomAct = Greenfoot.getRandomNumber(blackHoleSpawn);
        if(randomAct == 0)
        {   
            for(int i = 0; i < Greenfoot.getRandomNumber(3)+3; i++)
            {
                blackHoleAppears(); //if black hole appears randomly, generates 3 to 5 at once
            }   
        }
    }

    /**
     * (Almost) guaranteed to add a black hole to the world.
     */
    private void blackHoleAppears()
    {
        int count = 0;
        BlackHole b = new BlackHole();
        while(true)
        {
            count++;    //increases counter
            addObject(b, Greenfoot.getRandomNumber(960), Greenfoot.getRandomNumber(640));   //adds a blackhole to world

            if(b.touching(Planet.class) || b.touching(BlackHole.class)) //if black hole is touching a planet or another black hole
            {
                removeObject(b);    //remove object
                if(count % 10 == 0)
                    break;  //if tried adding black hole ten times and still fails, quit
            }
            else    //if planet was successfully added to world, break out of loop
            {
                break;
            }
        }
    }

    /**
     * Background World randomly spawns in a Fighter and depending on the randomized number, 
     * it will either spawn a human or an alien attacker or invader.
     */
    private void spawnFighters()
    {
        int randAlien = Greenfoot.getRandomNumber(alienSpawn);
        int randHuman = Greenfoot.getRandomNumber(humanSpawn);

        Fighter f;
        if(randAlien == 0)   //alien invader
        {
            f = new Invader(false);
            addObject (f, 0, Greenfoot.getRandomNumber(640));
        }
        else if(randAlien == 1)  //alien attacker
        {
            f = new Attacker(false);
            addObject (f, 0, Greenfoot.getRandomNumber(640));
        }
        if(randHuman == 0)  //human invader
        {
            f = new Invader(true);
            addObject (f, 960, Greenfoot.getRandomNumber(640));
        }
        else if(randHuman == 1)  //human attacker
        {
            f = new Attacker(true);
            addObject (f, 960, Greenfoot.getRandomNumber(640));
        }
    }

    /**
     * Will spawn comets in world.
     */
    private void spawnComets()
    {     
        int randomAct = Greenfoot.getRandomNumber(cometSpawn);
        if(randomAct == 0)
        {   //if comet is spawned randomly, will spawn 3 to 5 at once
            for(int i = 0; i < Greenfoot.getRandomNumber(3)+3; i++)
            {
                cometAppears();
            }
        }

    }

    /**
     * Adds a comet to the world.
     */
    private void cometAppears()
    {
        int rand = Greenfoot.getRandomNumber(4);
        Comet c;
        if(rand == 0)       //comet spawned from left
        {
            c = new Comet();
            addObject(c, 0, Greenfoot.getRandomNumber(640));
            c.setRotation(Greenfoot.getRandomNumber(120)-60);
        }
        else if(rand == 1)      //comet spawned from right
        {
            c = new Comet();
            addObject(c, 960, Greenfoot.getRandomNumber(640));
            c.setRotation(Greenfoot.getRandomNumber(120)+120);
        }
        else if(rand == 2)      //comet spawned from top
        {
            c = new Comet();
            addObject(c, Greenfoot.getRandomNumber(960), 0);
            c.setRotation(Greenfoot.getRandomNumber(120)+30);
        }
        else if(rand == 3)      //comet spawned from bottom
        {
            c = new Comet();
            addObject(c, Greenfoot.getRandomNumber(960), 640);
            c.setRotation(Greenfoot.getRandomNumber(120)+210);
        }
    }

    /**
     * Static method that gets the distance between the x,y coordinates of two Actors
     * using Pythagorean Theorum.
     * 
     * @param a     First Actor
     * @param b     Second Actor
     * @return float
     */
    public static float getDistance (Actor a, Actor b)
    {
        double distance;
        double xLength = a.getX() - b.getX();
        double yLength = a.getY() - b.getY();
        distance = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
        return (float)distance;
    }
}
