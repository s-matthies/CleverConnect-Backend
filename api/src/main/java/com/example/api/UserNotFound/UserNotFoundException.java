package com.example.api.UserNotFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private Long id;

    public UserNotFoundException(Long id) {
        super ("User mit der ID " + id + " wurde nicht gefunden!");
        this.id = id;
    }

    public String toJsonString() {
        return "{\"error\": \"User not found\", \"userId\": " + id + "}";
    }
}


