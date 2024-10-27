package cat.itacademy.s05.t01.blackjack.service.impl;

import cat.itacademy.s05.t01.blackjack.enums.CardValue;
import cat.itacademy.s05.t01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.blackjack.model.Card;
import cat.itacademy.s05.t01.blackjack.model.Game;
import cat.itacademy.s05.t01.blackjack.model.Player;
import cat.itacademy.s05.t01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.blackjack.repository.PlayerRepository;
import cat.itacademy.s05.t01.blackjack.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final int MAX_SCORE = 21;

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public Mono<Game> createNewGame() {
        Game game = new Game();
        game.setDeck(DeckServiceImpl.generateShuffledDeck());
        return gameRepository.save(game);
    }

    public Mono<Game> getGameDetails(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + id + " not found")));
    }

    public Mono<Game> addPlayerToGame(String gameId, Player player) {
        return playerRepository.save(player)
                .flatMap(savedPlayer -> gameRepository.findById(gameId)
                        .flatMap(game -> {
                            game.getPlayers().add(savedPlayer);
                            dealInitialCards(game, savedPlayer);
                            return gameRepository.save(game);
                        }));
    }

    private void dealInitialCards(Game game, Player player) {
        dealCardToPlayer(game, player);
        dealCardToPlayer(game, player);
    }

    public Mono<Game> playGame(String gameId, Long playerId, boolean playerWantsCard) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    Player player = game.getPlayers().stream()
                            .filter(p -> p.getId().equals(playerId))
                            .findFirst()
                            .orElseThrow(() -> new PlayerNotFoundException("Player with ID " + playerId + " not found"));

                    if (playerWantsCard && !player.isBust() && !player.isStanding()) {
                        dealCardToPlayer(game, player);
                    } else {
                        player.setStanding(true);
                    }

                    if (allPlayersDone(game)) {
                        determineWinner(game);
                    }
                    return gameRepository.save(game);
                });
    }

    private void dealCardToPlayer(Game game, Player player) {
        if (canDealCard(game, player)) {
            player.getHand().add(game.getDeck().removeFirst());
            checkPlayerBust(player);
        }
    }

    private boolean canDealCard(Game game, Player player) {
        return !game.getDeck().isEmpty() && !player.isBust() && !player.isStanding();
    }

    private boolean allPlayersDone(Game game) {
        return game.getPlayers().stream().allMatch(player -> player.isBust() || player.isStanding());
    }

    private void determineWinner(Game game) {
        Player winner = null;
        int highestScore = 0;

        for (Player player : game.getPlayers()) {
            int playerScore = calculateHandValue(player);
            if (!player.isBust() && playerScore > highestScore) {
                highestScore = playerScore;
                winner = player;
            }
        }

        if (winner != null) {
            game.setWinner(winner.getName());
        } else {
            game.setWinner("No winner, all players busted.");
        }

        game.setGameOver(true);
    }

    private void checkPlayerBust(Player player) {
        if (calculateHandValue(player) > MAX_SCORE) {
            player.setBust(true);
        }
    }

    private int calculateHandValue(Player player) {
        int total = 0;
        int aceCount = 0;

        for (Card card : player.getHand()) {
            total += card.getNumericValue();
            if (card.getCardValue() == CardValue.ACE) {
                aceCount++;
            }
        }

        while (total > MAX_SCORE && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }

    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + gameId + " not found")))
                .flatMap(existingGame -> gameRepository.deleteById(gameId));
    }
}