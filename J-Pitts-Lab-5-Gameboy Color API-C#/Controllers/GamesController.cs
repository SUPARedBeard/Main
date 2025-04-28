//Josh Pitts
//CPT 206
//Lab 5
//I commented out things so i could get iut to run with my index.html to consume the API for extra credit


using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using J_Pitts_Lab_5.Models;
using J_Pitts_Lab_5.Data;

namespace J_Pitts_Lab_5.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class GamesController : ControllerBase
    {
        [HttpGet]
        public ActionResult<IEnumerable<Game>> GetAllGames()
        {
            return Ok(GameRepository.Games);
        }

        //get api/games/1
        [HttpGet("{id}")]
        public ActionResult<Game> GetGame(int id)
        {
            var game = GameRepository.Games.FirstOrDefault(g => g.Id == id);
            if (game == null)
                return NotFound();
            return Ok(game);
        }

        //post api/games
        [HttpPost]
        public ActionResult<Game> AddGame(Game game)
        {
            game.Id = GameRepository.Games.Max(g => game.Id) + 1;
            GameRepository.Games.Add(game);
            return CreatedAtAction(nameof(GetGame), new { id = game.Id }, game);
        }

        //delete api/games/1
        [HttpDelete("{id}")]
        public IActionResult DeleteGame(int id)
        {
            var game = GameRepository.Games.FirstOrDefault(g => g.Id == id);
            if(game == null)
                return NotFound();

            GameRepository.Games.Remove(game); 
            return NoContent();
        }


    }
}
