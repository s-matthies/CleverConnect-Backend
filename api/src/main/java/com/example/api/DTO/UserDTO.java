package com.example.api.DTO;

import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;

import java.time.LocalDate;

/**
 * Diese Klasse ist ein DTO Objekt, welches die Daten für einen User enthält.
 */
public class UserDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    LocalDate registrationDate;
    Role role;

    public UserDTO(Long id, String firstName, String lastName, String email, LocalDate registrationDate, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    /**
     * Konstruktor, der ein User Objekt in ein UserDTO Objekt umwandelt.
     * @param user User Objekt
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.registrationDate = user.getRegistrationDate();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
