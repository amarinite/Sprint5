package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.model.User;

import java.util.Optional;

public interface UserService {
    User save(User user); // Changed from Mono<User> to User

    Optional<User> findByUsername(String username);
}

