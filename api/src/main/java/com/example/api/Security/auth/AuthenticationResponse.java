package com.example.api.Security.auth;

/**
 * Diese Klasse ist ein DTO Objekt, welches die Daten für eine Authentifizierungsanfrage enthält.
 */
public class AuthenticationResponse {

    private final String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }



}


