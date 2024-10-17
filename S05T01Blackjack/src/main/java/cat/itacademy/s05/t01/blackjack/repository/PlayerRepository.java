package cat.itacademy.s05.t01.blackjack.repository;

import cat.itacademy.s05.t01.blackjack.model.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
}
