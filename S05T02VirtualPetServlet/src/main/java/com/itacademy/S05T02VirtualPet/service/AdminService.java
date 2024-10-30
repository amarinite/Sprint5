package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.model.Pet;

import java.util.List;

public interface AdminService {
    List<Pet> findAllPets(); // Changed from Flux<Pet> to List<Pet>

    Pet updatePet(String id, Pet updatedPet); // Changed from Mono<Pet> to Pet

    void deletePet(String id); // Changed from Mono<Void> to void
}

