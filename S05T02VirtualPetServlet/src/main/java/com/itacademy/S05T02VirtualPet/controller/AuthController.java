package com.itacademy.S05T02VirtualPet.controller;

import com.itacademy.S05T02VirtualPet.dto.AuthRequest;
import com.itacademy.S05T02VirtualPet.dto.AuthResponse;
import com.itacademy.S05T02VirtualPet.enums.Role;
import com.itacademy.S05T02VirtualPet.exception.AuthenticationException;
import com.itacademy.S05T02VirtualPet.exception.UserNotFoundException;
import com.itacademy.S05T02VirtualPet.model.User;
import com.itacademy.S05T02VirtualPet.service.impl.UserServiceImpl;
import com.itacademy.S05T02VirtualPet.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        User userDetails = userService.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authRequest.getUsername()));

        if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
            log.warn("Invalid login attempt for user {}", authRequest.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(userDetails);
        log.info("User {} logged in successfully", authRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of(Role.ROLE_USER));
        }

        User savedUser = userService.save(user);
        log.info("User {} signed up successfully", savedUser.getUsername());
        return ResponseEntity.ok("User signed up successfully");
    }

    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("You have accessed a protected endpoint!");
    }
}






