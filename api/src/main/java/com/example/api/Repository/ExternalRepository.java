package com.example.api.Repository;

import com.example.api.Entitys.External;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//mit Hilfe von "extends" auf die Datenbank zugreifen, um Daten zu speichern
public interface ExternalRepository extends JpaRepository<External, Long> {

    /**
     * Userin (hier Extern) soll sich mit seiner E-Mail-Adresse einloggen können.
     * Dafür muss die gewährleistet werden, dass die E-Mail-Adresse eindeutig ist.
     * Wir checken, ob in unserer DB die eingegebene E-Mail bereits vorhanden ist.
     * Über das Repository können wir unsere DB nach bestimmten Usern durchsuchen.
     */

    // Userin (Externe) mittels E-Mail in der DB finden
    Optional<External> findByEmail(String email);

    Optional <External> findByEmailAndPassword (String email, String password);

}
