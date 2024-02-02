package com.example.api.Security.auth;

/**
 * Diese Klasse ist ein DTO Objekt, welches die Daten für eine Authentifizierungsanfrage enthält.
 */
public class AuthenticationRequest {

    private final String email;
    private final String password;

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

