package com.example.api.Service;

import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.External;
import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.BachelorSubjectRepository;
import com.example.api.Repository.ExternalRepository;
import com.example.api.Request.ExternalRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExternalService {

    //mit Repository verknüpfen, damit wir auf die DB zugreifen können
    @Autowired
    private final ExternalRepository externalRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private BachelorSubjectRepository bachelorSubjectRepository;

    @Autowired
    private EmailService emailService;

    //Konstruktor
    public ExternalService(ExternalRepository externalRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.externalRepository = externalRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Methode für die Regostrierung einer externen Person (Zweitbtreuer*in)
     * @param externalRequest Die Anfrage mit den Daten der zu registrierenden Person (Zweitbetreuer*in)
     * @return  ResponseEntity mit den Daten der registrierten Person (Zweitbetreuer*in)
     * @throws IllegalStateException wenn die E-Mail bereits vergeben ist
     */
    public ResponseEntity<?> registration(ExternalRequest externalRequest) {
        try{
            External external = new External(
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
                    null  // Set BachelorSubjects to null initially
            );

            //existiert student mit dieser email bereits in der DB?
            boolean externExists = externalRepository.findByEmail(external.getEmail()).isPresent();

            //wenn E-Mail bereits existiert, dann Exception
            if (externExists) {
                throw new IllegalStateException(("Die E-Mail ist bereits vergeben!"));
            }

            // Passwort verschlüsseln
            String encodedPassword = bCryptPasswordEncoder.encode(external.getPassword());
            external.setPassword(encodedPassword);

            // das Registrierungsdatum auf das aktuelle Datum setzen
            external.setRegistrationDate(LocalDate.now());

            External savedExternal = externalRepository.save(external);

            emailService.sendEmail(external.getEmail(),
                    "Willkommen im System",
                    "Hallo, Sie haben sich erfolgreich registriert und können die Plattform nun nutzen. " +
                            "Viel Freude dabei!");

            // BachelorSubjects behandeln
            List<BachelorSubject> bachelorSubjects = externalRequest.getBachelorSubjects();
            if (bachelorSubjects != null) {
                for (BachelorSubject subject : bachelorSubjects) {
                    // Set the External entity for each BachelorSubject
                    subject.setExternal(savedExternal);
                }
                // dazugehörige BachelorSubjects speichern
                savedExternal.setBachelorSubjects(bachelorSubjectRepository.saveAll(bachelorSubjects));
            }


            return ResponseEntity.ok(savedExternal);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
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
        existingUser.setBachelorSubjects(newUser.getBachelorSubjects());
        External savedExternal = externalRepository.save(existingUser);

        //String message = "{\"User*in mit der ID\" " + id + "\" erfolgreich aktualisiert!\"}";
        //return ResponseEntity.ok().body(message);
        return ResponseEntity.ok(savedExternal);
    }
}

