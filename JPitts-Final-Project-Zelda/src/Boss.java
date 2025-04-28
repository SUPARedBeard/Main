/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.InputStream;

//boss class that extends the base enemy class
public class Boss extends Enemy 
{
  //constant size for the boss image view
  private static final int BOSS_SIZE = 80;

  //image view to visually represent the boss
  private ImageView bossImageView;

  //shared static image for the boss to avoid reloading it multiple times
  private static Image bossImage;

  //rectangle used as the boss's hitbox
  private Rectangle bossHitbox;

  //flag to track whether the boss has been defeated
  private boolean isDefeated = false;

  //constructor for the boss with position and initial health
  public Boss(double startX, double startY, int initialHealth) 
  {
      //call superclass constructor
      super(startX, startY, initialHealth);

      //set boss's health
      this.health = initialHealth;

      //create and configure the boss's hitbox
      bossHitbox = new Rectangle(startX, startY, 50, 100);
      bossHitbox.setFill(Color.RED);

      //load the boss's image from resources
      loadBossImage();

      //if image was successfully loaded, create and configure the image view
      if (bossImage != null) 
      {
          bossImageView = new ImageView(bossImage);
          bossImageView.setFitWidth(BOSS_SIZE);
          bossImageView.setFitHeight(BOSS_SIZE);
          bossImageView.setX(startX);
          bossImageView.setY(startY);
      }

      //use reflection to assign the image view to the superclass field
      setBossImageView(bossImageView);
  }

  //helper method to load the boss's image from file
  private void loadBossImage() 
  {
      try (InputStream is = getClass().getResourceAsStream("/ganon.png")) 
      {
          //if image file is found, create the image
          if (is != null) 
          {
              bossImage = new Image(is);
              System.out.println("Successfully loaded Ganon image");
          } 
          //if image not found, print error
          else 
          {
              System.err.println("Could not find Ganon image resource");
          }
      } 
      //catch and report errors during image loading
      catch (Exception e) 
      {
          System.err.println("Error loading Ganon image: " + e.getMessage());
      }
  }

  //uses reflection to set the private enemyImageView field in the Enemy superclass
  private void setBossImageView(ImageView imageView) 
  {
      try 
      {
          java.lang.reflect.Field f = Enemy.class.getDeclaredField("enemyImageView");
          f.setAccessible(true);
          f.set(this, imageView);
      } 
      catch (Exception e) 
      {
          e.printStackTrace();
      }
  }

  //override to return the bounds of the boss for collision detection
  @Override
  public javafx.geometry.Bounds getBounds() 
  {
      //if the image view exists, use its bounds
      if (bossImageView != null) 
      {
          return bossImageView.getBoundsInParent();
      }

      //otherwise fall back to the base class's bounds
      return super.getBounds();
  }

  //getter for the boss's image view
  public ImageView getBossImageView() 
  {
      return bossImageView;
  }

  //getter for the boss's hitbox
  public Rectangle getBossHitbox() 
  {
      return bossHitbox;
  }

  //getter for the boss's current health
  public int getHealth() 
  {
      return health;
  }

  //method that applies damage to the boss
  @Override
  public void takeDamage(int damage) 
  {
      //ignore damage if boss is already defeated
      if (isDefeated) return;

      //reduce the boss's health
      this.health -= damage;
      System.out.println("Boss took damage! Current health: " + health);

      //if health drops to 0 or below, handle defeat
      if (this.health <= 0) 
      {
          this.health = 0;
          isDefeated = true;

          //hide the hitbox
          bossHitbox.setVisible(false);

          //play the death animation
          playDeathAnimation();

          //play sound effect for boss death
          SoundManager.BOSS_DEATH.play();

          //call the superclass's die method
          die();
      }
  }

  //returns whether the boss is defeated
  public boolean isDefeated() 
  {
      return isDefeated;
  }

  //animates the boss's disappearance when defeated
  private void playDeathAnimation() 
  {
      //create a fade out effect for the hitbox
      FadeTransition fade = new FadeTransition(Duration.seconds(1), bossHitbox);
      fade.setFromValue(1.0);
      fade.setToValue(0.0);

      //once the animation finishes, hide both hitbox and image
      fade.setOnFinished(e -> 
      {
          bossHitbox.setVisible(false);
          if (bossImageView != null) 
          {
              bossImageView.setVisible(false);
          }
      });

      //start the animation
      fade.play();
  }

  //sets the boss's position on screen
  @Override
  public void setPosition(double x, double y) 
  {
      //call the base class's method to handle position
      super.setPosition(x, y);

      //update the image view position if it exists
      if (bossImageView != null) 
      {
          bossImageView.setX(x);
          bossImageView.setY(y);
      }

      //update the hitbox position if it exists
      if (bossHitbox != null)
      {
          bossHitbox.setX(x);
          bossHitbox.setY(y);
      }
  }
}
