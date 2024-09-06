package com.example.api.DTO;

import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.External;
import com.example.api.Entitys.SpecialField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Diese Klasse ist ein DTO Objekt, welches die Daten eines externen Benutzers enthält.
 */
public class ExternalDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String registrationDate;
    private String role;
    private String company;
    private String availabilityStart;
    private String availabilityEnd;
    private String description;
    private List<SpecialFieldDTO> specialFields;
    private List<BachelorSubjectDTO> bachelorSubjects;

    public ExternalDTO(External external,
                       List<SpecialField> specialFields,
                       List<BachelorSubject> bachelorSubjects) {
        this.id = external.getId();
        this.firstName = external.getFirstName();
        this.lastName = external.getLastName();
        this.email = external.getEmail();
        this.registrationDate = (external.getRegistrationDate() != null) ?
                external.getRegistrationDate().toString() : null;
        this.role = String.valueOf(external.getRole());
        this.company = external.getCompany();
        this.availabilityStart = (external.getAvailabilityStart() != null) ?
                external.getAvailabilityStart().toString() : null;
        this.availabilityEnd = (external.getAvailabilityEnd() != null) ?
                external.getAvailabilityEnd().toString() : null;
        this.description = external.getDescription();
        this.specialFields = convertSpecialFieldsToDTO(specialFields);
        this.bachelorSubjects = convertBachelorSubjectsToDTO(bachelorSubjects);

    }

    private List<SpecialFieldDTO> convertSpecialFieldsToDTO(List<SpecialField> specialFields) {
        if (specialFields == null) {
            return new ArrayList<>(); // Leere Liste, falls keine SpecialFields vorhanden
        }
        return specialFields.stream()
                .map(field -> new SpecialFieldDTO(field.getName()))
                .collect(Collectors.toList());
    }

    private List<BachelorSubjectDTO> convertBachelorSubjectsToDTO(List<BachelorSubject> bachelorSubjects) {
        if (bachelorSubjects == null){
            return new ArrayList<>(); // Leere Liste, falls keine BachelorSubjects vorhanden
    }
    return bachelorSubjects.stream()
                    .map(subject -> new BachelorSubjectDTO(
                            subject.getTitle(),
                            subject.getBDescription(),
                            subject.getDate()))
                    .collect(Collectors.toList());
}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<SpecialFieldDTO> getSpecialFields() {
        return specialFields;
    }

    public void setSpecialFields(List<SpecialFieldDTO> specialFields) {
        this.specialFields = specialFields;
    }

}
