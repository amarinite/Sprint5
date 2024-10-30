package com.itacademy.S05T02VirtualPet.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService extends UserDetailsService {
    UserDetails findByUsername(String username);
}

