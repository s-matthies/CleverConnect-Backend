package com.example.api.Entitys;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
//@Data // import lombok.Data;
public class UserEntity {

    @Id // Schlüsselattribut der Entität UserEntity ist id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Wert wird automatisch generiert und inkrementiert
    private Long id;
    private String username;
    private String password;

    public UserEntity(Long id, String username, String password) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserEntity() {
    }

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }

    //toString() ? von @Data (Lombok) -> muss das inkludiert werden
    //equals() ? von @Data (Lombok) -> muss das inkludiert werden
    //hashCode() ? von @Data (Lombok) -> muss das inkludiert werden

    // FetchType.EAGER -> wenn der User geladen wird, wollen wir auch immer seine Rolle (Admin/User) sehen
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    // Tabelle mit Schlüsselattribut id und Fremdschlüssel id von Role
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

}
