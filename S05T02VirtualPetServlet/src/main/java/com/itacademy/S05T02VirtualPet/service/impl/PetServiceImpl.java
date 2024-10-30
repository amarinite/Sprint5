package com.itacademy.S05T02VirtualPet.service.impl;

import com.itacademy.S05T02VirtualPet.exception.*;
import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.model.User;
import com.itacademy.S05T02VirtualPet.repository.PetRepository;
import com.itacademy.S05T02VirtualPet.repository.UserRepository;
import com.itacademy.S05T02VirtualPet.service.PetService;
import com.itacademy.S05T02VirtualPet.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;

    public Pet createPet(Pet pet, String username) {
        validationUtil.validatePet(pet);

        if (pet.getMood() == null || pet.getMood().isEmpty()) {
            pet.setMood("happy");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        Pet savedPet = petRepository.save(pet);

        if (user.getPets() == null) {
            user.setPets(new ArrayList<>());
        }
        user.getPets().add(savedPet.getId());
        userRepository.save(user);

        return savedPet;
    }

    public List<Pet> findAllPetsByUser(String username) {
        if (username == null) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (user.getPets() == null || user.getPets().isEmpty()) {
            throw new NoPetsFoundException("No pets found for user: " + username);
        }

        return petRepository.findAllById(user.getPets());
    }

    public Pet getPetById(String id, String username) {
        if (username == null) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (user.getPets() == null || !user.getPets().contains(pet.getId())) {
            throw new UnauthorizedAccessException("User is not authorized to view this pet");
        }

        return pet;
    }


    public Pet updatePet(String id, Pet updatedPet, String username) {
        if (username == null) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }
        validationUtil.validatePet(updatedPet);

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (user.getPets() == null || !user.getPets().contains(pet.getId())) {
            throw new UnauthorizedAccessException("User is not authorized to update this pet");
        }

        updatePetDetails(pet, updatedPet);
        return petRepository.save(pet);
    }

    public void deletePet(String id, String username) {
        if (username == null) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        if (user.getPets() == null || !user.getPets().contains(pet.getId())) {
            throw new UnauthorizedAccessException("User is not authorized to delete this pet");
        }

        user.getPets().remove(pet.getId());
        userRepository.save(user);
        petRepository.deleteById(id);
    }

    private void updatePetDetails(Pet pet, Pet updatedPet) {
        if (updatedPet.getMood() != null) {
            pet.setMood(updatedPet.getMood());
        }
        pet.setEnergyLevel(updatedPet.getEnergyLevel());
        if (updatedPet.getName() != null) pet.setName(updatedPet.getName());
        if (updatedPet.getColor() != null) pet.setColor(updatedPet.getColor());
        if (updatedPet.getCharacteristics() != null) pet.setCharacteristics(updatedPet.getCharacteristics());
    }

}





