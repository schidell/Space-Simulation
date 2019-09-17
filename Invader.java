import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Invaders search for target planets to conquer. If they hit a target planet, they will increase the population there.
 * Some invaders' goal is to reach the nearest planet; 
 * others try to reach the nearest planet that is not conquered by their team yet.
 * 
 * @author Shivani Chidella, Amber Chen, Kyle Chan
 * @version April 6 2018
 */
public class Invader extends Fighter
{
    private TargetPlanet planet;    //closest target planet
    private ArrayList<TargetPlanet> planets;    //list of target planets in world
    private boolean findNearestPlanet;  //if planet is looking for planet not conquered by their team or just whichever planet is closest

    int actCounter = 0; //count acts
    /**
     * Constructs an Invader and determines if the Invader is Human or Alien. 
     * If Human, change the image and move to the left. 
     * If Alien, move to the right.
     * 
     * @param isHuman   determines if the Invader is an Alien or Human
     */
    public Invader (boolean isHuman)
    {
        super(isHuman);
        if (isHuman)    //if is human, sets image to human invader
            setImage ("invaderHuman.png");

        saveMyWidth();  //saves myWidth

        int rand = Greenfoot.getRandomNumber(2);
        if(rand == 0)
            findNearestPlanet = true;
        else
            findNearestPlanet = false;
    }
    
    /**
     * Act - do whatever the Invader wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        move(speed);    //move at set speed
        
        if(actCounter < 2)  //increases act counter if only 1 act has passed
            actCounter++;

        if(actCounter == 1)     //if on the first act
        {
            targetClosestPlanet();  //will target closest planet depending on if it wants to find an unconquered planet, or just the closest one
            if(planet == null && findNearestPlanet)
                findNearestPlanet = false;
        }
            
        if(health > 0)  //if alive
        {
            if(findNearestPlanet)
                targetClosestPlanet();
            if(planet != null && planet.getWorld() != null) //if planet exists
                moveTowards();  //move toward it
            else
                moveRandomly(); //move randomly
        }
        
        if(removeMe || atWorldEdge())
            getWorld().removeObject(this);
    }    

    /**
     * Checks if given state of planet is on same team as invader.
     * 
     * @return boolean true if state is same as planet's team, otherwise false
     */
    private boolean sameTeamCheck(char state)
    {
        if((state == 'b' && isHuman) || (state == 'p' && !isHuman))
            return true;
        return false;
    }

    /**
     * Will find planet closest to it in the world that they are looking for.
     */
    protected void targetClosestPlanet ()
    {
        double closestTargetDistance = getWorld().getWidth();
        double distanceToActor;

        // search the whole World for planets
        planets = (ArrayList)getWorld().getObjects(TargetPlanet.class);

        if (planets.size() > 0)
        {
            // Loop through the objects in the ArrayList to find the closest target
            for (TargetPlanet o : planets)
            {
                // Cast for use in generic method
                Actor a = (Actor) o;

                //if looking for nearest planet OR nearest unconquered planet and current planet is not on the same team
                if(findNearestPlanet || (!findNearestPlanet && !sameTeamCheck(o.getState())))
                {
                    // Measure distance from me
                    distanceToActor = SpaceWorld.getDistance(this, a);

                    // if target planet closer than current target is found, target will change
                    if (distanceToActor < closestTargetDistance)
                    {
                        planet = o;
                        closestTargetDistance = distanceToActor;
                    }
                }
            }
        }
    }

    /**
     * Allows Invaders to move towards the closest planet. If Invader touches a planet,
     * remove the Invader and tell the Planet class that either an Alien or Human has 
     * removed at the planet to keep track of the population score.
     */
    protected void moveTowards()
    {
        if(planet != null)
            turnTowards(planet.getX(), planet.getY());

        //If the invader touches a planet, it will go into the planet
        TargetPlanet t = (TargetPlanet)getOneIntersectingObject (TargetPlanet.class);
        //if t exists and looking for nearest planet OR looking for unconquered planet and reached it or no set planet
        if (t != null && ((findNearestPlanet || (!findNearestPlanet && t.equals(planet))) || planet == null))
        {
            t.hitByFighter(isHuman);        //planet's stats will change
            removeMe = true;
        }
    }

    /**
     * A method to be used for moving randomly if there are no more neutral planets. Will mostly just move in its current direction, 
     * occasionally turning to face a new, random direction.
     */
    private void moveRandomly()
    {
        if (Greenfoot.getRandomNumber (100) == 50)
            turn(Greenfoot.getRandomNumber(360));
        else
            move(speed);
    } 
}
