package com.example.api.DTO;

import java.time.LocalDate;

/**
 * Diese Klasse ist ein DTO Objekt, welches die Daten für ein BachelorSubject enthält.
 */
public class BachelorSubjectDTO {

    private String title;
    private String bDescription;
    private LocalDate date;

    public BachelorSubjectDTO(String title, String bDescription, LocalDate date) {
        this.title = title;
        this.bDescription = bDescription;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBDescription() {
        return bDescription;
    }

    public void setBDescription(String bDescription) {
        this.bDescription = bDescription;
    }
}
