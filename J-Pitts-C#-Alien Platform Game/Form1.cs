//Josh Pitts
//CPT-185
//Final Project


using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;



namespace J_Pitts_CPT_185_Final_Project
{
    public partial class platformGame : Form
    {
        //list of game levels and index to track current level
        private List<Level> levels = new List<Level>();
        private int currentLevelIndex = 0;

        //enum to define the game states, Start screen, Playing, and Game Over
        private enum GameState
        {
            StartScreen,
            Playing,
            GameOver
        }

        //initial game state
        private GameState currentGameState = GameState.StartScreen;

        //variables to hold the player object, list of platforms, and movement flags
        private Player player; //player object
        private List<Platform> platforms; //list of platforms
        private bool leftPressed, rightPressed; //input flags for left and right keys
        private int gravity = 10; //gravity effect
        private int groundLevel;//Y-coordinate for the ground level
        private int verticalVelocity = 0;  //controls upward/downward movement
        private int jumpStrength = -15;    //jumping power
        private int maxFallSpeed = 15;     //terminal velocity, fall speed
        private bool isGrounded = false;   //tracks if the player is on the ground

        //variables to have scrolling background if stretch goal is reached
        private int backgroundX = 0; // X position of the background
        private int backgroundSpeed = 2; // How fast the background moves

        //image objects for backgrounds, player and goal
        private Image startScreenImage;
        private Image playerSprite;
        private Image backgroundImage;
        private Image goalImage;

        //rectangle to define the goal are in game
        private Rectangle goal = new Rectangle(700, 150, 50, 50); // Goal position and size

        //player class to define the player's properties
        public class Player
        {
            public int X { get; set; }
            public int Y { get; set; }
            public int Width { get; set; }
            public int Height { get; set; }
            public int Speed { get; set; }
        }

        //platform class to define the platform properties
        public class Platform
        {
            public int X { get; set; }
            public int Y { get; set; }
            public int Width { get; set; }
            public int Height { get; set; }
        }

       
        //constructor for the game form
        public platformGame()
        {
            InitializeComponent();

            InitializeLevels();//initialize game levels
            LoadLevel(0); //start with the first level

            //stop the game timer initially
            gameTimer.Stop();

            //load the start screen image
            try
            {
                //this is the loacation in the folder for the image, might need to be adjusted for use on other pcs
                startScreenImage = Image.FromFile("C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/startScreen.jpg");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error loading start screen image: " + ex.Message);
                startScreenImage = null; //fallback if the file doesn't load
            }

            if (currentGameState == GameState.Playing)
            {
                gameTimer.Start(); //to start the game timer if on first level
            }

            //to load the goal image, same loaction as mentioned before
            try
            {
                goalImage = Image.FromFile("C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/egg.png"); // Ensure this file exists
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error loading goal image: " + ex.Message);
                goalImage = null; //set to null if the file isn't found or fails to load
            }

            //prevent flickering on screen
            this.DoubleBuffered = true;

            //load background image
            try
            {
                backgroundImage = Image.FromFile("C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/background.png"); // Ensure this image exists
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error loading background image: " + ex.Message);
                backgroundImage = null; //fallback to null if the image is not found
            }


            // Initialize player with default values
            player = new Player { X = 50, Y = 400, Width = 50, Height = 50, Speed = 5 };

            //load player sprite
            try
            {
                playerSprite = Image.FromFile("C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/alien_gorilla.png"); // Ensure this file exists in the specified directory
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error loading player sprite: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                playerSprite = null; //set to null if the file isn't found or fails to load
            }

            //set the ground level to the forms height(bottom of the window)
            groundLevel = this.ClientSize.Height;
            gameTimer.Start(); //start the game timer
        }

        //load specific level by index
        private void LoadLevel(int levelIndex)
        {
            if (player == null)
            {
                player = new Player { X = 50, Y = 400, Width = 50, Height = 50, Speed = 5 };
            }

            var level = levels[levelIndex];

            //update platforms and goal
            platforms = level.Platforms;
            goal = level.Goal;

            //load background image for current level
            try
            {
                backgroundImage = Image.FromFile(level.BackgroundImagePath);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error loading background image: " + ex.Message);
                backgroundImage = null;
            }

            //reset player position to the starting point for the new level, have had trouble with this not working properly
            player.X = 100;
            player.Y = groundLevel - player.Height;

            //redraw the screen
            Invalidate();
        }

        //initialize levels by creating different platform layouts and goal positions
        private void InitializeLevels()
        {
            levels = new List<Level>
    {   //for new levels
        new Level(
            new List<Platform>
            {
                //this is to set the platform locations on screen 
                new Platform { X = 50, Y = 450, Width = 200, Height = 20 },
                new Platform { X = 300, Y = 350, Width = 150, Height = 20 },
                new Platform { X = 500, Y = 250, Width = 100, Height = 20 },
            },
            //this is for player and goal positions on screen
            new Rectangle(50, 450 - 50, 50, 50),
            new Rectangle(700, 150, 50, 50),
            "C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/background.png"
        ),
        new Level(
            new List<Platform>
            {
                new Platform { X =30, Y = 490, Width= 200, Height = 20 },
                new Platform { X = 50, Y = 400, Width = 160, Height = 25 },
                new Platform { X = 250, Y = 310, Width = 180, Height = 20 },
                new Platform { X = 540, Y = 210, Width = 130, Height = 15 },
            },
            new Rectangle(50, 380 - 50, 50, 50),
            new Rectangle(700, 100, 50, 50),  // Goal
            "C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/background2.jpg"
        ),

        new Level(
            new List<Platform>
            {
                new Platform { X =30, Y = 470, Width= 200, Height = 20 },
                new Platform { X = 100, Y = 380, Width = 180, Height = 20 },
                new Platform { X = 400, Y = 280, Width = 180, Height = 20 },
                new Platform { X = 650, Y = 190, Width = 120, Height = 25 },
            },
            new Rectangle(120, 420 - 50, 50, 50),
            new Rectangle(700, 100, 50, 50),
            "C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/background3.jpg"
        ),

        new Level(
            new List<Platform>
            {
                new Platform { X = 70, Y = 460, Width = 170, Height = 20 },
                new Platform { X = 220, Y = 360, Width = 180, Height = 20 },
                new Platform { X = 330, Y = 260, Width = 240, Height = 20 },
                new Platform { X = 590, Y = 180, Width = 110, Height = 18 },
            },
            new Rectangle(110, 420 - 50, 50, 50),
            new Rectangle(700, 100, 50, 50),
            "C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/background4.jpg"
        ),

        new Level(
            new List<Platform>
            {
                new Platform { X =30, Y = 470, Width= 200, Height = 20 },
                new Platform { X = 160, Y = 400, Width = 150, Height = 20 },
                new Platform { X = 360, Y = 300, Width = 200, Height = 20 },
                new Platform { X = 180, Y = 210, Width = 100, Height = 20 },
            },
            new Rectangle(80, 430 - 50, 50, 50),
            new Rectangle(100, 100, 50, 50),
            "C:\\Users/joshp/source/repos/J-Pitts-CPT-185-Final-Project/Resources/background5.jpg"

        ),
    };

        }
        

        //timer tick event that updates the game logic, movement, collisions and whatnot
        private void gameTimer_Tick(object sender, EventArgs e)
        {
            //move the background to simulate scrolling if time allows in strecth goal
            backgroundX -= backgroundSpeed;

            //reset background position when it has scrolled out of view
            if (backgroundX <= -this.ClientSize.Width)
            {
                backgroundX = 0;
            }

            //gravity effect
            if (!isGrounded)
            {
                verticalVelocity += 1; //gravity accelerates the player downward
                if (verticalVelocity > maxFallSpeed)
                    verticalVelocity = maxFallSpeed;
            }

            //update player position
            player.Y += verticalVelocity;
            if (leftPressed) player.X -= player.Speed;
            if (rightPressed) player.X += player.Speed;

            //keep the player within the bounds of the form
            if (player.X < 0) player.X = 0; // Left edge
            if (player.X + player.Width > this.ClientSize.Width)
                player.X = this.ClientSize.Width - player.Width; // Right edge

            //check collisions with platforms and goal
            CheckCollisions();

            //redraw the screen to reflect any changes
            Invalidate();

            //check if the player reaches goal
            CheckWinCondition();
        }

        //key down evet to handle player movement (left key, right key, space jump)
        private void platformGame_KeyDown(object sender, KeyEventArgs e)
        {
            //if on start screen, press enter to begin game
            if (currentGameState == GameState.StartScreen)
            {
                if (e.KeyCode == Keys.Enter)
                {
                    ShowInstructionForm();
                    currentGameState = GameState.Playing; //transition to gameplay
                    Invalidate(); //redraw the screen
                }
                return;
            }
            //to set movement flags based on key presses
            if (e.KeyCode == Keys.Left) leftPressed = true;
            if (e.KeyCode == Keys.Right) rightPressed = true;

            //jump if space bar is pressed while player is grounded
            if (e.KeyCode == Keys.Space && isGrounded)
            {
                
                verticalVelocity = jumpStrength; //apply jump force
                isGrounded = false; //player leaves the ground
            }
        }

        //key up event to stop player movement when the key is released
        private void platformGame_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Left) leftPressed = false;
            if (e.KeyCode == Keys.Right) rightPressed = false;
        }

        //paint event to draw the game objects on screen
        protected override void OnPaint(PaintEventArgs e)
        {
            base.OnPaint(e);
            var g = e.Graphics;

            if (currentGameState == GameState.StartScreen)
            {
                //if statement to display the start screen if available
                if (startScreenImage != null)
                {
                    g.DrawImage(startScreenImage, 0, 0, this.ClientSize.Width, this.ClientSize.Height);
                }
                else
                {
                    //fallback if the image is missing, fill background with black and display fallback text
                    g.Clear(Color.Black);
                    var font = new Font("Arial", 24, FontStyle.Bold); //set font of fallback text
                    var brush = Brushes.White; //set color of text to white


                    string fallbackText = "Alien Platform Jumper"; //text to display when image is missing
                    SizeF textSize = g.MeasureString(fallbackText, font); //measure the size of text
                    g.DrawString(fallbackText, font, brush,
                        (this.ClientSize.Width - textSize.Width) / 2, //horizontally center the text
                        (this.ClientSize.Height - textSize.Height) / 2); //vertically center the text
                }

                //draw the "Press Enter to Play" prompt
                var promptFont = new Font("Arial", 16, FontStyle.Regular); //font for prompt text
                var promptBrush = Brushes.White; //color of font
                string pressEnterText = "Press Enter to Play"; //promt text to display
                SizeF promptSize = g.MeasureString(pressEnterText, promptFont); //measure the size of the prompt text
                g.DrawString(pressEnterText, promptFont, promptBrush,
                    (this.ClientSize.Width - promptSize.Width) / 2, //horizontal center
                    this.ClientSize.Height / 2 + 40);  //position below the main title

                return; //stop further drawing for start screen
            }



            //draw background if available
            if (backgroundImage != null)
            {
                g.DrawImage(backgroundImage, 0, 0, this.ClientSize.Width, this.ClientSize.Height);
            }

            //draw the player, platforms, goals
            //draw player
            if (playerSprite != null)
            {
                g.DrawImage(playerSprite, player.X, player.Y, player.Width, player.Height);
            }
            //draw each platform
            foreach (var platform in platforms)
            {
                g.FillRectangle(Brushes.Green, platform.X, platform.Y, platform.Width, platform.Height);
            }
            //draw goal
            if (goalImage != null)
            {
                g.DrawImage(goalImage, goal.X, goal.Y, goal.Width, goal.Height);
            }
            else
            {
                //fallback to a rectangle if the image is missing
                g.FillRectangle(Brushes.Yellow, goal);


            }
        }

        //method to show instruction form
        private void ShowInstructionForm()
        {
            //open instruction form and hide main game window
            using (var instructionForm = new instructionForm())
            {
                this.Hide(); //hide the main form while the instructions are shown
                instructionForm.ShowDialog(); //show the instructions form modally
                this.Show(); //show the main form again
            }

            //update the game state to start playing
            currentGameState = GameState.Playing;
            Invalidate(); //redraw the screen to load the first level
        }
        
        //method to check collison between player and platform
        private void CheckCollisions()
        {
            //assume the player isnt grounded initially
            isGrounded = false;


            foreach (var platform in platforms)
            {
                //check if player is falling onto a platform
                if (player.X + player.Width > platform.X && player.X < platform.X + platform.Width &&
                    player.Y + player.Height >= platform.Y && player.Y + player.Height - verticalVelocity <= platform.Y)
                {
                    isGrounded = true; //player is on the platform
                    verticalVelocity = 0; //stop downward motion
                    player.Y = platform.Y - player.Height; //snap player to platform top
                }
            }

            //prevent the player from falling through the bottom of the form
            if (player.Y + player.Height >= groundLevel)
            {
                isGrounded = true; //player is on ground
                verticalVelocity = 0; //stop downward
                player.Y = groundLevel - player.Height; //adjust player position to be on top of ground
            }
        }

        //method to check if goal is reached by player
        private void CheckWinCondition()
        {
            //check if the player intersects with the goal
            if (new Rectangle(player.X, player.Y, player.Width, player.Height).IntersectsWith(goal))
            {
                gameTimer.Stop(); //stop the game
                MessageBox.Show("You have completed the level", "Congratulations!"); //message box to show you completed a level

                //increment to go to next level
                currentLevelIndex++;

                //if there are more levels, load the next one, otherwise show game over message
                if (currentLevelIndex < levels.Count)
                {
                    LoadLevel(currentLevelIndex); //load the next level
                    gameTimer.Start(); //restart the game timer
                }
                //to show that all levels are complete
                else
                {
                    MessageBox.Show("You completed all levels!", "Congratulations!");
                    Application.Exit(); //exit the game
                }
            }
        }                
    }
}
