package com.example.api.BachelorSubjectNotFound;

/**
 * Diese Klasse ist eine Ausnahme, die ausgelöst wird, wenn ein BachelorSubject nicht gefunden wurde.
 */
public class BachelorSubjectNotFoundException extends RuntimeException{

    public BachelorSubjectNotFoundException(Long id)
    {
        super ("Titel mit der ID " + id + " wurde nicht gefunden!");
    }

}
