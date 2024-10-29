package com.itacademy.S05T02VirtualPet.service.impl;

import com.itacademy.S05T02VirtualPet.exception.*;
import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.repository.PetRepository;
import com.itacademy.S05T02VirtualPet.repository.UserRepository;
import com.itacademy.S05T02VirtualPet.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public Mono<Pet> createPet(Pet pet, String username) {
        return petRepository.save(pet)
                .flatMap(savedPet -> {
                    return userRepository.findByUsername(username)
                            .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with username: " + username)))
                            .flatMap(user -> {
                                if (user.getPets() == null) {
                                    user.setPets(new ArrayList<>());
                                }
                                user.getPets().add(savedPet.getId());
                                return userRepository.save(user)
                                        .thenReturn(savedPet);
                            });
                });
    }

    public Flux<Pet> findAllPetsByUser(String username) {
        if (username == null) {
            return Flux.error(new UserNotAuthenticatedException("User is not authenticated"));
        }

        return userRepository.findByUsername(username)
                .flatMapMany(user -> {
                    if (user == null) {
                        return Flux.error(new UserNotFoundException("User not found: " + username));
                    }

                    if (user.getPets() == null || user.getPets().isEmpty()) {
                        return Flux.error(new NoPetsFoundException("No pets found for user: " + username));
                    }

                    return petRepository.findAllById(user.getPets());
                })
                .doOnError(e -> log.error("Error in findAllPetsByUser: {}", e.getMessage()))
                .onErrorResume(e -> {
                    return Flux.empty();
                });
    }



    public Mono<Pet> updatePet(String id, Pet updatedPet, String username) {
        if (username == null) {
            return Mono.error(new UserNotAuthenticatedException("User is not authenticated"));
        }
        return petRepository.findById(id)
                .switchIfEmpty(Mono.error(new PetNotFoundException("Pet not found with id: " + id)))
                .flatMap(pet -> userRepository.findByUsername(username)
                        .flatMap(user -> {
                            if (user.getPets() == null || !user.getPets().contains(pet.getId())) {
                                return Mono.error(new UnauthorizedAccessException("User is not authorized to update this pet"));
                            }
                            pet.setMood(updatedPet.getMood());
                            pet.setEnergyLevel(updatedPet.getEnergyLevel());
                            if (updatedPet.getName() != null) pet.setName(updatedPet.getName());
                            if (updatedPet.getColor() != null) pet.setColor(updatedPet.getColor());
                            if (updatedPet.getCharacteristics() != null) pet.setCharacteristics(updatedPet.getCharacteristics());

                            return petRepository.save(pet);
                        }));
    }

    public Mono<Void> deletePet(String id, String username) {
        if (username == null) {
            return Mono.error(new UserNotAuthenticatedException("User is not authenticated"));
        }
        return petRepository.findById(id)
                .flatMap(pet -> userRepository.findByUsername(username)
                        .flatMap(user -> {
                            if (user.getPets() == null || !user.getPets().contains(pet.getId())) {
                                return Mono.error(new UnauthorizedAccessException("User is not authorized to delete this pet"));
                            }
                            user.getPets().remove(pet.getId());
                            return userRepository.save(user)
                                    .then(petRepository.deleteById(id));
                        }));
    }
}

