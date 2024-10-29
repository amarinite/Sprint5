package com.itacademy.S05T02VirtualPet.security;

import com.itacademy.S05T02VirtualPet.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Authorization header or invalid prefix in request to {}", request.getURI());
            return chain.filter(exchange);
        }

        String jwt = authHeader.substring(7);
        log.info("Extracted JWT from Authorization header for request to {}", request.getURI());

        return jwtUtil.extractUsername(jwt)
                .doOnNext(username -> log.info("Extracted username: {}", username))
                .flatMap(username -> userDetailsService.findByUsername(username)
                        .doOnNext(userDetails -> log.info("Found user details for username: {}", username))
                        .flatMap(userDetails -> jwtUtil.isTokenValid(jwt, userDetails)
                                .flatMap(isValid -> {
                                    if (isValid) {
                                        log.info("Valid token for user: {}", username);
                                        UsernamePasswordAuthenticationToken auth =
                                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                        SecurityContext securityContext = new SecurityContextImpl(auth);

                                        return chain.filter(exchange)
                                                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
                                                .doOnSuccess(unused -> log.info("Security context set for user: {}", username));
                                    } else {
                                        log.warn("Invalid token for user: {}", username);
                                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
                                    }
                                })
                        )
                        .doOnError(error -> log.error("Error finding user details for username: {}: {}", username, error.getMessage()))
                )
                .switchIfEmpty(chain.filter(exchange))
                .doOnError(error -> log.error("Authentication error: {}", error.getMessage()));
    }
}









