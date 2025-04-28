//Josh Pitts
//CPT 206
//Lab 5
//I commented out things so i could get iut to run with my index.html to consume the API for extra credit


using J_Pitts_Lab_5.Models;


namespace J_Pitts_Lab_5.Data
{
    public class GameRepository
    {
        public static List<Game> Games { get; } = new List<Game>
        {
            new Game { Id = 1, Title = "The Legend of Zelda: Link's Awakening DX ", Platform ="GameBoyColor", Dev = "Capcom", ReleaseYear = 1998, Genre = "Action-Adventure"},
            new Game { Id = 2, Title = "Pokemon: Crystal Version", Platform ="GameBoyColor", Dev = "Game Freak", ReleaseYear = 2000, Genre = "JRPG" },
            new Game { Id = 3, Title = "Yu-Gi-Oh! Dark Duel Stories", Platform ="GameBoyColor",Dev = "Konami", ReleaseYear = 2002, Genre = "TCG" },
            new Game { Id = 4, Title = "Tetris DX", Platform ="GameBoyColor", Dev = "Nintendo R&D1", ReleaseYear = 1998, Genre  = "Puzzle" },
        };
        

    }
}
