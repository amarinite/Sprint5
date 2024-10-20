package com.itacademy.S05T02VirtualPet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthRequest {

    private String username;
    private String password;

}