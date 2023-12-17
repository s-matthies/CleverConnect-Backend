package com.example.api.Request;

public class ExternalRequest {

    //alle Attribute, die die Userin (Externe) hier ausfüllen soll

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String company;
    private String availability;

    private String description;


    public ExternalRequest(String firstName, String lastName, String email,
                         String password, String company, String availability, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.availability = availability;
        this.description = description;

    }

    // alle Getter erstellen
    // die Attribute, die wir bekommen -> für Registrierung der Userin einsetzen


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCompany() {
        return company;
    }

    public String getAvailability() {
        return availability;
    }

    public String getDescription() {
        return description;
    }

}
