package com.example.api.Request;


// hier kommen die Angaben(Request) von der Client-Seite, die zum Server weitergeleitet werden sollen
public class LoginRequest {

    //beim Login werden nur Email und Passwort eingegeben, deshalb nur diese beiden Attribute hier notwendig
    private String email;
    private String password;

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
