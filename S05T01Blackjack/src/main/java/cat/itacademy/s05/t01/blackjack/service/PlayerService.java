package cat.itacademy.s05.t01.blackjack.service;

import cat.itacademy.s05.t01.blackjack.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<Player> changePlayerName(Long playerId, String newName);
    Flux<Player> getPlayerRanking();
}
