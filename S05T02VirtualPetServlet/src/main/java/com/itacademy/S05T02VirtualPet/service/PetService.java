package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.model.Pet;

import java.util.List;

public interface PetService {
    Pet createPet(Pet pet, String username);

    List<Pet> findAllPetsByUser(String username);

    Pet getPetById(String id, String username);

    Pet updatePet(String id, Pet updatedPet, String username);

    void deletePet(String id, String username);
}

