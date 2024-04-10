package com.abbc.gamepricebackend;

import DTO.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
