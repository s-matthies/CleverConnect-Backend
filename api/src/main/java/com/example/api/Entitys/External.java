package com.example.api.Entitys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Externals")
public class External extends User{

    //zusätzliche Attribute der abgeleiteten Klasse
    private String company;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // eingefügt weil im Client Date im backend funktioniert...
    private LocalDate availabilityStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate availabilityEnd;
    private String description;

    /** es soll möglich sein, mehr als ein Bachelor-Thema angeben zu können,
     // deshalb wird eine Liste erstellt
     // oneToMany bedeutet, dass jedem External mehrere Bachelorthemen zugeordnet werden können
     mappedBy: die Liste der Bachelorthemen wird einem External zugewiesen, (zeigt auf darauf)
     CascadeType.ALL: alle Bachelorthemen die angelegt wurden, werden beim entsprechenden External gespeichert
     orphanRemoval = true : wenn der External gelöscht wird, werden dadurch auch seine jeweiligen Bachelorthemen gelöscht
     */
    @OneToMany(
            //mappedBy = "external",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @JoinColumn(name="external_id")
    private List<BachelorSubject> bachelorSubjects = new ArrayList<>();


    @JsonIgnore
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "choosen_fields",
            joinColumns = @JoinColumn(name = "external_id"),
            inverseJoinColumns = @JoinColumn(name = "specialField_id")
    )
    private List<SpecialField> specialFields = new ArrayList<>();

    public void choseField( SpecialField specialField) {
        specialFields.add(specialField);
    }



    public External(Long id, String firstName, String lastName, String email, String password, LocalDate registrationDate,
                    Role role, boolean locked, boolean enabled, String company, LocalDate availabilityStart,
                    LocalDate availabilityEnd,
                    String description, List<SpecialField> specialFields, List<BachelorSubject> bachelorSubjects) {
        super(id, firstName, lastName, email, password, registrationDate, role, locked, enabled);
        this.company = company;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
        this.description = description;
        this.specialFields = specialFields;
        this.bachelorSubjects = bachelorSubjects;
    }



    //Konstruktor ohne id, da diese automatisch beim Hinzufügen einer neuen Externen erstellt wird
    public External(String firstName, String lastName, String email, String password, LocalDate registrationDate,
                    Role role, boolean locked, boolean enabled, String company, LocalDate availabilityStart,
                    LocalDate availabilityEnd, String description, List<SpecialField> specialFields, List<BachelorSubject> bachelorSubjects) {
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

    public List<SpecialField> getSpecialFields() {
        return specialFields;
    }

    public void setSpecialFields(List<SpecialField> specialFields) {

        this.specialFields = specialFields;
        //this.specialFields = (specialFields != null) ? specialFields : new ArrayList<>();
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
