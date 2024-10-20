package com.itacademy.S05T02VirtualPet.repository;

import com.itacademy.S05T02VirtualPet.model.Pet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PetRepository extends ReactiveMongoRepository<Pet, String> {
    Flux<Pet> findByOwnerUsername(String username);
}
