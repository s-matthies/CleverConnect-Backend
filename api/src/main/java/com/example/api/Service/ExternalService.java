package com.example.api.Service;

import com.example.api.Entitys.External;
import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.ExternalRepository;
import com.example.api.Request.ExternalRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalService {

    //mit Repository verknüpfen, damit wir auf die DB zugreifen können
    @Autowired
    private final ExternalRepository externalRepository;

    @Autowired
    private EmailService emailService;

    //Konstruktor
    public ExternalService(ExternalRepository externalRepository) {
        this.externalRepository = externalRepository;
    }

    /*
     * User (hier Extern) soll sich mit seiner E-Mail-Adresse einloggen können.
     * Dafür muss die gewährleistet werden, dass die E-Mail-Adresse eindeutig ist.
     * Wir checken, ob in unserer DB die eingegebene E-Mail bereits vorhanden ist.
     * Über das Repository können wir unsere DB nach bestimmten Usern durchsuchen.
     */

    //SignUp-Methode erstellen
    public ResponseEntity<?> registerExtern(External external) {
        try {
            //existiert student mit dieser email bereits in der DB?
            // im Repository findByMethode angelegt (-> E-Mail-Adresse muss eindeutig sein)
            boolean externExists = externalRepository.findByEmail(external.getEmail()).isPresent();

            //wenn E-Mail bereits existiert, dann Exception
            if (externExists) {
                throw new IllegalStateException(("Die E-Mail ist bereits vergeben!"));
            }
            // das Registrierungsdatum auf das aktuelle Datum setzen
            external.setRegistrationDate(LocalDate.now());

            User savedUser = externalRepository.save(external);
            emailService.sendEmail(external.getEmail(),
                    "Willkommen im System",
                    "Hallo, Sie haben sich erfolgreich registriert und können die Platform nun nutzen. Viel Freude dabei!");


            return ResponseEntity.ok(savedUser);

            //String message = "{\"User*in wurde erfolgreich registriert!\"}";
            //return ResponseEntity.ok(message);
        }
        catch (IllegalStateException e) {
            // Exception abfangen und Fehler-JSON-Response zurückgeben
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    /*
     * wir erstellen eine Request: das was von uns an den Server weitergeben
     * werden soll, um eine Userin (Externe) zu erstellen
     */

    public ResponseEntity<?> registration(ExternalRequest externalRequest) {
        return registerExtern(new External(
                        externalRequest.getFirstName(),
                        externalRequest.getLastName(),
                        externalRequest.getEmail(),
                        externalRequest.getPassword(),
                        null,
                        Role.EXTERN,
                        false,
                        true,
                        externalRequest.getCompany(),
                        externalRequest.getAvailabilityStart(),
                        externalRequest.getAvailabilityEnd(),
                        externalRequest.getDescription(),
                        externalRequest.getExpertise(),
                        externalRequest.getBachelorSubjects()
                )
        );
    }


    // Methode um Externe nach id zu laden
    public External getExternal(Long id) {
        return externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // Methode um alle Externen zu laden
    public List<External> allExternal() {
        return externalRepository.findAll();
    }

    // Methode um Daten eine Userin zu aktualisieren
    public ResponseEntity<External> updateExternal(Long id, External newUser) {
        // Externe Person anhand der ID finden
        External existingUser = externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        //existierende Externe mit neuen Daten updaten
        existingUser.setFirstName(newUser.getFirstName());
        existingUser.setLastName(newUser.getLastName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setAvailabilityStart(newUser.getAvailabilityStart());
        existingUser.setAvailabilityEnd(newUser.getAvailabilityEnd());
        existingUser.setCompany(newUser.getCompany());
        existingUser.setDescription(newUser.getDescription());
        existingUser.setExpertise(newUser.getExpertise());
        existingUser.setBachelorSubjects(newUser.getBachelorSubjects());
        External savedExternal = externalRepository.save(existingUser);

        //String message = "{\"User*in mit der ID\" " + id + "\" erfolgreich aktualisiert!\"}";
        //return ResponseEntity.ok().body(message);
        return ResponseEntity.ok(savedExternal);
    }
}

