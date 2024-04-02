package com.abbc.gamepricebackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class GameLookUp {


    public static void main(String[] args) {

//        Create Formatted String that Gets Games 1-100 of the API to store within the JSON file that can be
//        sent to a mongodb data base

        String apiURL = "https://www.cheapshark.com/api/1.0/games?ids=";
        String gameIDs = "";
        int counter = 1;
        for(int i = 0; i <= 3 ; i ++){

            gameIDs = "";
            for(int j = 1; j <= 25; j ++){
                gameIDs += (j + (i * 25) ) + ",";
            }
            getAPIData(apiURL+gameIDs);

        }


    }


    private  static void getAPIData(String apiURL){


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL)) // Replace with your API URL
                .build();
        ObjectMapper mapper = new ObjectMapper();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode rootNode = mapper.readTree(response.body());


            rootNode.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                processGame(value);

                // Now you have each object, you can process it as needed
            });

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }




    }




    private static void processGame(JsonNode gameNode) {
        // Assuming you want to extract the title and cheapest price ever for demonstration
        String title = gameNode.get("info").get("title").asText();
        JsonNode cheapestPriceEverNode = gameNode.get("cheapestPriceEver");
        String cheapestPrice = cheapestPriceEverNode.get("price").asText();
        long date = cheapestPriceEverNode.get("date").asLong();



//        Write to the JSON FILE
        Map<String, Object> gameData = new HashMap<>();

        gameData.put("Title", title);
        gameData.put("Cheapest Price", cheapestPrice);

        ObjectMapper mapper = new ObjectMapper();


        try {
            mapper.writeValue(new File("userData.json"), gameData);
            System.out.println("JSON data is written to the file successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }






    }
}
