package cat.itacademy.s05.t01.blackjack.model;

import cat.itacademy.s05.t01.blackjack.enums.Suit;
import cat.itacademy.s05.t01.blackjack.enums.CardValue;
import lombok.Getter;

@Getter
public class Card {
    private final CardValue cardValue;
    private final Suit suit;

    public Card(CardValue cardValue, Suit suit) {
        this.cardValue = cardValue;
        this.suit = suit;
    }

    public int getNumericValue() { return cardValue.getCardValue(); }
}