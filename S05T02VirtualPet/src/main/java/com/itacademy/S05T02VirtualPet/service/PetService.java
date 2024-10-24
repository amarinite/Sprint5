package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.model.Pet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public interface PetService {
    public Mono<Pet> createPet(Pet pet, String username);

    public Flux<Pet> findAllPetsByUser(String username);

    public Mono<Pet> updatePet(String id, Pet updatedPet, String username);

    public Mono<Void> deletePet(String id, String username);
}
