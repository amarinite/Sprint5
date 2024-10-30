package com.itacademy.S05T02VirtualPet.controller;

import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.service.impl.PetServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Configuration
public class PetController {

    private final PetServiceImpl petService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet, Authentication authentication) {
        try {
            String username = authentication.getName();
            Pet createdPet = petService.createPet(pet, username);
            return ResponseEntity.ok(createdPet);
        } catch (Exception e) {
            log.error("Error creating pet: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/myPets")
    public ResponseEntity<List<Pet>> getAllUserPets(Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        try {
            List<Pet> pets = petService.findAllPetsByUser(username);
            return ResponseEntity.ok(pets);
        } catch (Exception e) {
            log.error("Error retrieving pets: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable String id, Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        Pet pet = petService.getPetById(id, username);
        return ResponseEntity.ok(pet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable String id, @RequestBody Pet pet, Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        try {
            Pet updatedPet = petService.updatePet(id, pet, username);
            return ResponseEntity.ok(updatedPet);
        } catch (Exception e) {
            log.error("Error updating pet: {}", e.getMessage());
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable String id, Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        try {
            petService.deletePet(id, username);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting pet: {}", e.getMessage());
            return ResponseEntity.status(403).build();
        }
    }
}




