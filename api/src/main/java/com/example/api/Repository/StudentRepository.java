package com.example.api.Repository;

import com.example.api.Entitys.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//Repository greift auf Datenbank zu und mithilfe "extends" auf Repository von Student zu
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Student wird mittels Email-Adresse in der Datenbank gefunden
    Optional <Student> findByEmail(String email);
}
