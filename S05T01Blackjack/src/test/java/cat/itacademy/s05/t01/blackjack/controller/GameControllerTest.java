package cat.itacademy.s05.t01.blackjack.controller;

import cat.itacademy.s05.t01.blackjack.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.model.Game;
import cat.itacademy.s05.t01.blackjack.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GameService gameService;

    private Game mockGame;

    @BeforeEach
    void setUp() {
        mockGame = new Game();
        mockGame.setId("1");
    }

    @Test
    public void testCreateNewGame() {
        Mockito.when(gameService.createNewGame()).thenReturn(Mono.just(mockGame));

        webTestClient.post()
                .uri("/game/new")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class)
                .isEqualTo(mockGame);
    }

    @Test
    public void testGetGameDetails_Success() {
        String gameId = "1";
        Mockito.when(gameService.getGameDetails(gameId)).thenReturn(Mono.just(mockGame));

        webTestClient.get()
                .uri("/game/" + gameId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class)
                .isEqualTo(mockGame);
    }

    @Test
    public void testGetGameDetails_NotFound() {
        String gameId = "invalid-id";
        Mockito.when(gameService.getGameDetails(gameId)).thenReturn(Mono.error(new GameNotFoundException("Game not found")));

        webTestClient.get()
                .uri("/game/" + gameId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

}

