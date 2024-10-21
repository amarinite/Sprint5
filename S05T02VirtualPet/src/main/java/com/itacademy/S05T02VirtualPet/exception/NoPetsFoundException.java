package com.itacademy.S05T02VirtualPet.exception;

public class NoPetsFoundException extends RuntimeException {
    public NoPetsFoundException(String message) {
        super(message);
    }
}
