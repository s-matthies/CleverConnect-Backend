package com.example.api.UserNotFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Diese Klasse ist eine Ausnahme, die ausgelöst wird, wenn ein User nicht gefunden wurde.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private Long id;
    private String email;

    public UserNotFoundException(Long id) {
        super ("User mit der ID " + id + " wurde nicht gefunden!");
        this.id = id;
    }

    public UserNotFoundException(String email) {
        super ("User mit der E-Mail " + email + " wurde nicht gefunden!");
        this.email = email;
    }

    /**
     * Gibt die ID oder die Email des nicht gefundenen Benutzers zurück.
     * @return Die ID des nicht gefundenen Benutzers.
     * @return Die Email des nicht gefundenen Benutzers.
     */
    public String toJsonString() {
        if (id != null) {
            return "{\"error\": \"User not found\", \"userId\": " + id + "}";
        }
        else {
            return "{\"error\": \"User not found\", \"email\": " + email + "}";
        }
    }
}


