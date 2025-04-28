/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */


import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class ZeldaDungeon extends Application {
    
    private static final int DUNGEON_WIDTH = 600;
    private static final int DUNGEON_HEIGHT = 400;

    private Game game;
    private Pane gamePane;
    private Stage primaryStage;
    private Pane rootPane;
    private Pane startScreenPane;
    private Music music;
    private Music victoryMusic;
    
    private AnimationTimer gameLoop;
    
    private Pane finalMessagePane;
    
    private final Set<KeyCode> pressedKeys = new HashSet<KeyCode>();
    
    private ControllerManager controllers;
    
    private Button startButton;
    
    

    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) 
    {
    	this.primaryStage = primaryStage;
        this.rootPane = new Pane();
        
        //create a new gamePane
        gamePane = new Pane();          
        game = new Game(gamePane); //pass it into Game constructor
        game.initializeGame();
        
        music = new Music("/music1.mp3");
        music.play(); 

        controllers = new ControllerManager();
        controllers.initSDLGamepad();
        //initialize both screens (but only show start screen)
        this.startScreenPane = createStartScreen(); 
       

        rootPane.getChildren().addAll(startScreenPane); // Add both panes

        primaryStage.setScene(new Scene(rootPane, DUNGEON_WIDTH, DUNGEON_HEIGHT));
        primaryStage.setTitle("Zelda Dungeon");
        primaryStage.show();
        
    }


    private Pane createStartScreen() 
    {
    	 Pane pane = new Pane();
         
         try {
             //background Image
             Image logo = new Image(getClass().getResourceAsStream("/startscreen.png"));
             ImageView backgroundView = new ImageView(logo);
             backgroundView.setFitWidth(DUNGEON_WIDTH);
             backgroundView.setFitHeight(DUNGEON_HEIGHT);
             
             //start Button
             startButton = new Button("Start Game");
             startButton.setLayoutX(DUNGEON_WIDTH/2 - 38);
             startButton.setLayoutY(DUNGEON_HEIGHT - 50);
             startButton.setOnAction(e -> startGame());
             
             pane.getChildren().addAll(backgroundView, startButton);
         } 
         catch (Exception e) 
         {
             //fallback if image fails to load
             pane.setStyle("-fx-background-color: black;");
             Label errorLabel = new Label("Start Game");
             errorLabel.setTextFill(Color.BLACK);
             pane.getChildren().add(errorLabel);
         }
         
         
         
         AnimationTimer controllerStartPoller = new AnimationTimer() 
         {
        	    @Override
        	    public void handle(long now) 
        	    {
        	        if (controllers != null) 
        	        {
        	            ControllerState state = controllers.getState(0);
        	            if (state.isConnected && state.a) 
        	            {
        	                this.stop(); //stop polling once pressed
        	                Platform.runLater(() -> startButton.fire()); //simulate button click
        	            }
        	        }
        	    }
        	};
    
        	controllerStartPoller.start();
        	
        	return pane;
    }

    private void startGame() 
    {
        try 
        {
        	
        	rootPane.getChildren().clear();
        	
        	
        	this.gamePane = new Pane(); //assign the gamePane
            this.game = new Game(gamePane);
            
            ControllerManager controllers = new ControllerManager();
            controllers.initSDLGamepad();
            
            
            rootPane.getChildren().add(gamePane);  
            
            setupGameControls();
            
            Platform.runLater(() -> gamePane.requestFocus());

            
            //debug verification
            Platform.runLater(() -> {
                System.out.println("Game initialized:");
                System.out.println("Root children: " + rootPane.getChildren().size());
                System.out.println("Player in scene: " + 
                    (game.player.getPlayerImageView().getScene() != null));
            });
            
            //show game screen
            gamePane.setVisible(true);
            gamePane.toFront();
            
        } 
        catch (Exception e) 
        {
            System.err.println("Failed to start game: " + e.getMessage());
            //fallback - show start screen again
            startScreenPane.setVisible(true);
         
        }
    }
    
   
    private void updateRoomDisplay() 
    {
    	//get references to current room and game pane
        Room currentRoom = game.rooms[game.currentRoomIndex];
        Pane gamePane = game.getGamePane(); //assuming Game class has getGamePane()
        
        
        
        //add the new room's content
        if (!gamePane.getChildren().contains(currentRoom.getRoomPane())) 
        {
            gamePane.getChildren().add(currentRoom.getRoomPane());
        }

        
        //re-add player to maintain proper z-order
        currentRoom.addEntityToRoom(game.player);
        gamePane.getChildren().add(game.player.getPlayerImageView());
        game.player.getPlayerImageView().toFront();
        
        game.updateHearts();
        
        //debug output
        System.out.println("Room changed to index: " + game.currentRoomIndex);
        System.out.println("Pane children count: " + gamePane.getChildren().size());
    }
    
    private void setupGameControls() 
    {
        Scene scene = primaryStage.getScene();
        
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
        
      
        scene.setOnKeyPressed(e -> {
            pressedKeys.add(e.getCode());
            handleKeyPress(e); // still want to react to things like SPACE
        });

        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        
        //game loop for smooth movement
        gameLoop = new AnimationTimer() 
        {
        	//override animation timer for controls 
            @Override
            public void handle(long now) 
            {
            	 double deltaX = 0, deltaY = 0;
                 double moveSpeed = 100; // pixels per second
                 double elapsedSeconds = 1.0 / 60.0;
                 double moveAmount = moveSpeed * elapsedSeconds;

                 // Controller input
                 ControllerState state = controllers.getState(0);
                 if (state.isConnected) {
                     if (state.dpadLeft || state.leftStickX < -0.3) {
                         deltaX -= moveAmount;
                     }
                     if (state.dpadRight || state.leftStickX > 0.3) {
                         deltaX += moveAmount;
                     }
                     if (state.dpadUp || state.leftStickY < -0.3) {
                         deltaY -= moveAmount;
                     }
                     if (state.dpadDown || state.leftStickY > 0.3) {
                         deltaY += moveAmount;
                     }

                     if (state.a) {
                         game.player.attack(); 
                     }
                 }

                 //keyboard movement
                 if (pressedKeys.contains(KeyCode.UP))    deltaY -= moveAmount;
                 if (pressedKeys.contains(KeyCode.DOWN))  deltaY += moveAmount;
                 if (pressedKeys.contains(KeyCode.LEFT))  deltaX -= moveAmount;
                 if (pressedKeys.contains(KeyCode.RIGHT)) deltaX += moveAmount;

                 if (deltaX != 0 || deltaY != 0) {
                     game.player.move(deltaX, deltaY);
                 }
                handleContinuousMovement();
                game.update(); 
                checkCollisions();
            }
        };
        gameLoop.start();
    }
    
    
    private void handleContinuousMovement() 
    {
        double deltaX = 0, deltaY = 0;
        
        double moveSpeed = 100; //pixels per second
        double elapsedSeconds = 1.0 / 60.0; //60 FPS
        double moveAmount = moveSpeed * elapsedSeconds;

        if (pressedKeys.contains(KeyCode.UP))    deltaY -= moveAmount;
        if (pressedKeys.contains(KeyCode.DOWN))  deltaY += moveAmount;
        if (pressedKeys.contains(KeyCode.LEFT))  deltaX -= moveAmount;
        if (pressedKeys.contains(KeyCode.RIGHT)) deltaX += moveAmount;

        
        
        
        if (deltaX != 0 || deltaY != 0) 
        {
            game.player.move(deltaX, deltaY);
        }
    }
   
    
    private void handleKeyPress(KeyEvent event) 
    {
        switch (event.getCode()) 
        {
           
            case SPACE:
            case Z: // Alternative attack key
                game.player.attack();
                break;
            default:
               	break;
        }
    }

  
    
    private void checkCollisions() 
    {
    	
    	if (game.isGameOver()) return;
    	
        Room currentRoom = game.rooms[game.currentRoomIndex];

        if (game.player.isAttacking()) 
        {
            Rectangle attackHitbox = game.player.getAttackHitbox();

            //check against enemies
            for (Enemy enemy : currentRoom.getEnemies()) 
            {
                if (attackHitbox.getBoundsInParent().intersects(enemy.getBounds())) 
                {
                    enemy.takeDamage(0); // Your damage value
                }
            }

            // Check against boss if applicable
            if (currentRoom.isBossRoom() && currentRoom.getBoss() != null) 
            {
                if (attackHitbox.getBoundsInParent().intersects(currentRoom.getBoss().getBounds())) 
                {
                    currentRoom.getBoss().takeDamage(0);
                }
            }
        }

        for (Enemy enemy : currentRoom.getEnemies()) 
        {
            if (game.player.getBounds().intersects(enemy.getBounds())) 
            {
                game.player.takeDamage(0);
                
            }
        }

        if (currentRoom.getHeart() != null &&
            !currentRoom.isHeartCollected() &&
            game.player.getBounds().intersects(currentRoom.getHeart().getBoundsInParent())) {
            if (currentRoom.collectHeart()) {
                game.player.heal(25);
            }
        }
        
        

        // Door/room transition
        if (currentRoom.getDoor() != null && game.player.getBounds().intersects(currentRoom.getDoor().getBoundsInParent()))
        {
            if (game.canEnterNextRoom()) 
            {
                game.nextRoom();
                updateRoomDisplay();
            }
        }

        if (game.isBossDefeated() && !game.isGameOver()) 
        {
            game.setGameOver(true); 
            gameLoop.stop();

            System.out.println("Stopping: " + game.music); 
            music.fadeOutAndStop(Duration.seconds(2));

            victoryMusic = new Music("/music2.mp3");
            Platform.runLater(() -> 
            {
                victoryMusic.play();
                MediaPlayer victoryPlayer = victoryMusic.getMediaPlayer();
                victoryPlayer.setOnEndOfMedia(() -> 
                    Platform.runLater(() -> rootPane.getChildren().remove(finalMessagePane))
                );
            });

            Pane finalMessagePane = new Pane();
            double height = rootPane.getHeight(); 
            double width = rootPane.getWidth();

            Image backgroundImage = new Image(getClass().getResource("/triforce1.png").toExternalForm());
            ImageView bgView = new ImageView(backgroundImage);

            bgView.setFitWidth(width);
            bgView.setFitHeight(height);
            bgView.setSmooth(true);
            bgView.setCache(true);
            bgView.setX((width - bgView.getBoundsInParent().getWidth()) / 2);
            bgView.setY((height - bgView.getBoundsInParent().getHeight()) / 2);

            finalMessagePane.getChildren().add(bgView);
            finalMessagePane.setPrefSize(width, height); 

            Font hyliaFont = Font.loadFont(getClass().getResource("/hylia.otf").toExternalForm(), 40);

            rootPane.getChildren().add(finalMessagePane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), finalMessagePane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            //after fade, go black and show credits
            fadeIn.setOnFinished(evt -> {
                finalMessagePane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

                Text credits = new Text(
                    "BRUH! \n\nGanon got GOT...\n\nThe Triforce is safe!\n\n...... For Now" +
                    "\n\nCREDITS\n\n" +
                    "Game Design\n Programming:\n Josh Pitts... \nAKA SUPARedBeard\n\n" +
                    "Art & Assets: Stream Avatars\n Canva Creation Suite\n\n" +
                    "Music\n The Legend of Zelda Main Theme\nComposed by Koji Kondo\n\n" +
                    "The Legend of Zelda: \nTwilight Princess \nMain Theme\b Composed by:\n Toru Minegishi & Asuka Ohta\n\n\n\n\n" +
                    "Thanks for Playing!\n\n" +
                    "Made with JavaFX ❤️\n\n\n\n\n\n\n" +
                    "Press any key to exit..."
                );

                credits.setFont(hyliaFont != null ? hyliaFont : Font.font("Arial", FontWeight.BOLD, 30));
                credits.setFill(Color.GOLD);
                credits.setStroke(Color.BLACK);
                credits.setStrokeWidth(1);
                credits.setTextAlignment(TextAlignment.CENTER);
                credits.setWrappingWidth(width * 0.8);
                credits.setLayoutX((width - credits.getWrappingWidth()) / 2);
                credits.setLayoutY(height + 50); //start below screen
                finalMessagePane.getChildren().add(credits);

                //scroll credits fully
                Platform.runLater(() -> {
                    double startY = credits.getLayoutY();
                    double endY = -credits.getBoundsInLocal().getHeight();

                    TranslateTransition scroll = new TranslateTransition(Duration.seconds(45), credits);
                    scroll.setFromY(startY);
                    scroll.setToY(endY);
                    scroll.setInterpolator(Interpolator.LINEAR);
                    scroll.play();
                });

                Scene scene = rootPane.getScene();
                scene.setOnKeyPressed(null);
                scene.setOnKeyPressed(e -> Platform.exit());
            });
        }      	           	          	
        	
    }
}

    
