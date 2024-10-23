package com.itacademy.S05T02VirtualPet.repository;

import com.itacademy.S05T02VirtualPet.model.Pet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PetRepository extends ReactiveMongoRepository<Pet, String> {
}
