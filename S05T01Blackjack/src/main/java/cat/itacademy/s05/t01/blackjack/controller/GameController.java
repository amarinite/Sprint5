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

    @GetMapping("/{id}")
    public Mono<Game> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id);
    }

    // Endpoint para añadir un jugador al juego y repartirle cartas iniciales
    @PostMapping("/{gameId}/add-player")
    public Mono<Game> addPlayerToGame(@PathVariable String gameId, @RequestParam Long playerId, @RequestParam String playerName) {
        Player player = new Player(playerId, playerName); // Crear el jugador
        return gameService.addPlayerToGame(gameId, player); // Añadirlo al juego y repartir cartas iniciales
    }

    @PostMapping("/{id}/play")
    public Mono<Game> playGame(@PathVariable String id, @RequestParam Long playerId, @RequestParam boolean playerWantsCard) {
        return gameService.playGame(id, playerId, playerWantsCard);
    }

    @DeleteMapping("/{id}/delete")
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id);
    }

}