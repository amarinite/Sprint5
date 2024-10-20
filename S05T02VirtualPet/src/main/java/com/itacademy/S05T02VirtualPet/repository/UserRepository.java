package com.itacademy.S05T02VirtualPet.repository;

import com.itacademy.S05T02VirtualPet.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);
}