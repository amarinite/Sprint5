package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.model.Pet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetService {
    Mono<Pet> createPet(Pet pet, String username);

    Flux<Pet> findAllPetsByUser(String username);

    Mono<Pet> updatePet(String id, Pet updatedPet, String username);

    Mono<Void> deletePet(String id, String username);
}
