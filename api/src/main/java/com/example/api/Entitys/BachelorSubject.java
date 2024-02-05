package com.example.api.Entitys;

import jakarta.persistence.*;

import java.time.LocalDate;


/**
 * Entity Klasse für die BachelorSubject
 */
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


    /**
     * Konstruktor
     *
     * @param title - Titel des BachelorSubjects
     * @param bDescription - Beschreibung des BachelorSubjects
     * @param date - Datum des BachelorSubjects
     * @param external - External, der das BachelorSubject erstellt hat
     */
    public BachelorSubject(String title, String bDescription, LocalDate date , External external) {
        this.title = title;
        this.bDescription = bDescription;
        this.date = LocalDate.now();
        this.external = external;
    }

    /** Konstruktor
     *
     * @param id - ID des BachelorSubjects
     * @param title - Titel des BachelorSubjects
     * @param bDescription - Beschreibung des BachelorSubjects
     * @param date - Datum des BachelorSubjects
     * @param external - External, der das BachelorSubject erstellt hat
     */
    public BachelorSubject(Long id, String title, String bDescription, LocalDate date , External external) {
        this.id = id;
        this.title = title;
        this.bDescription = bDescription;
        this.date = date != null ? date : LocalDate.now();
        this.external = external;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BachelorSubject() {
        this.date = LocalDate.now();
    }

    public String getTitle() {
        return title;
    }


    public String getBDescription() {
        return bDescription;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setExternal(External external) {
        this.external = external;
    }

}
