package com.itacademy.S05T02VirtualPet.service.impl;

import com.itacademy.S05T02VirtualPet.exception.PetNotFoundException;
import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.repository.PetRepository;
import com.itacademy.S05T02VirtualPet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public Flux<Pet> findAllPets() {
        return petRepository.findAll()
                .doOnError(e -> log.error("Error retrieving all pets: {}", e.getMessage()))
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<Pet> updatePet(String id, Pet updatedPet) {
        return petRepository.findById(id)
                .switchIfEmpty(Mono.error(new PetNotFoundException("Pet not found with id: " + id)))
                .flatMap(pet -> {
                    pet.setMood(updatedPet.getMood());
                    pet.setEnergyLevel(updatedPet.getEnergyLevel());
                    if (updatedPet.getName() != null) pet.setName(updatedPet.getName());
                    if (updatedPet.getColor() != null) pet.setColor(updatedPet.getColor());
                    if (updatedPet.getCharacteristics() != null) pet.setCharacteristics(updatedPet.getCharacteristics());

                    return petRepository.save(pet);
                });
    }

    public Mono<Void> deletePet(String id) {
        return petRepository.findById(id)
                .switchIfEmpty(Mono.error(new PetNotFoundException("Pet not found with id: " + id)))
                .flatMap(pet -> petRepository.deleteById(id));
    }
}

