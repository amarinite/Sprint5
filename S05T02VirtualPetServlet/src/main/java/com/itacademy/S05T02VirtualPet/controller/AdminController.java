package com.itacademy.S05T02VirtualPet.controller;

import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.service.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = adminService.findAllPets();
        return ResponseEntity.ok(pets);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Pet> updatePetByAdmin(@PathVariable String id, @RequestBody Pet pet) {
        Pet updatedPet = adminService.updatePet(id, pet);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePetByAdmin(@PathVariable String id) {
        adminService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}


