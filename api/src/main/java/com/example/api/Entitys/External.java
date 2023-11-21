package com.example.api.Entitys;

import java.util.Objects;

public class External extends User{

    //zusätzliche Attribute der abgeleiteten Klasse
    private String company;
    private String availability;

    public External() {
        super("Berta", "Cesar", "mail@mail.de", "passwort", "STUDENT");
    }

    public External(Long id, String firstName, String lastName, String email, String password,
                    Role role, String company, String availability) {
        super(id, firstName, lastName, email, password, role);
        this.company = company;
        this.availability = availability;
    }

    //Konstruktor ohne id, da diese automatisch beim Hinzufügen einer neuen Externen erstellt wird
    public External(String firstName, String lastName, String email, String password, Role role,
                    String company, String availability) {
        super(firstName, lastName, email, password, role);
        this.company = company;
        this.availability = availability;
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

    @Override
    public String toString() {
        return super.toString() +
                ", company='" + company + '\'' +
                ", availability='" + availability + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), company, availability);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        External external = (External) o;
        return Objects.equals(company, external.company) && Objects.equals(availability, external.availability);
    }


}
