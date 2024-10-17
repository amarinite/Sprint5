package cat.itacademy.s05.t01.blackjack.service;

import cat.itacademy.s05.t01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.blackjack.model.Game;
import cat.itacademy.s05.t01.blackjack.model.Player;
import cat.itacademy.s05.t01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.blackjack.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Mono<Game> createNewGame() {
        Game game = new Game();
        return gameRepository.save(game);
    }

    public Mono<Game> getGameDetails(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + id + " not found")));
    }

    public Mono<Game> addPlayerToGame(String gameId, Player player) {
        // Guardamos el jugador en la base de datos antes de añadirlo al juego
        return playerRepository.save(player)   // Guardamos el jugador en la tabla "players"
                .flatMap(savedPlayer -> gameRepository.findById(gameId)
                        .flatMap(game -> {
                            game.addPlayer(savedPlayer); // Añadimos el jugador al juego
                            game.dealInitialCards(savedPlayer); // Repartimos las cartas iniciales
                            return gameRepository.save(game);   // Guardamos el juego con el jugador añadido
                        })
                );
    }


    public Mono<Game> playGame(String gameId, Long playerId, boolean playerWantsCard) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    Player player = game.getPlayers().stream()
                            .filter(p -> p.getId().equals(playerId))
                            .findFirst()
                            .orElseThrow(() -> new PlayerNotFoundException("Player with ID " + playerId + " not found"));

                    // Si el jugador quiere otra carta y no está "bust" ni "plantado"
                    if (playerWantsCard && !player.isBust() && !player.isStanding()) {
                        game.dealCardToPlayer(player);
                    } else {
                        // El jugador decide plantarse
                        game.playerStand(player);
                    }

                    // Verificar si todos los jugadores han terminado
                    if (game.allPlayersDone()) {
                        game.determineWinner();
                    }

                    return gameRepository.save(game);
                });
    }


    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.deleteById(gameId);
    }



    private int calculateTotalScore(List<Player> players) {
        return players.stream().mapToInt(Player::calculateHandValue).sum();
    }
}