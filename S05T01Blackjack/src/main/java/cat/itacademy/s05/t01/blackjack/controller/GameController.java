package cat.itacademy.s05.t01.blackjack.controller;

import cat.itacademy.s05.t01.blackjack.model.Game;
import cat.itacademy.s05.t01.blackjack.model.Player;
import cat.itacademy.s05.t01.blackjack.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/new")
    public Mono<Game> createNewGame() {
        return gameService.createNewGame();
    }

    @GetMapping("/{gameId}")
    public Mono<Game> getGameDetails(@PathVariable String gameId) {
        return gameService.getGameDetails(gameId);
    }

    @PostMapping("/{gameId}/add-player")
    public Mono<Game> addPlayerToGame(@PathVariable String gameId, @RequestParam String playerName) {
        Player player = new Player(playerName);
        return gameService.addPlayerToGame(gameId, player);
    }

    @PostMapping("/{gameId}/play")
    public Mono<Game> playGame(@PathVariable String gameId, @RequestParam Long playerId, @RequestParam boolean playerWantsCard) {
        return gameService.playGame(gameId, playerId, playerWantsCard);
    }

    @DeleteMapping("/{id}/delete")
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id);
    }

}