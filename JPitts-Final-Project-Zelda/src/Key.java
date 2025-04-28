/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Key 
{

    //private instance variable for the shape of the key
    private Shape shape;
    
    //private instance variable to track if the key is collected
    private boolean collected;

    //constructor to initialize the key with a given shape
    public Key(Shape shape)
    {
        this.shape = shape;
        this.collected = false;
    }

    //constructor to initialize the key with position (x, y) and set its visual appearance
    public Key(double x, double y) 
    {
        shape = new Rectangle(x, y, 30, 10);
        shape.setFill(Color.GOLD); //set the fill color of the rectangle to gold
        collected = false;
    }

    //method to get the shape of the key
    public Shape getShape() 
    {
        return shape;
    }

    //method to check if the key has been collected
    public boolean isCollected() 
    {
        return collected;
    }

    //method to collect the key and make it invisible
    public void collect() 
    {
        this.collected = true; //mark the key as collected
        shape.setVisible(false); //make the shape invisible
        // Unlock door, play sound, etc. (can be expanded with game-specific logic)
    }
}
