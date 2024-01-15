package com.example.api.Entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "Special_Field")
public class SpecialField {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String value;
    private String title;
    private Boolean disabled;

    @JsonIgnore
    @ManyToMany(mappedBy = "specialFields", cascade = CascadeType.ALL)
    private List<External> external = new ArrayList<>();

    public SpecialField(String value, String title, Boolean disabled, List<External> external) {
        this.value = value;
        this.title = title;
        this.disabled = disabled;
        this.external = external;
    }

    public SpecialField(Long id, String value, String title, Boolean disabled, List<External> external) {
        this.id = id;
        this.value = value;
        this.title = title;
        this.disabled = disabled;
        this.external = external;
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

    public List<External> getExternal() {
        return external;
    }

    public void setExternal(List<External> expertsInField) {
        this.external = expertsInField;
    }
}




