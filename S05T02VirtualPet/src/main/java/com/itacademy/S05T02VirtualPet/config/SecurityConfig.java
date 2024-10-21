package com.itacademy.S05T02VirtualPet.config;

import com.itacademy.S05T02VirtualPet.util.JWTAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    private final JWTAuthenticationManager authenticationManager;

    public SecurityConfig(JWTAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/login", "/signup").permitAll()
                .anyExchange().authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .addFilterAt(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public WebFilter jwtAuthenticationWebFilter() {
        return (exchange, chain) -> {
            return authenticationManager.authenticationConverter().convert(exchange)
                    .flatMap(authentication -> {
                        if (authentication.isAuthenticated()) {
                            return chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                        }
                        return chain.filter(exchange);
                    })
                    .switchIfEmpty(chain.filter(exchange))
                    .doOnError(error -> {
                        log.error("Authentication error: {}", error.getMessage());
                    });
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}





