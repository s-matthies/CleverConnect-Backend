package com.example.api.DTO;

/**
 * Diese Klasse ist ein DTO Objekt, welches die Daten für ein Fachgebiet (SpecialField) enthält.
 */
public class SpecialFieldDTO {

    private String name;

    public SpecialFieldDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }
}
