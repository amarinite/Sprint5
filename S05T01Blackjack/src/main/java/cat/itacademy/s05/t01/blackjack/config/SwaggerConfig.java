package cat.itacademy.s05.t01.blackjack.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi gameApi() {
        return GroupedOpenApi.builder()
                .group("Game API")
                .pathsToMatch("/game/**")
                .build();
    }

    @Bean
    public GroupedOpenApi playerApi() {
        return GroupedOpenApi.builder()
                .group("Player API")
                .pathsToMatch("/player/**")
                .build();
    }
}
