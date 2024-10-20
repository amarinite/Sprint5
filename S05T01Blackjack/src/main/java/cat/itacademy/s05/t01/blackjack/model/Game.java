package cat.itacademy.s05.t01.blackjack.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "games")
@Getter
@Setter
public class Game {
    private static final int MAX_SCORE = 21;
    @Id
    private String id;
    private List<Player> players;
    private List<Card> deck;
    private boolean gameOver;
    private String winner;

    public Game() {
        this.players = new ArrayList<>();
        this.deck = Deck.generateShuffledDeck();
        this.gameOver = false;
        this.winner = null;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void dealInitialCards(Player player) {
        dealCardToPlayer(player);
        dealCardToPlayer(player);
    }

    public void dealCardToPlayer(Player player) {
        if (canDealCard(player)) {
            player.getHand().add(deck.removeFirst());
            checkPlayerBust(player);
        }
    }

    private boolean canDealCard(Player player) {
        return !deck.isEmpty() && !player.isBust() && !player.isStanding();
    }

    public void playerStand(Player player) {
        player.setStanding(true);
    }

    private void checkPlayerBust(Player player) {
        if (player.calculateHandValue() > MAX_SCORE) {
            player.setBust(true);
        }
    }

    public boolean allPlayersDone() {
        return players.stream().allMatch(player -> player.isBust() || player.isStanding());
    }

    public void determineWinner() {
        Player winner = null;
        int highestScore = 0;

        for (Player player : players) {
            int playerScore = player.calculateHandValue();
            if (!player.isBust() && playerScore > highestScore) {
                highestScore = playerScore;
                winner = player;
            }
        }

        if (winner != null) {
            setWinner(winner.getName());
        } else {
            setWinner("No winner, all players busted.");
        }

        setGameOver(true);
    }
}