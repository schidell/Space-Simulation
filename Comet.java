import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Comets can appear randomly or when the comet button is clicked. If they hit a fighter not on a planet, the fighter will disappear. 
 * If the comet hits a planet, an explosion will appear and the comet will disappear, and the population of the planet will be 0. 
 * For target planets, the planet will become neutral; for healing planets, the population will be wiped.
 * 
 * @author Catherine Lee 
 * @version March 2018
 */
public class Comet extends Actor
{
    private int speed;//speed the comet moves at
    private int scaleFactor;//scale of size of the comet

    private boolean removeMe;   //whether object should still exist
    
    //private GreenfootSound boom = new GreenfootSound("explosion2.mp3");
    private GreenfootSound boom;
    /**
     * Creates a comet with a random speed and random size.
     */
    public Comet()
    {
        speed = Greenfoot.getRandomNumber(10)+10;           //get a random speed between 10 and 19
        scaleFactor = (Greenfoot.getRandomNumber(4)+4);     //get a random scale factor between 4 and 7
        getImage().scale(scaleFactor*44,scaleFactor*20);    //sets the image between 2 to 18 times the image size (44 by 20)

        removeMe = false;   //object should exist
    }

    /**
     * Act - do whatever the Comet wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        move(speed);//the comet moves at the specified speed
        checkHitFighter();//checks if the comet hits the fighter
        checkHitPlanet();//checks if the comet hits the planet

        if(atWorldEdge() || removeMe)
            getWorld().removeObject(this);
    }    

    /**
     * Removes the fighter if the comet hits it.
     */
    private void checkHitFighter()
    {
        //checks if hit fighter
        Fighter f = (Fighter)getOneIntersectingObject(Fighter.class);
        if (f != null && !f.isOnHealPlanet())
        {
            getWorld().removeObject(f); //removes the fighter if the comet hits it
        }
    }

    /**
     * Wipes the planet population and explodes if the comet hits it.
     */
    private void checkHitPlanet()
    {
        //checks if hit planet
        Planet p = (Planet)getOneIntersectingObject(Planet.class);
        if (p != null)
        {
            //wipes the planet's population
            p.wipePopulation();
            //create an explosion
            getWorld().addObject(new Explosion(100, true), p.getX(),p.getY());
            boom = new GreenfootSound("explosion2.mp3");
            boom.setVolume(30);
            boom.play();
            //remove comet
            removeMe = true;    //object should no longer exist
        }
    }

    /**
     * Handy method that checks if this object is at the edge
     * of the World
     * 
     * @return boolean  true if at or past the edge of the World, otherwise false
     */
    private boolean atWorldEdge()
    {
        if (getX() < -getImage().getWidth() || getX() > getWorld().getWidth() + getImage().getWidth())
            return true;
        else if (getY() < -getImage().getWidth() || getY () > getWorld().getHeight() + getImage().getWidth())
            return true;
        else
            return false;
    }
}