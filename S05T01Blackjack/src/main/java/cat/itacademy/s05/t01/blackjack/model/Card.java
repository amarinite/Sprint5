package cat.itacademy.s05.t01.blackjack.model;

import cat.itacademy.s05.t01.blackjack.enums.Suit;
import cat.itacademy.s05.t01.blackjack.enums.Value;
import lombok.Data;


public class Card {
    private final Value value;
    private final Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public Value getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getNumericValue() {
        return switch (value) {
            case ACE -> 11;
            case JACK, QUEEN, KING -> 10;
            default -> Integer.parseInt(value.getSymbol());
        };
    }
}