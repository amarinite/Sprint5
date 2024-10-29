package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.model.Pet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminService {
    Flux<Pet> findAllPets();

    Mono<Pet> updatePet(String id, Pet updatedPet);

    Mono<Void> deletePet(String id);
}
