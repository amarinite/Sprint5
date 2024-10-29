package com.itacademy.S05T02VirtualPet.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface CustomUserDetailsService extends ReactiveUserDetailsService {
    Mono<UserDetails> findByUsername(String username);
}
