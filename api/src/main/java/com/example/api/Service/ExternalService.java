package com.example.api.Service;

import com.example.api.BachelorSubjectNotFound.BachelorSubjectNotFoundException;
import com.example.api.DTO.ExternalDTO;
import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.External;
import com.example.api.Entitys.Role;
import com.example.api.Entitys.SpecialField;
import com.example.api.NotFoundExceptions.SpecialFieldNotFoundException;
import com.example.api.Repository.BachelorSubjectRepository;
import com.example.api.Repository.ExternalRepository;
import com.example.api.Repository.SpecialFieldRepository;
import com.example.api.Request.ExternalRequest;
import com.example.api.UserNotFound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private SpecialFieldRepository specialFieldRepository;

    @Autowired
    private EmailService emailService;


    //Konstruktor
    public ExternalService(ExternalRepository externalRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           BachelorSubjectRepository bachelorSubjectRepository,
                           EmailService emailService) {
        this.externalRepository = externalRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bachelorSubjectRepository = bachelorSubjectRepository;
        this.emailService = emailService;
    }

    /**
     * Methode für die Registrierung einer externen Person (Zweitbtreuer*in)
     *
     * @param externalRequest Die Anfrage mit den Daten der zu registrierenden Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der registrierten Person (Zweitbetreuer*in)
     * @throws IllegalStateException wenn die E-Mail bereits vergeben ist
     */
    public ResponseEntity<Object> registration(ExternalRequest externalRequest) {
        try {
            //existiert student mit dieser email bereits in der DB?
            boolean externExists = externalRepository.findByEmail(externalRequest.getEmail()).isPresent();

            //wenn E-Mail bereits existiert, dann Exception
            if (externExists) {
                return ResponseEntity.badRequest().body("{\"error\": \"Die E-Mail ist bereits vergeben!\"}");
            }

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
                    null, // Specialfield ersteinmal null, da erst Id von external generiert werden muss
                    null  // Set BachelorSubjects to null initially
            );

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

                List<SpecialField> specialFields = externalRequest.getSpecialFields();
                if (specialFields != null) {
                    // Ensure the specialFields list is initialized only once
                    if (savedExternal.getSpecialFields() == null) {
                        savedExternal.setSpecialFields(new HashSet<>());
                    }

                    for (SpecialField field : specialFields) {

                        // Establish the bidirectional relationship
                        field.getExternal().add(savedExternal);
                        savedExternal.getSpecialFields().add(field);

                        specialFieldRepository.save(field);
                        externalRepository.save(savedExternal);
                    }
                    //savedExternal.setSpecialFields(specialFieldRepository.saveAll(specialFields));
            }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedExternal);
        } catch (IllegalStateException e) {
            String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.status(400).body(errorMessage);
        }
    }


    // Methode um Externe nach id zu laden
    public ExternalDTO getExternal(Long id) {
        External external = externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        List<BachelorSubject> bachelorSubjects = external.getBachelorSubjects();
        List<SpecialField> specialFields = new ArrayList<>(external.getSpecialFields());

        return new ExternalDTO(external, specialFields, bachelorSubjects);
    }


    // Methode um alle Externen mit Bachelorthemen zu laden
    public List<ExternalDTO> getAllExternalsWithSubjects() {
        List<External> allExternals = externalRepository.findAll();
        List<ExternalDTO> externalDTOs = new ArrayList<>();

        for (External external : allExternals) {
            List<BachelorSubject> bachelorSubjects = external.getBachelorSubjects();
            List<SpecialField> specialFields = new ArrayList<>(external.getSpecialFields());
            externalDTOs.add(new ExternalDTO(external,specialFields, bachelorSubjects));
        }
        return externalDTOs;
    }


    // Methode um Daten eine Userin zu aktualisieren
    public ResponseEntity<External> updateExternal(Long id, External newUser) {
        // Externe Person anhand der ID finden
        External existingUser = externalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));


        // Füge die aktualisierten Bachelor-Themen hinzu
        List<BachelorSubject> updatedSubjects = newUser.getBachelorSubjects();
        if (updatedSubjects != null) {
            existingUser.getBachelorSubjects().clear();
            for (BachelorSubject updatedSubject : updatedSubjects) {
                // Find the existing subject in the database
                BachelorSubject existingSubject = bachelorSubjectRepository.findById(updatedSubject.getId())
                        .orElseThrow(() -> new BachelorSubjectNotFoundException(updatedSubject.getId()));
                // Update the existing subject with the new data
                existingSubject.setTitle(updatedSubject.getTitle());
                //existingSubject.setDate(updatedSubject.getDate());
                existingSubject.setBDescription(updatedSubject.getBDescription());
                // Add the updated subject to the existingUser's bachelorSubjects
                existingUser.getBachelorSubjects().add(existingSubject);
            }
        }

        // Füge die aktualisierten Spezialgebiete hinzu
        Set<SpecialField> updatedFields = newUser.getSpecialFields();
        if (updatedFields != null) {
            existingUser.getSpecialFields().clear();
            for (SpecialField updatedField : updatedFields) {
                // Find the existing field in the database
                SpecialField existingField = specialFieldRepository.findById(updatedField.getId())
                        .orElseThrow(() -> new SpecialFieldNotFoundException(updatedField.getId()));
                // Update the existing field with the new data
                existingField.setName(updatedField.getName());
                // Add the updated field to the existingUser's specialFields
                existingUser.getSpecialFields().add(existingField);
            }
        }

        //existierende Externe mit neuen Daten updaten
        existingUser.setFirstName(newUser.getFirstName());
        existingUser.setLastName(newUser.getLastName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setAvailabilityStart(newUser.getAvailabilityStart());
        existingUser.setAvailabilityEnd(newUser.getAvailabilityEnd());
        existingUser.setCompany(newUser.getCompany());
        existingUser.setDescription(newUser.getDescription());
        existingUser.setSpecialFields(newUser.getSpecialFields());
        existingUser.setBachelorSubjects(newUser.getBachelorSubjects());
        External savedExternal = externalRepository.save(existingUser);

        return ResponseEntity.ok(savedExternal);
    }
}

