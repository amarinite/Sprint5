package cat.itacademy.s05.t01.blackjack.service;

import cat.itacademy.s05.t01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.blackjack.model.Player;
import cat.itacademy.s05.t01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.blackjack.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlayerService {

    @Autowired
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    public Mono<Player> changePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player with ID " + playerId + " not found")))
                .flatMap(player -> updatePlayerName(player, newName)
                        .flatMap(this::updateGamesWithPlayer));
    }

    private Mono<Player> updatePlayerName(Player player, String newName) {
        player.setName(newName);
        return playerRepository.save(player);
    }

    private Mono<Player> updateGamesWithPlayer(Player player) {
        return gameRepository.findAll()
                .flatMap(game -> {
                    if (game.getPlayers().stream().anyMatch(p -> p.getId().equals(player.getId()))) {
                        return gameRepository.save(game)
                                .then(Mono.just(player));
                    }
                    return Mono.just(player);
                })
                .next();
    }

    public Flux<Player> getPlayerRanking() {
        Map<Long, Player> playerWinsMap = new HashMap<>();

        return gameRepository.findAll() // Obtener todos los juegos
                .flatMap(game -> {
                    String winnerName = game.getWinner(); // Obtener el nombre del ganador
                    return Flux.fromIterable(game.getPlayers())
                            .filter(player -> player.getName().equals(winnerName)) // Filtrar por el jugador ganador
                            .doOnNext(player -> {
                                // Actualizar el número de victorias del jugador en el mapa
                                playerWinsMap.compute(player.getId(), (id, existingPlayer) -> {
                                    if (existingPlayer == null) {
                                        player.incrementWins(); // Incrementar victoria si es nuevo
                                        return player;
                                    } else {
                                        existingPlayer.incrementWins(); // Sumar victoria si ya existe
                                        return existingPlayer;
                                    }
                                });
                            });
                })
                .thenMany(Flux.fromIterable(playerWinsMap.values())) // Convertir el mapa a un flujo de jugadores
                .sort((p1, p2) -> Integer.compare(p2.getTotalWins(), p1.getTotalWins())); // Ordenar por número de victorias
    }
}

