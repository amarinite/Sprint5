package cat.itacademy.s05.t01.blackjack.controller;

import cat.itacademy.s05.t01.blackjack.model.Player;
import cat.itacademy.s05.t01.blackjack.service.impl.PlayerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@Tag(name = "Player API", description = "API for managing players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerServiceImpl playerService;

    @Operation(summary = "Change player name", description = "Change the name of an existing player")
    @PutMapping("/{playerId}")
    public Mono<Player> changePlayerName(@PathVariable Long playerId, @RequestParam String newName) {
        return playerService.changePlayerName(playerId, newName);
    }

    @Operation(summary = "Get player ranking", description = "Retrieve the ranking of players based on wins")
    @GetMapping("/ranking")
    public Flux<Player> getPlayerRanking() {
        return playerService.getPlayerRanking();
    }
}