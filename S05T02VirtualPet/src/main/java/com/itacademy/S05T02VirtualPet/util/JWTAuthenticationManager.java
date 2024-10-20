package com.itacademy.S05T02VirtualPet.util;


import com.itacademy.S05T02VirtualPet.service.UserService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    public JWTAuthenticationManager(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.extractUsername(token);

        return userService.findByUsername(username)
                .filter(userDetails -> jwtUtil.validateToken(token, userDetails.getUsername()))  // Validar el token
                .map(userDetails -> (Authentication) new UsernamePasswordAuthenticationToken(  // Convertir a Authentication
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid JWT token")));  // Excepción manejada
    }

    public ServerAuthenticationConverter authenticationConverter() {
        return new ServerAuthenticationConverter() {
            @Override
            public Mono<Authentication> convert(ServerWebExchange exchange) {
                String token = extractToken(exchange);
                if (token != null) {
                    return Mono.just(new UsernamePasswordAuthenticationToken(token, token)); // Pasa el token para validación
                }
                return Mono.empty();
            }
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remueve 'Bearer '
        }
        return null;
    }
}
