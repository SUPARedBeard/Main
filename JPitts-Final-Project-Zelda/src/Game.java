/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */


import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Game 
{
    protected Player player;
    protected Room[] rooms;
    protected int currentRoomIndex;
    protected int keysCollected;
    protected boolean bossDefeated;
    protected Room currentRoom;
    private final Pane gamePane;
    private AnimationTimer gameLoop;
    public Music music;
    private boolean gameOver = false;
   
    
    
    
    private HBox heartContainer = new HBox(5); //spacing between hearts
    private Image heartFull = new Image(getClass().getResource("/heart.png").toExternalForm());
    
    
    private boolean transitioningRoom = false;
    
    
    private static final int DUNGEON_WIDTH = 600;
    private static final int DUNGEON_HEIGHT = 400;

    public Game(Pane gamePane) 
    {
    	
    	
    	 if (gamePane == null) {
    	        throw new IllegalArgumentException("Game pane cannot be null");
    	    }
        this.gamePane = gamePane;
        this.gamePane.setPrefSize(DUNGEON_WIDTH, DUNGEON_HEIGHT);
        rooms = new Room[3];
        currentRoomIndex = 0;
        keysCollected = 0;
        bossDefeated = false;
        
        gamePane.setStyle("-fx-background-color: red;");
        
        initializeGame();              
        
        
        changeRoom(0);
        
        
        player.getPlayerImageView().toFront();
        
     
        
        System.out.println("\n=== PANE INITIALIZATION ===");
        System.out.println("Main rootPane hash: " + gamePane.hashCode());
        
       
        
    }
    
    private void initializeHealthUI(Pane gamePane) 
    {
    	heartContainer.setLayoutX(10);
        heartContainer.setLayoutY(10);

        if (!gamePane.getChildren().contains(heartContainer)) 
        {
            gamePane.getChildren().add(heartContainer);
        }

        updateHearts();  // Initial draw
    }
    
    public void updateHearts()
    {
        heartContainer.getChildren().clear();
        int currentHealth = player.getHealth();
        int heartsToShow = currentHealth / 25;

        for (int i = 0; i < heartsToShow; i++) 
        {
            ImageView heart = new ImageView(heartFull);
            heart.setFitWidth(32); // resize as needed
            heart.setFitHeight(32);
            heartContainer.getChildren().add(heart);
        }
    }
    
    public void initializeGame() 
    {   	
        //create player
        this.player = new Player(100, 100, 100, this);
        
        
            
        this.rooms = new Room[3];    
        
        initializeRooms();
        
       
        
        initializeHealthUI(gamePane); 
        
        
        startGameLoop();
    }
    
    public void stopBackgroundMusic() 
    {
        // Stop the music when needed
        if (music != null) {
            music.stop();
        }
    }
    
    public void gameOver() 
    {
        stopBackgroundMusic();
    }
    
    
    public Pane getGamePane() 
    {
        return gamePane;
    }
    
        
        
    private void initializeRooms()
    {
        //create rooms (first two have keys, last is boss room)
        rooms[0] = new Room(600, 400, false, true, false, gamePane); //normal room with key
        addSkullKidsToRoom(rooms[0], 2); //add 2 Skull Kids
        System.out.println("Room 0 initialized with pane: " + rooms[0].getRoomPane().hashCode());
        
        rooms[1] = new Room(600, 400, false, true, true, gamePane); //normal room with key
        System.out.println("Adding SkullKid to room1");
        addSkullKidsToRoom(rooms[1], 4); //add 4 Skull Kids
        System.out.println("Room 1 initialized with pane: " + rooms[1].getRoomPane().hashCode());
        
        rooms[2] = new Room(600, 400, true, false, true, gamePane); //boss room without key        
        addGanonToRoom(rooms[2]); //add Ganon boss
        System.out.println("Room 2 initialized with pane: " + rooms[2].getRoomPane().hashCode());
        
        
        setCurrentRoom(1);
    }

  
    
    public void changeRoom(int roomIndex) 
    {
    	    	
        if (roomIndex >= 0 && roomIndex < rooms.length) 
        {
        	
        	cleanupRoom(currentRoom);
        	
            this.currentRoomIndex = roomIndex;
            this.currentRoom = rooms[roomIndex];
            System.out.println("Changed to room: " + roomIndex);
            System.out.println("Current room pane: " + currentRoom.getRoomPane().hashCode());
            
            for (Enemy e : currentRoom.getEnemies()) 
            {
                e.setActive(true);
            }

            Boss boss = currentRoom.getBoss();
            if (boss != null) 
            {
                boss.setActive(true);
            }
            
        } 
        else 
        {
            System.out.println("Invalid room index: " + roomIndex);
            return;
        }
        
        

        // Clear the current game pane
        gamePane.getChildren().clear();

        // Add the current room's pane
        gamePane.getChildren().add(currentRoom.getRoomPane());
        
        
     // Add player to the room AND to the gamePane
        currentRoom.addEntityToRoom(player);
        if (!gamePane.getChildren().contains(player.getPlayerImageView())) 
        {
            gamePane.getChildren().add(player.getPlayerImageView());
        }

        // Add heart container AFTER player and room pane
        if (!gamePane.getChildren().contains(heartContainer)) 
        {
            gamePane.getChildren().add(heartContainer);
        }
        heartContainer.toFront();

        
        // Same for boss, if in boss room
        Boss boss = currentRoom.getBoss();
        if (boss != null) 
        {
            // First remove any previous boss image if it's already in the gamePane
            if (gamePane.getChildren().contains(boss.getEnemyImageView())) 
            {
                gamePane.getChildren().remove(boss.getEnemyImageView());
            }

            // Now, add the boss image view if not already in the gamePane
            if (!gamePane.getChildren().contains(boss.getEnemyImageView())) 
            {
                gamePane.getChildren().add(boss.getEnemyImageView());
            }
        }
        updateHearts();

        // Bring player to front
        heartContainer.toFront();
        player.getPlayerImageView().toFront();
        player.setCurrentRoom(currentRoom);
    }
    
    private void cleanupRoom(Room room) {
        if (room == null) return;

        // Remove enemies from gamePane
        for (Enemy e : room.getEnemies()) 
        {
        	e.setActive(false);
            gamePane.getChildren().remove(e.getEnemyImageView());
        }

        // Remove boss if present
        Boss boss = room.getBoss();
        if (boss != null) 
        {
        	boss.setActive(false);
            gamePane.getChildren().remove(boss.getEnemyImageView());
        }

        //clear any leftover attack effects
        room.getRoomPane().getChildren().removeIf(node -> 
            node instanceof javafx.scene.shape.Rectangle && ((Rectangle) node).getFill().equals(Color.RED)
        );
    }

    
    private void addSkullKidsToRoom(Room room, int count) 
    {
        for (int i = 0; i < count; i++) 
        {
        	double margin = 50; // prevent edges
        	double x = margin + Math.random() * (DUNGEON_WIDTH - 2 * margin);
        	double y = margin + Math.random() * (DUNGEON_HEIGHT - 2 * margin);
            Enemy skullKid = new Enemy(x, y, 20); 
            room.addEntityToRoom(skullKid);
            
            //get the hitbox (Bounds) of the skullKid
            Bounds skullKidHitbox = skullKid.getHitBox();
            
            
            System.out.println("SkullKid hitbox: " + skullKidHitbox);
            
           
            
            System.out.println("Created SkullKid at " + x + "," + y + 
                    " ImageView: " + skullKid.getEnemyImageView());
            
            
        }
    }
    
    private void addGanonToRoom(Room room) 
    {
    	double x = Math.random() * DUNGEON_WIDTH; //random position for variety
        double y = Math.random() * DUNGEON_HEIGHT;
        Boss boss = new Boss(x, y, 300); //center position, 300 health
       
        room.addEntityToRoom(boss); 
        room.setBoss(boss); 
        room.getEnemies().add(boss);
        
        
        
        Bounds bossHitbox = boss.getHitBox();
        
        
        
        System.out.println("Boss hitbox bounds: " + bossHitbox);
    }
    
    

    private void startGameLoop() 
    {
        gameLoop = new AnimationTimer() 
        {
            @Override
            public void handle(long now) 
            {
                update();
                updateEnemies();
                //checkCollisions();
            }
        };
        gameLoop.start();
    }
    
    private void updateEnemies() 
    {
        Room current = getCurrentRoom();
        if (current == null) return;

        for (Enemy enemy : current.getEnemies()) 
        {
        	
        	if (enemy.isAlive() && enemy.isActive()) 
        	{
        		//enemy.move();
        	    double dx = player.getPlayerImageView().getX() - enemy.getEnemyImageView().getX();
        	    double dy = player.getPlayerImageView().getY() - enemy.getEnemyImageView().getY();

        	    double distance = Math.sqrt(dx * dx + dy * dy);
        	    double speed = 0.5;

        	    //prevent freeze if distance is too small (overlapping)
        	    double moveX, moveY;

        	    double minDistance = 10.0; 

        	    if (distance < minDistance) 
        	    {
        	        
        	        moveX = (Math.random() - 0.5) * speed;
        	        moveY = (Math.random() - 0.5) * speed;
        	    } 
        	    else 
        	    {
        	        moveX = (dx / distance) * speed;
        	        moveY = (dy / distance) * speed;
        	    }

        	    if (!isGameOver()) 
        	    {
        	        enemy.move(moveX, moveY);
        	    }

        	    // Attack if close
        	    if (!isGameOver() && enemy.canAttack(player)) 
        	    {
        	        player.takeDamage(3);
        	        updateHearts();

        	        FadeTransition flash = new FadeTransition(Duration.seconds(0.1), player.getPlayerImageView());
        	        flash.setFromValue(1.0);
        	        flash.setToValue(0.2);
        	        flash.setCycleCount(2);
        	        flash.setAutoReverse(true);
        	        flash.play();

        	        Rectangle attackEffect = new Rectangle(
        	            enemy.getEnemyImageView().getX() + enemy.getEnemyImageView().getFitWidth() / 2 - 10,
        	            enemy.getEnemyImageView().getY() + enemy.getEnemyImageView().getFitHeight() / 2 - 10,
        	            20, 20
        	        );
        	        attackEffect.setFill(Color.RED);
        	        current.getRoomPane().getChildren().add(attackEffect);

        	        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), attackEffect);
        	        ft.setFromValue(1.0);
        	        ft.setToValue(0.0);
        	        ft.play();
        	    }
        	}
              
        }
        
    }
        
        
    
    
    
    
  

    public void nextRoom() 
    {
    	if (transitioningRoom) return; //prevent re-entry

        if (canEnterNextRoom()) {
            if (currentRoomIndex < rooms.length - 1) {
                transitioningRoom = true; //set flag
                changeRoom(currentRoomIndex + 1);

                //reset flag after a short delay (like half a second)
                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(e -> transitioningRoom = false);
                delay.play();
            }
        }
    }
    
    public void setCurrentRoom(int index) 
    {
    	 if (index < 0 || index >= rooms.length) {
    	        System.err.println("Invalid room index: " + index);
    	        return;
    	    }
    	    
    	    //update the current room index
    	    this.currentRoomIndex = index;
    	    
    	    //change to the specified room
    	    changeRoom(currentRoomIndex);  
    	    
    	    //debug output
    	    System.out.println("Set current room to index: " + index);
    	    System.out.println("Room type: " + 
    	        (rooms[index].isBossRoom() ? "Boss Room" : "Normal Room"));
    }

    public boolean canEnterNextRoom() 
    {
    	Room currentRoom = rooms[currentRoomIndex];
        
        
        if (!currentRoom.isBossRoom()) 
        {
        	return currentRoom.isDoorUnlocked();
        } 
        //for boss room check if boss is defeated
        else 
        {
            return currentRoom.getBoss() == null || !currentRoom.getBoss().isAlive();
        }
    }
    
   

    public void update() 
    {
    	
        Room currentRoomObj = rooms[currentRoomIndex];
              
                
        
        
        Room currentRoom = rooms[currentRoomIndex];
        currentRoom.update();
        player.updateAttack();
        updateHearts();
        
       
        

     //check for key collisions
        if (currentRoom.getKey() != null && !currentRoom.isKeyCollected()) 
        {
            if (player.getPlayerImageView().getBoundsInParent().intersects(currentRoom.getKey().getBoundsInParent())) {
                if (currentRoom.collectKey()) {
                    keysCollected++;
                }
            }
        }
        
        if (currentRoomObj.isKeyCollected() && !currentRoomObj.isDoorUnlocked()) 
        {
            currentRoomObj.setDoorUnlocked(true);

            //change door color when key is picked up
            Rectangle door = currentRoomObj.getDoor();
            door.setFill(Color.LIGHTGREEN); 
        }


     //check for heart collisions
     if (currentRoom.getHeart() != null && !currentRoom.isHeartCollected()) 
     {
    	    if (player.getPlayerImageView().getBoundsInParent().intersects(currentRoom.getHeart().getBoundsInParent())) {
    	        if (currentRoom.collectHeart()) {
    	            player.heal(25);
    	        }
    	    }
    	}

     player.heal(25);
     updateHearts();

          
        
        // Check for key pickup (if room has key)
        if (currentRoomObj.hasKey() && keysCollected < 2) 
        {
            
            if (playerCollidesWithKey()) 
            {
                keysCollected++;
                //remove key from room
            }
        }
        
        //check if boss is defeated
        if (currentRoomObj.isBossRoom() && allEnemiesDefeated(currentRoomObj)) 
        {
            bossDefeated = true;
            
        }
    }

    

    public boolean isGameOver() 
    {
        
        return gameOver;
    }
    
    

    public boolean isBossDefeated() 
    {
        return bossDefeated;
    }
    
    public Room getCurrentRoom() 
    {
        return currentRoom;
    }

    private boolean allEnemiesDefeated(Room room) {
        if (room.isBossRoom()) {
            return room.getBoss() != null && room.getBoss().getHealth() <= 0;
        }
        
        for (Enemy enemy : room.getEnemies()) {
            if (enemy.getHealth() > 0) {
                return false;
            }
        }
        return true;
    }

    
    private boolean playerCollidesWithKey() 
    {
        //key collision detection
        return false;
    }
    
    
    
    public void setGameOver(boolean over) 
    {
        this.gameOver = over;
    }
    
    
    
    
    
    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
    
    
    
    
}