/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */

import javafx.scene.shape.Shape; 

public class Heart 
{
    //private instance variable for the shape of the heart
    private Shape shape; 
    
    //private instance variable to check if heart is collected
    private boolean collected; 

    //constructor for Heart class that takes a Shape object
    public Heart(Shape shape) 
    {
        //initialize shape with the passed in parameter
        this.shape = shape; 
        
        //initialize collected to false by default
        this.collected = false; 
    }

    //method to get the shape of the heart
    public Shape getShape() 
    {
        //return the shape
        return shape; 
    }

    //method to check if the heart is collected
    public boolean isCollected() 
    {
        //return the collected status
        return collected; 
    }

    //method to collect the heart and heal the player
    public void collect(Player player) 
    {
        //check if the heart is not already collected
        if (!collected) 
        {
            //mark the heart as collected
            this.collected = true; 
            
            //hide the heart shape
            shape.setVisible(false); 
            
            //heal the player by 25 points
            player.heal(25); 
        }
    }
}
