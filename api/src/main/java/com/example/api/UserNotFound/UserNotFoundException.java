package com.example.api.UserNotFound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super ("User mit der ID " + id + " wurde nicht gefunden!");
    }
}


