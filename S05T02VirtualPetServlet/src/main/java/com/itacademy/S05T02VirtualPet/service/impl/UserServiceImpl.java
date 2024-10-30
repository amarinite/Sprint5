package com.itacademy.S05T02VirtualPet.service.impl;

import com.itacademy.S05T02VirtualPet.exception.UserNotFoundException;
import com.itacademy.S05T02VirtualPet.model.User;
import com.itacademy.S05T02VirtualPet.repository.UserRepository;
import com.itacademy.S05T02VirtualPet.service.UserService;
import com.itacademy.S05T02VirtualPet.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        ValidationUtil.validateUser(user, userRepository);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.info("Finding user: {}", username);
        return userRepository.findByUsername(username);
    }
}




