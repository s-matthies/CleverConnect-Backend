package com.example.api.DTO;

import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.External;

import java.util.List;

public class ExternalDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String registrationDate;
    private String role;
    private String company;
    private String availabilityStart;
    private String availabilityEnd;
    private String description;
    private List<BachelorSubjectDTO> bachelorSubjects;

    public ExternalDTO(External external, List<BachelorSubject> bachelorSubjects) {
        this.firstName = external.getFirstName();
        this.lastName = external.getLastName();
        this.email = external.getEmail();
        this.password = external.getPassword();
        this.registrationDate = (external.getRegistrationDate() != null) ?
                external.getRegistrationDate().toString() : null;
        this.role = String.valueOf(external.getRole());
        this.company = external.getCompany();
        this.availabilityStart = (external.getAvailabilityStart() != null) ?
                external.getAvailabilityStart().toString() : null;
        this.availabilityEnd = (external.getAvailabilityEnd() != null) ?
                external.getAvailabilityEnd().toString() : null;
        this.description = external.getDescription();

        // Mappe die BachelorSubjects auf die BachelorSubjectDTOs
        this.bachelorSubjects = bachelorSubjects.stream()
                .map(subject -> new BachelorSubjectDTO(
                        subject.getTitle(),
                        subject.getBDescription(),
                        subject.getDate().toString()))
                .toList();
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

    public String getCompany() {
        return company;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(String availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public String getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(String availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BachelorSubjectDTO> getBachelorSubjects() {
        return bachelorSubjects;
    }

    public void setBachelorSubjects(List<BachelorSubjectDTO> bachelorSubjects) {
        this.bachelorSubjects = bachelorSubjects;
    }

    public static class BachelorSubjectDTO {
        private String title;
        private String bDescription;
        private String date;

        public BachelorSubjectDTO(String title, String bDescription, String date) {
            this.title = title;
            this.bDescription = bDescription;
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public String getBDescription() {
            return bDescription;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBDescription(String bDescription) {
            this.bDescription = bDescription;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
