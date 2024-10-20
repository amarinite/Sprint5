package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.exception.UserNotFoundException;
import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.repository.PetRepository;
import com.itacademy.S05T02VirtualPet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public Mono<Pet> createPet(Pet pet, String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")))
                .flatMap(user -> {
                    pet.setOwnerUsername(user.getUsername());
                    return petRepository.save(pet);
                });
    }

    public Flux<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Flux<Pet> findAllPetsByUser(String username) {
        return petRepository.findByOwnerUsername(username);
    }

    public Mono<Pet> updatePet(String id, Pet updatedPet) {
        return petRepository.findById(id)
                .flatMap(pet -> {
                    pet.setMood(updatedPet.getMood());
                    pet.setEnergyLevel(updatedPet.getEnergyLevel());
                    return petRepository.save(pet);
                });
    }

    public Mono<Void> deletePet(String id) {
        return petRepository.deleteById(id);
    }
}

