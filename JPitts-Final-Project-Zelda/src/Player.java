/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */


import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player 
{
    private double x, y;
    private int health;
    private final int maxHealth;
    private final ImageView playerImageView;
    private final Map<String, Image> animationImages = new HashMap<>();
    private String currentAnimation = "idle_down";
    private String previousAnimation = "idle_down";
    private int frameIndex = 0;
    private long lastUpdateTime = 0;
    private final int ANIMATION_SPEED = 150;
    static Rectangle attackHitbox;
    private boolean isAttacking = false;
    private long attackStartTime;
    private static final long ATTACK_DURATION = 300;
    private List<Rectangle> attackHitboxes = new ArrayList<>();
    private Room currentRoom;
    private Game game;

    private final Map<String, Integer> animationFrames = Map.of(
        "walk_down", 4,
        "walk_right", 4,
        "attack", 3,
        "idle_down", 1,
        "idle_right", 1
    );

    public Player(double startX, double startY, int maxHealth, Game game) 
    {
    	
        this.x = startX;
        this.y = startY;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.game = game;
        

        //initialize ImageView
        playerImageView = new ImageView();
        playerImageView.setStyle(
            "-fx-effect: dropshadow(three-pass-box, red, 10, 0.8);" +
            "-fx-border-color: lime;" +
            "-fx-border-width: 2px;"
        );
        playerImageView.setPreserveRatio(true);
        playerImageView.setX(x);
        playerImageView.setY(y);

        //initialize attack hitbox
        attackHitbox = new Rectangle(0, 0, 30, 20);
        attackHitbox.setFill(Color.TRANSPARENT);
        attackHitbox.setStroke(Color.RED);
        attackHitbox.setVisible(false);

        //load animations
        loadEnforcedImages();

        //set initial image
        if (!animationImages.isEmpty()) 
        {
            Image initialImage = animationImages.get(currentAnimation);
            if (initialImage != null) 
            {
                playerImageView.setImage(initialImage);
                playerImageView.setFitWidth(initialImage.getWidth());
                playerImageView.setFitHeight(initialImage.getHeight());
            }
        }

        //scene attachment listener
        playerImageView.sceneProperty().addListener((obs, oldScene, newScene) -> 
        {
            if (newScene == null) 
            {
                System.err.println("WARNING: Player detached from scene!");
            }
        });

        startAnimationLoop();
    }

    private void loadEnforcedImages() 
    {
        Image idle = enforceImageVisibility("/link.png");
        Image walkRight = enforceImageVisibility("/linkwalkright.png");
        Image attack = enforceImageVisibility("/linkattack.png");

        if (idle != null) 
        {
            animationImages.put("idle_down", idle);
            animationImages.put("walk_down", idle);
        }
        if (walkRight != null) 
        {
            animationImages.put("walk_right", walkRight);
            animationImages.put("idle_right", walkRight);
        }
        if (attack != null) 
        {
            animationImages.put("attack", attack);
        }
    }

    private Image enforceImageVisibility(String path) 
    {
        try (InputStream is = getClass().getResourceAsStream(path)) 
        {
            if (is != null) 
            {
                Image original = new Image(is);
                WritableImage visibleImage = new WritableImage(
                    (int)original.getWidth(), 
                    (int)original.getHeight()
                );

                PixelReader reader = original.getPixelReader();
                PixelWriter writer = visibleImage.getPixelWriter();

                for (int y = 0; y < visibleImage.getHeight(); y++) {
                    for (int x = 0; x < visibleImage.getWidth(); x++) 
                    {
                        Color c = reader.getColor(x, y);
                        writer.setColor(x, y, 
                            c.getOpacity() > 0.1 ? 
                            Color.color(c.getRed(), c.getGreen(), c.getBlue(), 1.0) :
                            Color.TRANSPARENT
                        );
                    }
                }
                return visibleImage;
            }
        } catch (Exception e) 
        {
            System.err.println("Error loading image: " + e.getMessage());
        }
        return createFallbackImage();
    }

    private Image createFallbackImage() 
    {
        WritableImage img = new WritableImage(16, 19);
        PixelWriter writer = img.getPixelWriter();
        for (int y = 0; y < 19; y++) {
            for (int x = 0; x < 16; x++) 
            {
                writer.setColor(x, y, Color.MAGENTA);
            }
        }
        return img;
    }
    
    

    public void move(double deltaX, double deltaY) {
        double newX = x + deltaX;
        double newY = y + deltaY;
        
        double leftBound = 0;
        double rightBound = 600 - playerImageView.getFitWidth();
        double topBound = 0;
        double bottomBound = 400 - playerImageView.getFitHeight();
        
        newX = Math.max(leftBound, Math.min(newX, rightBound));
        newY = Math.max(topBound, Math.min(newY, bottomBound));
        
        if (checkCollision(newX, newY)) {
            return; //if there's a collision, don't move the player
        }
        
        x = newX;
        y = newY;
        playerImageView.setX(x);
        playerImageView.setY(y);

        if (deltaX == 0 && deltaY == 0) 
        {
            returnToIdle();
        } 
        else if (Math.abs(deltaX) > Math.abs(deltaY)) 
        {
            if (deltaX > 0) 
            {
                requestAnimation("walk_right");
                playerImageView.setScaleX(1);
            } 
            else 
            {
                requestAnimation("walk_right");
                playerImageView.setScaleX(-1);
            }
        } 
        else 
        {
            requestAnimation(deltaY > 0 ? "walk_down" : "walk_down");
        }
    }
    
    private boolean checkCollision(double newX, double newY) 
    {
        if (currentRoom == null) 
        {
            System.out.println("Error: currentRoom is null!");
            return false;  
            
        }
        
        Bounds playerBounds = new Rectangle(newX, newY, playerImageView.getFitWidth(), playerImageView.getFitHeight()).getBoundsInParent();

        //check for collision with obstacles
        for (Rectangle obstacle : currentRoom.getObstacles()) 
        {
            if (playerBounds.intersects(obstacle.getBoundsInParent())) 
            {
                return true; //collision with obstacle
            }
        }

        // Check collision with key
        if (currentRoom.getKey() != null && currentRoom.getKey().isVisible()) 
        {
            if (playerBounds.intersects(currentRoom.getKey().getBoundsInParent())) 
            {
                System.out.println("Player picked up the key!");
                currentRoom.getKey().setVisible(false); //hide the key
                
            }
        }

        //check collision with heart
        if (currentRoom.getHeart() != null && currentRoom.getHeart().isVisible()) 
        {
            if (playerBounds.intersects(currentRoom.getHeart().getBoundsInParent())) 
            {
                System.out.println("Player collected a heart!");
                currentRoom.getHeart().setVisible(false); //hide the heart
                SoundManager.HEART_PICKUP.play();
                
            }
        }

        return false; //no blocking collision
    }

    public void attack() 
   
    {
        if (!isAttacking) 
        {
        	 isAttacking = true;
             attackStartTime = System.currentTimeMillis();
             requestAnimation("attack");
             SoundManager.ATTACK_SOUND.play();

             attackHitbox = new Rectangle(0, 0, 40, 30); 
             attackHitbox.setFill(Color.TRANSPARENT);
             attackHitbox.setStroke(Color.RED); 
             attackHitbox.setVisible(true);

             positionAttackHitbox();

             //safely operate only if currentRoom is not null
             if (currentRoom != null) {
                 currentRoom.addAttackHitbox(attackHitbox);
                 
                 Bounds attackBounds = attackHitbox.getBoundsInParent();

                 //check collision with enemies
                 for (Enemy enemy : currentRoom.getEnemies()) 
                 {
                     if (attackHitbox.getBoundsInParent().intersects(enemy.getBoundsInParent())) 
                     {
                         enemy.takeDamage(getAttackDamage());
                     }
                 }
                 
                 Boss boss = currentRoom.getBoss();
                 if (boss != null && boss.isAlive()) 
                 {
                     if (attackBounds.intersects(boss.getBounds())) 
                     {
                         boss.takeDamage(getAttackDamage());
                         System.out.println("Boss took damage!");
                     }
                 }
             } 
             else 
             {
                 System.out.println("Attack failed: currentRoom is null");
                 System.out.println("Attack called. CurrentRoom is: " + currentRoom);
             }
        }
    }
    
    private void positionAttackHitbox() 
    {
        if (attackHitbox != null) 
        {
            if (playerImageView.getScaleX() > 0) { //facing right
                attackHitbox.setX(x + playerImageView.getFitWidth() - 10);
            } else { //facing left
                attackHitbox.setX(x - attackHitbox.getWidth() + 10);
            }
            attackHitbox.setY(y + playerImageView.getFitHeight()/2 - attackHitbox.getHeight()/2);
        }
    }
    
    

    public void updateAttack() 
    {
        if (isAttacking) 
        {
            long currentTime = System.currentTimeMillis();
            if (currentTime - attackStartTime > ATTACK_DURATION) 
            {
                isAttacking = false;
                attackHitbox.setVisible(false);
                if (currentRoom != null) 
                {
                    currentRoom.removeAttackHitbox(attackHitbox);
                }
                returnToIdle();
            }
            updateAttackHitboxPosition();
        }
    }
    
    
    private void returnToIdle() 
    {
        switch (currentAnimation) 
        {
            case "walk_down": requestAnimation("idle_down"); break;
            case "walk_right": requestAnimation("idle_right"); break;
        }
    }

    private void requestAnimation(String newAnimation) 
    {
        if (currentAnimation.equals("attack") && 
            frameIndex < animationFrames.get("attack") - 1) 
        {
            return;
        }
        previousAnimation = currentAnimation;
        currentAnimation = newAnimation;
        frameIndex = 0;
        updateAnimationFrame();
    }

    private void startAnimationLoop() 
    {
        new AnimationTimer() 
        {
            @Override
            public void handle(long now) 
            {
                if (now - lastUpdateTime > ANIMATION_SPEED * 1_000_000) 
                {
                    updateAnimation();
                    lastUpdateTime = now;
                }
                updateAttack();
            }
        }.start();
    }

    private void updateAnimation() 
    {
        if (animationImages.isEmpty()) return;

        int frameCount = animationFrames.getOrDefault(currentAnimation, 1);
        frameIndex = (frameIndex + 1) % frameCount;

        if (currentAnimation.equals("attack") && 
            frameIndex == animationFrames.get("attack") - 1) {
            currentAnimation = previousAnimation;
            frameIndex = 0;
        }

        updateAnimationFrame();
    }

    private void updateAnimationFrame() 
    {
        Image currentImage = animationImages.get(currentAnimation);
        if (currentImage != null) {
            playerImageView.setImage(currentImage);
        }
    }
    
   

    public int getAttackDamage() 
    {
        return 20; 
    }
    
    public void updateAttackHitboxPosition() 
    {
        if (attackHitbox != null && isAttacking) 
        {
            if (playerImageView.getScaleX() > 0) 
            { //facing right
                attackHitbox.setX(x + playerImageView.getFitWidth());
            } 
            else 
            { //facing left
                attackHitbox.setX(x - attackHitbox.getWidth());
            }
            attackHitbox.setY(y + playerImageView.getFitHeight()/2 - attackHitbox.getHeight()/2);
        }
    }
    
    public Bounds getBounds() 
    {
        return playerImageView.getBoundsInParent();
    }

    
    public List<Rectangle> getAttackHitboxes() 
    {
        return attackHitboxes;
    }
    
    public ImageView getPlayerImageView() 
    { 
    	return playerImageView; 
    }
    public Rectangle getAttackHitbox() 
    { 
    	return attackHitbox; 
    }
    
    public boolean isAttacking() 
    { 
    	return isAttacking; 
    	
    }
    
    public long getAttackStartTime() 
    {
        return attackStartTime;
    }
    
    public void stopAttacking() 
    {
        isAttacking = false;
        attackHitbox = null;
    }
    
    public int getHealth() 
    { 
    	return health; 
    }
    
    public void setHealth(int health) 
    {
        this.health = Math.max(0, health); //prevent negative values
    }
    
    public void takeDamage(int damage) 
    { 
    	health = Math.max(0, health - damage); 
    	game.updateHearts();
    	
    	if (health == 0) 
    	{
            die();
        }
    }
    
    public void die() 
    {
    	 game.stopGameLoop(); 
    }
    
    public int heal(int amount) 
    {
        int oldHealth = health;
        health = Math.min(maxHealth, health + amount);
        game.updateHearts();
        return health - oldHealth;
        
    }
    public double getX() 
    { 
    	return x; 
    }
    public double getY() 
    { 
    	return y; 
    }
    public void setCurrentRoom(Room room) 
    { 
    	this.currentRoom = room; 
    }
    
    
    public Room getCurrentRoom() 
    { 
    	return currentRoom; 
    }

    public void ensureSceneAttachment() 
    {
        Platform.runLater(() -> 
        {
            if (playerImageView.getParent() != null) 
            {
                Pane parent = (Pane)playerImageView.getParent();
                parent.getChildren().remove(playerImageView);
                parent.getChildren().add(playerImageView);
                playerImageView.toFront();
            }
        });
    }
}