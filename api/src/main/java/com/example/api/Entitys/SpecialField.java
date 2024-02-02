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


    public SpecialField(String name,  Set<External> externals) {
        this.name = name;
        this.externals = externals;
    }

    public SpecialField(Long id, String name, Set<External> externals) {
        this.id = id;
        this.name = name;
        this.externals = externals;
    }

    public SpecialField() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<External> getExternals() {
        return externals;
    }

    public void setExternals(Set<External> externals) {
        this.externals = externals;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }


    public Set<External> getExternal() {

        return externals;
    }

    public void setExternal(Set<External> externals) {

        this.externals = externals;
    }
}




