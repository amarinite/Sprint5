package com.itacademy.S05T02VirtualPet.repository;

import com.itacademy.S05T02VirtualPet.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PetRepository extends MongoRepository<Pet, String> {
    Optional<Pet> findById(String id);

}


