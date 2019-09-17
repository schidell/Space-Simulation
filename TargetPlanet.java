import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The TargetPlanet creates target planets that invaders aim to conquer. 
 * Multiple planets can be created in a world for invaders to conquer.
 * <p>If hit by an Invader, the planet's population will change.
 * Depending on its population, it will belong to either the human or alien team, or be neutral.
 * 
 * @author Maggie Lin, Catherine Lee
 * @version April 1, 2018
 */
public class TargetPlanet extends Planet
{
    private static final int DEFAULT_MIN_CONQUER_POP = 10;

    //variables for planet statistics.
    private int humanPopulation = 0;
    private int alienPopulation = 0;
    private int minConquerPopulation;
    private boolean isConquered = false;
    /**
     * Constructs a planet that fighters are trying to conquer with custom size, state, and minimum conquering population.
     * 
     * @param size      size of planet; must be greater than 0, ideally below 60
     * @param state     state of planet (b: alien conquered, g: neutral, l: alien healing, p: human healing, r: human conquered)
     * @param minPop    minimum population needed to conquer planet
     */
    public TargetPlanet(int size, char state, int minPop)
    {
        super(size,state);

        setState(state); //ensures state is valid
        setInitialImage();  //set initial image

        //ensure min pop to conquer is valid
        if(minPop > 0)
            minConquerPopulation = minPop;
        else
            minConquerPopulation = DEFAULT_MIN_CONQUER_POP;
    }

    /**
     * Constructs a planet that fighters are trying to conquer with custom size, state, 
     * and default minimum conquering population
     * 
     * @param size      size of planet; must be greater than 0, ideally below 60
     * @param state     state of planet (b: alien conquered, g: neutral, l: alien healing, p: human healing, r: human conquered)
     */
    public TargetPlanet(int size, char state)
    {
        this(size, state, DEFAULT_MIN_CONQUER_POP);
    }

    /**
     * Act - do whatever the TargetPlanet wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        rotate();
        checkConquered();
    }    

    /**
     * When hit by a fighter (should be an invader), the planet's population will increase.
     * 
     * @param isHuman   true if fighter is human, otherwise false
     */
    public void hitByFighter(boolean isHuman)
    {
        if(isHuman)     //if human, human population will increase
            humanPopulation++;
        else            //if alien, alien population will increase
            alienPopulation++;
    }

    /**
     * Will erase population of planet and will return to neutral state.
     */
    public void wipePopulation()
    {
        //populations set to 0
        alienPopulation = 0;    
        humanPopulation = 0;
        state = 'g';    //neutral planet
        isConquered = false;    //planet is not conquered
    }

    /**
     * Sets state and ensures it is a reasonable value (conquered or neutral).
     * If an unreasonable value is used, the state will be set to neutral.
     */
    private void setState(char state)
    {
        if(state == 'g' || state == 'b' || state == 'p')
            this.state = state;
        else    //set as neutral planet
        {
            this.state = 'g';
        }
    }

    /**
     * Checks if planet has been conquered and what state it should be.
     */
    private void checkConquered()
    {
        if(!isConquered)    //if not conquered
        {
            //if there are more than min conquer pop for either population, planet will be conquered
            if(humanPopulation > minConquerPopulation || alienPopulation > minConquerPopulation)
                isConquered = true;
        }
        if(isConquered)
        {
            if(humanPopulation == alienPopulation)      //if populations equal
                setState('g');  //neutral planet
            else if(humanPopulation > alienPopulation)
                setState('b');  //change to human planet
            else if(alienPopulation > humanPopulation)
                setState('p');  //change to alien planet
        }
    }

    /**
     * Gets population of either the human or alien population.
     * 
     * @param isHuman true if getting human population, false if getting alien population 
     * @return int population of either humans or aliens
     */
    public int getPopulation(boolean isHuman)
    {
        if(isHuman)
            return humanPopulation;
        return alienPopulation;
    }
}
