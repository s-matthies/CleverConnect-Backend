package com.example.api.Entitys;

import jakarta.persistence.*;


@Entity
@Table(name="Student")
public class Student {

    //Schlüsselattribut Id vergeben
    @Id
    // Id wird mit SequenceGenerator und GeneratedValue eigenständig generiert
    @SequenceGenerator(name= "studentSequence", sequenceName= "studentSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentSequence")

    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Student() {
    }

    //Konstruktor ohen id, weil bei Ausgabe des studenten, id nicht mit angezeigt werden soll
    public Student(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Student(long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
