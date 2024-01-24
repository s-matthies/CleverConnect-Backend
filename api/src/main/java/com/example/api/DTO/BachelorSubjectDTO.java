package com.example.api.DTO;

public class BachelorSubjectDTO {

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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBDescription() {
        return bDescription;
    }

    public void setBDescription(String bDescription) {
        this.bDescription = bDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        this.date = value;
    }
}
