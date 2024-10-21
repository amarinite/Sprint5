package com.itacademy.S05T02VirtualPet.controller;

import com.itacademy.S05T02VirtualPet.exception.NoPetsFoundException;
import com.itacademy.S05T02VirtualPet.exception.UserNotAuthenticatedException;
import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PetController {

    private final PetService petService;

    @PostMapping
    public Mono<Pet> createPet(@RequestBody Pet pet, Authentication authentication) {
        String username = authentication.getName();
        return petService.createPet(pet, username);
    }

    @GetMapping("/myPets")
    public Flux<Pet> getAllUserPets(Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        return petService.findAllPetsByUser(username);
    }

    @PutMapping("/{id}")
    public Mono<Pet> updatePet(@PathVariable String id, @RequestBody Pet pet, Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        return petService.updatePet(id, pet, username);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePet(@PathVariable String id) {
        return petService.deletePet(id);
    }
}



