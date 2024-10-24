package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.model.User;
import reactor.core.publisher.Mono;

public interface UserService {
    public Mono<User> save(User user);

    public Mono<User> findByUsername(String username);
}
