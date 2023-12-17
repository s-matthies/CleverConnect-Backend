package com.example.api.Entitys;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Externals")
public class External extends User{

    //zusätzliche Attribute der abgeleiteten Klasse
    private String company;

    private String availability;
    private String description;

    public External(Long id, String firstName, String lastName, String email, String password, LocalDate registrationDate,
                    Role role, boolean locked, boolean enabled, String company, String availability, String description) {
        super(id, firstName, lastName, email, password, registrationDate, role, locked, enabled);
        this.company = company;
        this.availability = availability;
        this.description = description;
    }

    //Konstruktor ohne id, da diese automatisch beim Hinzufügen einer neuen Externen erstellt wird
    public External(String firstName, String lastName, String email, String password, LocalDate registrationDate,
                    Role role, boolean locked, boolean enabled, String company, String availability, String description) {
        super(firstName, lastName, email, password, registrationDate, role, locked, enabled);
        this.company = company;
        this.availability = availability;
        this.description = description;
    }



    public External() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return super.toString() +
                ", company='" + company + '\'' +
                ", availability='" + availability + '\'' +
                ", description='" + description + '\'' +
                '}';

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), company, availability, description);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        External external = (External) o;
        return Objects.equals(company, external.company) && Objects.equals(availability, external.availability)
                && Objects.equals(description, external.description);

    }


}
