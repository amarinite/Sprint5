package com.itacademy.S05T02VirtualPet.util;

import com.itacademy.S05T02VirtualPet.model.Pet;
import com.itacademy.S05T02VirtualPet.model.User;
import com.itacademy.S05T02VirtualPet.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtil {

    public void validatePet(Pet pet) {
        if (pet.getName() == null || pet.getType() == null || pet.getColor() == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }

        if (pet.getEnergyLevel() < 0 || pet.getEnergyLevel() > 100) {
            throw new IllegalArgumentException("Energy level must be between 0 and 100");
        }
    }

    public static void validateUser(User user, UserRepository userRepository) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Username, email, and password must not be null");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email must have a valid format");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (user.getPassword().length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long");
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

}

