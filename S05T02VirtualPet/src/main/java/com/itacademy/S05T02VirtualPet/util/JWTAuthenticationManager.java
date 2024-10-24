package com.itacademy.S05T02VirtualPet.util;


import com.itacademy.S05T02VirtualPet.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;
    private final UserServiceImpl userService;

    public JWTAuthenticationManager(JWTUtil jwtUtil, UserServiceImpl userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.extractUsername(token);

        return userService.findByUsername(username)
                .filter(userDetails -> jwtUtil.validateToken(token, userDetails.getUsername()))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ))
                .cast(Authentication.class)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid JWT token")));
    }

    public ServerAuthenticationConverter authenticationConverter() {
        return exchange -> {
            String token = extractToken(exchange);
            log.debug("Extracted token: {}", token);
            if (token != null) {
                String username = jwtUtil.extractUsername(token);
                log.debug("Extracted username: {}", username);
                if (username != null) {
                    log.info("Authentication successful for user: {}", username);
                    return Mono.just(new UsernamePasswordAuthenticationToken(username, token, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
                } else {
                    log.warn("No username found in the token.");
                }
            } else {
                log.warn("No token found in the request.");
            }
            return Mono.empty();
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        log.warn("No JWT token found in the Authorization header.");
        return null;
    }
}





