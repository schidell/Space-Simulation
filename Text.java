import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
/**
 * Displays text in world.
 * 
 * @author Maggie Lin
 * @version April 2, 2018
 */
public class Text extends Actor
{
    private GreenfootImage t;
    
    /**
     * Creates text with a specified color and specified background with Arial Bold font.
     * 
     * @param  text text to display
     * @param  size of text
     */
    public Text(String text, int size, Color fontColor, Color backColor)
    {
        t = new GreenfootImage(text, size, fontColor, backColor);
        t.setFont(new Font("Arial", Font.BOLD, size));
        setImage(t);    //set text image
    }
    
    /**
     * Creates text with white letters and transparent background with Arial Bold font.
     * 
     * @param text  text to display
     * @param size  size of text
     */
    public Text(String text, int size)
    {
        this(text,size,Color.WHITE, new Color(0,0,0,0));
    }
}
