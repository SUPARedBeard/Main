/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.InputStream;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.util.Duration;

public class Enemy 
{
    //define constant for enemy size
    private static final int ENEMY_SIZE = 40;  
    
    //instance variables for enemy position and health
    protected double x; 
    protected double y; 
    protected int health; 
    
    //instance variables for enemy's visual representation
    protected Rectangle enemyRectangle; 
    private ImageView enemyImageView; 
    private Image enemyImage;
    
    //instance variables for attack cooldown and range
    private long lastAttackTime = 0; 
    private long attackCooldown = 10000; //10 seconds between enemy attacks
    private double attackRange = 40;  
    
    //instance variable to check if the enemy is active
    private boolean active = true;

    //constructor to initialize the enemy's position and health
    public Enemy(double startX, double startY, int initialHealth) 
    {
        this.x = startX;
        this.y = startY;
        this.health = initialHealth;
        
        //load enemy image
        loadEnemyImage();
        
        //set up visual representation of the enemy
        if (enemyImage != null) 
        {
            enemyImageView = new ImageView(enemyImage);
            enemyImageView.setFitWidth(ENEMY_SIZE);
            enemyImageView.setFitHeight(ENEMY_SIZE);
            enemyImageView.setX(x);
            enemyImageView.setY(y);
        } 
        else 
        {
            //fallback to a colored rectangle if image is not found
            enemyRectangle = new Rectangle(ENEMY_SIZE, ENEMY_SIZE, Color.BLUE);
            enemyRectangle.setX(x);
            enemyRectangle.setY(y);
        }
    }
    
    //method to load the enemy image from resources
    private void loadEnemyImage() 
    {
        try (InputStream is = getClass().getResourceAsStream("/skullkid.png")) 
        {
            if (is != null) 
            {
                enemyImage = new Image(is);
                //enemyImageView = new ImageView(enemyImage);
                System.out.println("Successfully loaded Skull Kid image");
            } 
            else 
            {
                System.err.println("Could not find Skull Kid image resource");
            }
        }
        catch (Exception e) 
        {
            System.err.println("Error loading Skull Kid image: " + e.getMessage());
        }
    }
    
    //method to set the position of the enemy
    public void setPosition(double x, double y) 
    {
        this.x = x;
        this.y = y;
        enemyImageView.setX(x);
        enemyImageView.setY(y);
        
        //update rectangle position if fallback to rectangle
        if (enemyRectangle != null) 
        {
            enemyRectangle.setX(x);
            enemyRectangle.setY(y);
        }
        
        //update image position if image view exists
        if (enemyImageView != null) 
        {
            enemyImageView.setX(x);
            enemyImageView.setY(y);
        }
    }

    //method to get the bounds of the enemy for collision detection
    public javafx.geometry.Bounds getBounds() 
    {
        //check if the enemy is a Boss and return its bounds
        if (this instanceof Boss) 
        {
            Boss boss = (Boss) this; 
            if (boss.getBossImageView() != null) 
            {
                return boss.getBossImageView().getBoundsInParent();
            }
        }
        
        //return the bounds for enemy image view or rectangle
        if (enemyImageView != null) 
        {
            return enemyImageView.getBoundsInParent();
        } 
        else if (enemyRectangle != null) 
        {
            return enemyRectangle.getBoundsInParent();
        }
        return null;
    }

    //method to get the image view of the enemy
    public ImageView getEnemyImageView() 
    {
        return enemyImageView;
    }

    //method to move the enemy by a certain delta
    public void move(double deltaX, double deltaY) 
    {
        if (!active) return;
        
        x += deltaX;
        y += deltaY;
        
        setPosition(x, y);
    }
    
    //method to set the enemy as active or inactive
    public void setActive(boolean active) 
    {
        this.active = active;
    }
    
    //method to check if the enemy is active
    public boolean isActive() 
    {
        return active;
    }
    
    //method to get the bounds of the enemy's image
    public Bounds getBoundsInParent() 
    {
        return enemyImageView.getBoundsInParent();
    }

    //method to get the health of the enemy
    public int getHealth() 
    { 
        return health; 
    }
    
    //method to get the hitbox of the enemy for collision detection
    public Bounds getHitBox() 
    {
        return enemyImageView.getBoundsInParent();
    }

    //method to apply damage to the enemy
    public void takeDamage(int damage) 
    {
        health -= damage;

        //check if the enemy is a boss and apply visual feedback
        if (this instanceof Boss) 
        {
            Boss boss = (Boss) this;
            if (boss.getBossImageView() != null) 
            {
                //apply opacity effect to boss image view when taking damage
                boss.getBossImageView().setOpacity(0.7);
                PauseTransition pause = new PauseTransition(Duration.millis(200));
                pause.setOnFinished(e -> boss.getBossImageView().setOpacity(1.0));
                pause.play();
            }
        } 
        else 
        {
            //apply opacity effect to regular enemy image view when taking damage
            if (enemyImageView != null) 
            {
                enemyImageView.setOpacity(0.7);
                PauseTransition pause = new PauseTransition(Duration.millis(200));
                pause.setOnFinished(e -> enemyImageView.setOpacity(1.0));
                pause.play();
            }
        }
        
        //check if health reaches 0, and handle enemy death
        if (health <= 0)
        {
            SoundManager.ENEMY_DEATH.play();
            die();
        }
    }
    
    //method to check if the enemy can attack the player
    public boolean canAttack(Player player) 
    {
        long now = System.currentTimeMillis();

        double dx = player.getPlayerImageView().getX() - getEnemyImageView().getX();
        double dy = player.getPlayerImageView().getY() - getEnemyImageView().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < attackRange && (now - lastAttackTime) > attackCooldown) 
        {
            lastAttackTime = now;
            return true;
        }
        return false;
    }
    
    //method to handle the enemy's death
    public void die() 
    {
        if (enemyImageView != null) 
        {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), enemyImageView);
            fade.setToValue(0);
            fade.setOnFinished(e -> enemyImageView.setVisible(false)); 
            fade.play();
        }
    }

    //method to check if the enemy is still alive
    public boolean isAlive() 
    {
        return health > 0;
    }
}