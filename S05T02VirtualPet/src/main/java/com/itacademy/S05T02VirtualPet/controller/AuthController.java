package com.itacademy.S05T02VirtualPet.controller;

import com.itacademy.S05T02VirtualPet.util.JWTUtil;
import com.itacademy.S05T02VirtualPet.model.AuthRequest;
import com.itacademy.S05T02VirtualPet.model.AuthResponse;
import com.itacademy.S05T02VirtualPet.model.User;
import com.itacademy.S05T02VirtualPet.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    private final JWTUtil jwtUtil;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JWTUtil jwtUtil, UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return userService.findByUsername(authRequest.getUsername())
                .flatMap(userDetails -> {
                    if (passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
                        String token = jwtUtil.generateToken(authRequest.getUsername());
                        log.info("User {} logged in successfully", authRequest.getUsername());
                        return Mono.just(ResponseEntity.ok(new AuthResponse(token)));
                    } else {
                        log.warn("Invalid password attempt for user {}", authRequest.getUsername());
                        return Mono.error(new RuntimeException("Invalid username or password"));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("User {} not found", authRequest.getUsername());
                    return Mono.error(new RuntimeException("Invalid username or password"));
                }))
                .onErrorResume(e -> {
                    log.error("Authentication failed for user {}: {}", authRequest.getUsername(), e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new AuthResponse("Authentication failed: " + e.getMessage())));
                });
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signup(@RequestBody User user) {
        return userService.save(user)
                .map(savedUser -> {
                    log.info("User {} signed up successfully", savedUser.getUsername());
                    return ResponseEntity.ok("User signed up successfully");
                })
                .doOnError(e -> log.error("Error signing up user: {}", e.getMessage()))
                .onErrorResume(e -> {
                    log.error("Sign up failed: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Sign up failed: " + e.getMessage()));
                });
    }

    @GetMapping("/protected")
    public Mono<ResponseEntity<String>> protectedEndpoint() {
        return Mono.just(ResponseEntity.ok("You have accessed a protected endpoint!"));
    }
}


