package com.itacademy.S05T02VirtualPet.controller;

import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.service.PetService;
import com.itacademy.S05T02VirtualPet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PetController {

    private final PetService petService;
    private final UserService userService;

    @PostMapping
    public Mono<Pet> createPet(@RequestBody Pet pet) {
        String username = pet.getOwnerUsername();
        return petService.createPet(pet, username);
    }

    @GetMapping()
    public Flux<Pet> getAllPets(@RequestBody String username) {
        return petService.findAllPetsByUser(username);
    }

    @PutMapping("/{id}")
    public Mono<Pet> updatePet(@PathVariable String id, @RequestBody Pet pet) {
        return petService.updatePet(id, pet);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePet(@PathVariable String id) {
        return petService.deletePet(id);
    }
}

//    @GetMapping()
//    public Flux<Pet> getAllUserPets(Authentication authentication) {
//        String username = authentication.getName();
//        return petService.findAllPetsByUser(username);
//    }
