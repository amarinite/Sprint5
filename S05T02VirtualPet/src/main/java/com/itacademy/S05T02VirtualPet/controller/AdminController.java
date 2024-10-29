package com.itacademy.S05T02VirtualPet.controller;

import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.service.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/admin/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Flux<Pet> getAllPets() {
        return adminService.findAllPets();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<Pet> updatePetByAdmin(@PathVariable String id, @RequestBody Pet pet) {
        return adminService.updatePet(id, pet)
                .doOnError(e -> log.error("Error updating pet by admin: {}", e.getMessage()))
                .onErrorResume(e -> {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update pet", e));
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<Void> deletePetByAdmin(@PathVariable String id) {
        return adminService.deletePet(id)
                .doOnError(e -> log.error("Error deleting pet by admin: {}", e.getMessage()))
                .onErrorResume(e -> {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete pet", e));
                });
    }
}
