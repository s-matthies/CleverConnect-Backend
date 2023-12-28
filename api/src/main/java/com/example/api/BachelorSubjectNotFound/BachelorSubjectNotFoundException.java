package com.example.api.BachelorSubjectNotFound;

public class BachelorSubjectNotFoundException extends RuntimeException{

    public BachelorSubjectNotFoundException(Long id)
    {
        super ("Titel mit der ID " + id + " wurde nicht gefunden!");
    }

}
