package com.abbc.gamepricebackend;

import DTO.Deal;
import DTO.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    public Game addGame(Game newGame){
        return gameRepository.insert(newGame);
    }

    public Optional<Game> findGameByTitle(String title){ return  gameRepository.findGameByTitle(title); }

    public Optional<Game> findGameByID(long id){ return  gameRepository.findGameBySteamAppID(id); }

    public ResponseEntity<Game> updateGameDeals(long id , Deal newDeal){
//        Find the game in the repository
        Game game = gameRepository.findGameBySteamAppID(id).orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id"+id));

        game.addNewDeal(newDeal);

        Game updatedGame = gameRepository.save(game);

        return ResponseEntity.ok(updatedGame);

    }







}
