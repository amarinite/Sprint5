package cat.itacademy.s05.t01.blackjack.enums;

import lombok.Getter;

@Getter
public enum CardValue {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9),
    TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);

    private final int cardValue;

    CardValue(int cardValue) {
        this.cardValue = cardValue;
    }
}
