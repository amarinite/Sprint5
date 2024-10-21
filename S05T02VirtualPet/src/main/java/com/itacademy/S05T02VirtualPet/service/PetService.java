package com.itacademy.S05T02VirtualPet.service;

import com.itacademy.S05T02VirtualPet.exception.PetNotFoundException;
import com.itacademy.S05T02VirtualPet.exception.NoPetsFoundException;
import com.itacademy.S05T02VirtualPet.exception.UserNotAuthenticatedException;
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

    public Flux<Pet> findAllPetsByUser(String username) {
        if (username == null) {
            return Flux.error(new UserNotAuthenticatedException("User is not authenticated"));
        }
        return petRepository.findByOwnerUsername(username)
                .switchIfEmpty(Flux.error(new NoPetsFoundException("No pets found for user: " + username)));
    }

    public Mono<Pet> updatePet(String id, Pet updatedPet, String username) {
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

