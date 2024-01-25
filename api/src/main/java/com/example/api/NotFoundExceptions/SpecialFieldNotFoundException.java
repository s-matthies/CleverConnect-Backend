package com.example.api.NotFoundExceptions;

public class SpecialFieldNotFoundException extends RuntimeException {
    public SpecialFieldNotFoundException(Long id) {
        super("Fachgebiet wurde nicht gefunden!");
    }
}
