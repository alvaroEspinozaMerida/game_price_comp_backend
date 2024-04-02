package com.abbc.gamepricebackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLookUp {


    public static void main(String[] args) {

//        Create Formatted String that Gets Games 1-100 of the API to store within the JSON file that can be
//        sent to a mongodb data base

        List<Map<String, Object>> gameList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);

//        Call to the API has a limit of 25 game ids
        String apiURL = "https://www.cheapshark.com/api/1.0/games?ids=";
        String gameIDs = "";
        for(int i = 0; i <= 3 ; i ++){

            gameIDs = "";
            for(int j = 1; j <= 25; j ++){
                gameIDs += (j + (i * 25) ) + ",";
            }
//            each call to getAPIData adds a new game object into a list; the list is then written into a json file at the end
//            within this function there is a helper function processGame that is incharge of formatting each indivdual game object data
            getAPIData(apiURL+gameIDs, gameList);

        }

        try {
            mapper.writeValue(new File("usersData.json"), gameList);
            System.out.println("JSON data is written to the file successfully");
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }


    private  static void getAPIData(String apiURL, List<Map<String, Object>> gameList ){


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL)) // Replace with your API URL
                .build();
        ObjectMapper mapper = new ObjectMapper();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode rootNode = mapper.readTree(response.body());

//            List will be used to write all of the game items into a json file all at once

            rootNode.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();
                Map<String, Object> gameData = processGame(key,value);
                gameList.add(gameData);
            });

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }



//    Creates a new object that will be able to be stored with in the formatted JSON file that can
//    be used to map these objects into components
    private static Map<String, Object> processGame(String key, JsonNode gameNode) {
        // Assuming you want to extract the title and cheapest price ever for demonstration
        String title = gameNode.get("info").get("title").asText();
        JsonNode cheapestPriceEverNode = gameNode.get("cheapestPriceEver");
        String cheapestPrice = cheapestPriceEverNode.get("price").asText();
        long date = cheapestPriceEverNode.get("date").asLong();



//        Write to the JSON FILE
        Map<String, Object> gameData = new HashMap<>();
        gameData.put("Key",key);
        gameData.put("Title", title);
        gameData.put("Cheapest Price", cheapestPrice);


        return gameData;
    }
}
