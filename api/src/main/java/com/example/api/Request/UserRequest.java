package com.example.api.Request;

import java.time.LocalDate;

/**
 * Diese Klasse ist ein Request Objekt, welches die Daten für die Registrierung einer Studierenden* enthält.
 */
public class UserRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final LocalDate registrationDate;

    public UserRequest(String firstName, String lastName, String email, String password, LocalDate registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
}
