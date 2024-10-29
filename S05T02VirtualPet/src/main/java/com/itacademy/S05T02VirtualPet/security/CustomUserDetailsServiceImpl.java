package com.itacademy.S05T02VirtualPet.security;

import com.itacademy.S05T02VirtualPet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("Looking up user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> {
                    log.info("User found: {}", user.getUsername());
                    return (UserDetails) user;
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("User not found for username: {}", username);
                    return Mono.error(new UsernameNotFoundException("User not found: " + username));
                }));
    }


}

