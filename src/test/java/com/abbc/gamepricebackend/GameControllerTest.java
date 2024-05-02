package com.abbc.gamepricebackend;

import DTO.Game;
import DTO.Deal;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {


    @Test
    void fetchAllGames() {
        GameService gameServiceMock = mock(GameService.class);
        Deal deal1 = new Deal("Store 1", LocalDate.now(), "Deal 1", 20.0, 30.0, 10.0);
        Deal deal2 = new Deal("Store 2", LocalDate.now(), "Deal 2", 15.0, 25.0, 10.0);
        Deal deal3 = new Deal("Store 3", LocalDate.now(), "Deal 3", 10.0, 20.0, 10.0);

        List<Game> expectedGames = Arrays.asList(
                new Game("Game 1", 1, Collections.singletonList(deal1)),
                new Game("Game 2", 2, Collections.singletonList(deal2)),
                new Game("Game 3", 3, Collections.singletonList(deal3))
        );

        when(gameServiceMock.getAllGames()).thenReturn(expectedGames);

        GameController gameController = new GameController(gameServiceMock);

        List<Game> games = gameController.fetchAllGames().getBody();

        assertNotNull(games, "This list of games should not be null");


        assertEquals(expectedGames.size(), games.size());
        assertEquals(expectedGames.get(0), games.get(0));
        assertEquals(expectedGames.get(1), games.get(1));

        //Test for empty list
        when(gameServiceMock.getAllGames()).thenReturn(Collections.emptyList());
        List<Game> emptyGames = gameController.fetchAllGames().getBody();
        assertNotNull(emptyGames);
        assertEquals(0, emptyGames.size());


        //Test for exception
        when(gameServiceMock.getAllGames()).thenThrow(new RuntimeException("Database unavailable"));
        assertThrows(RuntimeException.class, gameController::fetchAllGames);
    }

    @Test
    void addGame() {
        GameService gameServiceMock = mock(GameService.class);

        Game newGame = new Game("Game 4", 4, null);

        when(gameServiceMock.addGame(newGame)).thenReturn(newGame);

        GameController gameController = new GameController(gameServiceMock);
        Game addedGame = gameController.addGame(newGame);
        assertEquals(newGame, addedGame);

    }

    @Test
    void findGameByTitle() {
        GameService gameServiceMock = mock(GameService.class);

        String title = "Game 1";
        Game foundGame = new Game("Game 1" ,1, null);
        Optional<Game> optionalGame = Optional.of(foundGame);

        when(gameServiceMock.findGameByTitle(title)).thenReturn(optionalGame);
        GameController gameController = new GameController(gameServiceMock);
        Optional<Game> gameOptional = gameController.findGameByTitle(title);

        assertTrue(gameOptional.isPresent());
        assertEquals(foundGame, gameOptional.get());

        //Test for title not found
        String nonExistentTitle = "Non-existent Game";
        when(gameServiceMock.findGameByTitle(nonExistentTitle)).thenReturn(Optional.empty());
        Optional<Game> nonExistentGameOptional = gameController.findGameByTitle(nonExistentTitle);
        assertFalse(nonExistentGameOptional.isPresent());

        // Test for title with maximum length
        String maxTitle = "a".repeat(255);
        when(gameServiceMock.findGameByTitle(maxTitle)).thenReturn(optionalGame);
        Optional<Game> maxTitleGameOptional = gameController.findGameByTitle(maxTitle);
        assertTrue(maxTitleGameOptional.isPresent());
        assertEquals(foundGame, maxTitleGameOptional.get());
    }

    @Test
    void updateGameDeals() {
        //Create mock of gameService
        GameService gameServiceMock = mock(GameService.class);
        //Get current date
        LocalDate currentDate = LocalDate.now();
        Deal deal1 = new Deal("Store 1", "Store Name 1", currentDate, "Deal 1", 15.0, 35.0, 20.0);
        //create an existingGame with some data
        Game existingGame = new Game("Existing Game", 1, "thumb", Collections.singletonList(deal1), Collections.singletonList("screenshot1"));
        //Create a new deal that will be added
        Deal newDeal = new Deal("Store 4", "Store Name 4", currentDate, "Deal 4", 25.0, 35.0, 10.0);
        //Define behavior of the mock when its asked to find a game by the ID
        when(gameServiceMock.findGameByID(1)).thenReturn(Optional.of(existingGame));
        //Define behavior of the mock when it's asked to update game deals
        when(gameServiceMock.updateGameDeals(1, newDeal)).thenReturn(ResponseEntity.ok(existingGame));
        //create instance of gameController
        GameController gameController = new GameController(gameServiceMock);
        //Call method to update game deals and get responseEntity
        ResponseEntity<Game> responseEntity = gameController.updateGameDeals(1, newDeal);
        System.out.println("Response Entity: " + responseEntity);
        System.out.println("existingGame: " + existingGame);
        //Verify that the returned game is the same as the existing game
        assertEquals(existingGame, responseEntity.getBody());

        Game updateGame = responseEntity.getBody();
        System.out.println("updateGame: " + updateGame);
        assertNotNull(updateGame);
        int initialSize = existingGame.getDeals().size();
        int finalSize = updateGame.getDeals().size();
        assertEquals(initialSize + 1, finalSize);



    }

}