import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.awt.Color;

/**
 * A Greenfoot Actor object for creating a simple explosion graphic - basically
 * an expanding circle that changes from fire-red to yellow while also going from
 * opaque to transparent.
 * 
 * @author Jordan Cohen (Twitter: @mrjcohen)
 * @version April 2013
 */
public class Explosion extends Actor
{
    private GreenfootImage fireImage;

    private Color currentColor;
    private static Color backColor;

    private int radius;
    private int speed;
    private int steps;
    private int red, green, blue;
    private int transparency;
    private int maxSize;
    private int damage;
    private int removeDelay;
    private boolean huge;

    /**
     * Constructor for Explosion. MaxSize is the radius.
     * 
     * @param maxSize   The maximum size to which the explosion should grow. Larger
     *                  sizes will not affect the speed at which the explosion grows
     *                  and eventually disappears.
     */
    public Explosion (int maxSize)
    {
        huge = false;
        // Create image to manage this graphic
        fireImage = new GreenfootImage (maxSize, maxSize);
        // Variables that control Colors for fire effect:
        red = 255;
        green = 40;
        blue = 1;
        // Start as fully opaque
        transparency = 255;

        // Set starting Color
        currentColor = new Color (red, green, blue);
        backColor = new Color (255,244,40,128);
        // Set starting Radius
        radius = 2;

        // Duration of Animation
        steps = 24;

        speed = (maxSize - radius) / steps;

        // Store maxSize (from parameter) in instance variable for
        // future user
        this.maxSize = maxSize;

        fireImage.setColor(backColor);
        fireImage.fillOval(0, 0, maxSize, maxSize);
        // Method to actually draw the circle
        redraw();
        // Set this Actor's graphic as the image I just created
        this.setImage(fireImage);

        // Amount of time this image will stay on screen after finishing explosion
        removeDelay = 10;
    }

    /**
     * Calling this method will make an explosion with lots of mini explosions
     * inside it. This can be taxing on performance and should be used sparingly.
     * 
     * @param maxSize The size of the main explosion
     * @param huge Value doesn't matter - just signals to use BIG explosion
     */
    public Explosion (int maxSize, boolean huge)
    {
        this(maxSize);
        this.huge = true;
    }

    /**
     * Act method gets called by Greenfoot every frame. In this class, this method
     * will serve to increase the size each act until maxSize is reached, at which
     * point the object will remove itself from existence.
     */
    public void act() 
    {
        redraw();

        if (huge)
        {
            World w = getWorld();
            int miniExplosionMaxOffset = (int)((double)maxSize * 0.75);
            int half = miniExplosionMaxOffset / 2;
            int miniExplosionMaxSize = (int)Math.abs(((double)maxSize * 0.3));

            for (int i = 0; i < 45; i++)
            {
                int xOffset = Greenfoot.getRandomNumber(miniExplosionMaxOffset) - half;
                int yOffset = Greenfoot.getRandomNumber(miniExplosionMaxOffset) - half;

                int size = Greenfoot.getRandomNumber(miniExplosionMaxSize) + 30;
                if (size > 0)
                    w.addObject(new Explosion (size), getX() - xOffset, getY() - yOffset);
            }
            huge = false;
        }
        else
        {
            if (radius + speed <= maxSize)
                radius += speed;
            else
            {
                radius = maxSize;
                removeDelay--;
                if (removeDelay == 0)
                {
                    getWorld().removeObject(this);
                }
            }
        }
    }    

    /**
     * redraw() method is a private method called by this object each act
     * in order to redraw the graphic.
     */
    private void redraw()
    {

        // adjust colors
        green += (150 / steps);
        blue += (10 / steps);
        // reduce transparency, but ensure it doesn't fall below zero - that would cause
        // a crash
        //         if (transparency - (255 / (steps/2)) > 0)
        //             transparency -= (255 / (steps/2));
        //         else
        //             transparency = 0;
        transparency -= 3;

        if (green > 255)
            green = 255;
        // update Color
        currentColor = new Color (red, green, blue);

        // update transparency
        fireImage.setTransparency(transparency);
        fireImage.setColor (currentColor);
        // redraw image
        fireImage.fillOval ((maxSize - radius)/2, (maxSize - radius)/2, radius, radius);
    }
}
