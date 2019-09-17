import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Fighters are alien and human ships that can take damage and enter the world with the same level of health.
 * Their goal is to fight and conquer planets.
 * <p> When they get hit by a laser, they will take damage. 
 * When they get hit by a laser, they will take damage. When health reaches 0 for any fighter, 
 * they will be removed from the world. 
 * They can be killed by being hit by comets and by being sucked into black holes.
 * 
 * @author Amber Chen, Shivani Chidella, Kyle Chan
 * @version March 2018
 */
public abstract class Fighter extends Actor
{
    protected int health = 5;   //Sets the inital health of all fighters
    protected boolean isHuman;  //determines whether fighter is human or alien
    protected int speed;        //speed of fighter
    protected int movingSpeed = Greenfoot.getRandomNumber(3)+2; //speed of fighter when it is moving

    protected int transparency = 255;   //set initial transparency
    protected int myWidth;

    protected boolean onHealPlanet = false; //planet is not currently on heal planet
    protected boolean removeMe = false;

    /**
     * Constructs a fighter that will move through the world. 
     * 
     * @param isHuman   true if fighter is human, false if fighter is alien
     */
    public Fighter(boolean isHuman)
    {
        this.isHuman = isHuman; //sets team
        speed = movingSpeed;    //sets initial speed
    }

    /**
     * Find if fighter is on human or alien team.
     * 
     * @return boolean true if human team, false for alien
     */
    public boolean getTeam()
    {
        return isHuman;
    }
    
    /**
     * Finds the closest planet fighter is aiming to reach.
     */
    protected abstract void targetClosestPlanet();
    
    /**
     * Moves to the planet is targetting.
     */
    protected abstract void moveTowards();

    /**
     * Initializes value of myWidth.
     */
    protected void saveMyWidth()
    {
        myWidth = getImage().getWidth();
    }

    /**
     * When hit by another Fighter, take the value of damage and subtract it from the Fighter's health.
     * If Fighter's health reaches zero, remove the Fighter from the world.
     * 
     * @param healthDecrease    takes in the value of the damage done by the weapon
     * 
     */
    public void takeDamage(int healthDecrease)
    {
        // Takes the damage and subtracts it from the current health
        health -= healthDecrease;   //health is decreased
        
        transparency = (40*health+45);  //transparency changes according to health

        if(transparency >= 0)    //if transparency is greater than 0, set transparency
            getImage().setTransparency(transparency);

        if (health <= 0)    //if health is below 0, should remove from world
        {
            removeMe = true;
        }
    }

    /**
     * Checks if fighter is on heal planet.
     * 
     * @return boolean true if fighter is on heal planet, otherwise false
     */
    protected boolean isOnHealPlanet()
    {
        return onHealPlanet;
    }

    /**
     * Handy method that checks if this object is at the edge
     * of the World
     * 
     * @return boolean  true if at or past the edge of the World, otherwise false
     */
    public boolean atWorldEdge()
    {
        if (getX() < -(myWidth / 2) || getX() > getWorld().getWidth() + (myWidth / 2))
            return true;
        else if (getY() < -(myWidth / 2) || getY () > getWorld().getHeight() + (myWidth / 2))
            return true;
        else
            return false;
    }
}
