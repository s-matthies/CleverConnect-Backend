package com.example.api.Entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity Klasse für die SpecialField
 */
@Entity
@Table (name = "Special_Field")
public class SpecialField {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "specialFields", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<External> externals = new HashSet<>();


    public SpecialField() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public Set<External> getExternal() {

        return externals;
    }

}




