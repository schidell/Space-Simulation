import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Human and Alien attackers shoot laser weapons at the opposing team. 
 * Being hit by a weapon from the opposite team forces a Fighter to take damage and reduces their health.
 * 
 * @author Shivani Chidella, Amber Chen, Kyle Chan 
 * @version April 9, 2018
 */
public class Weapon extends Actor
{
    boolean isHumanWeapon;      //true = human, false = alien
    private int myWidth;        
    private boolean removeMe = false;   //should be removed

    /**
     * Constructs weapons and gives certain lasers to its' respective Attackers.
     * 
     * @param isHumanWeapon     determines if the Attacker is Alien or Human
     * 
     */
    public Weapon (boolean isHumanWeapon)
    {
        this.isHumanWeapon = isHumanWeapon;
        if (!isHumanWeapon)
        {
            setImage ("purplelaser.png");
        }
        myWidth = getImage().getWidth();
    }

    /**
     * Act - do whatever the Weapon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(isHumanWeapon)   //if human weapon, move left
            move(-7);
        else                //if alien weapon, move right
            move(7);

        Fighter f = (Fighter)getOneIntersectingObject(Fighter.class);    //get fighter touching weapon
        if(f != null && isHumanWeapon != f.getTeam() && !f.isOnHealPlanet())   //if intersecting fighter exists, is on oppoosite team, and not on heal planet
        {
            f.takeDamage(1);    //fighter takes damage
            removeMe = true;    //weapon should be removed
        }

        if (removeMe || atWorldEdge())   //if object at edge or should not exist, remove from world
        {
            getWorld().removeObject(this);
        }
    }   

    /**
     * Determines if the Weapon is at the edge of the world. If so,
     * remove the weapon.
     */
    public boolean atWorldEdge()
    {
        if (getX() < - (myWidth / 2) || getX() > getWorld().getWidth() + (myWidth / 2))
            return true;
        else if (getY() < - (myWidth / 2) || getY() > getWorld().getHeight() + (myWidth / 2))
            return true;
        else
            return false;
    }
}
