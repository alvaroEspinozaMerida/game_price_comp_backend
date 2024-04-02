package com.abbc.gamepricebackend;

import DTO.Deal;
import DTO.DealsContainer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
        mapper.registerModule(new JavaTimeModule());


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
//create the objects here
//                String key = entry.getKey();
                JsonNode value = entry.getValue();
                JsonNode dealsNode = value.get("deals");
                List<Deal> deals = null; 


                if (dealsNode != null && dealsNode.isArray()){
                    // Convert 'dealsNode' to a List of Deal objects
                   deals = mapper.convertValue(dealsNode, new TypeReference<List<Deal>>() {});
//                    // Now 'deals' is a List of Deal objects, and you can process each deal object
                    for (Deal deal : deals) {
                        // Process each 'deal' object as needed
                       deal.setDate();
                    }
                }
//                System.out.println("JSON OBJECT:");
//                System.out.println(value.get("deals"));



                Map<String, Object> gameData = processGame(value,deals);
                gameList.add(gameData);
            });

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


//    Creates a new object that will be able to be stored with in the formatted JSON file that can
//    be used to map these objects into components
    private static Map<String, Object> processGame(JsonNode gameNode,  List<Deal> deals) {
        // Assuming you want to extract the title and cheapest price ever for demonstration
//        TODO: use the Game class to create new game objects

        String title = gameNode.get("info").get("title").asText();
        String gameID =  gameNode.get("info").get("steamAppID").asText();





//        DealsContainer deals =



//        for(Deal deal : gameNode.get("deals")){
//
//            System.out.println(deal.toString());
//        }



//        Extract out the deals from within the json data
//        JsonNode cheapestPriceEverNode = gameNode.get("cheapestPriceEver");
//        String cheapestPrice = cheapestPriceEverNode.get("price").asText();
//        long date = cheapestPriceEverNode.get("date").asLong();

//        List<Map<String, Object>> dealsList = gameNode.get("deals");



//        Write to the JSON FILE
        Map<String, Object> gameData = new HashMap<>();
//        gameData.put("Key",key);
        gameData.put("ID",gameID);
        gameData.put("Title", title);
        gameData.put("Deals", deals);


        return gameData;
    }
}
