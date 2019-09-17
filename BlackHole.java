import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Creates a black hole. If a fighter, comet, or weapon touches the black hole, they will disappear.
 * <p>
 * The black holes will fade in, and then shrink in size and remove themselves from the world.
 * 
 * @author Maggie Lin
 * @version April 2018
 */
public class BlackHole extends Actor
{
    private GreenfootImage image;   //variable for image

    //instance variables
    private boolean isFading;
    private boolean removeMe;
    private int transparency;

    //variables to count amount of acts that have passed
    private int sizeCounter;    //counts acts so black hole will shrink
    private int opacityCounter; //counts acts for opacity to fade
    
    /**
     * Constructs a black hole of random size and speed.
     */
    public BlackHole()
    {
        image = new GreenfootImage("blackhole.png");    //sets image

        setImage(image);
        int size = Greenfoot.getRandomNumber(150)+100;
        getImage().scale(size,size);

        transparency = 1;       //starts faded out
        getImage().setTransparency(transparency);   //sets transparency to 1

        isFading = false;   //is not fading out
        removeMe = false;   //should exist in world

        sizeCounter = 0;
        opacityCounter = 1;
    }
    
    //if a comet, fighter, or weapon hits the black hole, it will disappear
    private void hit()
    {
        for(Object o : getIntersectingObjects(null))    //for every object touching the black hole
        {
            Actor actor = (Actor) o;

            //if a comet, weapon, or a fighter not on a heal planet hits it, they will disappear
            if(actor instanceof Comet || actor instanceof Weapon || (actor instanceof Fighter && !((Fighter)actor).isOnHealPlanet()))  
            {
                getWorld().removeObject(actor); //if it is a comet, fighter, or weapon, will remove from world
            }
        }
    }

    /**
     * Checks if an object from another class is touching the object.
     * 
     * @param c         class that object should check to be touching
     * @return boolean  true if an object of class c is touching it, otherwise false
     */
    public boolean touching(Class c)
    {
        if(isTouching(c))
            return true;
        return false;
    }

    /**
     * Act - do whatever the Blackhole wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        setRotation(getRotation()+Greenfoot.getRandomNumber(5)+1);  //constantly rotating
        fixImage(); //image is updated
        
        hit();  //checks if something has hit it
        
        if(removeMe)        //remove from world
            getWorld().removeObject(this);
    }    

    //adjusts image of black hole
    private void fixImage()
    {
        if(isFading)    //if image should be fading away
        {
            shrink();   //image will shrink
            if(transparency > 30)   //if transparency greater than 30, decrease transparency
                transparency -= (Greenfoot.getRandomNumber(20)+10);
            else
                removeMe = true;    //object should be removed from world
        }
        else
        {
            if(transparency <= 250) //if image is appearing
                transparency +=  (Greenfoot.getRandomNumber(5)+1);  //increase transparency
            else if(transparency > 250 && opacityCounter % 100 == 0)    //if transparency at max and waited 100 acts
            {
                isFading = true;    //black hole should start to fade
                opacityCounter = 1; //resets act counter
            }
            else
                opacityCounter++;   //another act has passed
        }
        getImage().setTransparency(transparency);   //set transparency
    }

    private void shrink()//shrinks black hole
    {
        sizeCounter++;  //counts number of acts it takes for object to shrink
        if(sizeCounter % 2 == 0)
        {
            sizeCounter = 0;
            int newWidth, newHeight;

            //sets new size of image
            newWidth = (int)(getImage().getWidth()*0.9);
            newHeight = (int)(getImage().getHeight()*0.9);

            if(newWidth > 20 && newHeight > 20) //if size is greater than 20, scale image
                getImage().scale(newWidth, newHeight);
            else    //if smaller than 20, scale image to 20 x 20, and object should be removed from world
            {
                getImage().scale(20,20);
                removeMe = true;
            }
        }
    }
}
