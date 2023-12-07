package com.example.api.UserNotFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Diese Ausnahme wird ausgelöst, wenn ein Benutzer nicht gefunden wird.
 * Ein Fehler mit dem HTTP-Statuscode 404 (NOT_FOUND) wird zurückgegeben.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private Long id;
    /**
     * Konstruktor für die UserNotFoundException.
     *
     * @param id Die ID des nicht gefundenen Users.
     */
    public UserNotFoundException(Long id) {
        super ("User mit der ID " + id + " wurde nicht gefunden!");
        this.id = id;
    }

    /**
     * Gibt eine JSON-Zeichenfolge zurück, die Informationen über die Exception enthält.
     *
     * @return JSON-Zeichenfolge mit Fehlerinformationen.
     */
    public String toJsonString() {
        return "{\"error\": \"User not found\", \"userId\": " + id + "}";
    }
}


