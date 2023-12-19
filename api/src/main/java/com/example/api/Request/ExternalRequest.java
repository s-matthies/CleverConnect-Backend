package com.example.api.Request;

import java.time.LocalDate;

public class ExternalRequest {

    //alle Attribute, die die Userin (Externe) hier ausfüllen soll

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate registrationDate;
    private String company;
    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;
    private String description;
    private String expertise;


    public ExternalRequest(String firstName, String lastName, String email,
                           String password, LocalDate registrationDate, String company, LocalDate availabilityStart ,
                           LocalDate availabilityEnd, String description, String expertise) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.company = company;
        //this.availability = availability;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
        this.description = description;
        this.expertise = expertise;

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

    public LocalDate getAvailabilityStart() {
        return availabilityStart;
    }

    public LocalDate getAvailabilityEnd() {
        return availabilityEnd;
    }

    public String getDescription() {
        return description;
    }

    public String getExpertise() { return expertise; }


}
