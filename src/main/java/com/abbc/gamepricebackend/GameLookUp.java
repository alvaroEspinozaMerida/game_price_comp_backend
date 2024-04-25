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
//Important Note:
// Mapss are necessary for sending data through json format through https request
public class GameLookUp {
    public static void main(String[] args) {

        List<Map<String, Object>> gameList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
        mapper.registerModule(new JavaTimeModule());


//        Call to the API has a limit of 25 game ids
        String apiURL = "https://www.cheapshark.com/api/1.0/games?ids=1,2,3,4,5,6,7,8,9,10,11,12,13,15";
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

    /**
     * Makes the request to the backend spring REST api for writing to the MONGODB database
     * this is essential for adding in the games in the first time; afterwards the
     * functions will need to be updated for updating the game data only for the deals data
     *
     * @param gameData game data for an indivdual game object
     *///

    private static void writeToDataBase(Map<String, Object> gameData){

        HttpClient client = HttpClient.newHttpClient();
        String requestBody;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        try {
            requestBody = mapper.writeValueAsString(gameData);
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

    }



    /**
     * Makes the request to the api, extracts the data for each individual game object
     * the response from the api is stored within the @rootNode,
     * each game object within this response is then parsed out with the @value,
     * inside each of the @value all of its  deals are extracted and placed within the @dealsNode,
     * each individual deal is then given a time stamp and turned in an individual deal objects
     * @param apiURL api url for a list of games based on the ids determined the api
     * @param gameList the second number to add
     *///
    private  static void getAPIData(String apiURL, List<Map<String, Object>> gameList ){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .build();

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = mapper.readTree(response.body());
            rootNode.fields().forEachRemaining(entry -> {
                JsonNode value = entry.getValue();
//                Deals is a list with in the
                JsonNode dealsNode = value.get("deals");
                List<Deal> deals = null;
                if (dealsNode != null && dealsNode.isArray()){
                   deals = mapper.convertValue(dealsNode, new TypeReference<List<Deal>>() {});
                    for (Deal deal : deals) {
                       deal.setDate();
                    }
                }

                Map<String, Object> gameData = processGame(value,deals);
                writeToDataBase(gameData);
                gameList.add(gameData);
            });

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * Creates a map for an individual game object that contains data for the Game object
     * and Deals with time stamps added to it
     * @param gameNode data for the individual game extracted from the cheap shark api
     * @param deals list of deals for an individual game object
     *///
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
