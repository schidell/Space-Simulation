import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * A HealPlanet is where an attacker can recharge its health.
 * <p>
 * Ideally, the fighter will check if the planet has the correct state and if the planet is full. 
 * Then, they will call the hitByFighter method. When the fighter leaves the planet, 
 * it should call the fighterLeaves method.
 * <p>
 * The planet's health will decrease as more attackers recharge at the planet. If the health becomes 0,
 * the planet recharges and cannot revive any attackers until its health is restored.
 * 
 * @author Maggie Lin, Catherine Lee
 * @version March 31, 2018
 */
public class HealPlanet extends Planet
{
    private static final int DEFAULT_MAX_HEALTH = 100;
    private static final int DEFAULT_MAX_POPULATION = 5;

    //planet statistics
    private int maxHealth;
    private int health;
    private int maxPopulation;
    private int population;

    //dictates transparency of planet according to its health
    private boolean isRecharging;

    int transparency;
    /**
     * Constructs healing planet that aliens or humans can recharge at.
     * 
     * @param size  size of planet
     */
    public HealPlanet(int size)
    {
        super(size,'r');

        setInitialImage();  //set initial image

        //set planet statistics
        maxHealth = DEFAULT_MAX_HEALTH;
        health = maxHealth;
        maxPopulation = DEFAULT_MAX_POPULATION;
        population = 0;

        isRecharging = false;   //is not recharging

        actCounter = 0;

        transparency = getImage().getTransparency();
    }

    /**
     * Act - do whatever the HealPlanet wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        rotate();   //planet appears to rotate

        checkHealth();  //checks health of planet

        fixOpacity();   //transparency adjusted depending on health
    }    

    /**
     * When hit by fighter, will change planet's population and health statistics.
     * 
     * @param boolean isHuman true if fighter is human, false otherwise
     */
    public void hitByFighter(boolean isHuman)
    {
        if(!planetFull())   //if planet is not full
        {
            //if planet belongs to figher that hit planet and state is correct or planet is neutral
            population++;   //population increases
            health -= 10;    //planet's health decreases
        }
    }

    /**
     * When fighter leaves planet, will change planet's population.
     */
    public void fighterLeaves()
    {
        if(population > 0)
            population--;
    }

    /**
     * Entire population is wiped and set to 0.
     */
    public void wipePopulation()
    {
        //take every fighter in contact with planet and remove it
        for(Fighter f: (List<Fighter>)getIntersectingObjects(Fighter.class))
        {
            getWorld().removeObject(f);
        }

        population = 0;
        health = maxHealth; //health of planet restored
    }

    /**
     * Checks if planet can be landed on.
     * 
     * @return boolean true if planet can be landed on, otherwise false
     */
    public boolean canLand()
    {
        if(!planetFull() && !isRecharging)
            return true;
        return false;
    }

    /**
     * Check if planet is already full.
     * 
     * @return boolean true if planet is already at maximum capacity, otherwise false
     */
    private boolean planetFull()
    {
        if(population >= maxPopulation)     //if population is greater than or equal to the maximum population, the planet is full
            return true;
        return false;
    }

    /**
     * Checks health of planet and sets stats.
     */
    private void checkHealth()
    {
        //regainHealth(); //health is regained

        if(health <= 0 && !isRecharging)    //if health is depleted and hasn't started recharging, start recharging
            isRecharging = true;
        else if(health >= maxHealth && isRecharging)    //if health restored and is recharging, set to no longer recharging
            isRecharging = false;
        else if(isRecharging && health < maxHealth)
        {
            regainHealth();
        }
    }

    /**
     * Planet will regain health if health is below max health and recharging.
     */
    private void regainHealth()
    {
        if(health < maxHealth && isRecharging && actCounter % 20 == 0)
        {
            health++;   //if recharging and health below max and 50 acts have passed, increase health
        }
    }

    /**
     * Planet's opacity will change depending on its health.
     */
    private void fixOpacity()
    {
        if(health >= maxHealth) //if health is restored
            transparency = 250; //not transparent
        else if(health >= 0 && !isRecharging)   //if healthy and not recharging
            transparency = (int)(health * 1.8) + 70;
        else if(isRecharging && health > -100)  //if recharging, planet will be more faded
            transparency = health+50;
        getImage().setTransparency(transparency);   //set transparency
    }
}
