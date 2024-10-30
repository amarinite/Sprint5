package com.itacademy.S05T02VirtualPet.service.impl;

import com.itacademy.S05T02VirtualPet.exception.NoPetsFoundException;
import com.itacademy.S05T02VirtualPet.exception.PetNotFoundException;
import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.repository.PetRepository;
import com.itacademy.S05T02VirtualPet.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final PetRepository petRepository;

    public List<Pet> findAllPets() {
        List<Pet> pets = petRepository.findAll();
        if (pets.isEmpty()) {
            throw new NoPetsFoundException("No pets found in the repository.");
        }
        return pets;
    }

    public Pet updatePet(String id, Pet updatedPet) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + id));

        if (updatedPet.getMood() != null) pet.setMood(updatedPet.getMood());
        if (updatedPet.getEnergyLevel() != null) pet.setEnergyLevel(updatedPet.getEnergyLevel());
        if (updatedPet.getName() != null) pet.setName(updatedPet.getName());
        if (updatedPet.getColor() != null) pet.setColor(updatedPet.getColor());
        if (updatedPet.getCharacteristics() != null) pet.setCharacteristics(updatedPet.getCharacteristics());

        return petRepository.save(pet);
    }

    public void deletePet(String id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + id));

        petRepository.deleteById(id);
    }
}



