package cat.itacademy.s05.t01.blackjack.service.impl;

import cat.itacademy.s05.t01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.blackjack.model.Player;
import cat.itacademy.s05.t01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.blackjack.repository.PlayerRepository;
import cat.itacademy.s05.t01.blackjack.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public Mono<Player> changePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player with ID " + playerId + " not found")))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player).flatMap(this::updateGamesWithPlayer);
                });
    }

    private Mono<Player> updateGamesWithPlayer(Player player) {
        return gameRepository.findAll()
                .flatMap(game -> {
                    if (game.getPlayers().stream().anyMatch(p -> p.getId().equals(player.getId()))) {
                        return gameRepository.save(game).then(Mono.just(player));
                    }
                    return Mono.just(player);
                })
                .next();
    }

    private void incrementWins(Player player) {
        player.setTotalWins(player.getTotalWins() + 1);
    }

    public Flux<Player> getPlayerRanking() {
        Map<Long, Player> playerWinsMap = new HashMap<>();

        return gameRepository.findAll()
                .flatMap(game -> {
                    String winnerName = game.getWinner();
                    return Flux.fromIterable(game.getPlayers())
                            .filter(player -> player.getName().equals(winnerName))
                            .doOnNext(player -> playerWinsMap.merge(player.getId(), player, (existing, newPlayer) -> {
                                incrementWins(existing);
                                return existing;
                            }));
                })
                .thenMany(Flux.fromIterable(playerWinsMap.values()))
                .sort((p1, p2) -> Integer.compare(p2.getTotalWins(), p1.getTotalWins()));
    }
}


