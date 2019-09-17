import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Scorebar that displays the population of humans and aliens on each planet.
 * <p>The planet numbers are determined by their distance from the score bar.
 * 
 * @author Catherine Lee 
 * @version April 2018
 */
public class Scorebar extends Actor
{
    //stores the planets
    private ArrayList<TargetPlanet> planets;
    private TargetPlanet[] planet = new TargetPlanet[5];
    //stores the distance of the planets from the score bar
    private int[] distance = new int[5];
    private int[] sortedDistance = new int[5];//array of the distances sorted from least to greatest
    //variables used to draw the score bar
    private GreenfootImage scorebar;
    private Color barColor;
    private Color fontColor;
    private Font barFont;
    //stores each planet's variables
    private String[] planetName = new String[5];//stores name for each planet
    private String[] humanPop = new String[5];//stores the human population of each planet
    private String[] alienPop = new String[5];//stores the alien population of each planet
    //stores the y location used to align the scorebar
    private int[] yAlign = new int[]{10,210,410,610,810};
    //stores the colors of the words on the score bar
    private Color alienColor = new Color (234,123,231);//color if planet is conquered by aliens
    private Color humanColor = new Color (123,234,231);//color if planet is conquered by humans

    private int actCounter = 0;//counts the number of acts

    /**
     * Creates a scorebar of width 960 and height 65. The bar is gray, and the font is white.
     */
    public Scorebar()
    {
        scorebar = new GreenfootImage (960, 65);//sets size of score bar
        barColor = new Color (50,50,50);//sets color of the bar
        fontColor = new Color (255,255,255);//sets font color
        barFont = new Font ("Arial", Font.BOLD,20);//sets font
        scorebar.setColor(barColor);//use bar color
        scorebar.fill();//fills the image with the selected color
        scorebar.setFont(barFont);//sets the font to the bar font
        this.setImage(scorebar);//sets the score bar iamge

    }

    /**
     * Act - do whatever the Scorebar wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        actCounter++;//increases the act counter by 1 each act
        //if it's the first act, find planets
        //this makes it so Scorebar only needs to find the planets once and prevents the simulation from crashing
        //since the planets will already be in the world and won't cause problems if a blackhole removed a planet
        if(actCounter==1)
        {
            findPlanets();
        }

        update();//updates the variables in the score bar
    }

    /**
     * Updates variables in scorebar.
     */
    private void update()
    {
        scorebar.setColor(barColor);//sets the color to the bar color
        scorebar.fill();//fills the image with the selected color

        planetNames();//updates the planet names
        updateHumanPop();//updates the human population on each planet
        updateAlienPop();//updates the alien population on each planet

        this.setImage(scorebar); //sets the image      
    }

    /**
     * Updates the name of all the planets.
     */
    private void planetNames()
    {
        for (int i = 0; i < 5; i++)
        {
            //sets the font color to the alien color if the planet state is alien
            if(planet[i].getState()=='p')
            {
                scorebar.setColor(alienColor);
            }
            //sets the font color to the human color if the planet state is human
            else if(planet[i].getState()=='b')
            {
                scorebar.setColor(humanColor);
            }
            //sets the font color to white (neutral) if the state of the planet isn't alien or human
            else 
            {
                scorebar.setColor(fontColor);
            }
            planetName[i] = "Planet " + (i+1);//sets the planet name to the planet and number
            //draws the name on the specified location of the scorebar
            scorebar.drawString(planetName[i], yAlign[i], 20);
        }
    }

    /**
     * Updates the alien popultion of all the planets.
     */
    private void updateAlienPop()
    {
        for (int i = 0; i < 5; i++)
        {
            //sets the font color to the alien color if the planet state is alien
            if(planet[i].getState()=='p')
            {
                scorebar.setColor(alienColor);
            }
            //sets the font color to the human color if the planet state is human
            else if(planet[i].getState()=='b')
            {
                scorebar.setColor(humanColor);
            }
            //sets the font color to white (neutral) if the state of the planet isn't alien or human
            else 
            {
                scorebar.setColor(fontColor);
            }
            alienPop[i] = "Aliens:       " + planet[i].getPopulation(false);//creates the string for the score bar
            int center = yAlign[i];//centers the string in the appropriate spot
            //decreases the space between the numbers and "Aliens:" by 1 if the alien population is less than 10
            if(planet[i].getPopulation(false)<10)
            {
                alienPop[i] = "Aliens:        " + planet[i].getPopulation(false);
            }
            //draws the alien population on the specified location of the scorebar
            scorebar.drawString(alienPop[i], center, 40);
        }
    }

    /**
     * Updates the human population of all the planets.
     */
    private void updateHumanPop()
    {
        for (int i = 0; i < 5; i++)
        {
            //sets the font color to the alien color if the planet state is alien
            if(planet[i].getState()=='p')
            {
                scorebar.setColor(alienColor);
            }
            //sets the font color to the human color if the planet state is human
            else if(planet[i].getState()=='b')
            {
                scorebar.setColor(humanColor);
            }
            //sets the font color to white (neutral) if the state of the planet isn't alien or human
            else 
            {
                scorebar.setColor(fontColor);
            }
            //creates the human population string for the score bar
            humanPop[i] = "Humans:    " + planet[i].getPopulation(true);
            int center = yAlign[i];//sets the y coordinate of the string according to the planet number
            //decrease the space between "humans:" and the number of humans by one if the population is less than 10
            if(planet[i].getPopulation(true)<10)
            {
                humanPop[i] = "Humans:     " + planet[i].getPopulation(true);
            }
            //draws the human population at the specified population
            scorebar.drawString(humanPop[i], center, 60);
        }
    }

    /**
     * Finds all the planets and orders the planets 1 to 5 according to their location away from the score bar.
     */
    private void findPlanets()
    {
        //finds all target planets within a 600 pixel range
        planets = (ArrayList)getObjectsInRange(600,TargetPlanet.class);
        //calculates distances from all 5 planets to the score bar
        for(int i = 0; i<5; i++)
        {
            distance[i] = (int)SpaceWorld.getDistance(this,planets.get(i));
        }

        sortedDistance = distance.clone();//clones the array of distances from the planet to the scorebar
        Arrays.sort(sortedDistance);//sorts all the cloned distances from least to greatest

        //sets the target planet array to the planets found within the range in order of least to greatest distance
        for(int i = 0;i<5;i++)
        {
            for(int j = 0; j<5; j++)
            {
                if (distance[i]==sortedDistance[j])
                {
                    planet[j] = planets.get(i);
                }
            }
        }
    }
}

