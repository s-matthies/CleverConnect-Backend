package com.example.api.Entitys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Entity Klasse für die Externen
 */
@Entity
@Table(name = "Externals")
public class External extends User{

    private String company;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Formatierung des Datums
    private LocalDate availabilityStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate availabilityEnd;
    private String description;

    @OneToMany(
            //mappedBy = "external", // zeigt auf das Attribut external in der Klasse BachelorSubject
            fetch = FetchType.LAZY, // Lazy: Daten werden erst dann geladen, wenn sie benötigt werden
            cascade = CascadeType.ALL // alle Bachelorthemen, werden beim entsprechenden External gespeichert
            //orphanRemoval = true // wenn der External gelöscht wird, werden auch seine jeweiligen Bachelorthemen gelöscht
    )
    @JsonIgnore
    @JoinColumn(name="external_id")
    private List<BachelorSubject> bachelorSubjects = new ArrayList<>();


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "choosen_fields",
            joinColumns = @JoinColumn(name = "external_id"),
            inverseJoinColumns = @JoinColumn(name = "specialField_id")
    )
    private Set<SpecialField> specialFields = new HashSet<>();


    /**
     * Konstruktor
     * @param firstName - Vorname des Externen
     * @param lastName - Nachname des Externen
     * @param email - Email des Externen
     * @param password - Passwort des Externen
     * @param registrationDate - Registrierungsdatum des Externen
     * @param role - Rolle des Externen
     * @param locked - gesperrt
     * @param enabled - aktiviert
     * @param company - Firma des Externen
     * @param availabilityStart - Verfügbarkeitsstart des Externen
     * @param availabilityEnd - Verfügbarkeitsende des Externen
     * @param description - Beschreibung des Externen
     * @param specialFields - gewählte SpecialFields des Externen
     * @param bachelorSubjects - Bachelorthemen des Externen
     */
    public External(String firstName, String lastName, String email, String password, LocalDate registrationDate,
                    Role role, boolean locked, boolean enabled, String company, LocalDate availabilityStart,
                    LocalDate availabilityEnd, String description, Set<SpecialField> specialFields, List<BachelorSubject> bachelorSubjects) {
        super(firstName, lastName, email, password, registrationDate, role, locked, enabled);
        this.company = company;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
        this.description = description;
        this.specialFields = specialFields;
        this.bachelorSubjects = bachelorSubjects;

    }


    public External() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDate getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(LocalDate availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public LocalDate getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(LocalDate availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BachelorSubject> getBachelorSubjects()
    {
        return bachelorSubjects;
    }

    public void setBachelorSubjects(List<BachelorSubject> bachelorSubjects) {
        this.bachelorSubjects = bachelorSubjects;
    }

    public Set<SpecialField> getSpecialFields() {
        return specialFields;
    }

    public void setSpecialFields(Set<SpecialField> specialFields) {
        this.specialFields = specialFields;
    }


    @Override
    public String toString() {
        return super.toString() +
                ", company='" + company + '\'' +
                ", availabilityStart='" + availabilityStart + '\'' +
                ", availabilityEnd='" + availabilityEnd + '\'' +
                ", description='" + description + '\'' +
                ", specialFields='" + specialFields + '\'' +
                ", bachelorSubjects='" + bachelorSubjects + '\'' +
                '}';
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), company, availabilityStart, availabilityEnd, description, specialFields, bachelorSubjects);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        External external = (External) o;
        return Objects.equals(company, external.company)
                && Objects.equals(availabilityStart, external.availabilityStart)
                && Objects.equals(availabilityEnd, external.availabilityEnd)
                && Objects.equals(description, external.description)
                && Objects.equals(specialFields, external.specialFields)
                && Objects.equals(bachelorSubjects, external.bachelorSubjects);
    }

}
