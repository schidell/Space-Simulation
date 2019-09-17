import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Attackers shoot the enemy fighters. If their health is low, they search for a heal planet they can land on to revive.
 * 
 * @author Kyle Chan, Shivani Chidella, Amber Chen, Maggie Lin, Catherine Lee
 * @version April 6 2018
 */
public class Attacker extends Fighter
{
    private HealPlanet healPlanet;              //closest heal planet available
    private ArrayList<HealPlanet> healPlanets;  //list of all heal planets in array

    private int actCounter;     //counts acts

    private int weaponFrequency;      //determines how often a weapon is shot

    private static int alienWeaponFrequency = 30;  //initial alien weapon frequency
    private static int humanWeaponFrequency = 30;  //initial alien weapon frequency

    /**
     * Constructs and determines if the Fighter is Alien or Human. If Alien, spawn from the left and move towards the 
     * positive x-axis. If Human, spawn from the right and move towards the negative x-axis.
     * 
     * @param isHuman   true = human, false = alien
     */
    public Attacker (boolean isHuman)
    {
        super(isHuman);
        if (isHuman == true)    //if human, sets different image and rotated to face left
        {
            setImage("attackerHuman.png");
            this.setRotation(180);
        }

        saveMyWidth();  //save myWidth value

        actCounter = 100;   //sets act counter to 100
        if(isHuman)
            weaponFrequency = humanWeaponFrequency;     //weapon frequency initialized
        else
            weaponFrequency = alienWeaponFrequency;
    }

    /**
     * Changes the weapon frequency on either team.
     * 
     * @param change    difference in weapon frequency
     * @param isHuman   true if human, otherwise false; determines which team's weapon frequency will change
     */
    public static void changeWeaponFrequency(int change, boolean isHuman)
    {
        if(isHuman) //if is human, change human weapon frequency by specified amount
        {
            if(humanWeaponFrequency+change >= 5)
                humanWeaponFrequency += change;
        }
        else        //if alien, change alien weapon frequency by specified amount
        {
            if(alienWeaponFrequency+change >= 5)
                alienWeaponFrequency += change;
        }
    }

    /**
     * Attacker spawns laser (depending on if they are alien or human).
     */
    public void shoot()
    {
        if(!onHealPlanet)   //if not on a healing planet
        {
            Weapon projectile = new Weapon(isHuman);
            if (!isHuman)   //shoots laser from alien
            {
                getWorld().addObject(projectile, getX() + 20 , getY());
            }
            else           //shoots laser from human
            {
                getWorld().addObject(projectile, getX() - 20 , getY());
            }
        }
    }

    /**
     * Act - do whatever the Attacker wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        move(speed);    //move

        if(health > 2 && weaponFrequency <= 0)  //if health above 2 and enough acts have passed for weapon to spawn
        {
            shoot();    //fire weapon
            if(isHuman)
                weaponFrequency = humanWeaponFrequency; //resets frequency counter to human frequency
            else
                weaponFrequency = alienWeaponFrequency; //resets frequency counter to alien frequency
        }

        weaponFrequency--;  //weapon frequency counter goes down

        if (health <= 2 && !onHealPlanet)   //if health is below 2 and not on heal planet
        {
            targetClosestPlanet();  //find closest planet
            moveTowards();      //move to closest planet
        }

        if(onHealPlanet)    //if on heal planet, will recharge
            recharge();

        if (health <= 0)    //if health is smaller than or equal to 0, remove attacker
            removeMe = true;

        if(atWorldEdge() || removeMe)
            getWorld().removeObject(this);      //remove object
    }    

    /**
     * Finds the nearest Heal Planet available when its health is low.
     */
    protected void targetClosestPlanet()
    {
        double closestTargetDistance = getWorld().getWidth();
        double distanceToActor;

        // If any planets are found, search the whole World
        healPlanets = (ArrayList)getWorld().getObjects(HealPlanet.class);

        if (healPlanets.size() > 0)
        {
            if(healPlanets.get(0).canLand())    //if first planet can be landed on
            {
                healPlanet = healPlanets.get(0);    //set heal planet as the first planet
                closestTargetDistance = SpaceWorld.getDistance(this, healPlanet);   //checks distance to target; used to see if there are closer targets
            }

            // Loop through the objects in the ArrayList to find the closest target
            for (HealPlanet o : healPlanets)
            {
                Actor a = (Actor) o;    
                // Cast for use in generic method
                distanceToActor = SpaceWorld.getDistance(this, a);  // Measure distance from me

                // If I find a HealPlanet closer than my current target that I can land on, I will change targets
                if (distanceToActor < closestTargetDistance && o.canLand())
                {
                    healPlanet = o; //save new planet
                    closestTargetDistance = distanceToActor;    //new closest distance to compare to
                }
            }
        }
    }

    /**
     * Allows Attackers to move towards the closest healing planet. If the 
     * Attacker reaches the healing planet, stop for 5 intervals, 
     * then continue moving.
     */
    protected void moveTowards()
    {
        if(healPlanet != null && healPlanet.canLand())
            turnTowards(healPlanet.getX(), healPlanet.getY());
        else
        {
            if(isHuman)
                setRotation(180);   //face right
            else
                setRotation(0);     //face left
        }

        //If the invader touches a planet, it will go stop
        HealPlanet t = (HealPlanet)getOneIntersectingObject (HealPlanet.class); //gets planet it has landed on
        if (t != null && !onHealPlanet && t.canLand()) //if landed on a planet and not recharging, recharge
        {
            onHealPlanet = true;    //has landed on a planet
            t.hitByFighter(isHuman);    //planet's population will change
            speed = 0;      //does not move
        }
    }

    /**
     * Attacker will recharge to full health while it is on the heal planet.
     */
    private void recharge()
    {
        if(onHealPlanet)
        {
            actCounter--;   //decrease act counter
            HealPlanet t = (HealPlanet)getOneIntersectingObject (HealPlanet.class); //gets planet it has landed on
            if(actCounter <= 0 && t != null) //if 100 acts have passed
            {
                health = 5;                         //health resets
                getImage().setTransparency(255);    //transparency resets
                onHealPlanet = false;               //no longer on heal planet
                t.fighterLeaves();  //planet stats will update
                actCounter = 100;       //reset counter
                if (isHuman)        //if human, rotated to face left
                    this.setRotation(180);
                else                //if alien, rotated to face right
                    this.setRotation(0);
                speed = movingSpeed;    //speed is no longer 0
            }
        }
    }
}
