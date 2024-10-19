package cat.itacademy.s05.t01.blackjack.controller;

import cat.itacademy.s05.t01.blackjack.model.Game;
import cat.itacademy.s05.t01.blackjack.model.Player;
import cat.itacademy.s05.t01.blackjack.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@Tag(name = "Game API", description = "API for managing blackjack games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Operation(summary = "Create a new game", description = "Creates a new game instance")
    @PostMapping("/new")
    public Mono<Game> createNewGame() {
        return gameService.createNewGame();
    }

    @Operation(summary = "Get game details", description = "Retrieve the details of an existing game by its ID")
    @GetMapping("/{gameId}")
    public Mono<Game> getGameDetails(@PathVariable String gameId) {
        return gameService.getGameDetails(gameId);
    }

    @Operation(summary = "Add player to a game", description = "Adds a new player to an existing game")
    @PostMapping("/{gameId}/add-player")
    public Mono<Game> addPlayerToGame(@PathVariable String gameId, @RequestParam String playerName) {
        Player player = new Player(playerName);
        return gameService.addPlayerToGame(gameId, player);
    }

    @Operation(summary = "Play the game", description = "Allows a player to either draw a card or stand")
    @PostMapping("/{gameId}/play")
    public Mono<Game> playGame(@PathVariable String gameId, @RequestParam Long playerId, @RequestParam boolean playerWantsCard) {
        return gameService.playGame(gameId, playerId, playerWantsCard);
    }

    @Operation(summary = "Delete a game", description = "Deletes an existing game by its ID")
    @DeleteMapping("/{id}/delete")
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id);
    }

}