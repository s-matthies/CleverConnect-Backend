package com.example.api.Request;

public class BachelorSubjectRequest {

    private final String title;

    private final String bDescription;

    public BachelorSubjectRequest(String title, String bDescription) {
        this.title = title;
        this.bDescription = bDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getbDescription() {
        return bDescription;
    }
}
