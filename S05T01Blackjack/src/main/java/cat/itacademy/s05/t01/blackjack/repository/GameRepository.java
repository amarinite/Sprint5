package cat.itacademy.s05.t01.blackjack.repository;

import cat.itacademy.s05.t01.blackjack.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}