package com.example.api.Entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "Rolle")
public class Role {

    @Id // Schlüsselattribut der Entität Role ist id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Wert wird automatisch generiert und inkrementiert
    private Long id;

    private String name;

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

}
