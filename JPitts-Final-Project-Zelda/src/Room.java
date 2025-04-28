/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */


import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Room 
{
    private int width, height;
    private Pane roomPane;
    private Pane gamePane;
    private List<Enemy> enemies;
    private boolean bossRoom;
    private Boss boss;
    private boolean hasKey;
    private boolean hasHeart;
    private boolean doorUnlocked = false;
    private Rectangle door;
    private ImageView heart;
    private boolean heartCollected;
    private ImageView key;
    private boolean keyCollected;
    private ImageView backgroundImageView;
    private static Image roomBackgroundImage;
    private Player currentPlayer;
    private List<Rectangle> activeAttackHitboxes = new ArrayList<>();
    private List<Rectangle> obstacles = new ArrayList<>();

    public Room(int width, int height, boolean bossRoom, boolean hasKey, boolean hasHeart, Pane sharedPane) 
    {
    	
    	
        this.width = width;
        this.height = height;
        this.gamePane = sharedPane;
        this.roomPane = new Pane();
        this.roomPane.setPrefSize(width, height);
        this.enemies = new ArrayList<>();
        this.bossRoom = bossRoom;
        this.hasKey = hasKey;
        this.hasHeart = hasHeart;
        this.heartCollected = false;
        this.keyCollected = false;
        
        initializeBackground();
        initializeRoomContents();
    }

    private void initializeBackground()
    {
        // Load background image
        if (roomBackgroundImage == null) {
            roomBackgroundImage = loadBackgroundImage(width, height);
        }

        // Setup background ImageView
        backgroundImageView = new ImageView(roomBackgroundImage);
        backgroundImageView.setFitWidth(width);
        backgroundImageView.setFitHeight(height);
        roomPane.getChildren().add(backgroundImageView);
    }

    private void initializeRoomContents()
    {
    // Room-specific setup
        if (hasHeart) 
        {
          
            Image heartImg = new Image("heart.png");
            heart = new ImageView(heartImg);
            heart.setX(100); // adjust as needed
            heart.setY(200);
            roomPane.getChildren().add(heart);
        } 
        
        if (hasKey) 
        {
            Image keyImg = new Image("key.png");
            key = new ImageView(keyImg);
            key.setX(367); // adjust as needed
            key.setY(200);
            roomPane.getChildren().add(key);
        }
        if (!bossRoom) 
        {
            createDoor();
        }
    }
    
    
    public int getEnemyCount() 
    {
        return enemies != null ? enemies.size() : 0;
    }
    
   
    public int getPaneChildrenCount() 
    {
        return roomPane != null ? roomPane.getChildren().size() : 0;
    }
    
    
    public void activate() 
    {
        if (!gamePane.getChildren().contains(roomPane)) {
            gamePane.getChildren().add(roomPane);
        }
    }

    
    public void deactivate() 
    {
        
    	gamePane.getChildren().remove(roomPane);
    }
    
   
        
    public void setPlayer(Player player) 
    {
        this.currentPlayer = player;
        if (!roomPane.getChildren().contains(player.getPlayerImageView())) 
        {
            roomPane.getChildren().add(player.getPlayerImageView());
            player.getPlayerImageView().toFront();
        }
    }

    private Image loadBackgroundImage(int width, int height) 
    {
        // Try loading from resources
        String resourcePath = "/background.png";
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        
        if (stream != null) {
            try {
                return new Image(stream);
            } catch (Exception e) {
                System.err.println("Resource loading error: " + e.getMessage());
            }
        }

        // Try loading from file system
        String filePath = "resources/background.png";
        File file = new File(filePath);
        
        if (file.exists()) {
            try {
                return new Image(file.toURI().toString());
            } catch (Exception e) {
                System.err.println("File loading error: " + e.getMessage());
            }
        }

        // Fallback pattern
        return createFallbackImage(width, height);
    }
    
    public List<Rectangle> getObstacles() 
    {
        return obstacles;
    }

    public void addObstacle(Rectangle obstacle) 
    {
        obstacles.add(obstacle);
    }

   

    

    public void setKey(Node key) 
    {
        this.key = (ImageView) key;
    }

    

    public void setHeart(Node heart) 
    {
        this.heart = (ImageView) heart;
    }
    
    public void addAttackHitbox(Rectangle hitbox) 
    {
    	if (!roomPane.getChildren().contains(hitbox)) 
    	{
            roomPane.getChildren().add(hitbox);
            hitbox.toFront();
        }
    	if (!activeAttackHitboxes.contains(hitbox)) 
    	{
    	    activeAttackHitboxes.add(hitbox);
    	}
    }

    public void removeAttackHitbox(Rectangle hitbox)
    {
        activeAttackHitboxes.remove(hitbox);
        roomPane.getChildren().remove(hitbox);
    }

    private Image createFallbackImage(int width, int height) 
    {
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        
        // Create checkerboard pattern
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = ((x + y) % 40 < 20) ? Color.DARKGRAY : Color.GRAY;
                writer.setColor(x, y, color);
            }
        }
        return image;
    }

    public Pane getRoomPane() 
    {
        return this.roomPane;
    }


    public ImageView getKey() 
    {
        return key;
    }
    
    public boolean isKeyCollected() {
        return keyCollected;
    }

    public boolean collectKey() 
    {
        if (!keyCollected) 
        {
            keyCollected = true;
            roomPane.getChildren().remove(key);
            return true;
        }
        return false;
    }

    private void createDoor() 
    {
        door = new Rectangle(width / 2 - 25, 20, 50, 10);
        door.setFill(Color.BROWN);
        roomPane.getChildren().add(door);
    }

   

    public boolean isDoorUnlocked() 
    {
        return doorUnlocked;
    }
    
    public void setDoorUnlocked(boolean doorUnlocked) 
    {
        this.doorUnlocked = doorUnlocked;
    }
    
    public boolean collectHeart() 
    {
        if (!heartCollected && heart != null) 
        {
            roomPane.getChildren().remove(heart);
            heartCollected = true;
            return true;
        }
        return false;
    }

    public ImageView getHeart() 
    {
        return heart;
    }

    public boolean isHeartCollected() {
        return heartCollected;
    }

    public boolean hasKey() 
    {
        return hasKey;
    }

    public Rectangle getDoor() {
        return door;
    }
    public Pane getRoot() {
        return roomPane;
    }

    public void addEntityToRoom(Object entity) 
    {
    	
    	if (entity == null) 
    	{
            System.err.println("Attempted to add null entity to room");
            return;
        }

        try 
        {
            if (entity instanceof Player) 
            {
                addPlayerToRoom((Player)entity);
            } 
            else if (entity instanceof Enemy) 
            {
                addEnemyToRoom((Enemy)entity);
            } 
            else 
            {
                System.err.println("Unknown entity type: " + entity.getClass().getName());
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Error adding entity to room: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addPlayerToRoom(Player player) {
        if (player == null || player.getPlayerImageView() == null) {
            System.err.println("Invalid player or player image view");
            return;
        }

        ImageView playerView = player.getPlayerImageView();
        if (!roomPane.getChildren().contains(playerView)) {
            roomPane.getChildren().add(playerView);
            currentPlayer = player; // Update current player reference
        }
        playerView.toFront(); // Ensure player is always on top
        logZOrder("Player added", playerView);
    }
    
    private void addEnemyToRoom(Enemy enemy) 
    {
        if (enemy == null) 
        {
            System.err.println("Attempted to add null enemy");
            return;
        }

        Node enemyVisual = getEnemyVisualRepresentation(enemy);
        if (enemyVisual == null) 
        {
            System.err.println("No visual representation found for enemy");
            return;
        }

        if (!roomPane.getChildren().contains(enemyVisual)) 
        {
            
            enemies.add(enemy);
            
            if (enemy instanceof Boss) 
            {
            	roomPane.getChildren().add(0, enemyVisual);
            }
            else
            {
            	roomPane.getChildren().add(enemyVisual);
            }
            
            logZOrder("Enemy added", enemyVisual);
        }

        // Debug output
        System.out.println("Pane reference: " + roomPane + " hash: " + roomPane.hashCode());
        System.out.println("Total children: " + roomPane.getChildren().size());
        System.out.println("Enemy added to room: " + this);

    }
    
    private Node getEnemyVisualRepresentation(Enemy enemy) 
    {
        if (enemy instanceof Boss) 
        {
            Boss boss = (Boss)enemy;
            return boss.getBossImageView();
        } 
        else 
        {
        	return enemy.getEnemyImageView();
        }
    }
    
    private void logZOrder(String context, Node node) {
        System.out.println("\n=== " + context + " ===");
        System.out.println("Node: " + node);
        System.out.println("Z-Index: " + roomPane.getChildren().indexOf(node));
        System.out.println("Parent: " + node.getParent());
        System.out.println("In Scene: " + (node.getScene() != null));
        
        if (currentPlayer != null && currentPlayer.getPlayerImageView() != null) {
            System.out.println("Player Z: " + 
                roomPane.getChildren().indexOf(currentPlayer.getPlayerImageView()));
        }
    }

    public List<Enemy> getEnemies() 
    {
        return this.enemies;
    }

    public boolean isBossRoom() 
    {
        return bossRoom;
    }
    
    public void setBoss(Boss boss) 
    {
        this.boss = boss;
    }

    public Boss getBoss() 
    {
        return boss;
    }
    
    public void checkAttackCollisions() 
    {
        if (currentPlayer == null || !currentPlayer.isAttacking()) return;
        
        Rectangle attackHitbox = currentPlayer.getAttackHitbox();
        
        // Check against enemies
        for (Enemy enemy : enemies) 
        {
        	 Bounds attackBounds = attackHitbox.getBoundsInParent();
        	 Bounds enemyBounds = enemy.getEnemyImageView().getBoundsInParent();
        	
        	 if (attackBounds.intersects(enemyBounds)) {
        	        // Handle the collision, e.g., deal damage
        	        enemy.takeDamage(currentPlayer.getAttackDamage());
            }
        }
        
        //check against boss if this is a boss room
        if (bossRoom && boss != null) 
        {
        	
        	Bounds bossBounds = boss.getBossHitbox().getBoundsInParent();

            if (attackHitbox.getBoundsInParent().intersects(bossBounds)) 
            {
                boss.takeDamage(currentPlayer.getAttackDamage());
                System.out.println("HIT! Boss was struck by player.");
            }
        }
    }

    public void update() 
    {
    	
    	 // Unlock the door if key was collected and door isn't yet unlocked
        if (keyCollected && !doorUnlocked) 
        {
            doorUnlocked = true;
            
        }
    	
        
        if (currentPlayer != null && currentPlayer.isAttacking()) 
        {
            long attackDuration = System.currentTimeMillis() - currentPlayer.getAttackStartTime();
            if (attackDuration > 300) 
            { 
                currentPlayer.stopAttacking(); 
                removeAttackHitbox(currentPlayer.getAttackHitbox());
            }
        }
        
        
        //remove expired attack hitboxes
        List<Rectangle> toRemove = new ArrayList<>();
        for (Rectangle hitbox : activeAttackHitboxes) {
            if (!currentPlayer.getAttackHitboxes().contains(hitbox)) {
                toRemove.add(hitbox);
            }
        }
        toRemove.forEach(this::removeAttackHitbox);
    }
}