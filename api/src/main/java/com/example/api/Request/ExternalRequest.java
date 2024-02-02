package com.example.api.Request;

import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.SpecialField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Diese Klasse ist ein Request Objekt, welches die Daten für die Registrierung eines externen Benutzers (Zweitbetreuer*in) enthält.
 */

public class ExternalRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate registrationDate;
    private String company;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  // damit date wirklich im richtigen For
    private LocalDate availabilityStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate availabilityEnd;
    private String description;

    private List<SpecialField> specialFields;
    private List<BachelorSubject> bachelorSubjects;


    public ExternalRequest(String firstName, String lastName, String email,
                           String password, LocalDate registrationDate, String company, LocalDate availabilityStart ,
                           LocalDate availabilityEnd, String description, List<SpecialField> specialFields, List<BachelorSubject> bachelorSubjects) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.company = company;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
        this.description = description;
        this.specialFields = specialFields;
        this.bachelorSubjects = bachelorSubjects;

    }


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

    public LocalDate getRegistrationDate() { return registrationDate; }

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

    public List<SpecialField> getSpecialFields() {
        return specialFields;
    }

    public List<BachelorSubject> getBachelorSubjects() {return bachelorSubjects; }


}
