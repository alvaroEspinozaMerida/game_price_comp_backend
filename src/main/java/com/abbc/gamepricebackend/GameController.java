package com.abbc.gamepricebackend;

import DTO.Game;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/find_game")
    public Optional<Game> findGameByTitle(@RequestBody String title) {return gameService.findGameByTitle(title);}


}
