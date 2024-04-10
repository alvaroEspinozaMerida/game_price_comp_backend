package com.abbc.gamepricebackend;

import DTO.Deal;
import DTO.DealsContainer;
import DTO.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
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
//
public class GameLookUp {
    public static void main(String[] args) {
//        CONNECTION TO THE DATABASE
//        Create Formatted String that Gets Games 1-100 of the API to store within the JSON file that can be
//        sent to a mongodb data base

        List<Map<String, Object>> gameList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
        mapper.registerModule(new JavaTimeModule());


//        Call to the API has a limit of 25 game ids
        String apiURL = "https://www.cheapshark.com/api/1.0/games?ids=1,2,3";
        String gameIDs = "";
//        for(int i = 0; i <= 1 ; i ++){
//
//            gameIDs = "";
//            for(int j = 1; j <= 2; j ++){
//                gameIDs += (j + (i * 25) ) + ",";
//            }
//            each call to getAPIData adds a new game object into a list; the list is then written into a json file at the end
//            within this function there is a helper function processGame that is incharge of formatting each indivdual game object data
//            this will be useful for the first time the data gets added into the database the first time
//            however once the games have already been added what will need to happen is the deals list will need to be updated
            getAPIData(apiURL, gameList);

//        }

        try {
            mapper.writeValue(new File("usersData.json"), gameList);
            System.out.println("JSON data is written to the file successfully");
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

//function that makes the indivdual request of 25 game intervals and then breaks the 25 games
    private  static void getAPIData(String apiURL, List<Map<String, Object>> gameList ){


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL)) // Replace with your API URL
                .build();


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = mapper.readTree(response.body());
//            List will be used to write all of the game items into a json file all at once

            rootNode.fields().forEachRemaining(entry -> {

//                Extraction of the game data fromthe response body that was covnerted to a JSONode
                JsonNode value = entry.getValue();
//                Deals is a list with in the
                JsonNode dealsNode = value.get("deals");
                List<Deal> deals = null;

                if (dealsNode != null && dealsNode.isArray()){
                    // Convert 'dealsNode' to a List of Deal objects

//                    After deals is created from the data extraced from the deals node
//                    each deal from each respective store has the date set to the date of when the api was run
                   deals = mapper.convertValue(dealsNode, new TypeReference<List<Deal>>() {});
                    for (Deal deal : deals) {
                       deal.setDate();
                    }
                }
//                System.out.println("JSON OBJECT:");
//                System.out.println(value.get("deals"));

                Map<String, Object> gameData = processGame(value,deals);
//              BACKEND API CALL:
//
                String requestBody;

//Create new function here
                System.out.println("WRITING TO DATABASE");

                try {
                    requestBody = mapper.writeValueAsString(gameData);
                    System.out.println("WRITING");
                    System.out.println(gameData.toString());
                    HttpRequest backEndApi = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/api/v1/games/add_game"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    HttpResponse<String> bresponse = client.send(backEndApi, HttpResponse.BodyHandlers.ofString());

                    // Output the status code and response body
                    System.out.println("Response status code: " + bresponse.statusCode());
                    System.out.println("Response body: " + bresponse.body());

                } catch (IOException | InterruptedException e) {
                    System.out.println("ERROR GETTING DATA FROM API CALL");

                    throw new RuntimeException(e);
                }
                gameList.add(gameData);
            });

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

//    Creates a new object that will be able to be stored with in the formatted JSON file that can
//    be used to map these objects into components
    private static Map<String, Object> processGame(JsonNode gameNode,  List<Deal> deals) {

        String title = gameNode.get("info").get("title").asText();
        String gameID =  gameNode.get("info").get("steamAppID").asText();
        Map<String, Object> gameData = new HashMap<>();
        gameData.put("steamAppID",gameID);
        gameData.put("title", title);
        gameData.put("deals", deals);

        return gameData;
    }
}
