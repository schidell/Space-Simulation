import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The planets are where fighters will land. 
 * Depending on the type of planet, they will influence the fighters differently.
 * <p>Multiple planets can be created in a world. 
 * When a fighter hits a planet, they should call the hitByFighter method to update the planet's statistics.
 * When the population is wiped by a comet, the wipePopulation method should be called.
 * 
 * @author Maggie Lin, Catherine Lee
 * @version April 1, 2018
 */
public abstract class Planet extends Actor
{
    //constants
    private static final int DEFAULT_SIZE = 57;
    //variables for planet statistics
    protected char state;

    //variables for image and animation of planet
    private int size;
    private int speedOfRotation;
    protected int actCounter;
    private int imageNumber;

    //store images
    private GreenfootImage[] bluePlanets = new GreenfootImage[19];    //array of blue planets
    private GreenfootImage[] greenPlanets = new GreenfootImage[19];   //array of green planets
    private GreenfootImage[] purplePlanets = new GreenfootImage[19];  //array of purple planets
    private GreenfootImage[] redPlanets = new GreenfootImage[19];     //array of red planets

    /**
     * Constructs a planet with specified size and state.
     * 
     * @param size  size of planet; must be greater than 0, ideally below 60
     * @param state state of planet (b: alien conquered, g: neutral, l: alien healing, p: human healing, r: human conquered)
     */
    public Planet(int size, char state)
    {
        //set size
        if(size > 0)
            this.size = size;
        else
            this.size = DEFAULT_SIZE;

        this.state = state; //set state

        loadImages();   //load images

        //set initial values for animation
        speedOfRotation = Greenfoot.getRandomNumber(5) + 3;
        actCounter = 0;
    }

    /**
     * If fighter hits planet successfully, fighter should call this method to update Planet's statistics.
     */
    public abstract void hitByFighter(boolean isHuman);

    /**
     * Population of planet is wiped and reset.
     */
    public abstract void wipePopulation();

    /**
     * Gets state of planet: whether it is conquered, neutral, or a healing planet.
     * A neutral planet is denoted with 'g'; alien conquered is 'p', human conquered is 'b', and healing is 'r'.
     */
    public char getState()
    {
        return state;
    }

    /**
     * Images update to appear to rotate.
     */
    protected void rotate()
    {
        actCounter++;   //counts number of acts that pass

        if(actCounter % speedOfRotation == 0)   //image changes when certain number of acts have passed
        {
            //set image based on what state it is in
            if(state == 'b')
                setImage(bluePlanets[imageNumber]);
            else if(state == 'p')
                setImage(purplePlanets[imageNumber]);
            else if(state == 'r')
                setImage(redPlanets[imageNumber]);
            else        //should be neutral planet (state = 'g')
                setImage(greenPlanets[imageNumber]);

            getImage().scale(size,size);    //set image to appropriate size

            //set imageNumber so planet will appear to rotate
            if(imageNumber < 18)
                imageNumber++;
            else
                imageNumber = 0;
        }
    }

    /**
     * Load images into image arrays.
     */
    protected void loadImages()
    {
        //red planet array loads
        for(int i = 0; i < redPlanets.length; i++)
        {
            redPlanets[i] = new GreenfootImage("redPlanet" + (i+1) + ".png");
        }
        //green planet array loads
        for(int i = 0; i < greenPlanets.length; i++)
        {
            greenPlanets[i] = new GreenfootImage("greenPlanet" + (i+1) + ".png");
        }
        //blue planet array loads
        for(int i = 0; i < greenPlanets.length; i++)
        {
            bluePlanets[i] = new GreenfootImage("bluePlanet" + (i+1) + ".png");
        }
        //purple planet array loads
        for(int i = 0; i < greenPlanets.length; i++)
        {
            purplePlanets[i] = new GreenfootImage("purplePlanet" + (i+1) + ".png");
        }
    }

    /**
     * Sets initial image.
     */
    protected void setInitialImage()
    {
        //sets image based on state
        if(state == 'b')
            setImage(bluePlanets[0]);
        else if(state == 'p')
            setImage(purplePlanets[0]);
        else if(state == 'r')
            setImage(redPlanets[0]);
        else        //should be neutral planet (state = 'g')
            setImage(greenPlanets[0]);
        getImage().scale(size,size);    //resize image
        imageNumber = 1;
    }
}
