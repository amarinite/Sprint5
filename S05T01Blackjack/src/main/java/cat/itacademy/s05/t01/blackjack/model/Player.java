package cat.itacademy.s05.t01.blackjack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "players")
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    private Long id;
    private String name;
    private int totalWins;

    @Transient
    private List<Card> hand = new ArrayList<>();
    @Transient
    private boolean bust = false;
    @Transient
    private boolean standing = false;

    public Player(String name) {
        this.name = name;
        this.totalWins = 0;
    }
}