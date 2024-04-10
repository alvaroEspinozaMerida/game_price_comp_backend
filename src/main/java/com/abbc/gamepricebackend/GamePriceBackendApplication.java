package com.abbc.gamepricebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class GamePriceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamePriceBackendApplication.class, args);
    }


}
