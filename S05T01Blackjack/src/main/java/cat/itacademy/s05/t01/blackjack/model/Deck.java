package cat.itacademy.s05.t01.blackjack.model;

import cat.itacademy.s05.t01.blackjack.enums.Suit;
import cat.itacademy.s05.t01.blackjack.enums.Value;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Deck {
    public static List<Card> generateShuffledDeck() {
        List<Card> deck = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                deck.add(new Card(value, suit));
            }
        }

        Collections.shuffle(deck);
        return deck;
    }
}