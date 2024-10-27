package cat.itacademy.s05.t01.blackjack.service;

import cat.itacademy.s05.t01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.model.Game;
import cat.itacademy.s05.t01.blackjack.repository.GameRepository;
import cat.itacademy.s05.t01.blackjack.repository.PlayerRepository;
import cat.itacademy.s05.t01.blackjack.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewGame() {
        Game mockGame = new Game();
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(mockGame));

        Mono<Game> result = gameService.createNewGame();

        StepVerifier.create(result)
                .expectNext(mockGame)
                .verifyComplete();
    }

    @Test
    public void testGetGameDetails_GameExists() {
        String gameId = "123";
        Game mockGame = new Game();
        when(gameRepository.findById(gameId)).thenReturn(Mono.just(mockGame));

        Mono<Game> result = gameService.getGameDetails(gameId);

        StepVerifier.create(result)
                .expectNext(mockGame)
                .verifyComplete();
    }

    @Test
    public void testGetGameDetails_GameNotFound() {
        String gameId = "invalid-id";
        when(gameRepository.findById(gameId)).thenReturn(Mono.empty());

        Mono<Game> result = gameService.getGameDetails(gameId);

        StepVerifier.create(result)
                .expectError(GameNotFoundException.class)
                .verify();
    }
}
