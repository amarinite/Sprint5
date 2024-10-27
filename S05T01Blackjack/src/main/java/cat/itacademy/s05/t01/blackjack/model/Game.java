package cat.itacademy.s05.t01.blackjack.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "games")
@Data
public class Game {
    @Id
    private String id;
    private List<Player> players = new ArrayList<>();
    private List<Card> deck = new ArrayList<>();
    private boolean gameOver = false;
    private String winner;
}