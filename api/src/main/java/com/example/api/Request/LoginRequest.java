package com.example.api.Request;

/**
 * Diese Klasse ist ein Request Objekt, welches die Daten für den Login eines Benutzers enthält.
 */
public class LoginRequest {

    private final String email;
    private final String password;

    public LoginRequest(String email, String password) {
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
