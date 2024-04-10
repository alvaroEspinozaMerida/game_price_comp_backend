package com.abbc.gamepricebackend;

import DTO.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface GameRepository extends MongoRepository<Game,String>{
    Optional<Game> findGameBySteamAppID(long id );
    Optional<Game> findGameByTitle(String title);



}
