//Josh Pitts
//CPT-185
//Final Project

using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static J_Pitts_CPT_185_Final_Project.platformGame;

namespace J_Pitts_CPT_185_Final_Project
{
    //class for levels
    public class Level
    {
        //to store a list of platforms in the level
        public List<Platform> Platforms { get; set; }
        //to represent the players position on the screen
        public Rectangle Player { get; set; }
        //to represent the goals position on the screen
        public Rectangle Goal { get; set; }
        //to store the file path for the background images
        public string BackgroundImagePath { get; set; }

        //constructor for the level class, which initializes the properties
        //takes in a list of platforms, player rectangle, goal rectangle, and backgournd image path
        public Level(List<Platform> platforms, Rectangle player, Rectangle goal, string backgroundImagePath)
        {
            //initialize platform
            Platforms = platforms;
            //initialize player
            Player = player;
            //initialize goal
            Goal = goal;
            //initialize background
            BackgroundImagePath = backgroundImagePath;
        }
    }

}
