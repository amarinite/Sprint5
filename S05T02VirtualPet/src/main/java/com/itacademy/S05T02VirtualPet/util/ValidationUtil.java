package com.itacademy.S05T02VirtualPet.util;

import com.itacademy.S05T02VirtualPet.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public void validatePet(Pet pet) {
        if (pet.getName() == null || pet.getType() == null || pet.getColor() == null || pet.getMood() == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }

        if (!isValidSize(pet.getType())) {
            throw new IllegalArgumentException("Size must be 'small', 'medium', or 'large'");
        }

        if (!isValidMood(pet.getMood())) {
            throw new IllegalArgumentException("Mood must be 'grumpy', 'friendly', or 'serious'");
        }

        if (pet.getEnergyLevel() < 0 || pet.getEnergyLevel() > 100) {
            throw new IllegalArgumentException("Energy level must be between 0 and 100");
        }
    }

    private boolean isValidSize(String size) {
        return "small".equalsIgnoreCase(size) || "medium".equalsIgnoreCase(size) || "large".equalsIgnoreCase(size);
    }

    private boolean isValidMood(String mood) {
        return "grumpy".equalsIgnoreCase(mood) || "friendly".equalsIgnoreCase(mood) || "serious".equalsIgnoreCase(mood);
    }
}

