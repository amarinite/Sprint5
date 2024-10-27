package cat.itacademy.s05.t01.blackjack.service.impl;

import cat.itacademy.s05.t01.blackjack.enums.Suit;
import cat.itacademy.s05.t01.blackjack.enums.CardValue;
import cat.itacademy.s05.t01.blackjack.model.Card;
import cat.itacademy.s05.t01.blackjack.service.DeckService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeckServiceImpl implements DeckService {
    public static List<Card> generateShuffledDeck() {
        List<Card> deck = Arrays.stream(Suit.values())
                .flatMap(suit -> Arrays.stream(CardValue.values())
                        .map(cardValue -> new Card(cardValue, suit)))
                .collect(Collectors.toList());

        Collections.shuffle(deck);
        return deck;
    }
}
