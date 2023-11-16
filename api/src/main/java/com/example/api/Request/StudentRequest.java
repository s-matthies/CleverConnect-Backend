package com.example.api.Request;

public class StudentRequest {
    // es werden alle Attribute angeben, die der Nutzer selbst ausfüllt
    // Namen der Attribute sollen die der Entity Student entsprechen
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String password;

    public StudentRequest(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
