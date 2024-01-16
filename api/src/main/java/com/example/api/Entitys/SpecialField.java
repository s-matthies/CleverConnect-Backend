package com.example.api.Entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "Special_Field")
public class SpecialField {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;
    private String title;
    private Boolean disabled;

    @JsonIgnore
    @ManyToMany(mappedBy = "specialFields", cascade = CascadeType.ALL)
    private Set<External> externals = new HashSet<>();

    public SpecialField(String value, String title, Boolean disabled, Set<External> externals) {
        this.value = value;
        this.title = title;
        this.disabled = disabled;
        this.externals = externals;
    }

    public SpecialField(Long id, String value, String title, Boolean disabled, Set<External> externals) {
        this.id = id;
        this.value = value;
        this.title = title;
        this.disabled = disabled;
        this.externals = externals;
    }

    public SpecialField() {
    }




    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Set<External> getExternal() {

        return externals;
    }

    public void setExternal(Set<External> externals) {

        this.externals = externals;
    }
}




