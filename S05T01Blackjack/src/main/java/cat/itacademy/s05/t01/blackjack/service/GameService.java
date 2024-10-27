package cat.itacademy.s05.t01.blackjack.service;

import cat.itacademy.s05.t01.blackjack.model.Game;
import cat.itacademy.s05.t01.blackjack.model.Player;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createNewGame();
    Mono<Game> getGameDetails(String id);
    Mono<Game> addPlayerToGame(String gameId, Player player);
    Mono<Game> playGame(String gameId, Long playerId, boolean playerWantsCard);
    Mono<Void> deleteGame(String gameId);
}
