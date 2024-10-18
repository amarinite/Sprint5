package cat.itacademy.s05.t01.blackjack.model;

import cat.itacademy.s05.t01.blackjack.enums.Value;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table("players")
public class Player {
    @Id
    private Long id;
    private String name;
    @Column("total_wins")
    private int totalWins;

    @Transient
    private List<Card> hand;
    @Transient
    private boolean bust;
    @Transient
    private boolean standing;


    public Player() {
        this.name = "";
        this.hand = new ArrayList<>();
        this.bust = false;
        this.standing = false;
        this.totalWins = 0;
    }

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.bust = false;
        this.standing = false;
        this.totalWins = 0;
    }

    public void incrementWins() {
        this.totalWins++;
    }

    public int calculateHandValue() {
        int total = 0;
        int aceCount = 0;

        for (Card card : hand) {
            total += card.getNumericValue();
            if (card.getValue() == Value.ACE) {
                aceCount++;
            }
        }
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }
}