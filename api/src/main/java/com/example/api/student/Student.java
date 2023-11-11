package com.example.api.student;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name="Student")
class Student{

    @Id @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    //private Boolean enabled;

    public Student(){}

    public Student(String firstName, String lastName, String mail){
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Student))
            return false;
        Student student = (Student) o;
        return Objects.equals(this.id, student.id)
                && Objects.equals(this.mail, student.mail)
                && Objects.equals(this.lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.mail, this.password);
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='"
                + this.lastName + '\'' + '\'' + ", mail='" + this.mail + '}';
    }
}
