package com.example.api.student;

public class StudentNotFoundException extends RuntimeException {

    StudentNotFoundException(Long id) {

        super("Could not find student " + id);
    }
}
