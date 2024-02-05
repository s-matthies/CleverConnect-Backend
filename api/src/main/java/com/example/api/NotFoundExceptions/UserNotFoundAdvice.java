package com.example.api.NotFoundExceptions;

import com.example.api.NotFoundExceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Globale UserNotFoundAdvice-klasse für die Behandlung von {@link UserNotFoundException}.
 * Hier werden Exception-Handler-Methoden definiert, um auf UserNotFound-Exceptions zu reagieren.
 */
@RestControllerAdvice
public class UserNotFoundAdvice {

    /**
     * Behandelt die Ausnahme {@link UserNotFoundException}.
     *
     * @param ex Die ausgelöste Ausnahme.
     * @return Eine ResponseEntity, die den Fehlercode und die Fehlermeldung enthält.
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        String errorMessage = ex.toJsonString();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
