package com.example.api.Service;

import com.example.api.Entitys.Student;
import com.example.api.Repository.StudentRepository;
import com.example.api.Request.StudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    // Verknüpfung mit dem Repository, damit auf die Datenbank zugegriffen werden kann
    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String studentRegistration(Student student){

        // da es sein kann, dass Email Adresse schon im System vorhanden ist, wird boolean gesetzt
        boolean studentExists = studentRepository.findByEmail(student.getEmail()).isPresent();
        // wenn Email-Adresse des Studenten schon in Datenbank, dann Exception
        if(studentExists) {
            throw new IllegalMonitorStateException("E-Mail Adresse ist bereits vergeben");
        }

        studentRepository.save(student);
        return "Student wurde erfolgreich registriert.";

    }

    // weitere Methode, um auch die  Attribute zu vergeben/speichern,
    // praktisch, wenn Attribute zb Profilbild automatisch von System erzeugt wird und
    // nicht alle Attribute vom Nutzer selbst eingeben werden

    public String register(StudentRequest studentRequest){
        // greift vorher erstellte Methode zurück
        return studentRegistration(new Student(
                // Eingabe der Attribute
                studentRequest.getFirstname(),
                studentRequest.getLastname(),
                studentRequest.getEmail(),
                studentRequest.getPassword()
        ));
    }
}
