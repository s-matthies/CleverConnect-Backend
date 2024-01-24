package com.example.api.Entitys;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="Bachelor_Subject")

public class BachelorSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    @Column(name = "b_description")
    private String bDescription;
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    // ManyToOne, denn viele(Many) Bachelorthemen können nur einem(One) External zugeordnet werden
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="external_id")
    private External external;

    public BachelorSubject(String title, String bDescription, LocalDate date , External external) {
        this.title = title;
        this.bDescription = bDescription;
        this.date = LocalDate.now();
        this.external = external;
    }

    public BachelorSubject(Long id, String title, String bDescription, LocalDate date , External external) {
        this.id = id;
        this.title = title;
        this.bDescription = bDescription;
        this.date = date != null ? date : LocalDate.now();
        this.external = external;
    }


    public BachelorSubject() {
        this.date = LocalDate.now();
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

    public LocalDate getDate() {
        return date;
    }


    public External getExternal() {
        return external;
    }

    public void setExternal(External external) {
        this.external = external;
    }

}
