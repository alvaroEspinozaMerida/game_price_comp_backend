package com.abbc.gamepricebackend;

import DTO.Game;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:5173/"})

@RestController
@RequestMapping("/api/v1/games")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;
    @GetMapping("/show_all")
    public ResponseEntity<List<Game>> fetchAllGames(){
        return new ResponseEntity<>(gameService.getAllGames(), HttpStatus.OK);
    }

    @PostMapping("/add_game")
    public Game addGame(@RequestBody Game newGame){
        return gameService.addGame(newGame);
    }

    @GetMapping("/find_game_by_title")
    public Optional<Game> findGameByTitle(@RequestParam String title) {return gameService.findGameByTitle(title);}

    @GetMapping("/find_game_by_id")
    public Optional<Game> findGameByID(@RequestParam long id) {
        System.out.println("ID:"+id);
        return gameService.findGameByID(id);
    }


}
