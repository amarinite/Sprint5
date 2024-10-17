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
        dealCardToPlayer(player);  // Primera carta
        dealCardToPlayer(player);  // Segunda carta
    }

    public void dealCardToPlayer(Player player) {
        if (canDealCard(player)) {
            player.getHand().add(deck.removeFirst());
            checkPlayerBust(player);
        }
    }

    // Método para verificar si se puede repartir una carta al jugador
    private boolean canDealCard(Player player) {
        return !deck.isEmpty() && !player.isBust() && !player.isStanding();
    }

    // Método para que un jugador se plante
    public void playerStand(Player player) {
        player.setStanding(true);
    }

    // Comprobar si un jugador ha superado 21 puntos
    private void checkPlayerBust(Player player) {
        if (player.calculateHandValue() > MAX_SCORE) {
            player.setBust(true);
        }
    }

    // Verificar si todos los jugadores han terminado (busteado o decidido plantarse)
    public boolean allPlayersDone() {
        return players.stream().allMatch(player -> player.isBust() || player.isStanding());
    }

    // Determinar si el juego ha terminado y quién es el ganador
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